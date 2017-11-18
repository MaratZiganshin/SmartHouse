package com.marat.smarthouse;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import Model.SessionData;

public class Rooms extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rooms);
        TextView t = (TextView)findViewById(R.id.textView);
        t.setText(SessionData.getMainToken().getToken());
    }
}
