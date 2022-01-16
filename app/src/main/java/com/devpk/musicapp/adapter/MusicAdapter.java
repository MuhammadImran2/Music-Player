package com.devpk.musicapp.adapter;

import android.content.Context;
import android.media.Image;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.devpk.musicapp.Model.MusicFiles;
import com.devpk.musicapp.R;

import java.util.ArrayList;

public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<MusicFiles> musicFilesArrayList;

    public MusicAdapter(Context context, ArrayList<MusicFiles> musicFilesArrayList) {
        this.context = context;
        this.musicFilesArrayList = musicFilesArrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.music_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.fileName.setText(musicFilesArrayList.get(position).getTitle());
        byte[] image = getAlbumArt(musicFilesArrayList.get(position).getPath());

        if (image != null) {
            Glide.with(context).asBitmap().load(image).into(holder.album_art);
        } else {
            Glide.with(context).load(android.R.drawable.stat_notify_error).into(holder.album_art);
        }

    }

    @Override
    public int getItemCount() {
        return musicFilesArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private ImageView album_art;
        private TextView fileName;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            album_art = itemView.findViewById(R.id.music_img);
            fileName = itemView.findViewById(R.id.music_file_name);
        }

    }

    private byte[] getAlbumArt(String path) {
        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
        mediaMetadataRetriever.setDataSource(path);
        byte[] art = mediaMetadataRetriever.getEmbeddedPicture();
        mediaMetadataRetriever.release();
        return art;
    }
}
