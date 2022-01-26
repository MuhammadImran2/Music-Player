package com.devpk.musicapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.widget.ImageView;

import static com.devpk.musicapp.activity.MainActivity.musicFilesArrayList;

import com.bumptech.glide.Glide;
import com.devpk.musicapp.R;
import com.devpk.musicapp.adapter.AlbumDetailAdapter;
import com.devpk.musicapp.model.MusicFiles;

import java.util.ArrayList;

public class AlbumDetailActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ImageView album_photo;
    String albumName;
    private AlbumDetailAdapter adapter;
    ArrayList<MusicFiles> albumSong = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_detail);
        recyclerView = findViewById(R.id.recyclerview);
        album_photo = findViewById(R.id.album_photo);
        albumName = getIntent().getStringExtra("albumName");
        int j = 0;

        for (int i = 0; i < musicFilesArrayList.size(); i++) {
            if (albumName.equals(musicFilesArrayList.get(i).getAlbum())) {
                albumSong.add(j, musicFilesArrayList.get(i));
                j++;
            }

        }

        byte[] image = getAlbumArt(albumSong.get(0).getPath());
        if (image != null) {
            Glide.with(this).asBitmap().load(image).into(album_photo);
        } else {
            Glide.with(this).load(android.R.drawable.stat_notify_error).into(album_photo);
        }

    }


    private byte[] getAlbumArt(String path) {
        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
        mediaMetadataRetriever.setDataSource(path);
        byte[] art = mediaMetadataRetriever.getEmbeddedPicture();
        mediaMetadataRetriever.release();
        return art;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (albumSong.size() > 0) {
            adapter = new AlbumDetailAdapter(this, albumSong);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        }
    }
}


