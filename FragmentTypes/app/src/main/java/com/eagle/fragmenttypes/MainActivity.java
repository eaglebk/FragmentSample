package com.eagle.fragmenttypes;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends Activity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView textView = (TextView) findViewById(R.id.txtTest);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDoneClick();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){

                    case R.id.one:
                        Intent intent = new Intent(this,MyLIstActivty.class);
                        startActivity(intent);
                        break;

                    case R.id.two:
//                        Toast.makeText(this, "Item2 was been clicked", Toast.LENGTH_SHORT).show();
                          onDoneClick();
                        break;
                    case R.id.three:
                          onDialogClick();
                        break;

                }

        return super.onOptionsItemSelected(item);
    }

    private void onDialogClick() {
        FragmentManager fm = getFragmentManager();
        MyAlertDialogFragment myAlertDialogFragment = new MyAlertDialogFragment();
        myAlertDialogFragment.show(fm,"MyDialog");
    }

    private void onDoneClick() {
        FragmentManager fm = getFragmentManager();
        MyDialogFragment theFragment = new MyDialogFragment();
        theFragment.setStyle(DialogFragment.STYLE_NO_TITLE,0);
        theFragment.show(fm,"MyDialog");

    }

}
