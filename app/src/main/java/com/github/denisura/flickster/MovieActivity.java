package com.github.denisura.flickster;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ListView;

import com.github.denisura.flickster.adapters.MovieArrayAdapter;
import com.github.denisura.flickster.models.Movie;
import com.github.denisura.flickster.models.Video;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cz.msebera.android.httpclient.Header;

public class MovieActivity extends AppCompatActivity {

    private Unbinder unbinder;
    ArrayList<Movie> movies = new ArrayList<>();
    MovieArrayAdapter movieAdapter;

    @BindView(R.id.swipeContainer)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @BindView(R.id.lvMovies)
    ListView lvItems;

    @BindView(R.id.toolbar)
    public Toolbar mToolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);

        unbinder = ButterKnife.bind(this);
        setSupportActionBar(mToolbar);

        movieAdapter = new MovieArrayAdapter(this, movies);
        lvItems.setAdapter(movieAdapter);
        fetchMoviesAsync(0);

        // Setup refresh listener which triggers new data loading
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchMoviesAsync(0);
            }
        });
        // Configure the refreshing colors
        mSwipeRefreshLayout.setColorSchemeResources(
                android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
    }


    public void fetchMoviesAsync(int page) {
        String url = "https://api.themoviedb.org/3/movie/now_playing?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed";
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(url, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                movieAdapter.clear();
                try {
                    JSONArray movieJsonResults = response.getJSONArray("results");

                    movies.addAll(Movie.fromJSONArray(movieJsonResults));
                    movieAdapter.notifyDataSetChanged();
                    for (int i = 0; i < movies.size(); i++) {
                        fetchMovieVideo(i);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                mSwipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });
    }


    private void fetchMovieVideo(final int index) {

        final Movie movie = movies.get(index);
        if (movie == null) {
            return;
        }

        int movieId = movie.getId();
        AsyncHttpClient client = new AsyncHttpClient();
        String videosUrl = "https://api.themoviedb.org/3/movie/" +
                movieId + "/videos?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed";
        client.get(videosUrl, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    JSONArray jsonResults = response.getJSONArray("results");
                    ArrayList<Video> videos = Video.fromJSONArray(jsonResults);
                    movie.setVideos(videos);
                    movies.set(index, movie);
                    movieAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
