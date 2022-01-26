package com.devpk.musicapp.adapter;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.devpk.musicapp.model.MusicFiles;
import com.devpk.musicapp.R;
import com.devpk.musicapp.activity.PlayerActivity;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.util.ArrayList;

@SuppressLint("RecyclerView")
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
    public void onBindViewHolder(@NonNull MyViewHolder holder,  int position) {
        MusicFiles musicFiles = musicFilesArrayList.get(position);
        holder.fileName.setText(musicFiles.getTitle());

        byte[] image = getAlbumArt(musicFiles.getPath());

        if (image != null) {
            Glide.with(context).asBitmap().load(image).into(holder.album_art);
        } else {
            Glide.with(context).load(android.R.drawable.stat_notify_error).into(holder.album_art);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, PlayerActivity.class);
                intent.putExtra("position", position);
                context.startActivity(intent);
            }
        });

        holder.menuDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                PopupMenu popupMenu = new PopupMenu(context, view);
                popupMenu.getMenuInflater().inflate(R.menu.popup, popupMenu.getMenu());
                popupMenu.show();
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getItemId() == R.id.idDelete) {
                            delete(position, view);
                        }
                        return true;
                    }
                });

            }
        });
    }

    private void delete(int position, View view) {

        Uri uri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                Long.parseLong(musicFilesArrayList.get(position).getId())); //     content://
        File file = new File(musicFilesArrayList.get(position).getPath());
        boolean deleted = file.delete(); // Delete your Song
        if (deleted) {
            context.getContentResolver().delete(uri, null, null);
            musicFilesArrayList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, musicFilesArrayList.size());
            Snackbar.make(view, "Song Delete : ", Snackbar.LENGTH_LONG).show();
        } else {
            //may be song in sd card
            Snackbar.make(view, "Can't be Delete : ", Snackbar.LENGTH_LONG).show();

        }

    }

    @Override
    public int getItemCount() {
        return musicFilesArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private ImageView album_art, menuDelete;
        private TextView fileName;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            album_art = itemView.findViewById(R.id.music_img);
            fileName = itemView.findViewById(R.id.music_file_name);
            menuDelete = itemView.findViewById(R.id.menuDelete);
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
