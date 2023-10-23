package com.kaze2.wt;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.kaze2.wt.api.TimeApi;
import com.kaze2.wt.api.impl.WorldTimeApi;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "WT_MAIN";

    private final TimeApi timeApi = new WorldTimeApi();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final String timeAtTokyo = timeApi.getTimeAtZone("Asia/Tokyo");
                    Log.i(TAG, timeAtTokyo);
                    final TextView txtTime = findViewById(R.id.txtTime);

                    runOnUiThread(new Runnable() {
                        public void run() {
                            txtTime.setText(timeAtTokyo);
                        }
                    });

                } catch (IOException e) {
                    Log.e(TAG, e.getMessage());
                }
            }
        }).start();
    }
}