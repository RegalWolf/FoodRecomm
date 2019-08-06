package com.iqbalhasan.foodrecomm.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.iqbalhasan.foodrecomm.Adapter.HistoryMakananAdapter;
import com.iqbalhasan.foodrecomm.Interface.ServerApiInterface;
import com.iqbalhasan.foodrecomm.Model.HistoryMakanan;
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

public class DetailHistoryActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private String tanggal;
    private String kalori_dibutuhkan;
    private String kalori_dikonsumsi;

    private TextView text_tanggal;
    private TextView text_kalori_dibutuhkan;
    private TextView text_kalori_dikonsumsi;

    private RecyclerView recyclerViewHistoryMakanan;
    private TextView notFound;

    private ServerApiInterface serverApiInterface;

    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_history);

        Retrofit retrofit = new Retrofit
                .Builder()
                .baseUrl(SERVER_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        serverApiInterface = retrofit.create(ServerApiInterface.class);

        initDialog();

        Bundle bundleGet = getIntent().getExtras();

        tanggal = bundleGet.getString("tanggal");
        kalori_dibutuhkan = bundleGet.getString("kalori_dibutuhkan");
        kalori_dikonsumsi = bundleGet.getString("kalori_dikonsumsi");

        text_tanggal = findViewById(R.id.tanggal_history);
        text_kalori_dibutuhkan = findViewById(R.id.jml_kalori_dibutuhkan);
        text_kalori_dikonsumsi = findViewById(R.id.jml_kalori_dikonsumsi);
        recyclerViewHistoryMakanan = findViewById(R.id.recyclerViewHistoryMakanan);
        notFound = findViewById(R.id.text_not_found);

        text_tanggal.setText(tanggal);
        text_kalori_dibutuhkan.setText(kalori_dibutuhkan);
        text_kalori_dikonsumsi.setText(kalori_dikonsumsi);

        getHistoryMakanan();

        mRecyclerView = findViewById(R.id.recyclerViewHistoryMakanan);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);

        mRecyclerView.setLayoutManager(mLayoutManager);
    }

    private void initDialog(){
        dialog = new ProgressDialog(this);
        dialog.setTitle("Loading");
        dialog.setMessage("Sedang Memproses...");
    }

    private void getHistoryMakanan() {
        dialog.show();

        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);

        String token = sharedPreferences.getString(TOKEN, "token");

        Call<List<HistoryMakanan>> call = serverApiInterface.getHistoryMakanan(tanggal, token);

        call.enqueue(new Callback<List<HistoryMakanan>>() {
            @Override
            public void onResponse(Call<List<HistoryMakanan>> call, Response<List<HistoryMakanan>> response) {
                if (!response.isSuccessful()) {
                    Log.i("Code", String.valueOf(response.code()));
                    recyclerViewHistoryMakanan.setVisibility(View.GONE);
                    notFound.setVisibility(View.VISIBLE);
                    dialog.dismiss();
                    return;
                }

                List<HistoryMakanan> historyMakananList = response.body();

                Log.i("Body", String.valueOf(response.body()));

                mAdapter = new HistoryMakananAdapter(historyMakananList);

                mRecyclerView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();

                dialog.dismiss();
            }

            @Override
            public void onFailure(Call<List<HistoryMakanan>> call, Throwable t) {
                Log.i("Error", t.getMessage());
                dialog.dismiss();
            }
        });
    }

    public void kembaliButtonClick(View view) {
        onBackPressed();
    }
}
