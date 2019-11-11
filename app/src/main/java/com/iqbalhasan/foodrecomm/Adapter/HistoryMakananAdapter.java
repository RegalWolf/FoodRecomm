package com.iqbalhasan.foodrecomm.Adapter;

import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.iqbalhasan.foodrecomm.Interface.ServerApiInterface;
import com.iqbalhasan.foodrecomm.Model.HistoryMakanan;
import com.iqbalhasan.foodrecomm.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class HistoryMakananAdapter extends RecyclerView.Adapter<HistoryMakananAdapter.HistoryMakananViewHolder> {

    private List<HistoryMakanan> mList;

    private ServerApiInterface serverApiInterface;
    private ConstraintLayout popupUbahMakanan;
    private Button btnSimpanUbah;
    private ImageView btnClose;
    private Button btnBatal;

    public static class HistoryMakananViewHolder extends RecyclerView.ViewHolder {
        public TextView tanggal;
        public TextView nama_makanan;
        public TextView kalori;
        public TextView jumlahProtein;
        public TextView jumlahLemak;
        public TextView jumlahKarbohidrat;
        public TextView jumlahKalsium;
        public TextView jumlahFosfor;
        public TextView jumlahVitaminA;
        public TextView jumlahVitaminB;
        public TextView jumlahVitaminC;
        public TextView jumlahZatBesi;
        public TextView jumlahMakanan;
        public TextView totalKalori;
        public ImageView iconPrioritas;

        public View rootLayout;

        public HistoryMakananViewHolder(@NonNull View itemView) {
            super(itemView);

            tanggal = itemView.findViewById(R.id.tanggal_history);
            nama_makanan = itemView.findViewById(R.id.nama_makanan);
            kalori = itemView.findViewById(R.id.jml_kalori);
            jumlahProtein = itemView.findViewById(R.id.jml_protein);
            jumlahLemak = itemView.findViewById(R.id.jml_lemak);
            jumlahKarbohidrat = itemView.findViewById(R.id.jml_karbohidrat);
            jumlahKalsium = itemView.findViewById(R.id.jml_kalsium);
            jumlahFosfor = itemView.findViewById(R.id.jml_fosfor);
            jumlahVitaminA = itemView.findViewById(R.id.jml_vit_a);
            jumlahVitaminB = itemView.findViewById(R.id.jml_vit_b);
            jumlahVitaminC = itemView.findViewById(R.id.jml_vit_c);
            jumlahZatBesi = itemView.findViewById(R.id.jml_zat_besi);
            jumlahMakanan = itemView.findViewById(R.id.jumlah_makanan);
            totalKalori = itemView.findViewById(R.id.total_kalori);
            iconPrioritas = itemView.findViewById(R.id.icon_prioritas);
            rootLayout = itemView;
        }
    }

    public HistoryMakananAdapter(List<HistoryMakanan> list, ConstraintLayout popupUbahMakanan,
                                 ImageView btnClose, Button btnBatal, Button btnSimpanUbah) {
        this.mList = list;
        this.popupUbahMakanan = popupUbahMakanan;
        this.btnClose = btnClose;
        this.btnBatal = btnBatal;
        this.btnSimpanUbah = btnSimpanUbah;
    }

    @NonNull
    @Override
    public HistoryMakananViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_history_makanan, viewGroup, false);
        HistoryMakananAdapter.HistoryMakananViewHolder historyMakananViewHolder = new HistoryMakananViewHolder(v);
        return historyMakananViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final HistoryMakananViewHolder historyMakananViewHolder, final int i) {
        final HistoryMakanan currentItem = mList.get(i);

        String waktu = currentItem.getTanggal().substring(11,19);

        historyMakananViewHolder.tanggal.setText(waktu + " WIB");
        historyMakananViewHolder.nama_makanan.setText(currentItem.getNama() + " (100 g)");
        historyMakananViewHolder.kalori.setText(String.valueOf(currentItem.getKalori()));
        historyMakananViewHolder.jumlahProtein.setText(String.valueOf(currentItem.getProtein()));
        historyMakananViewHolder.jumlahLemak.setText(String.valueOf(currentItem.getLemak()));
        historyMakananViewHolder.jumlahKarbohidrat.setText(String.valueOf(currentItem.getKarbohidrat()));
        historyMakananViewHolder.jumlahKalsium.setText(String.valueOf(currentItem.getKalsium()));
        historyMakananViewHolder.jumlahFosfor.setText(String.valueOf(currentItem.getFosfor()));
        historyMakananViewHolder.jumlahVitaminA.setText(String.valueOf(currentItem.getVit_a()));
        historyMakananViewHolder.jumlahVitaminB.setText(String.valueOf(currentItem.getVit_b1()));
        historyMakananViewHolder.jumlahVitaminC.setText(String.valueOf(currentItem.getVit_c()));
        historyMakananViewHolder.jumlahZatBesi.setText(String.valueOf(currentItem.getZat_besi()));
        historyMakananViewHolder.jumlahMakanan.setText(String.valueOf(currentItem.getJumlah()) + " gram");
        historyMakananViewHolder.totalKalori.setText(String.valueOf(currentItem.getTotal_kalori()) + " Kkal");

        final int makanan_id = Integer.parseInt(currentItem.getMakanan_id());

        String prioritas = currentItem.getPrioritas();
        if (prioritas.equals("Dianjurkan")) {
            historyMakananViewHolder.iconPrioritas.setImageResource(R.drawable.ic_dianjurkan_24dp);
        } else if (prioritas.equals("Normal")) {
            historyMakananViewHolder.iconPrioritas.setImageResource(R.drawable.ic_normal_24dp);
        } else if (prioritas.equals("Dibatasi")) {
            historyMakananViewHolder.iconPrioritas.setImageResource(R.drawable.ic_dibatasi_24dp);
        } else if (prioritas.equals("Dihindari")) {
            historyMakananViewHolder.iconPrioritas.setImageResource(R.drawable.ic_dihindari_24dp);
        }

        String tanggal = currentItem.getTanggal().substring(0,10);

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date today = new Date();

//        if (tanggal.equals(formatter.format(today))) {
//            historyMakananViewHolder.btnHapus.setVisibility(View.VISIBLE);
//            historyMakananViewHolder.btnEdit.setVisibility(View.VISIBLE);
//        }

//        historyMakananViewHolder.btnHapus.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(final View v) {
//                final AlertDialog builder = new AlertDialog.Builder(v.getContext())
//                        .setTitle("Hapus Makanan")
//                        .setMessage("Hapus makanan ini?")
//                        .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(final DialogInterface dialog, int which) {
//                                SharedPreferences sharedPreferences = v.getContext().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
//
//                                String token = sharedPreferences.getString(TOKEN, "token");
//
//                                Call<HapusMakananDisukai> call = serverApiInterface.hapusMakananDisukai(
//                                        makanan_id,
//                                        token
//                                );
//
//                                call.enqueue(new Callback<HapusMakananDisukai>() {
//                                    @Override
//                                    public void onResponse(Call<HapusMakananDisukai> call, Response<HapusMakananDisukai> response) {
//                                        if (!response.isSuccessful()) {
//                                            Log.i("Code", String.valueOf(response.code()));
//                                            Toast.makeText(v.getContext(), "Gagal terhapus", Toast.LENGTH_SHORT).show();
//                                            return;
//                                        }
//
//                                        mList.remove(i);
//                                        notifyItemRemoved(i);
//
//                                        Toast.makeText(v.getContext(), "Sukses terhapus", Toast.LENGTH_SHORT).show();
//                                    }
//
//                                    @Override
//                                    public void onFailure(Call<HapusMakananDisukai> call, Throwable t) {
//                                        Toast.makeText(v.getContext(), "Gagal terhapus", Toast.LENGTH_SHORT).show();
//                                    }
//                                });
//                            }
//                        })
//                        .setNegativeButton("Batal", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                dialog.dismiss();
//                            }
//                        })
//                        .create();
//
//                builder.show();
//            }
//        });
//
//        historyMakananViewHolder.btnEdit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                popupUbahMakanan.setVisibility(View.VISIBLE);
//
//                popupUbahMakanan.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        popupUbahMakanan.setVisibility(View.GONE);
//                    }
//                });
//
//                btnClose.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        popupUbahMakanan.setVisibility(View.GONE);
//                    }
//                });
//
//                btnBatal.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        popupUbahMakanan.setVisibility(View.GONE);
//                    }
//                });
//
//                btnSimpanUbah.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Toast.makeText(v.getContext(), "Bissmillah", Toast.LENGTH_SHORT).show();
//                    }
//                });
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

}
