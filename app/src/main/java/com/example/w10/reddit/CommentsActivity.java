package com.example.w10.reddit;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.w10.reddit.Account.CheckLogin;
import com.example.w10.reddit.Account.LoginActivity;
import com.example.w10.reddit.model.Feed;
import com.example.w10.reddit.model.entry.Entry;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.V;
import static com.example.w10.reddit.URLS.BASE_URL;

/**
 * Created by W10 on 9/26/2017.
 */

public class CommentsActivity extends AppCompatActivity{

    private final String LOG_TAG = getClass().getSimpleName();

    public static final String COMMENT_URL = "https://www.reddit.com/api/";

    private String mPostURL;
    private String mPostThumbnailURL;
    private String mPostTitle;
    private String mPostAuthor;
    private String mPostDateUpdated;
    private String mPostId;

    private String mModhash;
    private String mCookie;
    private String mUsername;

    private String mCurrentFeed;

    private ArrayList<Comment> mComments = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);

        getSessionParams();

        setupImageLoader();
        iniPost();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URLS.BASE_URL)
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .build();

        FeedAPI feedAPI = retrofit.create(FeedAPI.class);
        Call<Feed> call = feedAPI.getFeed(mCurrentFeed);

        call.enqueue(new Callback<Feed>() {
            @Override
            public void onResponse(Call<Feed> call, Response<Feed> response) {
//                Log.d(LOG_TAG, "onResponse: feed: " + response.body());
                Log.d(LOG_TAG, "onResponse: Server Response: " + response.toString());

                List<Entry> entries = response.body().getEntries();

                for (int i = 1; i < entries.size(); i++) {

                    XMLExtractor xmlExtractor = new XMLExtractor(entries.get(i).getContent(), "<div class=\"md\"><p>", "</p>");
                    List<String> extractedxml = xmlExtractor.extract();

                    Log.d(LOG_TAG, "onResponse: entries: " + entries.get(i).toString());
                    try {
//
//                        Log.d(LOG_TAG, "Comment: " + extractedxml.size() +
//                                entries.get(i).getAuthor().getName()+
//                                entries.get(i).getUpdated()+
//                                entries.get(i).getId())   ;

                        Log.d(LOG_TAG, "onResponse: entries: " + entries.get(i).toString());
                        mComments.add(new Comment(extractedxml.get(0),
                                                 entries.get(i).getAuthor().getName(),
                                                 entries.get(i).getUpdated(),
                                                 entries.get(i).getId()));

                    }catch (IndexOutOfBoundsException e){
                        mComments.add(new Comment("No Text Found",
                                                "None",
                                                "None",
                                                "None"));
                        Log.e(LOG_TAG, "onResponse: IndexOutOfBoundsException: " + e.getMessage());

                    }catch (NullPointerException e){
                        mComments.add(new Comment(extractedxml.get(0),
                                "None",
                                entries.get(i).getUpdated(),
                                entries.get(i).getId()));
                        Log.e(LOG_TAG, "onResponse: NullPointerException: " + e.getMessage());
                    }
                }

                ListView commentsListView = (ListView) findViewById(R.id.comments_list_view);
                CommentsAdapter adapter = new CommentsAdapter(CommentsActivity.this, R.layout.comments_layout, mComments);
                commentsListView.setAdapter(adapter);

                commentsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                        getUserComment(mComments.get(position).getId());
                    }
                });

                ProgressBar listProgressBar = (ProgressBar) findViewById(R.id.comments_loading_progress_bar);
                listProgressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<Feed> call, Throwable t) {
                Log.e(LOG_TAG, "onResponse: Unable to retrieve rss: " + t.getMessage());
                Toast.makeText(CommentsActivity.this, "An Error Occured", Toast.LENGTH_SHORT).show();
            }
        });



    }


    // original post
    private void iniPost(){
        Intent incomingIntent = getIntent();

        mPostTitle = incomingIntent.getStringExtra("@string/post_title");
        mPostAuthor = incomingIntent.getStringExtra("@string/post_author");
        mPostDateUpdated = incomingIntent.getStringExtra("@string/post_date_updated");
        mPostURL = incomingIntent.getStringExtra("@string/post_url");
        mPostThumbnailURL = incomingIntent.getStringExtra("@string/post_thumbnail_url");
        mPostId = incomingIntent.getStringExtra("@string/post_id");

        TextView title = (TextView) findViewById(R.id.post_title);
        TextView author = (TextView) findViewById(R.id.post_author);
        TextView dateUpdated = (TextView) findViewById(R.id.post_date_updated);
        ImageView thumbnail = (ImageView) findViewById(R.id.post_thumbnail);
        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.post_loading_progress_bar);
        Button replyButton = (Button) findViewById(R.id.post_reply_button);

        title.setText(mPostTitle);
        author.setText(mPostAuthor);
        dateUpdated.setText(mPostDateUpdated);

        //create the imageLoader object
        ImageLoader imageLoader = ImageLoader.getInstance();

        int defaultImage = CommentsActivity.this.getResources().getIdentifier("@drawable/default_thumbnail",null,CommentsActivity.this.getPackageName());

        //create display options
        DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true)
                .cacheOnDisc(true).resetViewBeforeLoading(true)
                .showImageForEmptyUri(defaultImage)
                .showImageOnFail(defaultImage)
                .showImageOnLoading(defaultImage).build();

        //download and display image from url
        imageLoader.displayImage(mPostThumbnailURL, thumbnail, options, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {
                progressBar.setVisibility(View.VISIBLE);
            }
            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                progressBar.setVisibility(View.GONE);
            }
            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                progressBar.setVisibility(View.GONE);
            }
            @Override
            public void onLoadingCancelled(String imageUri, View view) {
                progressBar.setVisibility(View.GONE);
            }
        });

        replyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getUserComment(mPostId);
            }
        });

        thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CommentsActivity.this, WebviewActivity.class);
                intent.putExtra("url", mPostURL);
                startActivity(intent);
            }
        });

        // NOTE: NSFW(Not Suitable For Work) posts will cause an error.
        try {
            String[] splitURL = mPostURL.split(BASE_URL);
            mCurrentFeed = splitURL[1];
            Log.d(LOG_TAG, "initPost: currentFeed: " + mCurrentFeed);
        } catch (ArrayIndexOutOfBoundsException e){
            Log.e(LOG_TAG, "initPost: ArrayIndexOutOfBoundsException: " + e.getMessage());
        }
    }

    private void getUserComment(final String postId){
        final Dialog commentDialog = new Dialog(CommentsActivity.this);
        commentDialog.setTitle("Comment");
        commentDialog.setContentView(R.layout.comment_input_dialog);

        int width = (int) (getResources().getDisplayMetrics().widthPixels*0.9);
        int height = (int) (getResources().getDisplayMetrics().heightPixels*0.5);

        commentDialog.getWindow().setLayout(width, height);
        commentDialog.show();

        Button commentDialogButton = (Button) commentDialog.findViewById(R.id.button_comment_dialog);
        final EditText commentDialogView = (EditText) commentDialog.findViewById(R.id.comment_dialog_text);

        commentDialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(LOG_TAG, "Attempting to post comment");
                // retrofit stuff for posting comment
                String comment = commentDialogView.getText().toString().trim();
                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(COMMENT_URL)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();

                    FeedAPI feedAPI = retrofit.create(FeedAPI.class);

                    HashMap<String, String> headerMap = new HashMap<>();
                    headerMap.put("User-Agent", mUsername);
                    headerMap.put("X-Modhash", mModhash);
                    headerMap.put("cookie", "reddit_session=" + mCookie);


                    Call<CheckComment> call = feedAPI.submitComment(headerMap, "comment", postId, comment);

                    call.enqueue(new Callback<CheckComment>() {
                        @Override
                        public void onResponse(Call<CheckComment> call, Response<CheckComment> response) {

                            try {
                                //Log.d(LOG_TAG, "onResponse: feed: " + response.body().toString());
                                Log.d(LOG_TAG, "onResponse: Server Response: " + response.toString());

                                String postSuccess = response.body().getSuccess();

                                if (postSuccess.equals("true")){
                                    commentDialog.dismiss();
                                    Toast.makeText(CommentsActivity.this,  "Post Successful", Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    Toast.makeText(CommentsActivity.this,  "An Error Occurred. Did yoou SignIn?", Toast.LENGTH_SHORT).show();
                                }

                            } catch (NullPointerException e){
                                Log.e(LOG_TAG, "onResponse: NullPointerException" + e.getMessage());
                            }
                        }

                        @Override
                        public void onFailure(Call<CheckComment> call, Throwable t) {
                            Log.e(LOG_TAG, "onFailure: Unable to post comment: " + t.getMessage() );
                            Toast.makeText(CommentsActivity.this, "An Error Occured", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
        });
    }

    /**
     * Required for setting up the Universal Image loader Library
     */
    private void setupImageLoader(){
        // UNIVERSAL IMAGE LOADER SETUP
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheOnDisc(true).cacheInMemory(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .displayer(new FadeInBitmapDisplayer(300)).build();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                this)
                .defaultDisplayImageOptions(defaultOptions)
                .memoryCache(new WeakMemoryCache())
                .discCacheSize(100 * 1024 * 1024).build();

        ImageLoader.getInstance().init(config);
        // END - UNIVERSAL IMAGE LOADER SETUP
    }

    @Override
    protected void onPostResume() {
        Log.d(LOG_TAG, "onPostResume: Resuming Activity");
        super.onPostResume();
        getSessionParams();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_for_all, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_feed_login:
                Intent intent = new Intent(CommentsActivity.this, LoginActivity.class);
                startActivity(intent);
                return true;
            default: return true;
        }
    }

    // get session params stored in memory from logging in
    private void getSessionParams(){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(CommentsActivity.this);

        mUsername = preferences.getString("@string/session_username", "");
        mModhash = preferences.getString("@string/session_modhash", "");
        mCookie = preferences.getString("@string/session_cookie", "");

        Log.d(LOG_TAG, "getSessionParams: Storing session variables: \n" +
                "username: " + mUsername + "\n" +
                "modhash: " + mModhash + "\n" +
                "cookie: " + mCookie + "\n");
    }
}
