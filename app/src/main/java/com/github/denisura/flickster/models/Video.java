package com.github.denisura.flickster.models;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

public class Video implements Serializable {

    private String key;

    private String name;

    private String site;

    public String getSite() {
        return site;
    }

    public String getKey() {
        return key;
    }

    public String getName() {
        return name;
    }


    public String getThumbnail() {
        return String.format("http://img.youtube.com/vi/%s/0.jpg", key);
    }

    private Video(JSONObject jsonObject) throws JSONException {
        this.key = jsonObject.getString("key");
        this.name = jsonObject.getString("name");
        this.site = jsonObject.getString("site");
    }

    public static ArrayList<Video> fromJSONArray(JSONArray array) {
        ArrayList<Video> results = new ArrayList<>();

        for (int i = 0; i < array.length(); i++) {
            try {
                results.add(new Video(array.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        return results;
    }
}
