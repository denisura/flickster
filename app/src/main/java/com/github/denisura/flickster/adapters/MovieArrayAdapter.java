package com.github.denisura.flickster.adapters;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.percent.PercentRelativeLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.graphics.ColorUtils;
import android.support.v7.graphics.Palette;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.denisura.flickster.R;
import com.github.denisura.flickster.dialog.MovieDetailsFragment;
import com.github.denisura.flickster.models.Movie;
import com.github.denisura.flickster.models.Video;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

import static android.content.Context.WINDOW_SERVICE;

public class MovieArrayAdapter extends ArrayAdapter<Movie> {

    private Context mContext;

    private final static int VIEW_TYPE_COUNT = 2;
    private final static int VIEW_TYPE_SHMENCY = 0;
    private final static int VIEW_TYPE_FANCY = 1;
    private final static int VIEW_TYPE_UNKNOWN = -1;

    public static class ViewHolder {

        @BindView(R.id.ivMovie)
        public PercentRelativeLayout ivMovie;
        @BindView(R.id.tvTitle)
        public TextView tvTitle;
        @BindView(R.id.tvOverview)
        public TextView tvOverview;
        @BindView(R.id.ivMovieImage)
        public ImageView ivImage;


        private Target target;
        private int mMutedColor = 0xFF333333;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

        public void bindView(final Movie movie, final Context context) {
            ivImage.setImageResource(0);
            tvTitle.setText(movie.getOriginalTitle());

            tvOverview.setText(movie.getOverview());
            Display display = ((WindowManager) context.getSystemService(WINDOW_SERVICE))
                    .getDefaultDisplay();

            int orientation = display.getRotation();

            @StringRes int contentDescriptionId;
            @DrawableRes int imagePlaceholder;
            String imgUrl;
            if (orientation == Surface.ROTATION_90
                    || orientation == Surface.ROTATION_270) {
                imgUrl = movie.getBackdropPath();
                contentDescriptionId = R.string.format_backdrop_image_content_description;
                imagePlaceholder = R.drawable.placeholder;
            } else {
                imgUrl = movie.getPosterPath();
                contentDescriptionId = R.string.format_poster_image_content_description;
                imagePlaceholder = R.drawable.poster_placeholder;
            }

            target = new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    // Bitmap is loaded, use image here
                    if (bitmap != null) {
                        Palette p = Palette.generate(bitmap, 12);
                        ivImage.setImageBitmap(bitmap);
                        mMutedColor = p.getDarkMutedColor(0xFF333333);
                        ivMovie.setBackgroundColor(mMutedColor);
                    }
                }

                @Override
                public void onBitmapFailed(Drawable errorDrawable) {

                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {
                    ivImage.setImageDrawable(placeHolderDrawable);
                }
            };


            Picasso.with(context)
                    .load(imgUrl)
                    .placeholder(imagePlaceholder)
                    .error(imagePlaceholder)
                    .transform(new RoundedCornersTransformation(10, 10))
                    .into(target);

            String contentDescription = context.
                    getResources().
                    getString(contentDescriptionId, movie.getOriginalTitle());
            ivImage.setContentDescription(contentDescription);
            ivMovie.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentManager fm = ((FragmentActivity) context).getSupportFragmentManager();
                    MovieDetailsFragment showFragment = MovieDetailsFragment.newInstance(movie);
                    showFragment.show(fm, "dialog_movie_details");
                }
            });
        }
    }


    public static class FancyViewHolder {

        @BindView(R.id.ivMovie)
        public FrameLayout ivMovie;
        @BindView(R.id.tvTitle)
        public TextView tvTitle;
        @BindView(R.id.ivPlay)
        public ImageView ivPlay;
        @BindView(R.id.ivMovieImage)
        public ImageView ivImage;


        private Context mContext;
        private Target target;

        private int mMutedColor = 0xFF333333;


        FancyViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

        public void bindView(final Movie movie, final Context context) {

            mContext = context;
            ivImage.setImageResource(0);
            tvTitle.setText(movie.getOriginalTitle());
            String imgUrl = movie.getBackdropPath();
            @StringRes
            int contentDescriptionId = R.string.format_backdrop_image_content_description;
            target = new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    // Bitmap is loaded, use image here
                    if (bitmap != null) {
                        Palette p = Palette.generate(bitmap, 12);
                        ivImage.setImageBitmap(bitmap);
                        mMutedColor = p.getDarkMutedColor(0xFF333333);
                        tvTitle.setBackgroundColor(ColorUtils.setAlphaComponent(mMutedColor, 0xCC));
                        ivMovie.setBackgroundColor(mMutedColor);
                    }
                }

                @Override
                public void onBitmapFailed(Drawable errorDrawable) {

                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {
                    ivImage.setImageDrawable(placeHolderDrawable);
                }
            };

            Picasso.with(context)
                    .load(imgUrl)
                    .placeholder(R.drawable.placeholder)
                    .transform(new RoundedCornersTransformation(10, 10))
                    .into(target);

            String contentDescription = context.
                    getResources().
                    getString(contentDescriptionId, movie.getOriginalTitle());
            ivImage.setContentDescription(contentDescription);

            ArrayList<Video> videos = movie.getVideos();
            if (videos != null && videos.size() > 0) {

                final String source = videos.get(0).getKey();

                ivPlay.setVisibility(View.VISIBLE);
                ivPlay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        try {
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + source));
                            mContext.startActivity(intent);
                        } catch (ActivityNotFoundException ex) {
                            Intent intent = new Intent(Intent.ACTION_VIEW,
                                    Uri.parse("http://www.youtube.com/watch?v=" + source));
                            mContext.startActivity(intent);
                        }

                    }
                });

            } else {
                ivPlay.setVisibility(View.GONE);
                ivPlay.setOnClickListener(null);
            }


            ivMovie.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentManager fm = ((FragmentActivity) context).getSupportFragmentManager();
                    MovieDetailsFragment showFragment = MovieDetailsFragment.newInstance(movie);
                    showFragment.show(fm, "dialog_movie_details");
                }
            });
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
        View view;
        switch (viewType) {
            case VIEW_TYPE_SHMENCY: {
                view = LayoutInflater.from(context).inflate(R.layout.item_movie, parent, false);
                ViewHolder viewHolder = new ViewHolder(view);
                view.setTag(viewHolder);
                break;
            }
            case VIEW_TYPE_FANCY: {
                view = LayoutInflater.from(context).inflate(R.layout.item_movie_hr, parent, false);
                FancyViewHolder viewHolder = new FancyViewHolder(view);
                view.setTag(viewHolder);
                break;
            }
            default:
                view = LayoutInflater.from(context).inflate(layoutId, parent, false);
        }
        return view;
    }

    private void bindView(View view, Context context, int position) {

        final Movie movie = getItem(position);
        if (movie == null) {
            return;
        }

        int viewType = getItemViewType(position);
        switch (viewType) {
            case VIEW_TYPE_SHMENCY: {
                ViewHolder viewHolder = (ViewHolder) view.getTag();
                viewHolder.bindView(movie, getContext());
                break;
            }
            case VIEW_TYPE_FANCY: {
                FancyViewHolder viewHolder = (FancyViewHolder) view.getTag();
                viewHolder.bindView(movie, getContext());
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
