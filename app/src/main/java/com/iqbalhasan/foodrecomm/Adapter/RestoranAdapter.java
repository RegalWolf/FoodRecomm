package com.iqbalhasan.foodrecomm.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.iqbalhasan.foodrecomm.Activity.MapsActivity;
import com.iqbalhasan.foodrecomm.Model.Zomato.Restaurant;
import com.iqbalhasan.foodrecomm.R;

import java.util.List;

public class RestoranAdapter extends RecyclerView.Adapter<RestoranAdapter.ZomatoViewHolder> {

    private List<Restaurant> mList;
    private Context context;
    double lattitude;
    double longitude;

    public static class ZomatoViewHolder extends RecyclerView.ViewHolder {
        public TextView namaRestoran;
        public TextView alamatRestoran;
        public ImageView btnMore;

        public ZomatoViewHolder(@NonNull View itemView) {
            super(itemView);

            namaRestoran = itemView.findViewById(R.id.nama_restoran);
            alamatRestoran = itemView.findViewById(R.id.alamat_restoran);
            btnMore = itemView.findViewById(R.id.btn_more);
        }
    }

    public RestoranAdapter(List<Restaurant> list, Context context, double lattitude, double longitude){
        this.mList = list;
        this.context = context;
        this.lattitude = lattitude;
        this.longitude = longitude;
    }

    @NonNull
    @Override
    public ZomatoViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_restoran, viewGroup, false);
        ZomatoViewHolder zomatoViewHolder = new ZomatoViewHolder(v);
        return zomatoViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ZomatoViewHolder zomatoViewHolder, int i) {
        final Restaurant currentItem = mList.get(i);

        zomatoViewHolder.namaRestoran.setText(currentItem.getRestaurant().getName());
        zomatoViewHolder.alamatRestoran.setText(currentItem.getRestaurant().getLocation().getAddress());

        final String lattitude1 = currentItem.getRestaurant().getLocation().getLatitude();
        final String longitude2 = currentItem.getRestaurant().getLocation().getLongitude();

        zomatoViewHolder.btnMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                PopupMenu popupMenu = new PopupMenu(context, zomatoViewHolder.btnMore);
                popupMenu.inflate(R.menu.restoran_more);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.nav_map :
                                Intent intent = new Intent(v.getContext(), MapsActivity.class) ;
                                intent.putExtra("lat", lattitude1);
                                intent.putExtra("lon", longitude2);
                                intent.putExtra("lattitude", String.valueOf(lattitude));
                                intent.putExtra("longitude", String.valueOf(longitude));
                                v.getContext().startActivity(intent);
                                break;
                            case R.id.nav_detail :
                                String url = currentItem.getRestaurant().getUrl();
                                Intent browse = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                                v.getContext().startActivity(browse);
                                break;
                        }
                        return false;
                    }
                });

                popupMenu.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
}
