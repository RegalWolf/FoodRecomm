package com.example.calories.Fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.calories.Adapter.TutorialAdapter;
import com.example.calories.Interface.YoutubeApiInterface;
import com.example.calories.Model.Youtube.Item;
import com.example.calories.Model.Youtube.YoutubeApi;
import com.example.calories.R;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.calories.Config.Config.YOUTUBE_KEY;
import static com.example.calories.Config.Config.YOUTUBE_URL;

public class TutorialFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private YoutubeApiInterface youtubeApiInterface;

    private TextView fragmentTitle;
    private String namaMakanan;
    private TextView notFound;

    private ProgressDialog dialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tutorial, container, false);

        mRecyclerView = view.findViewById(R.id.recyclerViewTutorial);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());

        mRecyclerView.setLayoutManager(mLayoutManager);

        Retrofit retrofit = new Retrofit
                .Builder()
                .baseUrl(YOUTUBE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        youtubeApiInterface = retrofit.create(YoutubeApiInterface.class);

        initDialog();

        fragmentTitle = view.findViewById(R.id.fragment_title);
        notFound = view.findViewById(R.id.text_not_found);

        if (getArguments() != null) {
            namaMakanan = getArguments().getString("nama");

            getVideo();
        }

        return view;
    }

    private void initDialog(){
        dialog = new ProgressDialog(getContext());
        dialog.setTitle("Loading");
        dialog.setMessage("Sedang Memproses...");
    }

    private void getVideo() {
        dialog.show();

        fragmentTitle.setText("Tutorial Membuat " + namaMakanan);

        Call<YoutubeApi> call = youtubeApiInterface.getVideo(
                "Snippet",
                "Tutorial membuat " + namaMakanan,
                20,
                YOUTUBE_KEY
        );

        call.enqueue(new Callback<YoutubeApi>() {
            @Override
            public void onResponse(Call<YoutubeApi> call, Response<YoutubeApi> response) {
                if (!response.isSuccessful()) {
                    Log.i("Code", String.valueOf(response.code()));
                    dialog.dismiss();
                    return;
                }

                if (response.body().getItems().isEmpty()) {
                    mRecyclerView.setVisibility(View.GONE);
                    notFound.setVisibility(View.VISIBLE);
                    dialog.dismiss();
                    return;
                }

                List<Item> videoList = response.body().getItems();

                mAdapter = new TutorialAdapter(videoList);

                mRecyclerView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();

                dialog.dismiss();
            }

            @Override
            public void onFailure(Call<YoutubeApi> call, Throwable t) {
                dialog.dismiss();
                Log.i("Error", t.getMessage());
            }
        });
    }
}
