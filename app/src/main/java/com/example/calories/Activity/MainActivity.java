package com.example.calories.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;

import com.example.calories.Fragment.BiodataFragment;
import com.example.calories.Fragment.HistoryFragment;
import com.example.calories.Fragment.HomeFragment;
import com.example.calories.R;

import static com.example.calories.Config.Config.SHARED_PREFS;
import static com.example.calories.Config.Config.TOKEN;

public class MainActivity extends AppCompatActivity {

    public static final String EXTRAS_DEVICE_NAME = "name";
    public static final String EXTRAS_DEVICE_ADDRESS = "address";
    private ConstraintLayout infoPopupKategori;
    private ConstraintLayout infoPopupMakanan;
    private FloatingActionButton btnPencarian;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new HomeFragment()).commit();
    }


    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    Fragment selectedFragment = null;

                    switch (menuItem.getItemId()) {
                        case R.id.nav_home:
                            selectedFragment = new HomeFragment();
                            break;
                        case R.id.nav_history:
                            selectedFragment = new HistoryFragment();
                            break;
                        case R.id.nav_biodata:
                            selectedFragment = new BiodataFragment();
                            break;
                    }

                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            selectedFragment).commit();

                    return true;
                }
            };

    public void logoutButtonClick(View v) {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);

        sharedPreferences.edit().remove(TOKEN).commit();

        Intent a = new Intent(MainActivity.this, LoginActivity.class);
        a.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(a);
        finish();
    }

    public void pencarianButtonClick(View view) {
        Intent a = new Intent(MainActivity.this, PencarianMakananActivity.class);
        a.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(a);
    }

    public void infoKategoriButtonClick(View view) {
        infoPopupKategori = findViewById(R.id.info_popup_fragment);

        infoPopupKategori.setVisibility(View.VISIBLE);
    }

    public void infoKategoriPopupClick(View view) {
        infoPopupKategori = findViewById(R.id.info_popup_fragment);

        infoPopupKategori.setVisibility(View.GONE);
    }

    public void closeKategoriButtonClick(View view) {
        infoPopupKategori = findViewById(R.id.info_popup_fragment);

        infoPopupKategori.setVisibility(View.GONE);
    }

    public void infoMakananButtonClick(View view) {
        infoPopupMakanan = findViewById(R.id.info_popup_makanan);
        btnPencarian = findViewById(R.id.icon_pencarian);

        infoPopupMakanan.setVisibility(View.VISIBLE);
        btnPencarian.hide();
    }

    public void infoMakananPopupClick(View view) {
        infoPopupMakanan = findViewById(R.id.info_popup_makanan);
        btnPencarian = findViewById(R.id.icon_pencarian);

        infoPopupMakanan.setVisibility(View.GONE);
        btnPencarian.show();
    }

    public void closeMakananButtonClick(View view) {
        infoPopupMakanan = findViewById(R.id.info_popup_makanan);
        btnPencarian = findViewById(R.id.icon_pencarian);

        infoPopupMakanan.setVisibility(View.GONE);
        btnPencarian.show();
    }

    public void connect(View view) {
        Intent intent = new Intent(MainActivity.this,DeviceScanActivity.class);
        startActivity(intent);
    }
}
