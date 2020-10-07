package com.example.tunnel;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.OnMapReadyCallback;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "MainActivity";
    EditText mSearchText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startMap();

    }
    private void startMap(){
        Button startTunnel = (Button) findViewById(R.id.Start_Tunnel_Button);
        startTunnel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MapsActivity.class);
                startActivity(intent);
            }
        });
    }
}
