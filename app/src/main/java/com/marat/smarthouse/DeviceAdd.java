package com.marat.smarthouse;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import Controller.DataGetter;
import Model.Device;
import Model.Room;
import Model.SessionData;

public class DeviceAdd extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_add);
        List<Device> list = getAllDevices();
        Spinner spinner = findViewById(R.id.choose_device);
        String[] strings = new String[list.size()];
        for (int i = 0; i < list.size(); i++)
            strings[i] = list.get(i).getType();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, strings);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        Button button = findViewById(R.id.add_device_button);
        button.setOnClickListener(addListener);
    }

    private List<Device> getAllDevices(){
        List<Device> list = new ArrayList<>();
        for (Room room : SessionData.getRooms()){
            for (Device device : room.getDevices())
                list.add(device);
        }
        return list;
    }

    private View.OnClickListener addListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            final List<Device> list = getAllDevices();
            try {
                Thread thread = new Thread() {
                    @Override
                    public void run() {
                        try {
                            Spinner spinner = findViewById(R.id.choose_device);
                            int position = spinner.getSelectedItemPosition();
                            Device d = list.get(position);
                            DataGetter.renameDevice(Long.parseLong(SessionData.getId()), SessionData.getSessionToken().getToken(), d.getId(), d.getRoomId(), d.getName(), true);
                            Intent intent = new Intent(DeviceAdd.this, Rooms.class);
                            startActivity(intent);
                        } catch (Exception e) {}
                    }
                };
                thread.start();
                thread.join();
            }
            catch (Exception e){}
        }
    };
}
