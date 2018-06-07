package pavanasahithi.movieapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Lenovo on 16-05-2018.
 */

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.RecyclerViewHolder> {
    Context c;
    ArrayList<VideoJSONPOJO> videoList;

    public VideoAdapter(MovieActivity movieActivity, ArrayList<VideoJSONPOJO> videoJSONPOJOs) {
        c = movieActivity;
        videoList = videoJSONPOJOs;
    }

    @Override
    public VideoAdapter.RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(c).inflate(R.layout.videos_recycler, parent, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        String s = videoList.get(position).getName() + ":" + videoList.get(position).getType();
        holder.textView.setText(s);
    }

    @Override
    public int getItemCount() {
        return videoList.size();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.id_video_path);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=" + "" + videoList.get(getAdapterPosition()).getKey()));
                    view.getContext().startActivity(intent);
                }
            });
        }
    }
}

