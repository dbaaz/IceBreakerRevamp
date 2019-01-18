package com.arbiter.droid.icebreakerprot1;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.google.android.material.card.MaterialCardView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PubViewAdapter extends RecyclerView.Adapter<PubViewAdapter.PubViewHolder> {

    private final Context mContext;
    private List<Pub> mPubList;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    static class PubViewHolder extends RecyclerView.ViewHolder {
        MaterialCardView mPubCard;
        TextView mTitle;
        TextView mDescription;
        TextView mTags;
        TextView mRating;
        PubViewHolder(View v) {
            super(v);
            mTitle = v.findViewById(R.id.title);
            mDescription = v.findViewById(R.id.description);
            mTags = v.findViewById(R.id.tag_list);
            mRating = v.findViewById(R.id.rating);
            mPubCard = v.findViewById(R.id.card_pub);
        }
    }

    PubViewAdapter(List<Pub> pubList, Context context) {
        this.mPubList = pubList;
        this.mContext = context;
    }

    @NonNull
    @Override
    public PubViewAdapter.PubViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, final int i) {
        final View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.card_pub, viewGroup, false);

        return new PubViewHolder(itemView);
    }

    public void onBindViewHolder(@NonNull final PubViewHolder viewHolder, int i) {
        String tagList = "";
        Pub pub = mPubList.get(i);
        viewHolder.mTitle.setText(pub.getName());
        viewHolder.mDescription.setText(pub.getDesc());
        for (String tag: pub.getTags()) {
            tagList += tag + ", ";
        }
        viewHolder.mTags.setText(tagList.substring(0, tagList.length() - 2));
        viewHolder.mRating.setText(Double.toString(pub.getRating()));

        viewHolder.mPubCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pubView = new Intent(mContext, PubActivity.class);
                pubView.putExtra("name", viewHolder.mTitle.getText());
                pubView.putExtra("tags", viewHolder.mTags.getText());
                pubView.putExtra("rating", viewHolder.mRating.getText());
                mContext.startActivity(pubView);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mPubList.size();
    }
}
