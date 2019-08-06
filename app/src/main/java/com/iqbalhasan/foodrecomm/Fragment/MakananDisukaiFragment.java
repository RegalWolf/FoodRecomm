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
import android.widget.TextView;

import com.iqbalhasan.foodrecomm.Adapter.MakananDisukaiAdapter;
import com.iqbalhasan.foodrecomm.Interface.ServerApiInterface;
import com.iqbalhasan.foodrecomm.Model.MakananDisukai;
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

public class MakananDisukaiFragment extends Fragment {

    private ServerApiInterface serverApiInterface;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private TextView notFound;

    private ProgressDialog dialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_makanan_disukai, container, false);

        Retrofit retrofit = new Retrofit
                .Builder()
                .baseUrl(SERVER_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        serverApiInterface = retrofit.create(ServerApiInterface.class);

        notFound = view.findViewById(R.id.text_not_found);

        initDialog();

        getMakananDisukai();

        mRecyclerView = view.findViewById(R.id.recyclerViewMakananDisukai);
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

    private void getMakananDisukai() {
        dialog.show();

        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);

        String token = sharedPreferences.getString(TOKEN, "token");

        Call<List<MakananDisukai>> call = serverApiInterface.getMakananDisukai(
                token
        );

        call.enqueue(new Callback<List<MakananDisukai>>() {
            @Override
            public void onResponse(Call<List<MakananDisukai>> call, Response<List<MakananDisukai>> response) {
                if (!response.isSuccessful()) {
                    Log.i("Code", String.valueOf(response.code()));
                    mRecyclerView.setVisibility(View.GONE);
                    notFound.setVisibility(View.VISIBLE);
                    dialog.dismiss();
                    return;
                }

                List<MakananDisukai> makananDisukaiList = response.body();

                mAdapter = new MakananDisukaiAdapter(makananDisukaiList);

                mRecyclerView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();

                dialog.dismiss();
            }

            @Override
            public void onFailure(Call<List<MakananDisukai>> call, Throwable t) {
                dialog.dismiss();
                Log.i("Error", t.getMessage());
            }
        });
    }
}
