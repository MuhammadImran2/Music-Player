package com.devpk.musicapp.activity;

import static com.devpk.musicapp.adapter.AlbumDetailAdapter.albumFiles;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.devpk.musicapp.model.MusicFiles;
import com.devpk.musicapp.R;
import com.devpk.musicapp.adapter.ViewPagerAdapter;
import com.devpk.musicapp.fragment.AlbumFragment;
import com.devpk.musicapp.fragment.SongsFragment;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    private ViewPager viewPager;
    private TabLayout tabLayout;
    private ViewPagerAdapter viewPagerAdapter;
    public static ArrayList<MusicFiles> musicFilesArrayList;
    public static boolean shuffleBoolean = false, repeatBoolean = false;
    private String MY_SORT_PREF = "sortByOrder";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        permission();
    }

    private void permission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        } else {
            musicFilesArrayList = getAllAudio(getApplicationContext());
            initView();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                musicFilesArrayList = getAllAudio(getApplicationContext());
                initView();
            } else {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
        }
    }

    private void initView() {
        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tab_layout);
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(new SongsFragment(), "Songs");
        viewPagerAdapter.addFragment(new AlbumFragment(), "Album");
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    public static ArrayList<MusicFiles> getAllAudio(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("sortByOrder", MODE_PRIVATE);
        String sortByOrder = sharedPreferences.getString("sorting", "sortByName");
        String order = null;
        ArrayList<MusicFiles> tempAudioList = new ArrayList<>();
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

        switch (sortByOrder) {
            case "sortByName":
                order = MediaStore.MediaColumns.DISPLAY_NAME + " ASC";
                break;

            case "sortByDate":
                order = MediaStore.MediaColumns.DATE_ADDED + " ASC";
                break;

            case "sortBySize":
                order = MediaStore.MediaColumns.SIZE + " DESC";
                break;
        }

        String[] projection = {
                MediaStore.Audio.Media.DATA, //For Path
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media._ID
        };
        Cursor cursor = context.getContentResolver().query(uri, projection,
                null, null, order);

        if (cursor != null) {

            while (cursor.moveToNext()) {
                String path = cursor.getString(0);
                String title = cursor.getString(1);
                String artist = cursor.getString(2);
                String album = cursor.getString(3);
                String duration = cursor.getString(4);
                String id = cursor.getString(5);

                MusicFiles musicFiles = new MusicFiles(path, title, artist, album, duration, id);
                tempAudioList.add(musicFiles);
            }

            cursor.close();

        }

        return tempAudioList;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search, menu);
        MenuItem menuItem = menu.findItem(R.id.search_option);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setOnQueryTextListener(this);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        String userInput = newText.toLowerCase();
        ArrayList<MusicFiles> myFile = new ArrayList<>();
        for (MusicFiles song : musicFilesArrayList) {
            if (song.getTitle().toLowerCase().contains(userInput)) {
                myFile.add(song);
            }
        }

        SongsFragment.adapter.updateSong(myFile);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        SharedPreferences.Editor editor = getSharedPreferences(MY_SORT_PREF, MODE_PRIVATE).edit();
        switch (item.getItemId()) {

            case R.id.by_Name:
                editor.putString("sorting", "sortByName");
                editor.apply();
                this.recreate();
                break;

            case R.id.by_Date:
                editor.putString("sorting", "sortByDate");
                editor.apply();
                this.recreate();
                break;

            case R.id.by_Size:
                editor.putString("sorting", "sortBySize");
                editor.apply();
                this.recreate();
                break;
        }

        return super.onOptionsItemSelected(item);

    }
}