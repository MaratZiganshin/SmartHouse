package com.marat.smarthouse;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;

import Model.Device;
import Model.SessionData;

public class Devices extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_devices);
        LinearLayout linearLayout = findViewById(R.id.devices_grid);
        try{
            for (Device device : SessionData.getDevices()){
                String name = device.getName();
                Button button = new Button(this);
                button.setText(name);
                button.setBackgroundResource(R.drawable.room_button);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                params.setMargins(50,10,50, 20);
                button.setLayoutParams(params);
                linearLayout.addView(button);
            }
        }
        catch (Exception e){}
    }
}
