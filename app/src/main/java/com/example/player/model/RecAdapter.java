package com.example.player.model;

import android.content.Context;
import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.player.PlayerActivity;
import com.example.player.R;

import java.util.ArrayList;
import java.util.logging.Handler;

public class RecAdapter extends RecyclerView.Adapter<RecAdapter.Hodler> {

ArrayList<MusicFiles> musicFiles;
Context context;
    public RecAdapter(ArrayList<MusicFiles> musicFiles , Context context) {
        this.musicFiles = musicFiles;
        this.context = context;
    }

    @NonNull
    @Override
    public Hodler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.musicadpatercustom,parent,false);
        Hodler hodler = new Hodler(view);
        return hodler;
    }

    @Override
    public void onBindViewHolder(@NonNull Hodler holder, int position) {
        holder.tv.setText( musicFiles.get(position).getTitle());
        byte [] image = AlbumArt(musicFiles.get(position).getPath());

        if(image != null)
        {
            Glide.with(context).asBitmap()
                    .load(image)
                    .into(holder.iv);
        }
        else
        {
            Glide.with(context).asBitmap()
                    .load(R.drawable.ic_song)
                    .into(holder.iv);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PlayerActivity.class);
                intent.putExtra("position",position);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return musicFiles.size();
    }

    class Hodler extends RecyclerView.ViewHolder
    {
         ImageView iv ;
         TextView tv;
        public Hodler(@NonNull View itemView) {
            super(itemView);
            iv = itemView.findViewById(R.id.custom_iv);
            tv = itemView.findViewById(R.id.custom_tv);

        }
    }

    private byte[] AlbumArt(String uri)
    {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(uri);
        byte [] art = retriever.getEmbeddedPicture();
        retriever.release();
        return art;

    }
}
