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

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.RecyclerViewholder> {
    Context c;
    ArrayList<ReviewJSONPOJO> reviewJSONPOJOs;

    public ReviewAdapter(MovieActivity movieActivity, ArrayList<ReviewJSONPOJO> reviewJSONPOJOs) {
        c = movieActivity;
        this.reviewJSONPOJOs = reviewJSONPOJOs;
    }

    @Override
    public ReviewAdapter.RecyclerViewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(c).inflate(R.layout.reviews_recycler, parent, false);
        return new ReviewAdapter.RecyclerViewholder(view);
    }

    @Override
    public void onBindViewHolder(ReviewAdapter.RecyclerViewholder holder, int position) {
        holder.link.setText(reviewJSONPOJOs.get(position).getUrl());
        holder.author.setText(reviewJSONPOJOs.get(position).getAuthor());
        holder.review.setText(reviewJSONPOJOs.get(position).getContent());
    }

    @Override
    public int getItemCount() {
        return reviewJSONPOJOs.size();
    }

    public class RecyclerViewholder extends RecyclerView.ViewHolder {
        TextView author, review, link;

        public RecyclerViewholder(View itemView) {
            super(itemView);
            author = (TextView) itemView.findViewById(R.id.id_review_author);
            review = (TextView) itemView.findViewById(R.id.id_review);
            link = (TextView) itemView.findViewById(R.id.id_review_link);
            link.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(reviewJSONPOJOs.get(getAdapterPosition()).getUrl()));
                    view.getContext().startActivity(intent);
                }
            });
        }
    }
}
