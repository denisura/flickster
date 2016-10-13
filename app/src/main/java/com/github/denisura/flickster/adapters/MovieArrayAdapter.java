package com.github.denisura.flickster.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.denisura.flickster.R;
import com.github.denisura.flickster.models.Movie;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MovieArrayAdapter extends ArrayAdapter<Movie> {


    public MovieArrayAdapter(Context context, List<Movie> movies) {

        super(context, android.R.layout.simple_list_item_1, movies);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Movie movie = getItem(position);

        if (convertView == null) {
            LayoutInflater inflator = LayoutInflater.from(getContext());
            convertView = inflator.inflate(R.layout.item_movie, parent, false);
        }

        ImageView ivImage = (ImageView) convertView.findViewById(R.id.ivMovieImage);
        ivImage.setImageResource(0);

        TextView tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);

        TextView tvOverview = (TextView) convertView.findViewById(R.id.tvOverview);

        tvTitle.setText(movie.getOriginalTitle());
        tvOverview.setText(movie.getOverview());

        Picasso.with(getContext()).load(movie.getPosterPath()).into(ivImage);

        return convertView;
    }
}
