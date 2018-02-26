package com.marat.smarthouse;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import Controller.DataGetter;
import Model.Device;
import Model.Room;
import Model.SessionData;

public class Devices extends AppCompatActivity {

    private View.OnClickListener deviceClickListener = new View.OnClickListener() {
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
                SessionData.setCurrentDevice(r);
                deviceActivate();
                if (SessionData.getCurrentDevice().getState().equals("on"))
                    view.setBackgroundResource(R.drawable.room_button_on);
                if (SessionData.getCurrentDevice().getState().equals("off"))
                    view.setBackgroundResource(R.drawable.room_button);
            }
            catch (Exception e){}
        }
    };

    private View.OnLongClickListener deviceLongClickListener = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View view) {
            Device r = null;
            for (Device device : SessionData.getDevices())
                if (device.getId() == Long.parseLong(view.getContentDescription().toString()))
                {
                    r = device;
                    break;
                }
            SessionData.setCurrentDevice(r);
            Intent intent = new Intent(Devices.this, DeviceChange.class);
            startActivity(intent);
            return true;
        }
    };

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
                if (device.getState().equals("on"))
                    button.setBackgroundResource(R.drawable.room_button_on);
                else
                    button.setBackgroundResource(R.drawable.room_button);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                params.setMargins(50,50,50, 50);
                button.setLayoutParams(params);
                button.setImageResource(getDrawableByName(device));
                button.setScaleType(ImageButton.ScaleType.FIT_CENTER);
                button.setPadding(50,50,50,50);
                button.setAdjustViewBounds(true);
                button.setContentDescription(((Long)device.getId()).toString());
                button.setOnClickListener(deviceClickListener);
                button.setOnLongClickListener(deviceLongClickListener);

                linearLayout.addView(button);

                LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                p.setMargins(50, 10, 50, 10);
                TextView textView = new TextView(this);
                textView.setText(device.getName());
                textView.setLayoutParams(p);
                textView.setMaxLines(1);
                linearLayout.addView(textView);
            }
            index++;
            if (index % 2 == 0)
                linearLayout = findViewById(R.id.devices_grid1);
            else
                linearLayout = findViewById(R.id.devices_grid2);
            ImageButton button = new ImageButton(this);
            button.setBackgroundResource(R.drawable.room_button);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(50,50,50, 50);
            button.setLayoutParams(params);
            button.setImageResource(R.drawable.settings);
            button.setScaleType(ImageButton.ScaleType.FIT_CENTER);
            button.setPadding(50,50,50,50);
            button.setAdjustViewBounds(true);
            button.setOnClickListener(settingsButtonListener);
            linearLayout.addView(button);
            LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            p.setMargins(50, 10, 50, 10);
            TextView textView = new TextView(this);
            textView.setText("Настройки");
            textView.setLayoutParams(p);
            textView.setMaxLines(1);
            linearLayout.addView(textView);
        }
        catch (Exception e){}
    }

    private View.OnClickListener settingsButtonListener = new View.OnClickListener(){
        @Override
        public void onClick(View view) {
            try{
                Intent intent = new Intent(Devices.this, DeviceAdd.class);
                startActivity(intent);
            }
            catch (Exception e){}
        }
    };

    private void deviceActivate(){
        try {
            Device r = SessionData.getCurrentDevice();
            Thread thread = null;
            if ((r.getType().equals("Light") ||r.getType().equals("Outlet")) && r.getState().equals("on")) {
                thread = new Thread() {
                    @Override
                    public void run() {
                        try {
                            DataGetter.setCommand(Long.parseLong(SessionData.getId()), SessionData.getSessionToken().getToken(), SessionData.getCurrentDevice().getId(), "off", true);
                            SessionData.getCurrentDevice().setState("off");
                        } catch (Exception e) {

                        }
                    }
                };
                thread.start();
                thread.join();
            }
            else if ((r.getType().equals("Light")||r.getType().equals("Outlet")) && r.getState().equals("off")) {
                thread = new Thread() {
                    @Override
                    public void run() {
                        try {
                            DataGetter.setCommand(Long.parseLong(SessionData.getId()), SessionData.getSessionToken().getToken(), SessionData.getCurrentDevice().getId(), "on", true);
                            SessionData.getCurrentDevice().setState("on");
                        } catch (Exception e) {

                        }
                    }
                };
                thread.start();
                thread.join();
            }
            else if (r.getType().equals("Temperature")){
                Intent intent = new Intent(Devices.this, Temperature.class);
                startActivity(intent);
            }
            else if (r.getType().equals("RGB")){
                Intent intent = new Intent(Devices.this, RGB.class);
                startActivity(intent);
            }

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
                return R.drawable.light;
        }
    }
}
