package com.iqbalhasan.foodrecomm.Fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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

public class ProfileFragment extends Fragment {

    private ServerApiInterface serverApiInterface;

    private TextView et_usia;
    private TextView et_beratBadan;
    private TextView et_tinggiBadan;

    private TextView jenisKelaminMsg;
    private TextView usiaMsg;
    private TextView beratBadanMsg;
    private TextView tinggiBadanMsg;
    private TextView tingkatAktivitasMsg;

    private Button btnSimpanProfile;

    private ProgressDialog dialog;
    private Spinner spinnerJK;
    private Spinner spinnerAktivitas;
    private ArrayAdapter<CharSequence> adapterJK;
    private ArrayAdapter<CharSequence> adapterAktivitas;

    private String jenisKelamin;
    private String tingkatAktivitas;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        Retrofit retrofit = new Retrofit
                .Builder()
                .baseUrl(SERVER_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        serverApiInterface = retrofit.create(ServerApiInterface.class);

        initDialog();

        spinnerJK = view.findViewById(R.id.spinner_jenis_kelamin);
        et_usia = view.findViewById(R.id.editText_usia1);
        et_beratBadan = view.findViewById(R.id.editText_beratbadan1);
        et_tinggiBadan = view.findViewById(R.id.editText_tinggibadan1);
        spinnerAktivitas = view.findViewById(R.id.spinner_tingkat_aktivitas);

        adapterJK = ArrayAdapter
                .createFromResource(getContext(), R.array.jenis_kelamin, android.R.layout.simple_spinner_item);
        adapterJK.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        adapterAktivitas = ArrayAdapter
                .createFromResource(getContext(), R.array.tingkat_aktivitas, android.R.layout.simple_spinner_item);
        adapterAktivitas.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        jenisKelaminMsg = view.findViewById(R.id.jk_msg1);
        usiaMsg = view.findViewById(R.id.usia_msg1);
        beratBadanMsg = view.findViewById(R.id.beratBadan_msg1);
        tinggiBadanMsg = view.findViewById(R.id.tinggiBadan_msg1);
        tingkatAktivitasMsg = view.findViewById(R.id.tingkatAktivitas_msg1);

        btnSimpanProfile = view.findViewById(R.id.btn_simpan_profile);

        jkSpinner();
        aktivitasSpinner();
        getProfile();

        simpanButtonClick();

        return view;
    }

    private void jkSpinner() {
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

    private void aktivitasSpinner() {
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

    private void simpanButtonClick() {
        btnSimpanProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editProfile();
            }
        });
    }

    private void initDialog(){
        dialog = new ProgressDialog(getContext());
        dialog.setTitle("Loading");
        dialog.setMessage("Sedang Memproses...");
    }

    private void getProfile() {
        dialog.show();

        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);

        String token = sharedPreferences.getString(TOKEN, "token");

        Call<Profile> call = serverApiInterface.getProfile(token);

        call.enqueue(new Callback<Profile>() {
            @Override
            public void onResponse(Call<Profile> call, Response<Profile> response) {
                if (!response.isSuccessful()) {
                    Log.i("Code", String.valueOf(response.code()));
                    dialog.dismiss();
                    return;
                }

                Profile profile = response.body();

                String jenis_kelamin = "Laki-laki";

                if (profile.getJenis_kelamin().equals("P")) {
                    jenis_kelamin = "Perempuan";
                }

                int spinnerJKPosition = adapterJK.getPosition(jenis_kelamin);
                spinnerJK.setSelection(spinnerJKPosition);
                et_usia.setText(profile.getUsia());
                et_beratBadan.setText(profile.getBerat_badan());
                et_tinggiBadan.setText(profile.getTinggi_badan());
                int spinnerAktivitasPosition = adapterAktivitas.getPosition(profile.getTingkat_aktivitas());
                spinnerAktivitas.setSelection(spinnerAktivitasPosition);

                dialog.dismiss();
            }

            @Override
            public void onFailure(Call<Profile> call, Throwable t) {
                dialog.dismiss();
                Log.i("Error", t.getMessage());
            }
        });
    }

    private void editProfile() {
        final AlertDialog builder = new AlertDialog.Builder(getContext())
                .setTitle("Ubah Profile")
                .setMessage("Simpan Profile?")
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, int which) {
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

                        SharedPreferences sharedPreferences = getContext().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);

                        String token = sharedPreferences.getString(TOKEN, "token");

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

                                    return;
                                }

                                Toast.makeText(getContext(), "Sukses tersimpan", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onFailure(Call<Profile> call, Throwable t) {
                                Log.i("Error", t.getMessage());
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
}
