package com.marat.smarthouse;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import Controller.DataGetter;
import Model.Device;
import Model.Room;
import Model.SessionData;

public class Rooms extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rooms);
        LinearLayout linearLayout = null;
        try {
            List<Room> list = DataGetter.fakeGetRooms();
            int index = 0;
            for (Room room : list)
            {
                index++;
                if (index % 3 == 1)
                    linearLayout = findViewById(R.id.rooms_grid1);
                else if (index % 3 == 2)
                    linearLayout = findViewById(R.id.rooms_grid2);
                else
                    linearLayout = findViewById(R.id.rooms_grid3);
                String name = room.getName();
                Button button = new Button(this);
                button.setText(name);
                button.setBackgroundResource(R.drawable.room_button);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                params.setMargins(50,10,50, 20);
                button.setLayoutParams(params);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Room r = null;
                        for (Room temp : SessionData.getRooms())
                            if (temp.getName() == ((Button) view).getText())
                            {
                                r = temp;
                                break;
                            }

                        try {
                            ArrayList<Device> list = (ArrayList<Device>)DataGetter.getDevices(r.getId());
                            SessionData.setDevices(list);
                            Intent intent = new Intent(Rooms.this, Devices.class);
                            startActivity(intent);
                        }
                        catch (Exception e){}
                    }
                });
                linearLayout.addView(button);
            }
        }
        catch (Exception e){}
    }
}
