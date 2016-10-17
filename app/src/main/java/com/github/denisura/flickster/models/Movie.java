package com.github.denisura.flickster.models;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

public class Movie implements Serializable {

    private String posterPath;
    private String backdropPath;
    private String originalTitle;
    private String overview;

    public ArrayList<Video> getVideos() {
        return videos;
    }

    public void setVideos(ArrayList<Video> videos) {
        this.videos = videos;
    }

    private ArrayList<Video> videos;


    private float popularity;
    private float voteAverage;
    private int voteCount;
    private int id;

    public int getId() {
        return id;
    }

    public String getPosterPath() {
        return String.format("https://image.tmdb.org/t/p/w342/%s", posterPath);
    }

    public String getBackdropPath() {
        return String.format("https://image.tmdb.org/t/p/w500/%s", backdropPath);
    }

    public String getOriginalTitle() {
        return this.originalTitle;
    }

    public float getPopularity() {
        return popularity;
    }

    public float getVoteAverage() {
        return voteAverage;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public String getOverview() {
        return overview;
    }

    private Movie(JSONObject jsonObject) throws JSONException {
        this.posterPath = jsonObject.getString("poster_path");
        this.backdropPath = jsonObject.getString("backdrop_path");
        this.originalTitle = jsonObject.getString("original_title");
        this.overview = jsonObject.getString("overview");
        this.popularity = (float) jsonObject.getDouble("popularity");
        this.voteAverage =(float) jsonObject.getDouble("vote_average");
        this.voteCount = jsonObject.getInt("vote_count");
        this.id = jsonObject.getInt("id");
    }

    public static ArrayList<Movie> fromJSONArray(JSONArray array) {
        ArrayList<Movie> results = new ArrayList<>();

        for (int i = 0; i < array.length(); i++) {
            try {
                results.add(new Movie(array.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        return results;
    }
}
