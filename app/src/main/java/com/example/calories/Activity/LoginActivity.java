package com.example.calories.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.calories.Interface.ServerApiInterface;
import com.example.calories.Model.Login;
import com.example.calories.Model.Profile;
import com.example.calories.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.calories.Config.Config.SERVER_URL;
import static com.example.calories.Config.Config.SHARED_PREFS;
import static com.example.calories.Config.Config.TOKEN;

public class LoginActivity extends AppCompatActivity {

    private TextView et_email;
    private TextView et_password;
    private TextView emailMsg;
    private TextView passwordMsg;

    private ServerApiInterface serverApiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        et_email = findViewById(R.id.editText_email);
        et_password = findViewById(R.id.editText_password);
        emailMsg = findViewById(R.id.email_msg);
        passwordMsg = findViewById(R.id.password_msg);

        Retrofit retrofit = new Retrofit
                .Builder()
                .baseUrl(SERVER_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        serverApiInterface = retrofit.create(ServerApiInterface.class);
    }

    private void login() {
        final String email = et_email.getText().toString();
        final String password = et_password.getText().toString();

        Call<Login> call = serverApiInterface.postLogin(
                email,
                password
        );

        emailMsg.setText("");
        passwordMsg.setText("");
        emailMsg.setVisibility(View.GONE);
        passwordMsg.setVisibility(View.GONE);

        call.enqueue(new Callback<Login>() {
            @Override
            public void onResponse(Call<Login> call, Response<Login> response) {
                if (!response.isSuccessful()) {
                    Log.i("Code", String.valueOf(response.code()));

                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(response.errorBody().string());

                        String emailMessage = jsonObject.getString("email");
                        String passwordMessage = jsonObject.getString("password");

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

                SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();

                editor.putString(TOKEN, response.body().getToken());

                editor.apply();

                getProfile();
            }

            @Override
            public void onFailure(Call<Login> call, Throwable t) {
                Log.i("Error", t.getMessage());
            }
        });
    }

    private void getProfile() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);

        String token = sharedPreferences.getString(TOKEN, "token");

        Call<Profile> call = serverApiInterface.getProfile(token);

        call.enqueue(new Callback<Profile>() {
            @Override
            public void onResponse(Call<Profile> call, Response<Profile> response) {
                if (!response.isSuccessful()) {
                    Log.i("Code", String.valueOf(response.code()));

                    Intent a = new Intent(LoginActivity.this, ProfileActivity.class);
                    a.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(a);
                    finish();

                    return;
                }

                Intent a = new Intent(LoginActivity.this, MainActivity.class);
                a.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(a);
                finish();
            }

            @Override
            public void onFailure(Call<Profile> call, Throwable t) {
                Log.i("Error", t.getMessage());
            }
        });
    }

    public void registerButtonClick(View v) {
        Intent a = new Intent(LoginActivity.this, RegisterActivity.class);
        a.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(a);
        finish();
    }

    public void loginButtonClick(View v) {
        login();
    }

}
