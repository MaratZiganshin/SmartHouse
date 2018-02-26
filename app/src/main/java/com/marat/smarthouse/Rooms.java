package com.marat.smarthouse;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.provider.ContactsContract;
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
import android.widget.ImageButton;
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

    private View.OnClickListener roomClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Room r = null;
            for (Room temp : SessionData.getRooms())
                if (temp.getId() == Long.parseLong(view.getContentDescription().toString()))
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
    };

    private View.OnLongClickListener roomLongClickListener = new View.OnLongClickListener(){
        @Override
        public boolean onLongClick(View view) {
            Room r = null;
            for (Room temp : SessionData.getRooms())
                if (temp.getId() == Long.parseLong(view.getContentDescription().toString()))
                {
                    r = temp;
                    break;
                }
            SessionData.setCurrentRoom(r);
            Intent intent = new Intent(Rooms.this, RoomChange.class);
            startActivity(intent);
            return true;
        }
    };

    private View.OnClickListener settingsClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            try{
                Intent intent = new Intent(Rooms.this, RoomAdd.class);
                startActivity(intent);
            }
            catch (Exception e){}
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rooms);

        LinearLayout linearLayout = null;
        try {
            if (SessionData.getSessionToken() == null) {
                Thread thread = new Thread() {
                    @Override
                    public void run() {
                        try {
                            DataGetter.refresh(Long.parseLong(SessionData.getId()), SessionData.getMainToken().getToken());
                        } catch (Exception e) {
                        }
                    }
                };
                thread.start();
                thread.join();
            }
            Thread thread = new Thread(){
                @Override
                public void run(){
                    try {
                        long userId = Long.parseLong(SessionData.getId());
                        String sessionToken = SessionData.getSessionToken().getToken();
                        List<Room> list = DataGetter.getRooms(userId, sessionToken, true);
                        SessionData.setRooms(list);
                    }
                    catch (Exception e){}
                }
            };
            thread.start();
            thread.join();
            int index = 0;
            for (Room room : SessionData.getRooms())
            {
                index++;
                if (index % 3 == 1)
                    linearLayout = findViewById(R.id.rooms_grid1);
                else if (index % 3 == 2)
                    linearLayout = findViewById(R.id.rooms_grid2);
                else
                    linearLayout = findViewById(R.id.rooms_grid3);
                String name = room.getName();

                ImageButton button = new ImageButton(this);
                button.setBackgroundResource(R.drawable.room_button);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                params.setMargins(30,30,30, 30);
                button.setLayoutParams(params);
                button.setImageResource(getDrawableByName(room.getType()));
                button.setScaleType(ImageButton.ScaleType.FIT_CENTER);
                button.setPadding(10,10,10,10);
                button.setAdjustViewBounds(true);
                button.setContentDescription(((Long)room.getId()).toString());
                button.setOnClickListener(roomClickListener);
                button.setOnLongClickListener(roomLongClickListener);
                linearLayout.addView(button);

                LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                p.setMargins(30, 10, 30, 10);
                TextView textView = new TextView(this);
                textView.setText(room.getName());
                textView.setLayoutParams(p);
                textView.setMaxLines(1);
                linearLayout.addView(textView);
            }
            index++;
            if (index % 3 == 1)
                linearLayout = findViewById(R.id.rooms_grid1);
            else if (index % 3 == 2)
                linearLayout = findViewById(R.id.rooms_grid2);
            else
                linearLayout = findViewById(R.id.rooms_grid3);
            ImageButton button = new ImageButton(this);
            button.setBackgroundResource(R.drawable.room_button);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(30,30,30, 30);
            button.setLayoutParams(params);
            button.setImageResource(R.drawable.settings);
            button.setScaleType(ImageButton.ScaleType.FIT_CENTER);
            button.setPadding(10,10,10,10);
            button.setAdjustViewBounds(true);
            button.setOnClickListener(settingsClickListener);
            linearLayout.addView(button);
            LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            p.setMargins(30, 10, 30, 10);
            TextView textView = new TextView(this);
            textView.setText("Настройки");
            textView.setLayoutParams(p);
            linearLayout.addView(textView);
        }
        catch (Exception e){}
    }


    private int getDrawableByName(String name){
        if (name == null)
            return R.drawable.garage;
        if (name.equals("Garage"))
            return R.drawable.garage;
        if (name.equals("Sleeping"))
            return R.drawable.sleeping;
        if (name.equals("Bathroom"))
            return R.drawable.bathroom;
        if (name.equals("Toilet"))
            return R.drawable.toilet;
        if (name.equals("Kitchen"))
            return R.drawable.kitchen;
        return R.drawable.kitchen;
    }
}
