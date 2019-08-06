package com.iqbalhasan.foodrecomm.Fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
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

import com.iqbalhasan.foodrecomm.Adapter.HistoryKaloriAdapter;
import com.iqbalhasan.foodrecomm.Interface.ServerApiInterface;
import com.iqbalhasan.foodrecomm.Model.HistoryKalori;
import com.iqbalhasan.foodrecomm.R;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.iqbalhasan.foodrecomm.Config.Config.SERVER_URL;
import static com.iqbalhasan.foodrecomm.Config.Config.SHARED_PREFS;
import static com.iqbalhasan.foodrecomm.Config.Config.TOKEN;

public class HistoryFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private ServerApiInterface serverApiInterface;

    private ProgressDialog dialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);

        Retrofit retrofit = new Retrofit
                .Builder()
                .baseUrl(SERVER_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        serverApiInterface = retrofit.create(ServerApiInterface.class);

        initDialog();

        getHistoryKalori();

        mRecyclerView = view.findViewById(R.id.recyclerViewHistoryKalori);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());

        mRecyclerView.setLayoutManager(mLayoutManager);

        return view;
    }

    private void initDialog(){
        dialog = new ProgressDialog(getContext());
        dialog.setTitle("Loading");
        dialog.setMessage("Sedang Memproses...");
    }

    private void getHistoryKalori() {
        dialog.show();

        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);

        String token = sharedPreferences.getString(TOKEN, "token");

        Call<List<HistoryKalori>> call = serverApiInterface.getHistoryKalori(token);

        call.enqueue(new Callback<List<HistoryKalori>>() {
            @Override
            public void onResponse(Call<List<HistoryKalori>> call, Response<List<HistoryKalori>> response) {
                if (!response.isSuccessful()) {
                    Log.i("Code", String.valueOf(response.code()));
                    dialog.dismiss();
                    return;
                }

                List<HistoryKalori> historyKaloriList = response.body();

                Log.i("Body", String.valueOf(response.body()));

                mAdapter = new HistoryKaloriAdapter(historyKaloriList);

                mRecyclerView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();

                dialog.dismiss();
            }

            @Override
            public void onFailure(Call<List<HistoryKalori>> call, Throwable t) {
                dialog.dismiss();
                Log.i("Error", t.getMessage());
            }
        });
    }
}
