package com.marat.smarthouse;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import org.w3c.dom.Text;

import Model.SessionData;

public class Temperature extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        double grad = 0;
        try{
            grad = Double.parseDouble(SessionData.getCurrentDevice().getState());
        }catch (Exception e){}
        setContentView(R.layout.activity_temperature);
        TextView c = findViewById(R.id.celcium);
        String forC = "" + grad + c.getText();
        c.setText(forC);

        TextView f = findViewById(R.id.farengate);
        String forF = "" + (grad * 9.0 / 5.0 + 32) + f.getText();
        f.setText(forF);
    }
}
