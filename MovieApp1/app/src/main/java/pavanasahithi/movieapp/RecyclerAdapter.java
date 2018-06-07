package pavanasahithi.movieapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Lenovo on 05-05-2018.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.RecyclerViewHolder> {
    Context c;
    ArrayList<JsonPOJO> jsonPOJOs;

    public RecyclerAdapter(Context c, ArrayList<JsonPOJO> jsonPOJOs) {
        this.c = c;
        this.jsonPOJOs = jsonPOJOs;
    }

    @Override
    public RecyclerAdapter.RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(c).inflate(R.layout.poster_recycler, parent, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerAdapter.RecyclerViewHolder holder, final int position) {
        JSONConnection js = new JSONConnection();
        String s = js.buildImageUrl(jsonPOJOs.get(position).getPoster_path()).toString();
        Picasso.with(c).load(s).placeholder(R.drawable.error).error(R.drawable.error).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        if(jsonPOJOs==null){
            return 0;
        }
        return jsonPOJOs.size();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.id_recycler_poster);

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    Toast.makeText(c, jsonPOJOs.get(position).getTitle(), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(c, MovieActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("BACK_DROP_PATH", jsonPOJOs.get(position).getBackdrop_path());
                    bundle.putString("POSTER_PATH", jsonPOJOs.get(position).getPoster_path());
                    bundle.putString("TITLE_ORIGINAL", jsonPOJOs.get(position).getOriginal_title());
                    bundle.putString("TITLE", jsonPOJOs.get(position).getTitle());
                    bundle.putDouble("RATING", jsonPOJOs.get(position).getVote_average());
                    bundle.putString("RELEASE_DATE", jsonPOJOs.get(position).getRelease_date());
                    bundle.putString("LANGUAGE", jsonPOJOs.get(position).getOriginal_language());
                    bundle.putString("SYNOPSIS", jsonPOJOs.get(position).getOverview());
                    bundle.putString("ID", jsonPOJOs.get(position).getId());
                    intent.putExtras(bundle);
                    view.getContext().startActivity(intent);
                }
            });
        }
    }
}
