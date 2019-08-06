package com.iqbalhasan.foodrecomm.Adapter;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.iqbalhasan.foodrecomm.Activity.DetailMakananActivity;
import com.iqbalhasan.foodrecomm.Model.Makanan;
import com.iqbalhasan.foodrecomm.R;

import java.util.List;

public class MakananAdapter extends RecyclerView.Adapter<MakananAdapter.MakananServerViewHolder> {

    private List<Makanan> mList;

    public static class MakananServerViewHolder extends RecyclerView.ViewHolder {
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
        public ImageView iconPrioritas;

        public View rootLayout;

        public MakananServerViewHolder(@NonNull View itemView) {
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
            iconPrioritas = itemView.findViewById(R.id.icon_prioritas);
            rootLayout = itemView;
        }
    }

    public MakananAdapter(List<Makanan> list){
        this.mList = list;
    }

    @NonNull
    @Override
    public MakananServerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_makanan, viewGroup, false);
        MakananServerViewHolder makananServerViewHolder = new MakananServerViewHolder(v);
        return makananServerViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MakananServerViewHolder makananServerViewHolder, int i) {
        final Makanan currentItem = mList.get(i);

        makananServerViewHolder.namaMakanan.setText(currentItem.getNama() + " (100 g)");
        makananServerViewHolder.jumlahKalori.setText(String.valueOf(currentItem.getKalori()));
        makananServerViewHolder.jumlahProtein.setText(String.valueOf(currentItem.getProtein()));
        makananServerViewHolder.jumlahLemak.setText(String.valueOf(currentItem.getLemak()));
        makananServerViewHolder.jumlahKarbohidrat.setText(String.valueOf(currentItem.getKarbohidrat()));
        makananServerViewHolder.jumlahKalsium.setText(String.valueOf(currentItem.getKalsium()));
        makananServerViewHolder.jumlahFosfor.setText(String.valueOf(currentItem.getFosfor()));
        makananServerViewHolder.jumlahVitaminA.setText(String.valueOf(currentItem.getVit_a()));
        makananServerViewHolder.jumlahVitaminB.setText(String.valueOf(currentItem.getVit_b1()));
        makananServerViewHolder.jumlahVitaminC.setText(String.valueOf(currentItem.getVit_c()));
        makananServerViewHolder.jumlahZatBesi.setText(String.valueOf(currentItem.getZat_besi()));

        String prioritas = currentItem.getPrioritas();
        if (prioritas.equals("Dianjurkan")) {
            makananServerViewHolder.iconPrioritas.setImageResource(R.drawable.ic_dianjurkan_24dp);
        } else if (prioritas.equals("Normal")) {
            makananServerViewHolder.iconPrioritas.setImageResource(R.drawable.ic_normal_24dp);
        } else if (prioritas.equals("Dibatasi")) {
            makananServerViewHolder.iconPrioritas.setImageResource(R.drawable.ic_dibatasi_24dp);
        } else if (prioritas.equals("Dihindari")) {
            makananServerViewHolder.iconPrioritas.setImageResource(R.drawable.ic_dihindari_24dp);
        }

        makananServerViewHolder.rootLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), DetailMakananActivity.class);
                intent.putExtra("makanan_id", currentItem.getMakanan_id());
                intent.putExtra("nama", currentItem.getNama());
                intent.putExtra("kalori", currentItem.getKalori());
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

}
