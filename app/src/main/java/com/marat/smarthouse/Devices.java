package com.marat.smarthouse;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import Controller.DataGetter;
import Model.Device;
import Model.Room;
import Model.SessionData;

public class Devices extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_devices);
        LinearLayout linearLayout = null;
        try {
            List<Device> list = SessionData.getDevices();
            int index = -1;
            for (Device device : list)
            {
                index++;
                if (index % 2 == 0)
                    linearLayout = findViewById(R.id.devices_grid1);
                else
                    linearLayout = findViewById(R.id.devices_grid2);
                String name = device.getType();
                ImageButton button = new ImageButton(this);
                button.setBackgroundResource(R.drawable.room_button);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                params.setMargins(30,30,30, 30);
                button.setLayoutParams(params);
                button.setImageResource(getDrawableByName(device));
                button.setScaleType(ImageButton.ScaleType.FIT_CENTER);
                button.setPadding(10,10,10,10);
                button.setAdjustViewBounds(true);
                button.setContentDescription(((Long)device.getId()).toString());
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Device r = null;
                        for (Device device : SessionData.getDevices())
                            if (device.getId() == Long.parseLong(view.getContentDescription().toString()))
                            {
                                r = device;
                                break;
                            }

                        try {
                            SessionData.currentDevice = r;
                            if (r.getType().equals("Light")){
                                Thread thread = new Thread(){
                                    @Override
                                    public void run() {
                                        try {
                                            SessionData.currentDevice = DataGetter.setCommand(Long.parseLong(SessionData.getId()), SessionData.getSessionToken().getToken(), SessionData.currentDevice.getId(), "on", true);
                                        }
                                        catch (Exception e){

                                        }
                                    }
                                };
                                thread.start();
                                thread.join();
                            }
                        }
                        catch (Exception e){}
                    }
                });
                button.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        Device r = null;
                        for (Device device : SessionData.getDevices())
                            if (device.getId() == Long.parseLong(view.getContentDescription().toString()))
                            {
                                r = device;
                                break;
                            }
                        SessionData.currentDevice = r;
                        Intent intent = new Intent(Devices.this, DeviceChange.class);
                        startActivity(intent);
                        return true;
                    }
                });

                linearLayout.addView(button);
            }
            index++;
            if (index % 2 == 0)
                linearLayout = findViewById(R.id.devices_grid1);
            else
                linearLayout = findViewById(R.id.devices_grid2);
            ImageButton button = new ImageButton(this);
            button.setBackgroundResource(R.drawable.room_button);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(30,30,30, 30);
            button.setLayoutParams(params);
            button.setImageResource(R.drawable.settings);
            button.setScaleType(ImageButton.ScaleType.FIT_CENTER);
            button.setPadding(10,10,10,10);
            button.setAdjustViewBounds(true);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try{
                        Intent intent = new Intent(Devices.this, DeviceAdd.class);
                        startActivity(intent);
                    }
                    catch (Exception e){}
                }
            });
            linearLayout.addView(button);
        }
        catch (Exception e){}
    }

    private int getDrawableByName(Device device){
        switch (device.getType()){
            case "Light":
                if (device.getState().equals("off"))
                    return R.drawable.light_off;
                else
                    return R.drawable.light;
            case "Temperature":
                return R.drawable.temperature;
            case "Outlet":
                return R.drawable.outlet;
            default:
                return R.drawable.outlet;
        }
    }
}
