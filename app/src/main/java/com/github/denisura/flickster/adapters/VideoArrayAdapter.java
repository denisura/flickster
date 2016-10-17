package com.github.denisura.flickster.adapters;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.percent.PercentRelativeLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.denisura.flickster.R;
import com.github.denisura.flickster.models.Video;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VideoArrayAdapter extends ArrayAdapter<Video> {

    private Context mContext;


    public VideoArrayAdapter(Context context, List<Video> movies) {
        super(context, android.R.layout.simple_list_item_1, movies);
        mContext = context;
    }

    public static class ViewHolder {

        @BindView(R.id.ivVideo)
        public PercentRelativeLayout ivVideo;
        @BindView(R.id.tvName)
        public TextView tvName;
        @BindView(R.id.ivVideoImage)
        public ImageView ivVideoImage;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

        public void bindView(final Video video, final Context context) {
            ivVideoImage.setImageResource(0);
            tvName.setText(video.getName());
            String imgUrl = video.getThumbnail();

            Picasso.with(context)
                    .load(imgUrl)
                    .into(ivVideoImage);

            ivVideoImage.setContentDescription(video.getName());

            ivVideo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //todo start youtube activity
                    try {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + video.getKey()));
                        context.startActivity(intent);
                    } catch (ActivityNotFoundException ex) {
                        Intent intent = new Intent(Intent.ACTION_VIEW,
                                Uri.parse("http://www.youtube.com/watch?v=" + video.getKey()));
                        context.startActivity(intent);
                    }
                }
            });
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
            v = LayoutInflater.from(mContext).inflate(R.layout.item_video, parent, false);
            ViewHolder viewHolder = new ViewHolder(v);
            v.setTag(viewHolder);
        } else {
            v = convertView;
        }
        ViewHolder viewHolder = (ViewHolder) v.getTag();
        viewHolder.bindView(getItem(position), getContext());
        return v;
    }


}
