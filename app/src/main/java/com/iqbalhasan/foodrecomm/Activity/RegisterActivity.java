package com.iqbalhasan.foodrecomm.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.iqbalhasan.foodrecomm.Interface.ServerApiInterface;
import com.iqbalhasan.foodrecomm.Model.Register;
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

public class RegisterActivity extends AppCompatActivity {

    private TextView et_email;
    private TextView et_password;
    private TextView et_confirmPassword;
    private TextView emailMsg;
    private TextView passwordMsg;
    private TextView confirmPasswordMsg;

    private ServerApiInterface serverApiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        et_email = findViewById(R.id.editText_email);
        et_password = findViewById(R.id.editText_password);
        et_confirmPassword = findViewById(R.id.editText_confirmPassword);
        emailMsg = findViewById(R.id.email_msg);
        passwordMsg = findViewById(R.id.password_msg);
        confirmPasswordMsg = findViewById(R.id.confirmPassword_msg);

        Retrofit retrofit = new Retrofit
                .Builder()
                .baseUrl(SERVER_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        serverApiInterface = retrofit.create(ServerApiInterface.class);
    }

    private void Register() {
        final String email = et_email.getText().toString();
        final String password = et_password.getText().toString();
        final String confirmPassword = et_confirmPassword.getText().toString();

        Call<Register> call = serverApiInterface.postRegister(
                email,
                password,
                confirmPassword
        );

        confirmPasswordMsg.setText("");
        emailMsg.setText("");
        passwordMsg.setText("");
        confirmPasswordMsg.setVisibility(View.GONE);
        emailMsg.setVisibility(View.GONE);
        passwordMsg.setVisibility(View.GONE);

        call.enqueue(new Callback<Register>() {
            @Override
            public void onResponse(Call<Register> call, Response<Register> response) {
                if (!response.isSuccessful()) {
                    Log.i("Code", String.valueOf(response.code()));

                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(response.errorBody().string());

                        String confirmPasswordMessage = jsonObject.getString("confirmPassword");
                        String emailMessage = jsonObject.getString("email");
                        String passwordMessage = jsonObject.getString("password");

                        if (!confirmPasswordMessage.equals("null")) {
                            confirmPasswordMsg.setText(confirmPasswordMessage);
                            confirmPasswordMsg.setVisibility(View.VISIBLE);
                        }

                        if (!emailMessage.equals("null")) {
                            emailMsg.setText(emailMessage);
                            emailMsg.setVisibility(View.VISIBLE);
                        }

                        if (!passwordMessage.equals("null")) {
                            passwordMsg.setText(passwordMessage);
                            passwordMsg.setVisibility(View.VISIBLE);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    return;
                }

                Log.i("Body", response.body().getRegister());

                AlertDialog builder = new AlertDialog.Builder(RegisterActivity.this)
                    .setTitle("Daftar")
                    .setMessage("Akun berhasil terdaftar")
                    .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent a = new Intent(RegisterActivity.this, LoginActivity.class);
                                a.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                startActivity(a);
                                finish();
                            }
                        })
                        .create();
                builder.show();
            }

            @Override
            public void onFailure(Call<Register> call, Throwable t) {
                Log.i("Error", t.getMessage());
            }
        });
    }

    public void registerButtonClick(View v) {
        Register();
    }

    public void loginButtonClick(View v) {
        Intent   a = new Intent(RegisterActivity.this, LoginActivity.class);
        a.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(a);
        finish();
    }

}
