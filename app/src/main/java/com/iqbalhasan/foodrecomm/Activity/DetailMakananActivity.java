package com.iqbalhasan.foodrecomm.Activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.iqbalhasan.foodrecomm.Fragment.RestoranFragment;
import com.iqbalhasan.foodrecomm.Fragment.TutorialFragment;
import com.iqbalhasan.foodrecomm.Interface.ServerApiInterface;
import com.iqbalhasan.foodrecomm.Model.SimpanMakanan;
import com.iqbalhasan.foodrecomm.Model.SimpanMakananDisukai;
import com.iqbalhasan.foodrecomm.Model.SimpanMakananTidakDisukai;
import com.iqbalhasan.foodrecomm.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.iqbalhasan.foodrecomm.Config.Config.SERVER_URL;
import static com.iqbalhasan.foodrecomm.Config.Config.SHARED_PREFS;
import static com.iqbalhasan.foodrecomm.Config.Config.TOKEN;

public class DetailMakananActivity extends AppCompatActivity {

    private TextView namaMakanan;
    private TextView kaloriMakanan;

    private ProgressDialog dialog;

    private String nama;
    private int kalori;
    private int makanan_id;
    private ConstraintLayout moreView;
    private FloatingActionButton btnTambah;
    private ConstraintLayout popupSimpanMakanan;
    private EditText etJumlahMakanan;
    private TextView jumlahKaloriMsg;

    private ServerApiInterface serverApiInterface;

