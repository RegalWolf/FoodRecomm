package com.iqbalhasan.foodrecomm.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.iqbalhasan.foodrecomm.Interface.ServerApiInterface;
import com.iqbalhasan.foodrecomm.Model.Profile;
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

public class ProfileActivity extends AppCompatActivity {

    private ServerApiInterface serverApiInterface;

    private TextView et_usia;
    private TextView et_beratBadan;
    private TextView et_tinggiBadan;

    private Spinner spinnerJK;
    private Spinner spinnerAktivitas;

    private TextView jenisKelaminMsg;
    private TextView usiaMsg;
    private TextView beratBadanMsg;
    private TextView tinggiBadanMsg;
    private TextView tingkatAktivitasMsg;

    private ConstraintLayout infoPopup;

    private String jenisKelamin;
    private String tingkatAktivitas;

    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Retrofit retrofit = new Retrofit
                .Builder()
                .baseUrl(SERVER_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        serverApiInterface = retrofit.create(ServerApiInterface.class);

        ArrayAdapter<CharSequence> adapterJK = ArrayAdapter
                .createFromResource(this, R.array.jenis_kelamin, android.R.layout.simple_spinner_item);
        adapterJK.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ArrayAdapter<CharSequence> adapterAktivitas = ArrayAdapter
                .createFromResource(this, R.array.tingkat_aktivitas, android.R.layout.simple_spinner_item);
        adapterAktivitas.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerJK = findViewById(R.id.spinner_jk);
        et_usia = findViewById(R.id.editText_usia);
        et_beratBadan = findViewById(R.id.editText_beratbadan);
        et_tinggiBadan = findViewById(R.id.editText_tinggibadan);
        spinnerAktivitas = findViewById(R.id.spinner_aktivitas);

        jenisKelaminMsg = findViewById(R.id.jk_msg);
        usiaMsg = findViewById(R.id.usia_msg);
        beratBadanMsg = findViewById(R.id.beratBadan_msg);
        tinggiBadanMsg = findViewById(R.id.tinggiBadan_msg);
        tingkatAktivitasMsg = findViewById(R.id.tingkatAktivitas_msg);

        infoPopup = findViewById(R.id.info_popup);

        jkSpinner(adapterJK);
        aktivitasSpinner(adapterAktivitas);

        initDialog();
    }

    private void initDialog(){
        dialog = new ProgressDialog(this);
        dialog.setTitle("Loading");
        dialog.setMessage("Sedang Memproses...");
    }

    private void jkSpinner(ArrayAdapter<CharSequence> adapterJK) {
        spinnerJK.setAdapter(adapterJK);
        spinnerJK.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String jk = parent.getItemAtPosition(position).toString();
                if (jk.equals("Perempuan")) {
                    jenisKelamin = "P";
                } else if (jk.equals("Laki-laki")) {
                    jenisKelamin = "L";
                } else {
                    jenisKelamin = "";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void aktivitasSpinner(ArrayAdapter<CharSequence> adapterAktivitas) {
        spinnerAktivitas.setAdapter(adapterAktivitas);
        spinnerAktivitas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    tingkatAktivitas = parent.getItemAtPosition(position).toString();
                } else {
                    tingkatAktivitas = "";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void simpanButtonClick(View v) {
        createProfile();
    }

    private void createProfile() {
        dialog.show();

        Integer usia = null;
        if (et_usia.getText().toString().trim().length() > 0) {
            usia = Integer.parseInt(et_usia.getText().toString());
        }
        Integer beratBadan = null;
        if (et_beratBadan.getText().toString().trim().length() > 0) {
            beratBadan = Integer.parseInt(et_beratBadan.getText().toString());
        }
        Integer tinggiBadan = null;
        if (et_tinggiBadan.getText().toString().trim().length() > 0) {
            tinggiBadan = Integer.parseInt(et_tinggiBadan.getText().toString());
        }

        jenisKelaminMsg.setText("");
        jenisKelaminMsg.setVisibility(View.GONE);
        usiaMsg.setText(null);
        usiaMsg.setVisibility(View.GONE);
        beratBadanMsg.setText(null);
        beratBadanMsg.setVisibility(View.GONE);
        tinggiBadanMsg.setText(null);
        tinggiBadanMsg.setVisibility(View.GONE);
        tingkatAktivitasMsg.setText("");
        tingkatAktivitasMsg.setVisibility(View.GONE);

        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);

        String token = sharedPreferences.getString(TOKEN, "TOKEN");

        Call<Profile> call = serverApiInterface.createProfile(
                jenisKelamin,
                usia,
                beratBadan,
                tinggiBadan,
                tingkatAktivitas,
                token
        );

        call.enqueue(new Callback<Profile>() {
            @Override
            public void onResponse(Call<Profile> call, Response<Profile> response) {
                if (!response.isSuccessful()) {
                    Log.i("Code", String.valueOf(response.code()));

                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(response.errorBody().string());

                        String jkMessage = jsonObject.getString("jenis_kelamin");
                        String usiaMessage = jsonObject.getString("usia");
                        String beratBadanMessage = jsonObject.getString("berat_badan");
                        String tinggiBadanMessage = jsonObject.getString("tinggi_badan");
                        String tingkatAktivitasMessage = jsonObject.getString("tingkat_aktivitas");

                        if (!jkMessage.equals("null")) {
                            jenisKelaminMsg.setText(jkMessage);
                            jenisKelaminMsg.setVisibility(View.VISIBLE);
                        }

                        if (!usiaMessage.equals("null")) {
                            usiaMsg.setText(usiaMessage);
                            usiaMsg.setVisibility(View.VISIBLE);
                        }

                        if (!beratBadanMessage.equals("null")) {
                            beratBadanMsg.setText(beratBadanMessage);
                            beratBadanMsg.setVisibility(View.VISIBLE);
                        }

                        if (!tinggiBadanMessage.equals("null")) {
                            tinggiBadanMsg.setText(tinggiBadanMessage);
                            tinggiBadanMsg.setVisibility(View.VISIBLE);
                        }

                        if (!tingkatAktivitasMessage.equals("null")) {
                            tingkatAktivitasMsg.setText(tingkatAktivitasMessage);
                            tingkatAktivitasMsg.setVisibility(View.VISIBLE);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    dialog.dismiss();
                    return;
                }

                dialog.dismiss();

                Intent a = new Intent(ProfileActivity.this, MainActivity.class);
                a.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(a);
                finish();
            }

            @Override
            public void onFailure(Call<Profile> call, Throwable t) {
                dialog.dismiss();
                Log.i("Error", t.getMessage());
            }
        });
    }

    public void infoButtonClick(View view) {
        infoPopup.setVisibility(View.VISIBLE);
    }

    public void infoPopupClick(View view) {
        infoPopup.setVisibility(View.GONE);
    }

    public void closeButtonClick(View view) {
        infoPopup.setVisibility(View.GONE);
    }

}
