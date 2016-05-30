package com.eagle.applytheme;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        String msg = "";

        switch (item.getItemId()){

            case R.id.discard:
                msg = "Delete";
                break;

            case R.id.edit:
                msg = "Edit";
                break;

            case R.id.exit:
                msg = "Exit";
                break;

            case R.id.search:
                msg = "Search";
                break;

            case R.id.setting:
                msg = "Setting";
                break;
        }

        Toast.makeText(this, msg + " clicked", Toast.LENGTH_SHORT).show();
        return super.onOptionsItemSelected(item);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        getSupportActionBar().setTitle("Welcome!");
        getSupportActionBar().setSubtitle("Folks!");


//        toolbar.setLogo(R.drawable.ic_action_name);
//        toolbar.setNavigationIcon(R.drawable.ic_keyboard_backspace_black_24dp);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.setElevation(10f);
        }



    }
}
