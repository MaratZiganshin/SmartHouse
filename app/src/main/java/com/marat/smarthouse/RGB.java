package com.marat.smarthouse;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;

import java.util.ArrayList;
import java.util.List;

import Controller.DataGetter;
import Model.Device;
import Model.SessionData;

public class RGB extends AppCompatActivity {
    private static int redColor;
    private static int greenColor;
    private static int blueColor;
    private static ImageView example;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rgb);
        final Switch switcher = findViewById(R.id.on_rgb);
        final SeekBar red = findViewById(R.id.red_bar);
        final SeekBar green = findViewById(R.id.green_bar);
        final SeekBar blue = findViewById(R.id.blue_bar);
        redColor = red.getProgress();
        greenColor = green.getProgress();
        blueColor = blue.getProgress();
        example = findViewById(R.id.color_example);
        setColorForExample(example);
        red.setOnSeekBarChangeListener(redBarListener);
        green.setOnSeekBarChangeListener(greenBarListener);
        blue.setOnSeekBarChangeListener(blueBarListener);

        Button button = findViewById(R.id.change_rgb);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Thread thread = new Thread() {
                        @Override
                        public void run() {
                            try {
                                String newState = redColor + "." + greenColor + "." + blueColor + "." + (switcher.isChecked()?"on":"off");
                                SessionData.getCurrentDevice().setState(newState);
                                DataGetter.setCommand(Long.parseLong(SessionData.getId()), SessionData.getSessionToken().getToken(), SessionData.getCurrentDevice().getId(), newState, true);
                                Intent intent = new Intent(RGB.this, Devices.class);
                                startActivity(intent);
                            } catch (Exception e) {
                            }
                        }
                    };
                    thread.start();
                    thread.join();
                }
                catch (Exception e){}
            }
        });
    }

    private static void setColorForExample(ImageView image){
        image.setBackgroundColor(Color.rgb(redColor, greenColor, blueColor));
    }

    private SeekBar.OnSeekBarChangeListener redBarListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
            redColor = seekBar.getProgress();
            setColorForExample(example);
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    };

    private SeekBar.OnSeekBarChangeListener greenBarListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
            greenColor = seekBar.getProgress();
            setColorForExample(example);
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    };

    private SeekBar.OnSeekBarChangeListener blueBarListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
            blueColor = seekBar.getProgress();
            setColorForExample(example);
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    };
}
