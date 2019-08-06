package com.iqbalhasan.foodrecomm.Activity;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.iqbalhasan.foodrecomm.Adapter.MakananAdapter;
import com.iqbalhasan.foodrecomm.Interface.ServerApiInterface;
import com.iqbalhasan.foodrecomm.Model.Makanan;
import com.iqbalhasan.foodrecomm.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.iqbalhasan.foodrecomm.Config.Config.SERVER_URL;
import static com.iqbalhasan.foodrecomm.Config.Config.SHARED_PREFS;
import static com.iqbalhasan.foodrecomm.Config.Config.TOKEN;

public class PencarianMakananActivity extends AppCompatActivity {

    private ServerApiInterface serverApiInterface;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private TextView queryPencarian;
    private TextView pencarianMsg;
    private TextView notFound;

    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pencarian_makanan);

        Retrofit retrofit = new Retrofit
                .Builder()
                .baseUrl(SERVER_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        serverApiInterface = retrofit.create(ServerApiInterface.class);

        initDialog();

        queryPencarian = findViewById(R.id.editText_pencarian);
        pencarianMsg = findViewById(R.id.pencarian_msg);
        notFound = findViewById(R.id.text_not_found);

        mRecyclerView = findViewById(R.id.recyclerViewCariMakanan);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);

        mRecyclerView.setLayoutManager(mLayoutManager);
    }

    private void initDialog() {
        dialog = new ProgressDialog(this);
        dialog.setTitle("Loading");
        dialog.setMessage("Sedang Memproses...");
    }

    public void pencarianMakananOnClick(View view) {
        dialog.show();

        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);

        String token = sharedPreferences.getString(TOKEN, "TOKEN");

        String query = queryPencarian.getText().toString();

        pencarianMsg.setText("");
        pencarianMsg.setVisibility(View.GONE);

        mRecyclerView.setVisibility(View.VISIBLE);
        notFound.setVisibility(View.GONE);

        Call<List<Makanan>> call = serverApiInterface.cariMakanan(query, token);

        call.enqueue(new Callback<List<Makanan>>() {
            @Override
            public void onResponse(Call<List<Makanan>> call, Response<List<Makanan>> response) {
                if (!response.isSuccessful()) {
                    Log.i("Code", String.valueOf(response.code()));

                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(response.errorBody().string());

                        String pencarianMessage = jsonObject.getString("nama");

                        if (!pencarianMessage.equals("null")) {
                            pencarianMsg.setText(pencarianMessage);
                            pencarianMsg.setVisibility(View.VISIBLE);
                        } else {
                            mRecyclerView.setVisibility(View.GONE);
                            notFound.setVisibility(View.VISIBLE);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    dialog.dismiss();
                    return;
                }

                List<Makanan> pencarianMakananList = response.body();

                mAdapter = new MakananAdapter(pencarianMakananList);

                mRecyclerView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();

                dialog.dismiss();
            }

            @Override
            public void onFailure(Call<List<Makanan>> call, Throwable t) {
                Log.i("Error", t.getMessage());
                dialog.dismiss();
            }
        });
    }
}
