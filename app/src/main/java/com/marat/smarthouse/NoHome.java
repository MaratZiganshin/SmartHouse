package com.marat.smarthouse;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class NoHome extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_home);
        Button addHomeButton = (Button)findViewById(R.id.add_home_button);
        addHomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validNumber()){
                    try {
                        //send to server
                        Intent intent = new Intent(NoHome.this, Rooms.class);
                        startActivity(intent);
                    }
                    catch (Exception e){}
                }
            }
        });
    }

    private boolean validNumber(){
        EditText numberInput = (EditText) findViewById(R.id.password);
        return (numberInput.getText().length() == 8);
    }
}
