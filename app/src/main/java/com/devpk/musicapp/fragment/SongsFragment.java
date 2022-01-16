package com.devpk.musicapp.fragment;

import static com.devpk.musicapp.activity.MainActivity.musicFilesArrayList;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.devpk.musicapp.R;
import com.devpk.musicapp.adapter.MusicAdapter;

public class SongsFragment extends Fragment {

    private RecyclerView recyclerView;
    MusicAdapter adapter;

    public SongsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_songs, container, false);
        recyclerView = view.findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        if (musicFilesArrayList.size() > 0) {
            adapter = new MusicAdapter(getActivity(), musicFilesArrayList);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        }

        return view;
    }
}