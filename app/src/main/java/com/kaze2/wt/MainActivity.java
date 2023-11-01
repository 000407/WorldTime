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
import android.widget.Toast;

import com.kaze2.wt.adapter.LocationTimeListAdapter;
import com.kaze2.wt.api.RetrofitClient;
import com.kaze2.wt.api.TimeService;
import com.kaze2.wt.dto.WorldTimeResponse;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
  private static final String TAG = "WT_MAIN";

  private final TimeService timeService =
      RetrofitClient.getRetrofitInstance().create(TimeService.class); // Create the service that accesses the backend APIs using retrofit
  private final List<Pair<String, ZonedDateTime>> worldTimeLocations = new ArrayList<>();
  private final LocationTimeListAdapter adapter = new LocationTimeListAdapter(worldTimeLocations);

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    final RecyclerView recyclerView = findViewById(R.id.recyclerView);
    final LinearLayoutManager linearLayoutManager =
        new LinearLayoutManager(getApplicationContext());
    recyclerView.setLayoutManager(linearLayoutManager);

    // Setting Adapter to RecyclerView
    recyclerView.setAdapter(adapter);

    // Read about anonymous implementations of interfaces, lambda & functional interface
    new Thread(
            () -> { // Lambda function in the place of Runnable
              try {
                addNewLocationAtTime("Asia", "Tokyo");
              } catch (IOException e) {
                Log.e(TAG, e.getMessage());
              }
            })
        .start();
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
      new Thread(
              () -> {
                try {
                  addNewLocationAtTime("Asia", "Colombo");
                } catch (IOException e) {
                  Log.e(TAG, e.getMessage());
                }
              })
          .start();
    }

    return true;
  }

  private void addNewLocationAtTime(String continent, String city) throws IOException {
    timeService
        .getTimeAtZone(continent, city) // Calling the method that sends the HTTP request
        .enqueue(
            new Callback<WorldTimeResponse>() {

              @Override
              public void onResponse(
                  Call<WorldTimeResponse> call, Response<WorldTimeResponse> response) {
                final WorldTimeResponse body = response.body();

                if (body != null) {
                  final ZonedDateTime timestamp = ZonedDateTime.parse(body.getTimestamp());
                  worldTimeLocations.add(Pair.create(city, timestamp));
                  Log.i(TAG, worldTimeLocations.toString());

                  runOnUiThread(
                      () ->
                          adapter.notifyItemInserted(
                              worldTimeLocations.size() - 1)); // Read about multiline
                  // lambda vs expression
                  // lambda
                } else {
                    Log.e(TAG, Objects.toString(response));
                    Toast.makeText(MainActivity.this, "Something went wrong...", Toast.LENGTH_SHORT).show(); // Read about toasts in Android https://developer.android.com/guide/topics/ui/notifiers/toasts
                }
              }

              @Override
              public void onFailure(Call<WorldTimeResponse> call, Throwable t) {
                Log.e(TAG, t.getMessage());
                Toast.makeText(MainActivity.this, "Something went wrong...", Toast.LENGTH_SHORT).show();
              }
            });
  }
}
