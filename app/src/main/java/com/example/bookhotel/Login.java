package com.example.bookhotel;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Login extends AppCompatActivity {
    Button btnLogin;
    EditText email, password;
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        addListenerOnButton();
    }

    public void addListenerOnButton() {
        btnLogin = (Button) findViewById(R.id.buttonLog);
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);

        dbHelper = new DBHelper(this);

        btnLogin.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick (View v) {
                        final String eml = email.getText().toString();
                        final String pswd = password.getText().toString();

                        OkHttpClient client = new OkHttpClient();

                        String url = "https://for-thesis.space/users.json";

                        HttpUrl.Builder httpBuilder = HttpUrl.parse(url).newBuilder();
                        httpBuilder.addQueryParameter("email", eml).addQueryParameter("password", pswd);

                        Request request = new Request.Builder()
                                .url(httpBuilder.build())
                                .build();

                        client.newCall(request).enqueue(new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                e.printStackTrace();
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {

                                final String my_response = response.body().string();
                                if (my_response != "") {
                                    SQLiteDatabase database = dbHelper.getWritableDatabase();
                                    ContentValues contentValues = new ContentValues();

                                    contentValues.put(DBHelper.KEY_USER, "auth");
                                    contentValues.put(DBHelper.KEY_EMAIL, eml);

                                    database.insert(DBHelper.TABLE_LOGIN, null, contentValues);
                                }

                                Login.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        /*
                                        Intent intent = new Intent("com.example.bookhotel.ReservationList");
                                        startActivity(intent);*/
                                        finish();
                                    }
                                });
                            }
                        });
                    }
                }
        );
    }
}
