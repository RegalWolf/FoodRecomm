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

import com.example.calories.Adapter.RestoranAdapter;
import com.example.calories.Interface.ZomatoApiInterface;
import com.example.calories.Model.Zomato.Restaurant;
import com.example.calories.Model.Zomato.ZomatoApi;
import com.example.calories.R;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.calories.Config.Config.ZOMATO_URL;

public class RestoranFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private ZomatoApiInterface zomatoApiInterface;

    private String namaMakanan;
    private double lattitude;
    private double longitude;

    private TextView notFound;
    private TextView fragmentTitle;

    private ProgressDialog dialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_restoran, container, false);

        Retrofit retrofit = new Retrofit
                .Builder()
                .baseUrl(ZOMATO_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        zomatoApiInterface = retrofit.create(ZomatoApiInterface.class);

        initDialog();

        fragmentTitle = view.findViewById(R.id.fragment_title);
        notFound = view.findViewById(R.id.text_not_found);

        if (getArguments() != null) {
            namaMakanan = getArguments().getString("nama");
            lattitude = Double.parseDouble(getArguments().getString("lattitude"));
            longitude = Double.parseDouble(getArguments().getString("longitude"));

            getRestoran();
        }

        mRecyclerView = view.findViewById(R.id.recyclerViewRestoran);
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

    private void getRestoran() {
        dialog.show();

        fragmentTitle.setText("Restoran " + namaMakanan);

        Call<ZomatoApi> call = zomatoApiInterface.getRestoran(
                namaMakanan,
                lattitude,
                longitude,
                "real_distance",
                "asc"
        );

        call.enqueue(new Callback<ZomatoApi>() {
            @Override
            public void onResponse(Call<ZomatoApi> call, Response<ZomatoApi> response) {
                if (!response.isSuccessful()) {
                    Log.i("Code", String.valueOf(response.code()));
                    dialog.dismiss();
                    return;
                }

                if (response.body().getRestaurants().isEmpty()) {
                    mRecyclerView.setVisibility(View.GONE);
                    notFound.setVisibility(View.VISIBLE);
                    dialog.dismiss();
                    return;
                }

                List<Restaurant> restoranList = response.body().getRestaurants();

                Log.i("Hasil", restoranList.get(0).getRestaurant().getId());

                mAdapter = new RestoranAdapter(restoranList, getContext(), lattitude, longitude);

                mRecyclerView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();

                dialog.dismiss();
            }

            @Override
            public void onFailure(Call<ZomatoApi> call, Throwable t) {
                dialog.dismiss();
                Log.i("Error", t.getMessage());
            }
        });
    }
}
