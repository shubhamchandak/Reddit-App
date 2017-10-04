package com.example.w10.reddit.Account;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.w10.reddit.Account.CheckLogin;
import com.example.w10.reddit.FeedAPI;
import com.example.w10.reddit.R;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {
    public static final String LOGIN_URL = "https://www.reddit.com/api/login/";

    private static final String LOG_TAG = "LoginActivity";

    EditText mUsernameView;
    EditText mPasswordView;
    ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mUsernameView = (EditText) findViewById(R.id.login_input_username);
        mPasswordView = (EditText) findViewById(R.id.login_input_password);
        mProgressBar = (ProgressBar) findViewById(R.id.login_loading_progress_bar);

        Button buttonLogin = (Button) findViewById(R.id.button_login);


        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = mUsernameView.getText().toString().trim();
                String password = mPasswordView.getText().toString().trim();
                if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)){
                    Toast.makeText(LoginActivity.this, "Enter username and password", Toast.LENGTH_SHORT).show();
                } else {
                    mProgressBar.setVisibility(View.VISIBLE);
                    login(username, password);
                }
            }
        });

//
    }

    private void login(final String username, String password){

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(LOGIN_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        FeedAPI feedAPI = retrofit.create(FeedAPI.class);

        HashMap<String, String> headerMap = new HashMap<>();
        headerMap.put("Content-Type", "application/json");



        Call<CheckLogin> call = feedAPI.signIn(headerMap,username,username, password, "json");

        call.enqueue(new Callback<CheckLogin>() {
            @Override
            public void onResponse(Call<CheckLogin> call, Response<CheckLogin> response) {
                try{
                    //Log.d(TAG, "onResponse: feed: " + response.body().toString());
                    Log.d(LOG_TAG, "onResponse: Server Response: " + response.toString());

                    String modhash = response.body().getJson().getData().getModhash();
                    String cookie = response.body().getJson().getData().getCookie();
                    Log.d(LOG_TAG, "onResponse: modhash: " + modhash);
                    Log.d(LOG_TAG, "onResponse: cookie: " + cookie);

                    if(!modhash.equals("")){
                        setSessionParams(username, modhash, cookie);
                        mProgressBar.setVisibility(View.GONE);
                        mUsernameView.setText("");
                        mPasswordView.setText("");
                        Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();

                        //navigate back to previous activity
                        finish();
                    }
                }catch (NullPointerException e){
                    Log.e(LOG_TAG, "onResponse: NullPointerException: " +e.getMessage() );
                }
            }

            @Override
            public void onFailure(Call<CheckLogin> call, Throwable t) {
                  mProgressBar.setVisibility(View.GONE);
                Log.e(LOG_TAG, "onFailure: Unable to retrieve RSS: " + t.getMessage() );
                Toast.makeText(LoginActivity.this, "An Error Occured", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /*
     * Save the session params once login in successful
     * @param username
     * @param modhash
     * @param cookie
     */
    private void setSessionParams(String username, String modhash, String cookie){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
        SharedPreferences.Editor editor = preferences.edit();

        Log.d(LOG_TAG, "setSessionParams: Storing session variables:  \n" +
                "username: " + username + "\n" +
                "modhash: " + modhash + "\n" +
                "cookie: " + cookie + "\n"
        );


        editor.putString("@string/session_username", username);
        editor.commit();
        editor.putString("@string/session_modhash", modhash);
        editor.commit();
        editor.putString("@string/session_cookie", cookie);
        editor.commit();
    }
}
