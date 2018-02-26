package com.marat.smarthouse;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import Controller.DataGetter;
import Model.SessionData;

public class DeviceChange extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_change);

        Button rename =findViewById(R.id.rename_device_button);

        rename.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Thread thread = new Thread() {
                        @Override
                        public void run() {
                            try {
                                AutoCompleteTextView textView = findViewById(R.id.device_name);
                                String name = textView.getText().toString();
                                DataGetter.renameDevice(Long.parseLong(SessionData.getId()), SessionData.getSessionToken().getToken(), SessionData.getCurrentDevice().getId(), SessionData.getCurrentDevice().getRoomId(), name,true);
                                Intent intent = new Intent(DeviceChange.this, Rooms.class);
                                startActivity(intent);
                            } catch (Exception e) {}
                        }
                    };
                    thread.start();
                    thread.join();
                }
                catch (Exception e){}
            }
        });
    }
}
