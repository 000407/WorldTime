package com.kaze2.wt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.kaze2.wt.adapter.LocationTimeListAdapter;
import com.kaze2.wt.api.TimeApi;
import com.kaze2.wt.api.impl.WorldTimeApi;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "WT_MAIN";

    private final TimeApi timeApi = new WorldTimeApi();
    private final List<Pair<String, ZonedDateTime>> worldTimeLocations = new ArrayList<>();
    private final LocationTimeListAdapter adapter = new LocationTimeListAdapter(worldTimeLocations);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final RecyclerView recyclerView = findViewById(R.id.recyclerView);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        // Setting Adapter to RecyclerView
        recyclerView.setAdapter(adapter);

        // Read about anonymous implementations of interfaces, lambda & functional interface
        new Thread(() -> { // Lambda function in the place of Runnable
            try {
                addNewLocationAtTime("Asia/Tokyo", "Tokyo");
            } catch (IOException e) {
                Log.e(TAG, e.getMessage());
            }
        }).start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        final MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_bar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_add_location) {
            new Thread(() -> {
                try {
                    addNewLocationAtTime("Asia/Colombo", "Colombo");
                } catch (IOException e) {
                    Log.e(TAG, e.getMessage());
                }
            }).start();
        }

        return true;
    }

    private void addNewLocationAtTime(String timezone, String townName) throws IOException {
        final ZonedDateTime timeAtTokyo = timeApi.getTimeAtZone(timezone);
        worldTimeLocations.add(Pair.create(townName, timeAtTokyo));
        Log.i(TAG, worldTimeLocations.toString());

        runOnUiThread(() -> adapter.notifyItemInserted(worldTimeLocations.size() - 1)); // Read about multiline lambda vs expression lambda
    }
}