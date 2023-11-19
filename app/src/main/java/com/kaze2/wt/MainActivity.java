package com.kaze2.wt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.kaze2.wt.adapter.LocationTimeListAdapter;
import com.kaze2.wt.api.RetrofitClient;
import com.kaze2.wt.api.TimeService;
import com.kaze2.wt.data.DatabaseHelper;
import com.kaze2.wt.data.DbManager;
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
      RetrofitClient.getRetrofitInstance()
          .create(
              TimeService
                  .class); // Create the service that accesses the backend APIs using retrofit
  private final List<Pair<String, ZonedDateTime>> worldTimeLocations = new ArrayList<>();
  private final LocationTimeListAdapter adapter = new LocationTimeListAdapter(worldTimeLocations);
  private DbManager dbManager;
  private AlertDialog timezonePicker;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    dbManager = new DbManager(this);

    final RecyclerView recyclerView = findViewById(R.id.recyclerView);
    final LinearLayoutManager linearLayoutManager =
        new LinearLayoutManager(getApplicationContext());
    recyclerView.setLayoutManager(linearLayoutManager);

    // Setting Adapter to RecyclerView
    recyclerView.setAdapter(adapter);

    setupTimezonePicker(this);

    try (final Cursor cursor = dbManager.open().fetch()) {
      do {
        int columnIndex = cursor.getColumnIndex(DatabaseHelper.CONTINENT);

        if (columnIndex < 0) {
          throw new RuntimeException("No column was found with name " + DatabaseHelper.CONTINENT);
        }

        final String continent = cursor.getString(columnIndex);

        columnIndex = cursor.getColumnIndex(DatabaseHelper.CITY);

        if (columnIndex < 0) {
          throw new RuntimeException("No column was found with name " + DatabaseHelper.CITY);
        }

        final String city = cursor.getString(columnIndex);

        // Read about anonymous implementations of interfaces, lambda & functional interface
        new Thread(
                () -> { // Lambda function in the place of Runnable
                  try {
                    addNewTimeAtLocation(continent, city);
                  } catch (IOException e) {
                    Log.e(TAG, e.getMessage());
                  } catch (Exception e) {
                    Log.e(TAG, "ERROR: ", e);
                  }
                })
            .start();
      } while (cursor.moveToNext());
    }
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
      timezonePicker.show();
    }

    return true;
  }

  private void addNewTimeAtLocation(String continent, String city) throws IOException {
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
                              worldTimeLocations.size()
                                  - 1)); // Read about multiline lambda vs expression lambda
                } else {
                  Log.e(TAG, Objects.toString(response));
                  Toast.makeText(MainActivity.this, "Something went wrong...", Toast.LENGTH_SHORT)
                      .show(); // Read about toasts in Android
                               // https://developer.android.com/guide/topics/ui/notifiers/toasts
                }
              }

              @Override
              public void onFailure(Call<WorldTimeResponse> call, Throwable t) {
                Log.e(TAG, t.getMessage());
                Toast.makeText(MainActivity.this, "Something went wrong...", Toast.LENGTH_SHORT)
                    .show();
              }
            });
  }

  @Override
  protected void onPause() {
    super.onPause();
    dbManager.close();
  }

  private void setupTimezonePicker(Context context) {
    final AlertDialog.Builder builder = new AlertDialog.Builder(context);
    builder.setTitle("Choose a Time Zone");

    final View viewInflated = LayoutInflater.from(context).inflate(R.layout.timezone_picker, null);

    // Set up the input
    final EditText txtContinent = viewInflated.findViewById(R.id.txtContinent);
    final EditText txtCity = viewInflated.findViewById(R.id.txtCity);

    builder.setView(viewInflated);

    // Set up the buttons
    builder.setPositiveButton(
        android.R.string.ok,
        (dialog, which) -> {
          dialog.dismiss();
          final String continent = txtContinent.getText().toString();
          final String city = txtCity.getText().toString();

          dbManager.insert(continent, city, ""); // Let's keep timeOffset empty for now

          new Thread(
                  () -> {
                    try {
                      addNewTimeAtLocation(continent, city);
                    } catch (IOException e) {
                      Log.e(TAG, e.getMessage());
                    }
                  })
              .start();
        });
    builder.setNegativeButton(android.R.string.cancel, (dialog, which) -> dialog.cancel());

    timezonePicker = builder.create();
  }
}
