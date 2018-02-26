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

public class RoomAdd extends AppCompatActivity {

    private View.OnClickListener addButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (((AutoCompleteTextView)findViewById(R.id.room_name)).getText().toString().equals("")) {
                ((AutoCompleteTextView) findViewById(R.id.room_name)).setError("Обязательное поле");
                return;
            }
            try {
                Thread thread = new Thread() {
                    @Override
                    public void run() {
                        try {
                            Spinner spinner = findViewById(R.id.choose_room);
                            String s = spinner.getSelectedItem().toString();
                            AutoCompleteTextView textView = findViewById(R.id.room_name);
                            String name = textView.getText().toString();
                            boolean added = DataGetter.addRoom(Long.parseLong(SessionData.getId()), SessionData.getSessionToken().getToken(), name, getType(s), true);
                            if (added){
                                Intent intent = new Intent(RoomAdd.this, Rooms.class);
                                startActivity(intent);
                            }
                        } catch (Exception e) {}
                    }
                };
                thread.start();
                thread.join();
            }
            catch (Exception e){}
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_add);
        Button button = findViewById(R.id.add_button);
        button.setOnClickListener(addButtonListener);
    }

    String getType(String name){
        if (name.equals("Спальня"))
            return "Sleeping";
        if (name.equals("Гараж"))
            return "Garage";
        if (name.equals("Ванная"))
            return "Bathroom";
        if (name.equals("Туалет"))
            return "Toilet";
        if (name.equals("Кухня"))
            return "Kitchen";
        return "Garage";
    }
}
