package siri.android.recyclerviewendlessscrolling;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class PostsActivity extends AppCompatActivity {

    private static final String TAG = PostsActivity.class.getSimpleName();
    private static final int PAGE_LIMIT = 10; //pulls 10 posts on each service call.
    private static boolean sEndOfData = false;//used to prevent further service calls.

    private int sPageNumber = 1; //current offset of index it is loaded.
    private PostsAdapter mPostsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posts);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerview_posts);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        mPostsAdapter = new PostsAdapter(this);
        recyclerView.setAdapter(mPostsAdapter);
        //load data to adapter
        loadInitialData(sPageNumber);
        recyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(linearLayoutManager) {

            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                //           mPostsAdapter.addPosts(loadData(page));
                final int pageNumber = page;
                Log.d(TAG, "isLoading :: " + EndlessRecyclerViewScrollListener.loading);
                if (!sEndOfData && !EndlessRecyclerViewScrollListener.loading) {
                    Log.d(TAG, "Load More" + "page :: " + page + EndlessRecyclerViewScrollListener.loading);
                    EndlessRecyclerViewScrollListener.loading = true;
                    mPostsAdapter.addNullPost();// add null post to show progressbar at the end of list

                    //Load more data for reyclerview
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Log.e("hint", "Load More 2");

                            //Remove loading item i,e null post added in the last row
                            mPostsAdapter.removeLastPost();
                            //Load data in adapter
                            //++pageNumber;
                            mPostsAdapter.addPosts(loadData(pageNumber));
                            //loading is done.
                            EndlessRecyclerViewScrollListener.loading = false;
                        }
                    }, 3000);
                }

            }
        });
    }

    /**
     * This method is call once in activity create method and when sort order changes.
     *
     * @param page used to calculate the skip value in url
     */
    public void loadInitialData(int page) {
        mPostsAdapter.addPosts(loadData(page));
    }

    /**
     * creates url from input parameters.Make the service call internally and
     * returns posts list
     *
     * @param page used to calculate the skip value in url
     * @return PostModel list
     */
    public List<PostModel> loadData(int page) {
        List<PostModel> list = new ArrayList<>();
        String url = "https://jsonplaceholder.typicode.com/posts?_page=" + page;
        Log.d(TAG, "QUERY URL : " + url);
        list.addAll(getPostsListFrmServer(url));
        if (page > PAGE_LIMIT) {
            sEndOfData = true;
        }
        return list;
    }

    /**
     * gets posts/members list from server
     *
     * @param url url to make the service call
     * @return PostModel list
     */
    public List<PostModel> getPostsListFrmServer(String url) {
        final List<PostModel> postslist = new ArrayList<>();
        final PostModelHelper postModelHelper = new PostModelHelper();
        String postJson = null;
        try {
            postJson = new GetPostsJsonFrmServerTask().execute(url).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        if (postJson != null) {
            try {
                postslist.addAll(postModelHelper.postsJsonDserializer(postJson));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return postslist;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    //makes service call in the background to get members/posts json string from the server.
    private class GetPostsJsonFrmServerTask extends AsyncTask<String, Void, String> {
        ServiceHandler serviceHandler = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            String url = params[0];
            serviceHandler = new ServiceHandler();
            return serviceHandler.getJsonStrFromServer(url);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
        }
    }
}

