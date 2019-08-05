package com.example.calories.Adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.calories.Model.HistoryMakanan;
import com.example.calories.R;

import java.util.List;

public class HistoryMakananAdapter extends RecyclerView.Adapter<HistoryMakananAdapter.HistoryMakananViewHolder> {

    private List<HistoryMakanan> mList;

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

    public HistoryMakananAdapter(List<HistoryMakanan> list) {
        this.mList = list;
    }

    @NonNull
    @Override
    public HistoryMakananViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_history_makanan, viewGroup, false);
        HistoryMakananAdapter.HistoryMakananViewHolder historyMakananViewHolder = new HistoryMakananViewHolder(v);
        return historyMakananViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryMakananViewHolder historyMakananViewHolder, int i) {
        final HistoryMakanan currentItem = mList.get(i);

        String tanggal = currentItem.getTanggal().substring(11,19);

        historyMakananViewHolder.tanggal.setText(tanggal + " WIB");
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
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

}
