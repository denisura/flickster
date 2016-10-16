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

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.content.Context.WINDOW_SERVICE;

public class MovieArrayAdapter extends ArrayAdapter<Movie> {

    private Context mContext;

    private final static int VIEW_TYPE_COUNT = 2;
    private final static int VIEW_TYPE_SHMENCY = 0;
    private final static int VIEW_TYPE_FANCY = 1;
    private final static int VIEW_TYPE_UNKNOWN = -1;

    public  static class ViewHolder {
        @BindView(R.id.tvTitle)
        public TextView tvTitle;
        @BindView(R.id.tvOverview)
        public TextView tvOverview;
        @BindView(R.id.ivMovieImage)
        public ImageView ivImage;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    public MovieArrayAdapter(Context context, List<Movie> movies) {
        super(context, android.R.layout.simple_list_item_1, movies);
        mContext = context;
    }


    private View newView(Context context, int position, ViewGroup parent) {
        // Choose the layout type
        int viewType = getItemViewType(position);
        int layoutId = -1;
        switch (viewType) {
            case VIEW_TYPE_SHMENCY: {
                layoutId = R.layout.item_movie;
                break;
            }
            case VIEW_TYPE_FANCY: {
                layoutId = R.layout.item_movie_hr;
                break;
            }
        }
        View view = LayoutInflater.from(context).inflate(layoutId, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);

        return view;
    }

    private void bindView(View view, Context context, int position) {

        Movie movie = getItem(position);
        if (movie == null) {
            return;
        }
        ViewHolder viewHolder = (ViewHolder) view.getTag();

        int viewType = getItemViewType(position);
        switch (viewType) {
            case VIEW_TYPE_SHMENCY: {
                viewHolder.ivImage.setImageResource(0);
                viewHolder.tvTitle.setText(movie.getOriginalTitle());

                viewHolder.tvOverview.setText(movie.getOverview());
                Display display = ((WindowManager) getContext().getSystemService(WINDOW_SERVICE))
                        .getDefaultDisplay();

                int orientation = display.getRotation();

                @StringRes int contentDescriptionId;
                String imgUrl;
                if (orientation == Surface.ROTATION_90
                        || orientation == Surface.ROTATION_270) {
                    imgUrl = movie.getBackdropPath();
                    contentDescriptionId = R.string.format_backdrop_image_content_description;
                } else {
                    imgUrl = movie.getPosterPath();
                    contentDescriptionId = R.string.format_poster_image_content_description;
                }

                Picasso.with(getContext())
                        .load(imgUrl)
                        .placeholder(R.drawable.placeholder)
                        .into(viewHolder.ivImage);

                String contentDescription = getContext().
                        getResources().
                        getString(contentDescriptionId, movie.getOriginalTitle());
                viewHolder.ivImage.setContentDescription(contentDescription);

                break;
            }
            case VIEW_TYPE_FANCY: {

                viewHolder.ivImage.setImageResource(0);
                viewHolder.tvTitle.setText(movie.getOriginalTitle());
                viewHolder.tvOverview.setText(movie.getOverview());
                String imgUrl = movie.getBackdropPath();
                @StringRes
                int contentDescriptionId = R.string.format_backdrop_image_content_description;

                Picasso.with(getContext())
                        .load(imgUrl)
                        .placeholder(R.drawable.placeholder)
                        .into(viewHolder.ivImage);

                String contentDescription = getContext().
                        getResources().
                        getString(contentDescriptionId, movie.getOriginalTitle());
                viewHolder.ivImage.setContentDescription(contentDescription);

                break;
            }
        }
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        if (getItem(position) == null) {
            throw new IllegalStateException("couldn't move cursor to position " + position);
        }
        View v;
        if (convertView == null) {
            v = newView(mContext, position, parent);
        } else {
            v = convertView;
        }
        bindView(v, mContext, position);
        return v;
    }

    // Returns the number of types of Views that will be created by getView(int, View, ViewGroup)
    @Override
    public int getViewTypeCount() {
        // Returns the number of types of Views that will be created by this adapter
        // Each type represents a set of views that can be converted
        return VIEW_TYPE_COUNT;
    }

    @Override
    public int getItemViewType(int position) {
        Movie movie = getItem(position);
        if (movie == null) {
            return VIEW_TYPE_UNKNOWN;
        }
        return (movie.getVoteAverage() > 0.5) ? VIEW_TYPE_FANCY : VIEW_TYPE_SHMENCY;
    }
}
