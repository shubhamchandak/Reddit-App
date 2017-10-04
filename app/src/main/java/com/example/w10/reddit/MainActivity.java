package com.example.w10.reddit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.w10.reddit.Account.LoginActivity;
import com.example.w10.reddit.model.Feed;
import com.example.w10.reddit.model.entry.Entry;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

import static com.example.w10.reddit.URLS.BASE_URL;

public class MainActivity extends AppCompatActivity {

    private final String LOG_TAG = getClass().getSimpleName();

    private String mFeedName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EditText feedNameSearchView = (EditText) findViewById(R.id.feed_edit_text);
        final Button feedRefreshButton = (Button) findViewById(R.id.feed_refresh_button);

        feedRefreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mFeedName = feedNameSearchView.getText().toString().trim();
                if (mFeedName.isEmpty() || mFeedName == ""){
                    // do nothing
                } else {
                    getPosts(mFeedName);
                }
            }
        });
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
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                return true;

            default: return true;
        }
    }

    private void getPosts(String feedName){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URLS.BASE_URL)
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .build();

        FeedAPI feedAPI = retrofit.create(FeedAPI.class);

        Call<Feed> call = feedAPI.getFeed(feedName);

        call.enqueue(new Callback<Feed>() {
            @Override
            public void onResponse(Call<Feed> call, Response<Feed> response) {
//                Log.d(LOG_TAG, "onResponse: feed: " + response.body());
//                Log.d(LOG_TAG, "onResponse: Server Response: " + response.toString());

                final ArrayList<Post> posts = new ArrayList<Post>(); // will contain Post objects which are extracted

                try {
                    List<Entry> entries = response.body().getEntries();

                    for (int i = 0; i < entries.size(); i++) {
                        Entry currentEntry = entries.get(i);
//                    Log.e(LOG_TAG, " content: " + currentEntry.getContent());

                        XMLExtractor xmlExtractorHyperLinks = new XMLExtractor(currentEntry.getContent(), "<a href=");
                        XMLExtractor xmlExtractorImgSrcs = new XMLExtractor(currentEntry.getContent(), "<img src=");

                        // add hyperlinks to postContent List
                        List<String> postContent = xmlExtractorHyperLinks.extract();

                        // add img src link to postContent List
                        try {
                            postContent.add(xmlExtractorImgSrcs.extract().get(0)); // contains only one img src
                        } catch (NullPointerException e){
                            postContent.add(null);  // we will know whether to use default img if null is present
                            Log.e(LOG_TAG, "onResponse: NullPointerException(thumbnail): " + e);
                        } catch (IndexOutOfBoundsException e){
                            postContent.add(null);  // we will know whether to use default img if null is present
                            Log.e(LOG_TAG, "onResponse: IndexOutOfBoundException(thumbnail) + e");
                        }

                        // Element at last index of postContent contains img src (we have extracted lastly in try catch block)
                        int lastElementOfPostContent = postContent.size() - 1;

                        try {
                            posts.add(new Post(
                                    entries.get(i).getTitle(),
                                    entries.get(i).getAuthor().getName(),
                                    entries.get(i).getUpdated(),
                                    postContent.get(0),
                                    postContent.get(lastElementOfPostContent),
                                    entries.get(i).getId()));

                        }catch (NullPointerException e){
                            posts.add(new Post(
                                    entries.get(i).getTitle(),
                                    "None",
                                    entries.get(i).getUpdated(),
                                    postContent.get(0),
                                    postContent.get(lastElementOfPostContent),
                                    entries.get(i).getId()));

                        }

                    }

               /* for (int j = 0; j < posts.size(); j++){
                    Log.d(LOG_TAG, "onResponse: \n" +
                                        "Title: " + posts.get(j).getTitle() + "\n" +
                                        "Author: " + posts.get(j).getAuthor() + "\n" +
                                        "Date: " + posts.get(j).getDateUpdated() + "\n" +
                                        "PostURL: " + posts.get(j).getPostUrl() + "\n" +
                                        "PostId: " + posts.get(j).getmId() + "\n" +
                                        "ThumbnailURL: " + posts.get(j).getThumbnailUrl() + "\n\n");
                }*/


                    ListView feedListView = (ListView) findViewById(R.id.feed_list_view);
                    PostAdapter postAdapter = new PostAdapter(MainActivity.this, R.layout.card_layout_main, posts);

                    feedListView.setAdapter(postAdapter);

                    feedListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                            Intent intent = new Intent(MainActivity.this, CommentsActivity.class);
                            intent.putExtra("@string/post_title", posts.get(position).getTitle());
                            intent.putExtra("@string/post_author", posts.get(position).getAuthor());
                            intent.putExtra("@string/post_date_updated", posts.get(position).getDateUpdated());
                            intent.putExtra("@string/post_url", posts.get(position).getPostUrl());
                            intent.putExtra("@string/post_thumbnail_url", posts.get(position).getThumbnailUrl());
                            intent.putExtra("@string/post_id", posts.get(position).getmId());

                            startActivity(intent);

                        }
                    });

                } catch (NullPointerException e){
                    Log.e(LOG_TAG, "onResponse: response is null: " + e.getMessage());
                    Toast.makeText(MainActivity.this, "Try other sub reddit!", Toast.LENGTH_SHORT).show();
                }

            }



            @Override
            public void onFailure(Call<Feed> call, Throwable t) {
                Log.e(LOG_TAG, "onResponse: Unable to retrieve rss: " + t.getMessage());
                Toast.makeText(MainActivity.this, "An Error Occured", Toast.LENGTH_SHORT).show();
            }

        });



    }
}
