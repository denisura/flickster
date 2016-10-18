package com.github.denisura.flickster.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.github.denisura.flickster.R;
import com.github.denisura.flickster.adapters.VideoArrayAdapter;
import com.github.denisura.flickster.models.Movie;
import com.github.denisura.flickster.models.Video;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class MovieDetailsFragment extends DialogFragment {

    private Unbinder unbinder;

    private static final String ARG_ITEM = "item";

    @BindView(R.id.popularity)
    public TextView mPopularity;

    @BindView(R.id.tvRate)
    public RatingBar mTvRate;

    @BindView(R.id.synopsis)
    public TextView mSynopsis;

    @BindView(R.id.lvVideos)
    ListView lvItems;

    @BindView(R.id.labelVideos)
    public TextView mLabelVideos;

    ArrayList<Video> videos = new ArrayList<>();
    VideoArrayAdapter videoAdapter;

    public MovieDetailsFragment() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }

    public static MovieDetailsFragment newInstance(Movie movie) {
        MovieDetailsFragment frag = new MovieDetailsFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_ITEM, movie);
        frag.setArguments(args);
        return frag;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        @SuppressLint("InflateParams")
        View v = getActivity().getLayoutInflater().inflate(R.layout.dialog_movie_details, null);

        unbinder = ButterKnife.bind(this, v);
        final Movie movie = (Movie) getArguments().getSerializable(ARG_ITEM);

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());

        if (movie != null) {
            videos = movie.getVideos();
            dialogBuilder.setTitle(movie.getOriginalTitle())
                    .setView(v);

            mPopularity.setText(getContext().getString(R.string.popularity, movie.getPopularity()));
            mTvRate.setRating(movie.getVoteAverage() / 2);
            mSynopsis.setText(movie.getOverview());

            videoAdapter = new VideoArrayAdapter(getContext(), videos);
            lvItems.setAdapter(videoAdapter);
            if (videos.size() > 0) {
                lvItems.setVisibility(View.VISIBLE);
                mLabelVideos.setVisibility(View.VISIBLE);
            } else {
                lvItems.setVisibility(View.GONE);
                mLabelVideos.setVisibility(View.GONE);
            }
        }
        return dialogBuilder.create();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        unbinder.unbind();
        super.onDestroyView();
    }
}
