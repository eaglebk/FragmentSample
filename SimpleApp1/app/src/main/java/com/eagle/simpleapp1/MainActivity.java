package com.eagle.simpleapp1;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Objects;


public class MainActivity extends ActionBarActivity {

    private TextView mPasswordText;
    private TextView mEmailText;
    private TextView mDateText;
    private Button mClickButton;
    private RadioButton mRadioButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPasswordText = (EditText)findViewById(R.id.passwordText);
        mEmailText = (EditText)findViewById(R.id.emailText);
        mDateText = (EditText)findViewById(R.id.dateText);
        mClickButton = (Button)findViewById(R.id.clickButton);

        mClickButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String textEmail = mEmailText.getText().toString();
                String textPass = mPasswordText.getText().toString();
                String textDate = mDateText.getText().toString();
                if (!Objects.equals(textEmail, "")) {
                    Toast.makeText(MainActivity.this, mEmailText.getText().toString(), Toast.LENGTH_LONG).show();
                }
                if (!Objects.equals(textPass, "")) {
                    Toast.makeText(MainActivity.this, mPasswordText.getText().toString(), Toast.LENGTH_LONG).show();
                }
                if (!Objects.equals(textDate, "")) {
                    Toast.makeText(MainActivity.this, mDateText.getText().toString(), Toast.LENGTH_LONG).show();
                }
            }
        });


    }

    public void onRadioButtonClicked(View v){
        boolean checked = ((RadioButton) v).isChecked();

        switch (v.getId()){
            case R.id.radioButton:
                if (checked) Toast.makeText(MainActivity.this, "One is checked", Toast.LENGTH_SHORT).show();
                break;
            case R.id.radioButton2:
                if (checked)  Toast.makeText(MainActivity.this, "Two is checked", Toast.LENGTH_SHORT).show();
                break;
            case R.id.radioButton3:
                if (checked)  Toast.makeText(MainActivity.this, "Three is checked", Toast.LENGTH_SHORT).show();
                break;
        }
    };

}
