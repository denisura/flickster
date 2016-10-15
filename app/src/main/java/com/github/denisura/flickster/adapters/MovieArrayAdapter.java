package com.github.denisura.flickster.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.denisura.flickster.R;
import com.github.denisura.flickster.models.Movie;
import com.squareup.picasso.Picasso;

import java.util.List;

import static android.content.Context.WINDOW_SERVICE;
import static com.github.denisura.flickster.R.id.tvTitle;

public class MovieArrayAdapter extends ArrayAdapter<Movie> {

    private static class ViewHolder {
        TextView tvTitle;
        TextView tvOverview;
        ImageView ivImage;
    }

    public MovieArrayAdapter(Context context, List<Movie> movies) {
        super(context, android.R.layout.simple_list_item_1, movies);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        Movie movie = getItem(position);

        if (movie == null) {
            return convertView;
        }

        ViewHolder viewHolder; // view lookup cache stored in tag
        if (convertView == null) {
            // If there's no view to re-use, inflate a brand new view for row
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_movie, parent, false);
            viewHolder.tvOverview = (TextView) convertView.findViewById(R.id.tvOverview);
            viewHolder.tvTitle = (TextView) convertView.findViewById(tvTitle);
            viewHolder.ivImage = (ImageView) convertView.findViewById(R.id.ivMovieImage);
            // Cache the viewHolder object inside the fresh view
            convertView.setTag(viewHolder);
        } else {
            // View is being recycled, retrieve the viewHolder object from tag
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.ivImage.setImageResource(0);
        viewHolder.tvTitle.setText(movie.getOriginalTitle());

        viewHolder.tvOverview.setText(movie.getOverview());
        Display display = ((WindowManager) getContext().getSystemService(WINDOW_SERVICE))
                .getDefaultDisplay();

        int orientation = display.getRotation();

        @StringRes int contentDescriptionId;

        if (orientation == Surface.ROTATION_90
                || orientation == Surface.ROTATION_270) {
            Picasso.with(getContext()).load(movie.getBackdropPath()).into(viewHolder.ivImage);
            contentDescriptionId = R.string.format_backdrop_image_content_description;
        } else {
            Picasso.with(getContext()).load(movie.getPosterPath()).into(viewHolder.ivImage);
            contentDescriptionId = R.string.format_poster_image_content_description;
        }
        String contentDescription = getContext().
                getResources().
                getString(contentDescriptionId, movie.getOriginalTitle());
        viewHolder.ivImage.setContentDescription(contentDescription);

        return convertView;
    }
}
