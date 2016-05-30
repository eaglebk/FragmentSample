package com.eagle.com.helloworld;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


public class MyActivity extends Activity {

    private TextView message;
    private ImageView droid;
    private View.OnClickListener droidOnTapListener;
    private int counter = 0;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        initializeApp();
    }

    private void initializeApp() {
        message = (TextView) findViewById(R.id.message);
        droid = (ImageView) findViewById(R.id.imageView);

        droidOnTapListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                touchDroid();
            }
        };
        droid.setOnClickListener(droidOnTapListener);

    }

    private void touchDroid() {
        counter++;
        String temp = getStringForDisplay(counter);
        message.setText(String.format("You touched the droid %s", temp));
    }

    private String getStringForDisplay(int count) {
        String temp;
        switch (count) {
            case 1:
                temp = "once";

                break;
            case 2:
                temp = "twice";
                break;
            default:
                temp = String.format("%d times", count);

        }
        return temp;

    }

}
