package com.example.calories.Fragment;

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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.calories.Adapter.MakananAdapter;
import com.example.calories.Interface.ServerApiInterface;
import com.example.calories.Model.Kalori;
import com.example.calories.Model.Makanan;
import com.example.calories.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.calories.Config.Config.SERVER_URL;
import static com.example.calories.Config.Config.SHARED_PREFS;
import static com.example.calories.Config.Config.TOKEN;

public class HomeFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    ProgressDialog dialog;

    private ServerApiInterface serverApiInterface;

    private TextView kaloriDibutuhkan;
    private TextView kaloriDikonsumsi;
    private TextView kaloriSisa;
    private TextView kondisiTubuh;
    private RecyclerView recyclerViewMakanan;
    private TextView notFound;
    private TextView kaloriSarapan;
    private TextView kaloriMakanSiang;
    private TextView kaloriMakanMalam;

    private Spinner spinner;


    View view;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);

        Retrofit retrofit = new Retrofit
                .Builder()
                .baseUrl(SERVER_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        serverApiInterface = retrofit.create(ServerApiInterface.class);

        spinner = view.findViewById(R.id.spinner_rekomendasi);

        initDialog();

        kaloriDibutuhkan = view.findViewById(R.id.jumlahKaloriDibutuhkan);
        kaloriDikonsumsi = view.findViewById(R.id.jumlahKaloriDikonsumsi);
        kaloriSisa = view.findViewById(R.id.jumlah_kalori);
        kondisiTubuh = view.findViewById(R.id.kondisi_tubuh);
        recyclerViewMakanan = view.findViewById(R.id.recyclerViewMakanan);
        notFound = view.findViewById(R.id.text_not_found);
        kaloriSarapan = view.findViewById(R.id.value_sarapan);
        kaloriMakanSiang = view.findViewById(R.id.value_makan_siang);
        kaloriMakanMalam = view.findViewById(R.id.value_makan_malam);

        getKalori();

        mRecyclerView = view.findViewById(R.id.recyclerViewMakanan);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());

        mRecyclerView.setLayoutManager(mLayoutManager);


        return view;
    }


    private void getKalori() {
        dialog.show();

        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);

        String token = sharedPreferences.getString(TOKEN, "token");

        Call<Kalori> call = serverApiInterface.getKalori(token);

        call.enqueue(new Callback<Kalori>() {
            @Override
            public void onResponse(Call<Kalori> call, Response<Kalori> response) {
                if (!response.isSuccessful()) {
                    Log.i("Code", String.valueOf(response.code()));

                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(response.errorBody().string());

                        String noKalori = jsonObject.getString("noKalori");

                        if (!noKalori.equals("null")) {
                            Log.i("noKalori", noKalori);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    dialog.dismiss();

                    return;
                }

                Kalori kalori = response.body();

                kaloriDibutuhkan.setText(kalori.getKaloriDibutuhkan() + " Kkal");

                if (String.valueOf(kalori.getKaloriDikonsumsi()).equals(null)) {
                    kaloriDikonsumsi.setText("0 Kkal");
                } else {
                    kaloriDikonsumsi.setText(kalori.getKaloriDikonsumsi() + " Kkal");
                }

                kaloriSisa.setText((kalori.getKaloriDibutuhkan() - kalori.getKaloriDikonsumsi()) + " Kkal");
                kondisiTubuh.setText("( " + kalori.getKondisiTubuh() + " )");

                final int kaloriDibutuhkan = kalori.getKaloriDibutuhkan() - kalori.getKaloriDikonsumsi();

                final int jmlKaloriSarapan = (int) (kaloriDibutuhkan * 0.3);
                final int jmlKaloriMakanSiang = (int) (kaloriDibutuhkan * 0.4);
                final int jmlKaloriMakanMalam = (int) (kaloriDibutuhkan * 0.3);

                kaloriSarapan.setText(String.valueOf(jmlKaloriSarapan));
                kaloriMakanSiang.setText(String.valueOf(jmlKaloriMakanSiang));
                kaloriMakanMalam.setText(String.valueOf(jmlKaloriMakanMalam));

                ArrayAdapter<CharSequence> adapter = ArrayAdapter
                        .createFromResource(getContext(), R.array.rekomendasi_makanan, android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adapter);
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        String text = parent.getItemAtPosition(position).toString();

                        if (text.equals("Rekomendasi Sarapan")) {
                            getRekomendasiMakanan(jmlKaloriSarapan);
                        } else if (text.equals("Rekomendasi Makan Siang")) {
                            getRekomendasiMakanan(jmlKaloriMakanSiang);
                        } else if (text.equals("Rekomendasi Makan Malam")) {
                            getRekomendasiMakanan(jmlKaloriMakanMalam);
                        } else if (text.equals("Semua Rekomendasi")) {
                            getRekomendasiMakanan(kaloriDibutuhkan);
                        } else if (text.equals("Rekomendasi Olahan Susu")) {
                            String kode_kategori = "PH";
                            getRekomendasiMakananKategori(kode_kategori, kaloriDibutuhkan);
                        } else if (text.equals("Rekomendasi Olahan Sayuran")) {
                            String kode_kategori = "PF";
                            getRekomendasiMakananKategori(kode_kategori, kaloriDibutuhkan);
                        } else if (text.equals("Rekomendasi Olahan Telur")) {
                            String kode_kategori = "PD";
                            getRekomendasiMakananKategori(kode_kategori, kaloriDibutuhkan);
                        } else if (text.equals("Rekomendasi Olahan Daging")) {
                            String kode_kategori = "PC";
                            getRekomendasiMakananKategori(kode_kategori, kaloriDibutuhkan);
                        } else if (text.equals("Rekomendasi Olahan Ikan, Kerang, Udang")) {
                            String kode_kategori = "PE";
                            getRekomendasiMakananKategori(kode_kategori, kaloriDibutuhkan);
                        } else if (text.equals("Rekomendasi Olahan Serelia, Umbi-umbian")) {
                            String kode_kategori = "PA";
                            getRekomendasiMakananKategori(kode_kategori, kaloriDibutuhkan);
                        } else if (text.equals("Rekomendasi Olahan Kacang, Biji-bijian")) {
                            String kode_kategori = "PB";
                            getRekomendasiMakananKategori(kode_kategori, kaloriDibutuhkan);
                        } else if (text.equals("Rekomendasi Olahan Serba-serbi")) {
                            String kode_kategori = "PJ";
                            getRekomendasiMakananKategori(kode_kategori, kaloriDibutuhkan);
                        } else if (text.equals("Rekomendasi Buah-buahan")) {
                            String kode_kategori = "GA";
                            getRekomendasiMakananKategori(kode_kategori, kaloriDibutuhkan);
                        } else if (text.equals("Makanan Semua Ketegori")) {
                            getMakanan();
                        } else if (text.equals("Makanan Olahan Susu")) {
                            String kode_kategori = "PH";
                            getMakananKategori(kode_kategori);
                        } else if (text.equals("Makanan Olahan Sayuran")) {
                            String kode_kategori = "PF";
                            getMakananKategori(kode_kategori);
                        } else if (text.equals("Makanan Olahan Telur")) {
                            String kode_kategori = "PH";
                            getMakananKategori(kode_kategori);
                        } else if (text.equals("Makanan Olahan Daging")) {
                            String kode_kategori = "PD";
                            getMakananKategori(kode_kategori);
                        } else if (text.equals("Makanan Olahan Ikan, Kerang, Udang")) {
                            String kode_kategori = "PE";
                            getMakananKategori(kode_kategori);
                        } else if (text.equals("Makanan Olahan Serelia, Umbi-umbian")) {
                            String kode_kategori = "PA";
                            getMakananKategori(kode_kategori);
                        } else if (text.equals("Makanan Olahan Kacang, Biji-bijian")) {
                            String kode_kategori = "PB";
                            getMakananKategori(kode_kategori);
                        } else if (text.equals("Makanan Olahan Serba-serbi")) {
                            String kode_kategori = "PJ";
                            getMakananKategori(kode_kategori);
                        } else if (text.equals("Buah-buahan")) {
                            String kode_kategori = "GA";
                            getMakananKategori(kode_kategori);
                        } else if (text.equals("Makanan Dianjurkan")) {
                            String prioritas = "Dianjurkan";
                            getMakananPrioritas(prioritas);
                        } else if (text.equals("Makanan Normal")) {
                            String prioritas = "Normal";
                            getMakananPrioritas(prioritas);
                        } else if (text.equals("Makanan Dibatasi")) {
                            String prioritas = "Dibatasi";
                            getMakananPrioritas(prioritas);
                        } else if (text.equals("Makanan Dihindari")) {
                            String prioritas = "Dihindari";
                            getMakananPrioritas(prioritas);
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }

            @Override
            public void onFailure(Call<Kalori> call, Throwable t) {
                Log.i("Error", t.getMessage());
            }
        });
    }

    private void initDialog(){
        dialog = new ProgressDialog(getContext());
        dialog.setTitle("Loading");
        dialog.setMessage("Sedang Memproses...");
    }

    private void getRekomendasiMakanan(int kalori) {
        dialog.show();

        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);

        String token = sharedPreferences.getString(TOKEN, "token");

        Call<List<Makanan>> call = serverApiInterface.getRekomendasiMakanan(
                kalori,
                token
        );

        call.enqueue(new Callback<List<Makanan>>() {
            @Override
            public void onResponse(Call<List<Makanan>> call, Response<List<Makanan>> response) {
                if (!response.isSuccessful()) {
                    Log.i("Code", String.valueOf(response.code()));
                    recyclerViewMakanan.setVisibility(View.GONE);
                    notFound.setVisibility(View.VISIBLE);
                    dialog.dismiss();
                    return;
                }

                List<Makanan> makananList = response.body();

                mAdapter = new MakananAdapter(makananList);

                mRecyclerView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();

                dialog.dismiss();
            }

            @Override
            public void onFailure(Call<List<Makanan>> call, Throwable t) {
                dialog.dismiss();
                Log.i("Error", t.getMessage());
            }
        });
    }

    private void getRekomendasiMakananKategori(String kode_kategori, int kalori) {
        dialog.show();

        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);

        String token = sharedPreferences.getString(TOKEN, "token");

        Call<List<Makanan>> call = serverApiInterface.getRekomendasiMakananKategori(
                kalori,
                kode_kategori,
                token
        );

        call.enqueue(new Callback<List<Makanan>>() {
            @Override
            public void onResponse(Call<List<Makanan>> call, Response<List<Makanan>> response) {
                if (!response.isSuccessful()) {
                    Log.i("Code", String.valueOf(response.code()));
                    recyclerViewMakanan.setVisibility(View.GONE);
                    notFound.setVisibility(View.VISIBLE);
                    dialog.dismiss();
                    return;
                }

                List<Makanan> makananList = response.body();

                mAdapter = new MakananAdapter(makananList);

                mRecyclerView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();

                dialog.dismiss();
            }

            @Override
            public void onFailure(Call<List<Makanan>> call, Throwable t) {
                dialog.dismiss();
                Log.i("Error", t.getMessage());
            }
        });
    }

    private void getMakanan() {
        dialog.show();

        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);

        String token = sharedPreferences.getString(TOKEN, "token");

        Call<List<Makanan>> call = serverApiInterface.getMakanan(
                token
        );

        call.enqueue(new Callback<List<Makanan>>() {
            @Override
            public void onResponse(Call<List<Makanan>> call, Response<List<Makanan>> response) {
                if (!response.isSuccessful()) {
                    Log.i("Code", String.valueOf(response.code()));
                    recyclerViewMakanan.setVisibility(View.GONE);
                    notFound.setVisibility(View.VISIBLE);
                    dialog.dismiss();
                    return;
                }

                List<Makanan> makananList = response.body();

                mAdapter = new MakananAdapter(makananList);

                mRecyclerView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();

                dialog.dismiss();
            }

            @Override
            public void onFailure(Call<List<Makanan>> call, Throwable t) {
                dialog.dismiss();
                Log.i("Error", t.getMessage());
            }
        });
    }

    private void getMakananKategori(String kode_kategori) {
        dialog.show();

        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);

        String token = sharedPreferences.getString(TOKEN, "token");

        Call<List<Makanan>> call = serverApiInterface.getMakananKategori(
                kode_kategori,
                token
        );

        call.enqueue(new Callback<List<Makanan>>() {
            @Override
            public void onResponse(Call<List<Makanan>> call, Response<List<Makanan>> response) {
                if (!response.isSuccessful()) {
                    Log.i("Code", String.valueOf(response.code()));
                    recyclerViewMakanan.setVisibility(View.GONE);
                    notFound.setVisibility(View.VISIBLE);
                    dialog.dismiss();
                    return;
                }

                List<Makanan> makananList = response.body();

                mAdapter = new MakananAdapter(makananList);

                mRecyclerView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();

                dialog.dismiss();
            }

            @Override
            public void onFailure(Call<List<Makanan>> call, Throwable t) {
                dialog.dismiss();
                Log.i("Error", t.getMessage());
            }
        });
    }

    private void getMakananPrioritas(String prioritas) {
        dialog.show();

        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);

        String token = sharedPreferences.getString(TOKEN, "token");

        Call<List<Makanan>> call = serverApiInterface.getMakananPrioritas(
                prioritas,
                token
        );

        call.enqueue(new Callback<List<Makanan>>() {
            @Override
            public void onResponse(Call<List<Makanan>> call, Response<List<Makanan>> response) {
                if (!response.isSuccessful()) {
                    Log.i("Code", String.valueOf(response.code()));
                    recyclerViewMakanan.setVisibility(View.GONE);
                    notFound.setVisibility(View.VISIBLE);
                    dialog.dismiss();
                    return;
                }

                List<Makanan> makananList = response.body();

                mAdapter = new MakananAdapter(makananList);

                mRecyclerView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();

                dialog.dismiss();
            }

            @Override
            public void onFailure(Call<List<Makanan>> call, Throwable t) {
                dialog.dismiss();
                Log.i("Error", t.getMessage());
            }
        });
    }
}
