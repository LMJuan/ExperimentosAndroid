package com.lme.android.experimentosjava;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
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
    UserAdapter userAdapter;
    RecyclerView recyclerView;

    private SwipeRefreshLayout swipeRefresh;
    private static final String API_URL = "https://randomuser.me/api/?results=30&nat=es";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        userAdapter = new UserAdapter(userList, this);
        userAdapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("CONTROL", "Pulsado elemento " + recyclerView.getChildLayoutPosition(view));
            }
        });
        LayoutAnimationController animatorController = AnimationUtils.loadLayoutAnimation(this, R.anim.layout_animation);
        recyclerView.setLayoutAnimation(animatorController);
        recyclerView.setAdapter(userAdapter);

        swipeRefresh = findViewById(R.id.swiperefresh);
        swipeRefresh.setColorSchemeColors(
                getResources().getColor(R.color.colorAccent, null)
        );
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new RetrieveUsersData().execute();
            }
        });

        new RetrieveUsersData().execute();
    }

    @SuppressLint("StaticFieldLeak")
    private class RetrieveUsersData extends AsyncTask<Void, Void, Boolean> {

        private HttpsURLConnection urlConnection;

        @Override
        protected void onPreExecute() {
            userList.clear();
            userAdapter.resetLastPosition();
            userAdapter.notifyDataSetChanged();
            swipeRefresh.setRefreshing(true);
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
            swipeRefresh.setRefreshing(false);
            if (!ok) {
                Toast.makeText(MainActivity.this, "No se ha podido obtener datos", Toast.LENGTH_SHORT).show();
            } else {
                recyclerView.getAdapter().notifyDataSetChanged();
                recyclerView.scheduleLayoutAnimation();
            }
            Log.e("CONTROL", "Operación finalizada");
        }
    }
}
