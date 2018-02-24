package com.marat.smarthouse;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Spinner;

import Controller.DataGetter;
import Model.SessionData;

public class RoomChange extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_change);
        Button button = findViewById(R.id.delete_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Thread thread = new Thread() {
                        @Override
                        public void run() {
                            try {
                                DataGetter.deleteRoom(Long.parseLong(SessionData.getId()), SessionData.getSessionToken().getToken(), SessionData.currentRoom.getId(), true);
                                    Intent intent = new Intent(RoomChange.this, Rooms.class);
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

        Button rename =findViewById(R.id.rename_button);

        rename.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Thread thread = new Thread() {
                        @Override
                        public void run() {
                            try {
                                AutoCompleteTextView textView = findViewById(R.id.room_name);
                                String name = textView.getText().toString();
                                DataGetter.renameRoom(Long.parseLong(SessionData.getId()), SessionData.getSessionToken().getToken(), SessionData.currentRoom.getId(), name,true);
                                Intent intent = new Intent(RoomChange.this, Rooms.class);
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
