package com.lme.android.experimentosjava;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends Activity {

    private static List<User> userList = new ArrayList<>();
    private RecyclerView recyclerView;
    UserAdapter userAdapter;

    private ProgressBar progressBar;
    private static final String API_URL = "https://randomuser.me/api/?results=30&nat=es";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = findViewById(R.id.progressBar);
        recyclerView = findViewById(R.id.recyclerView);
        userAdapter = new UserAdapter(userList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(userAdapter);

        new RetrieveUsersData().execute();
    }

    private class RetrieveUsersData extends AsyncTask<Void, Void, Boolean> {

        private HttpsURLConnection urlConnection;

        @Override
        protected void onPreExecute() {
            userList.clear();
            userAdapter.notifyDataSetChanged();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            try {
                URL url = new URL(API_URL);
                urlConnection = (HttpsURLConnection) url.openConnection();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line).append("\n");
                }
                bufferedReader.close();
                String jsonReceived = stringBuilder.toString();

                try {
                    JSONObject jsonObject = (JSONObject) new JSONTokener(jsonReceived).nextValue();
                    JSONArray jsonArray = jsonObject.getJSONArray("results");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject userObject = jsonArray.getJSONObject(i);
                        String name = userObject.getJSONObject("name").getString("first");
                        String lastName = userObject.getJSONObject("name").getString("last");
                        String email = userObject.getString("email");
                        String strPicture = userObject.getJSONObject("picture").getString("large");
                        InputStream in = new URL(strPicture).openStream();
                        Bitmap picture = BitmapFactory.decodeStream(in);
                        in.close();
                        userList.add(new User(name, lastName, email, picture));
                    }
                    Log.e("CONTROL", "users: " + userList.size());
                    return true;
                } catch (JSONException e) {
                    Log.e("CONTROL", "JSON Ha petado amigo...");
                    e.printStackTrace();
                }
            } catch (IOException e) {
                Log.e("ERROR", e.getMessage(), e);
            } finally {
                urlConnection.disconnect();
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean ok) {
            if (!ok) {
                Toast.makeText(MainActivity.this, "No se ha podido obtener datos", Toast.LENGTH_SHORT).show();
                return;
            } else {
                userAdapter.notifyDataSetChanged();
            }

            progressBar.setVisibility(View.GONE);
            Log.e("CONTROL", "Operación finalizada");
        }
    }
}
