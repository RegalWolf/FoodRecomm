package com.iqbalhasan.foodrecomm.Adapter;

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

import com.iqbalhasan.foodrecomm.Activity.DetailMakananActivity;
import com.iqbalhasan.foodrecomm.Interface.ServerApiInterface;
import com.iqbalhasan.foodrecomm.Model.HapusMakananDisukai;
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

public class MakananDisukaiAdapter extends RecyclerView.Adapter<MakananDisukaiAdapter.MakananDisukaiViewHolder> {

    private List<MakananDisukai> mList;

    private ServerApiInterface serverApiInterface;

    public static class MakananDisukaiViewHolder extends RecyclerView.ViewHolder {
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

        public ImageView btnHapusMakananDisukai;

        public View rootLayout;

        public MakananDisukaiViewHolder(@NonNull View itemView) {
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
            btnHapusMakananDisukai = itemView.findViewById(R.id.btn_hapus);
            rootLayout = itemView;
        }
    }

    public MakananDisukaiAdapter(List<MakananDisukai> list){
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
    public MakananDisukaiViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_makanan_disukai, viewGroup, false);
        MakananDisukaiAdapter.MakananDisukaiViewHolder makananDisukaiViewHolder = new MakananDisukaiViewHolder(v);
        return makananDisukaiViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MakananDisukaiViewHolder makananDisukaiViewHolder, final int i) {
        final MakananDisukai currentItem = mList.get(i);

        final int makanan_id = Integer.parseInt(currentItem.getMakanan_id());

        makananDisukaiViewHolder.namaMakanan.setText(currentItem.getNama() + " (100 g)");
        makananDisukaiViewHolder.jumlahKalori.setText(String.valueOf(currentItem.getKalori()));
        makananDisukaiViewHolder.jumlahProtein.setText(String.valueOf(currentItem.getProtein()));
        makananDisukaiViewHolder.jumlahLemak.setText(String.valueOf(currentItem.getLemak()));
        makananDisukaiViewHolder.jumlahKarbohidrat.setText(String.valueOf(currentItem.getKarbohidrat()));
        makananDisukaiViewHolder.jumlahKalsium.setText(String.valueOf(currentItem.getKalsium()));
        makananDisukaiViewHolder.jumlahFosfor.setText(String.valueOf(currentItem.getFosfor()));
        makananDisukaiViewHolder.jumlahVitaminA.setText(String.valueOf(currentItem.getVit_a()));
        makananDisukaiViewHolder.jumlahVitaminB.setText(String.valueOf(currentItem.getVit_b1()));
        makananDisukaiViewHolder.jumlahVitaminC.setText(String.valueOf(currentItem.getVit_c()));
        makananDisukaiViewHolder.jumlahZatBesi.setText(String.valueOf(currentItem.getZat_besi()));

        makananDisukaiViewHolder.rootLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), DetailMakananActivity.class);
                intent.putExtra("makanan_id", currentItem.getMakanan_id());
                intent.putExtra("nama", currentItem.getNama());
                intent.putExtra("kalori", currentItem.getKalori());
                v.getContext().startActivity(intent);
            }
        });

        makananDisukaiViewHolder.btnHapusMakananDisukai.setOnClickListener(new View.OnClickListener() {
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

                                Call<HapusMakananDisukai> call = serverApiInterface.hapusMakananDisukai(
                                        makanan_id,
                                        token
                                );

                                call.enqueue(new Callback<HapusMakananDisukai>() {
                                    @Override
                                    public void onResponse(Call<HapusMakananDisukai> call, Response<HapusMakananDisukai> response) {
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
                                    public void onFailure(Call<HapusMakananDisukai> call, Throwable t) {
                                        Toast.makeText(v.getContext(), "Gagal terhapus", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        })
                        .setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
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
