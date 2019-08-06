package com.iqbalhasan.foodrecomm.Adapter;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.iqbalhasan.foodrecomm.Activity.DetailTutorialActivity;
import com.iqbalhasan.foodrecomm.Model.Youtube.Item;
import com.iqbalhasan.foodrecomm.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class TutorialAdapter extends RecyclerView.Adapter<TutorialAdapter.YoutubeViewHolder> {

    private List<Item> mList;

    public static class YoutubeViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageTutorial;
        public TextView namaTutorial;
        public TextView channel;
        public TextView published;
        public View rootLayout;

        public YoutubeViewHolder(@NonNull View itemView) {
            super(itemView);

            imageTutorial = itemView.findViewById(R.id.image_tutorial);
            namaTutorial = itemView.findViewById(R.id.nama_tutorial);
            channel = itemView.findViewById(R.id.channel);
            published = itemView.findViewById(R.id.published);
            rootLayout = itemView;
        }
    }

    public TutorialAdapter(List<Item> list){
        this.mList = list;
    }

    @NonNull
    @Override
    public YoutubeViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.card_tutorial, viewGroup, false);
        YoutubeViewHolder youtubeViewHolder = new YoutubeViewHolder(v);
        return youtubeViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull YoutubeViewHolder youtubeViewHolder, int i) {
        final Item currentItem = mList.get(i);

        youtubeViewHolder.namaTutorial.setText(currentItem.getSnippet().getTitle());
        youtubeViewHolder.channel.setText(currentItem.getSnippet().getChannelTitle());
        youtubeViewHolder.published.setText(currentItem.getSnippet().getPublishedAt());
        Picasso.get().load(currentItem.getSnippet().getThumbnails().getHigh().getUrl())
                .into(youtubeViewHolder.imageTutorial);

        youtubeViewHolder.rootLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), DetailTutorialActivity.class) ;

                intent.putExtra("id", currentItem.getId().getVideoId());
                intent.putExtra("title", currentItem.getSnippet().getTitle());
                intent.putExtra("channel", currentItem.getSnippet().getChannelTitle());
                intent.putExtra("publishedAt", currentItem.getSnippet().getPublishedAt());
                intent.putExtra("deskripsi", currentItem.getSnippet().getDescription());

                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

}
