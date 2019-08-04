package com.example.calories.Adapter;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.calories.Activity.DetailHistoryActivity;
import com.example.calories.Model.HistoryKalori;
import com.example.calories.R;

import java.util.List;

public class HistoryKaloriAdapter extends RecyclerView.Adapter<HistoryKaloriAdapter.HistoryKaloriViewHolder> {

    private List<HistoryKalori> mList;

    public static class HistoryKaloriViewHolder extends RecyclerView.ViewHolder {
        public TextView tanggal;
        public TextView kalori_dibutuhkan;
        public TextView kalori_dikonsumsi;
        public View rootLayout;

        public HistoryKaloriViewHolder(@NonNull View itemView) {
            super(itemView);

            tanggal = itemView.findViewById(R.id.tanggal_history);
            kalori_dibutuhkan = itemView.findViewById(R.id.jumlah_kalori_dibutuhkan);
            kalori_dikonsumsi = itemView.findViewById(R.id.jumlah_kalori_dikonsumsi);
            rootLayout = itemView;
        }
    }

    public HistoryKaloriAdapter(List<HistoryKalori> list) {
        this.mList = list;
    }

    @NonNull
    @Override
    public HistoryKaloriViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_history_kalori, viewGroup, false);
        HistoryKaloriAdapter.HistoryKaloriViewHolder historyKaloriViewHolder = new HistoryKaloriViewHolder(v);
        return historyKaloriViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryKaloriViewHolder historyKaloriViewHolder, int i) {
        final HistoryKalori currentItem = mList.get(i);

        final String tanggal = currentItem.getTanggal().substring(0,10);

        historyKaloriViewHolder.tanggal.setText(tanggal);
        historyKaloriViewHolder.kalori_dibutuhkan.setText(currentItem.getKalori_dibutuhkan() + " Kkal");
        historyKaloriViewHolder.kalori_dikonsumsi.setText(currentItem.getKalori_dikonsumsi() + " Kkal");

        historyKaloriViewHolder.rootLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), DetailHistoryActivity.class);
                intent.putExtra("tanggal", tanggal);
                intent.putExtra("kalori_dibutuhkan", currentItem.getKalori_dibutuhkan() + " Kkal");
                intent.putExtra("kalori_dikonsumsi", currentItem.getKalori_dikonsumsi() + " Kkal");
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

}
