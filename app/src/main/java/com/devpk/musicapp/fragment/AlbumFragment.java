package com.devpk.musicapp.fragment;

import static com.devpk.musicapp.activity.MainActivity.musicFilesArrayList;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.devpk.musicapp.R;
import com.devpk.musicapp.adapter.AlbumAdapter;
import com.devpk.musicapp.adapter.MusicAdapter;

public class AlbumFragment extends Fragment {


    private RecyclerView recyclerView;
    AlbumAdapter adapter;

    public AlbumFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_album, container, false);
        recyclerView = view.findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        if (musicFilesArrayList.size() > 0) {
            adapter = new AlbumAdapter(getActivity(), musicFilesArrayList);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2));
        }
        return view;
    }
}