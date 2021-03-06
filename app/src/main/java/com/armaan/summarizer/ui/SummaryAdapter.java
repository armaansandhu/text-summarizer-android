package com.armaan.summarizer.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.armaan.summarizer.models.Article;
import com.armaan.summarizer.models.Summary;
import com.armaan.summarizer.R;
import com.armaan.summarizer.SavedSummary;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;


public class SummaryAdapter extends RecyclerView.Adapter<SummaryAdapter.SummaryViewHolder >{

    private Context mCtx;
    private ArrayList<Summary> summaries;

    public SummaryAdapter(Context mCtx, ArrayList<Summary> summaries) {
        this.mCtx = mCtx;
        this.summaries = summaries;
    }

    @NonNull
    @Override
    public SummaryViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.favourite_summary_list,null);
        return new SummaryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final SummaryViewHolder summaryViewHolder, final int i) {
        final Summary summary = summaries.get(i);
        summaryViewHolder.titleText.setText(summary.getTitle());
        summaryViewHolder.descriptionText.setText(summary.getText());
        if(summary.getImageUrl() != null)
            Picasso.get().load(summary.getImageUrl()).into(summaryViewHolder.thumbnail);
        else
            summaryViewHolder.thumbnail.setVisibility(View.GONE);
        summaryViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), SavedSummary.class);
                intent.putExtra("savedSummary", summaries.get(i));
                mCtx.startActivity(intent, (Bundle) sharedAnimation(view,summaryViewHolder));

            }
        });
    }

    public Object sharedAnimation(View view,SummaryViewHolder holder){
        Pair[] pairs = new Pair[2];
        pairs[0] = new Pair(holder.titleText ,"summaryTitle");
        pairs[1] = new Pair(holder.titleText ,"summaryImage");
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) view.getContext(),pairs );
        return options.toBundle();
    }


    @Override
    public int getItemCount() {
        return summaries.size();
    }

    class SummaryViewHolder extends RecyclerView.ViewHolder{

        TextView titleText;
        TextView descriptionText;
        ImageView thumbnail;

        public SummaryViewHolder(@NonNull View itemView) {
            super(itemView);

            titleText = itemView.findViewById(R.id.listTitle);
            descriptionText = itemView.findViewById(R.id.listDescription);
            thumbnail = itemView.findViewById(R.id.listThumbnail);

        }
    }
}