    private static final int REQUEST_LOCATION = 1;
    private LocationManager locationManager;
    private String lattitude;
    private String longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_makanan);

        Retrofit retrofit = new Retrofit
                .Builder()
                .baseUrl(SERVER_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        serverApiInterface = retrofit.create(ServerApiInterface.class);

        initDialog();

        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission
                .ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

        startLocation();

        BottomNavigationView bottomNav = findViewById(R.id.detail_makanan_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        namaMakanan = findViewById(R.id.nama_makanan);
        kaloriMakanan = findViewById(R.id.kalori_makanan);
        moreView = findViewById(R.id.more_view);
        btnTambah = findViewById(R.id.btn_tambah);
        jumlahKaloriMsg = findViewById(R.id.jumlahMakanan_msg);

        popupSimpanMakanan = findViewById(R.id.popup_simpan_makanan);

        Bundle bundleGet = getIntent().getExtras();

        makanan_id = bundleGet.getInt("makanan_id");
        nama = bundleGet.getString("nama");
        kalori = bundleGet.getInt("kalori");

        namaMakanan.setText(nama + " (100 gram)");
        kaloriMakanan.setText(kalori + " Kkal");

        Bundle bundlePut = new Bundle();

        TutorialFragment tutorialFragment = new TutorialFragment();

        bundlePut.putString("nama", nama);
        bundlePut.putString("lattitude", lattitude);
        bundlePut.putString("longitude", longitude);

        tutorialFragment.setArguments(bundlePut);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                tutorialFragment).commit();
    }

    private void startLocation() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();

        } else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            getLocation();
        }
    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(DetailMakananActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (DetailMakananActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(DetailMakananActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

        } else {
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            Location location1 = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            Location location2 = locationManager.getLastKnownLocation(LocationManager. PASSIVE_PROVIDER);

            if (location != null) {
                double latti = location.getLatitude();
                double longi = location.getLongitude();
                lattitude = String.valueOf(latti);
                longitude = String.valueOf(longi);

                Log.i("Location", lattitude);
                Log.i("Location", longitude);
            } else  if (location1 != null) {
                double latti = location1.getLatitude();
                double longi = location1.getLongitude();
                lattitude = String.valueOf(latti);
                longitude = String.valueOf(longi);

                Log.i("Location1", lattitude);
                Log.i("Location1", longitude);
            } else  if (location2 != null) {
                double latti = location2.getLatitude();
                double longi = location2.getLongitude();
                lattitude = String.valueOf(latti);
                longitude = String.valueOf(longi);

                Log.i("Location2", lattitude);
                Log.i("Location2", longitude);
            } else {
                Toast.makeText(this,"Unable to Trace your location", Toast.LENGTH_SHORT).show();
            }
        }
    }

    protected void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Please Turn ON your GPS Connection")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    Fragment selectedFragment = null;

                    switch (menuItem.getItemId()) {
                        case R.id.nav_tutorial:
                            selectedFragment = new TutorialFragment();
                            break;
                        case R.id.nav_restoran:
                            selectedFragment = new RestoranFragment();
                            break;
                    }

                    Bundle bundleGet = getIntent().getExtras();

                    namaMakanan.setText(bundleGet.getString("nama"));
                    kaloriMakanan.setText(bundleGet.getInt("kalori") + " Kkal");

                    Bundle bundlePut = new Bundle();

                    bundlePut.putString("nama", bundleGet.getString("nama"));
                    bundlePut.putString("lattitude", lattitude);
                    bundlePut.putString("longitude", longitude);

                    selectedFragment.setArguments(bundlePut);

                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            selectedFragment).commit();

                    return true;
                }
            };

    private void initDialog(){
        dialog = new ProgressDialog(this);
        dialog.setTitle("Loading");
        dialog.setMessage("Sedang Memproses...");
    }

    public void simpanMakananHistoryClick(View v) {
        dialog.show();

        jumlahKaloriMsg.setText("");
        jumlahKaloriMsg.setVisibility(View.GONE);

        etJumlahMakanan = findViewById(R.id.et_jumlah_makanan);

        Integer jumlahMakanan = null;
        if (etJumlahMakanan.getText().toString().trim().length() > 0) {
            jumlahMakanan = Integer.parseInt(etJumlahMakanan.getText().toString());
        }

        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);

        String token = sharedPreferences.getString(TOKEN, "token");

        Call<SimpanMakanan> call = serverApiInterface.saveMakanan(
                makanan_id,
                jumlahMakanan,
                token
        );

        call.enqueue(new Callback<SimpanMakanan>() {
            @Override
            public void onResponse(Call<SimpanMakanan> call, Response<SimpanMakanan> response) {
                if (!response.isSuccessful()) {
                    Log.i("Code", String.valueOf(response.code()));
                    Log.i("Error Body", response.errorBody().toString());
                    dialog.dismiss();

                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(response.errorBody().string());

                        String jumlahMessage = jsonObject.getString("jumlah");

                        if (!jumlahMessage.equals("null")) {
                            jumlahKaloriMsg.setText(jumlahMessage);
                            jumlahKaloriMsg.setVisibility(View.VISIBLE);
                        } else {
                            btnTambah.show();
                            popupSimpanMakanan.setVisibility(View.GONE);
                            Toast.makeText(DetailMakananActivity.this, "Gagal tersimpan", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    return;
                }

                etJumlahMakanan.setText("100");

                btnTambah.show();
                popupSimpanMakanan.setVisibility(View.GONE);
                dialog.dismiss();

                Toast.makeText(DetailMakananActivity.this, "Sukses tersimpan", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<SimpanMakanan> call, Throwable t) {
                Log.i("Error", t.getMessage());
                btnTambah.show();
                popupSimpanMakanan.setVisibility(View.GONE);
                dialog.dismiss();

                Toast.makeText(DetailMakananActivity.this, "Gagal tersimpan", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void likeMakananButtonClick(View v) {
        final AlertDialog builder = new AlertDialog.Builder(this)
                .setTitle("Simpan Makanan")
                .setMessage("Simpan Makanan Disukai?")
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, int which) {
                        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);

                        String token = sharedPreferences.getString(TOKEN, "token");

                        Call<SimpanMakananDisukai> call = serverApiInterface.simpanMakananDisukai(
                                makanan_id,
                                token
                        );

                        call.enqueue(new Callback<SimpanMakananDisukai>() {
                            @Override
                            public void onResponse(Call<SimpanMakananDisukai> call, Response<SimpanMakananDisukai> response) {
                                if (!response.isSuccessful()) {
                                    Log.i("Code", String.valueOf(response.code()));
                                    Log.i("Error Body", response.errorBody().toString());
                                    btnTambah.show();
                                    moreView.setVisibility(View.GONE);

                                    Toast.makeText(DetailMakananActivity.this, "Gagal tersimpan", Toast.LENGTH_SHORT).show();
                                    return;
                                }

                                btnTambah.show();
                                moreView.setVisibility(View.GONE);

                                Toast.makeText(DetailMakananActivity.this, "Sukses tersimpan", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onFailure(Call<SimpanMakananDisukai> call, Throwable t) {
                                Log.i("Error", t.getMessage());
                                btnTambah.show();
                                moreView.setVisibility(View.GONE);

                                Toast.makeText(DetailMakananActivity.this, "Gagal tersimpan", Toast.LENGTH_SHORT).show();
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

    public void dislikeMakananButtonClick(View v) {
        final AlertDialog builder = new AlertDialog.Builder(this)
                .setTitle("Simpan Makanan")
                .setMessage("Simpan Makanan Tidak Disukai?")
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, int which) {
                        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);

                        String token = sharedPreferences.getString(TOKEN, "token");

                        Call<SimpanMakananTidakDisukai> call = serverApiInterface.simpanMakananTidakDisukai(
                                makanan_id,
                                token
                        );

                        call.enqueue(new Callback<SimpanMakananTidakDisukai>() {
                            @Override
                            public void onResponse(Call<SimpanMakananTidakDisukai> call, Response<SimpanMakananTidakDisukai> response) {
                                if (!response.isSuccessful()) {
                                    Log.i("Code", String.valueOf(response.code()));
                                    Log.i("Error Body", response.errorBody().toString());
                                    btnTambah.show();
                                    moreView.setVisibility(View.GONE);

                                    Toast.makeText(DetailMakananActivity.this, "Gagal tersimpan", Toast.LENGTH_SHORT).show();
                                    return;
                                }

                                btnTambah.show();
                                moreView.setVisibility(View.GONE);

                                Toast.makeText(DetailMakananActivity.this, "Sukses tersimpan", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onFailure(Call<SimpanMakananTidakDisukai> call, Throwable t) {
                                Log.i("Error", t.getMessage());
                                btnTambah.show();
                                moreView.setVisibility(View.GONE);

                                Toast.makeText(DetailMakananActivity.this, "Gagal tersimpan", Toast.LENGTH_SHORT).show();
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

    public void tambahButtonClick(View v) {
        btnTambah.hide();
        moreView.setVisibility(View.VISIBLE);
    }

    public void batalTambahButtonClick(View v) {
        btnTambah.show();
        moreView.setVisibility(View.GONE);
    }

    public void kembaliButtonClick(View view) {
        onBackPressed();
    }

    public void moreViewButtonClick(View view) {
        btnTambah.show();
        moreView.setVisibility(View.GONE);
    }

    public void closeSimpanMakananClick(View view) {
        popupSimpanMakanan.setVisibility(View.GONE);
        btnTambah.show();
        moreView.setVisibility(View.GONE);
    }

    public void popupSimpanMakananClick(View view) {
        popupSimpanMakanan.setVisibility(View.GONE);
        btnTambah.show();
        moreView.setVisibility(View.GONE);
    }

    public void simpanMakananButtonClick(View view) {
        popupSimpanMakanan.setVisibility(View.VISIBLE);
        btnTambah.hide();
        moreView.setVisibility(View.GONE);
    }
}
