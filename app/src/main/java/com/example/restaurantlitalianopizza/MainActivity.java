package com.example.restaurantlitalianopizza;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    Button button;
    LinearLayout linearLayout;
    ProgressBar progressBar;
//    private DatabaseHelper db;

    private ArrayList<String> pizzaTypes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        linearLayout = findViewById(R.id.layout);
        button = findViewById(R.id.start_btn);
        progressBar = findViewById(R.id.progressBar);

        setProgress(false);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ConnectionAsyncTask().execute();
            }
        });
    }

    public void setProgress(boolean progress) {
        if (progress) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }

    private class ConnectionAsyncTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... voids) {
            try {
                return HttpUtils.fetchData("https://18fbea62d74a40eab49f72e12163fe6c.api.mockbin.io/");
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }


        @Override
        protected void onPostExecute(String result) {
            Log.d("JSON Response", "Response: " + result);
            if (result != null) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray typesArray = jsonObject.getJSONArray("types");
                    for (int i = 0; i < typesArray.length(); i++) {
                        pizzaTypes.add(typesArray.getString(i));

                    }
                    if (pizzaTypes.isEmpty()) {
                        showUIToast("Connection Failed. No pizza types found");
                    } else {
                        showUIToast("Connection Successful. Welcome to L'italiano Pizza Restaurant");
                        NewPizzasSave.getInstance().setPizzaTypes(pizzaTypes);
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(intent);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    showUIToast("Failed to parse pizza types: " + e.getMessage());
                }
            } else {
                showUIToast("Failed to fetch pizza types");
            }
        }


        private void showUIToast(final String message) {
            MainActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}


