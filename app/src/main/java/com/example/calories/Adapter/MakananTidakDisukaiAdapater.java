package com.example.calories.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.calories.Activity.DetailMakananActivity;
import com.example.calories.Interface.ServerApiInterface;
import com.example.calories.Model.HapusMakananTidakDisukai;
import com.example.calories.Model.MakananTidakDisukai;
import com.example.calories.R;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.calories.Config.Config.SERVER_URL;
import static com.example.calories.Config.Config.SHARED_PREFS;
import static com.example.calories.Config.Config.TOKEN;

public class MakananTidakDisukaiAdapater extends RecyclerView.Adapter<MakananTidakDisukaiAdapater.MakananTidakDisukaiViewHolder> {

    private List<MakananTidakDisukai> mList;

    private ServerApiInterface serverApiInterface;

    public static class MakananTidakDisukaiViewHolder extends RecyclerView.ViewHolder {
        public TextView namaMakanan;
        public TextView jumlahKalori;
        public TextView jumlahProtein;
        public TextView jumlahLemak;
        public TextView jumlahKarbohidrat;
        public TextView jumlahKalsium;
        public TextView jumlahFosfor;
        public TextView jumlahVitaminA;
        public TextView jumlahVitaminB;
        public TextView jumlahVitaminC;
        public TextView jumlahZatBesi;

        public ImageView btnHapusMakananTidakDisukai;

        public View rootLayout;

        public MakananTidakDisukaiViewHolder(@NonNull View itemView) {
            super(itemView);

            namaMakanan = itemView.findViewById(R.id.nama_makanan);
            jumlahKalori = itemView.findViewById(R.id.jml_kalori);
            jumlahProtein = itemView.findViewById(R.id.jml_protein);
            jumlahLemak = itemView.findViewById(R.id.jml_lemak);
            jumlahKarbohidrat = itemView.findViewById(R.id.jml_karbohidrat);
            jumlahKalsium = itemView.findViewById(R.id.jml_kalsium);
            jumlahFosfor = itemView.findViewById(R.id.jml_fosfor);
            jumlahVitaminA = itemView.findViewById(R.id.jml_vit_a);
            jumlahVitaminB = itemView.findViewById(R.id.jml_vit_b);
            jumlahVitaminC = itemView.findViewById(R.id.jml_vit_c);
            jumlahZatBesi = itemView.findViewById(R.id.jml_zat_besi);
            btnHapusMakananTidakDisukai = itemView.findViewById(R.id.btn_hapus);
            rootLayout = itemView;
        }
    }

    public MakananTidakDisukaiAdapater(List<MakananTidakDisukai> list){
        this.mList = list;

        Retrofit retrofit = new Retrofit
                .Builder()
                .baseUrl(SERVER_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        serverApiInterface = retrofit.create(ServerApiInterface.class);
    }

    @NonNull
    @Override
    public MakananTidakDisukaiViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_makanan_tidak_disukai, viewGroup, false);
        MakananTidakDisukaiAdapater.MakananTidakDisukaiViewHolder makananTidakDisukaiViewHolder = new MakananTidakDisukaiViewHolder(v);
        return makananTidakDisukaiViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MakananTidakDisukaiViewHolder makananTidakDisukaiViewHolder, final int i) {
        final MakananTidakDisukai currentItem = mList.get(i);

        final int makanan_id = Integer.parseInt(currentItem.getMakanan_id());

        makananTidakDisukaiViewHolder.namaMakanan.setText(currentItem.getNama() + " (100 g)");
        makananTidakDisukaiViewHolder.jumlahKalori.setText(String.valueOf(currentItem.getKalori()));
        makananTidakDisukaiViewHolder.jumlahProtein.setText(String.valueOf(currentItem.getProtein()));
        makananTidakDisukaiViewHolder.jumlahLemak.setText(String.valueOf(currentItem.getLemak()));
        makananTidakDisukaiViewHolder.jumlahKarbohidrat.setText(String.valueOf(currentItem.getKarbohidrat()));
        makananTidakDisukaiViewHolder.jumlahKalsium.setText(String.valueOf(currentItem.getKalsium()));
        makananTidakDisukaiViewHolder.jumlahFosfor.setText(String.valueOf(currentItem.getFosfor()));
        makananTidakDisukaiViewHolder.jumlahVitaminA.setText(String.valueOf(currentItem.getVit_a()));
        makananTidakDisukaiViewHolder.jumlahVitaminB.setText(String.valueOf(currentItem.getVit_b1()));
        makananTidakDisukaiViewHolder.jumlahVitaminC.setText(String.valueOf(currentItem.getVit_c()));
        makananTidakDisukaiViewHolder.jumlahZatBesi.setText(String.valueOf(currentItem.getZat_besi()));

        makananTidakDisukaiViewHolder.rootLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), DetailMakananActivity.class);
                intent.putExtra("makanan_id", currentItem.getMakanan_id());
                intent.putExtra("nama", currentItem.getNama());
                intent.putExtra("kalori", currentItem.getKalori());
                v.getContext().startActivity(intent);
            }
        });

        makananTidakDisukaiViewHolder.btnHapusMakananTidakDisukai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                final AlertDialog builder = new AlertDialog.Builder(v.getContext())
                        .setTitle("Hapus Makanan")
                        .setMessage("Hapus makanan ini?")
                        .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(final DialogInterface dialog, int which) {
                                SharedPreferences sharedPreferences = v.getContext().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);

                                String token = sharedPreferences.getString(TOKEN, "token");

                                Call<HapusMakananTidakDisukai> call = serverApiInterface.hapusMakananTidakDisukai(
                                        makanan_id,
                                        token
                                );

                                call.enqueue(new Callback<HapusMakananTidakDisukai>() {
                                    @Override
                                    public void onResponse(Call<HapusMakananTidakDisukai> call, Response<HapusMakananTidakDisukai> response) {
                                        if (!response.isSuccessful()) {
                                            Log.i("Code", String.valueOf(response.code()));
                                            Toast.makeText(v.getContext(), "Gagal terhapus", Toast.LENGTH_SHORT).show();
                                            return;
                                        }

                                        mList.remove(i);
                                        notifyItemRemoved(i);

                                        Toast.makeText(v.getContext(), "Sukses terhapus", Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void onFailure(Call<HapusMakananTidakDisukai> call, Throwable t) {
                                        Toast.makeText(v.getContext(), "Gagal terhapus", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        })
                        .setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(v.getContext(), "Gagal terhapus", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        })
                        .create();

                builder.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
}
