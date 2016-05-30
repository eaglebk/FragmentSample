package com.eagle.fragmentsample;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.eagle.fragmentsample.model.Countur;
import com.eagle.fragmentsample.model.Lamp;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, Fragment1.LampListListener, FragmentLamp.LampListListenerFrLmp {

    public static final String FRM = "FR_1";
    public static final String SERVICE_CLASSNAME = "com.eagle.fragmentsample.Service7939";
    public static final String BROADCAST_ACTION = "BROADCAST_ACTION";
    public static final String CMD = "CMD";
    private Toolbar toolbar;
    static String LogMsgID = "loghub: ";

    private String[] titles;
    private String[] valueSetting;
    private ListView drawerList;
    StringBuilder stringCmd;
    private LinearLayout leftMenu;
    DBHelper dbHelper;
    private Button button;
    BroadcastReceiver br;
    private TextView txtView;
    boolean flagEndCmd = false;
    String finalStr;
    String stringCmdEnd;
    Countur currentCountur;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(br);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String msg = "";

        switch (item.getItemId()) {

            case R.id.one_fragment:
                msg = "one_fragment";
                setFragment(new Fragment1());
                break;

            case R.id.two_fragment:
                msg = "two_fragment";
                setFragment(new Fragment2());
                break;

        }

        Toast.makeText(this, msg + " clicked", Toast.LENGTH_SHORT).show();
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setUpToolbar();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.setElevation(10f);
        }

        findViewById(R.id.button1).setOnClickListener(this);
        findViewById(R.id.button2).setOnClickListener(this);


        titles = getResources().getStringArray(R.array.titles);
        valueSetting = getResources().getStringArray(R.array.value_setting);

        leftMenu = (LinearLayout) findViewById(R.id.leftMenu);

        drawerList = (ListView) findViewById(R.id.drawer);
        drawerList.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_activated_1, titles));

        drawerList.setOnItemClickListener(new DrawerItemClickListener());

        setFragment(new Fragment1());
        button = (Button) findViewById(R.id.btnConAndAdd);
        txtView = ((TextView) findViewById(R.id.txtCmd));
        stringCmd = new StringBuilder();

        br = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                final String cmd = intent.getStringExtra(CMD);
                Log.d(LogMsgID, cmd);
                final String commandString = cmd;
                txtView.setText(cmd);
                Thread trThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                       flagEndCmd = isFinalStr(commandString);
                        if (stringCmdEnd != null){

                            Log.d(LogMsgID, "run: " + stringCmdEnd);
                                currentCountur = getCurrentDetail(stringCmdEnd);
//                            String[] isbnParts = stringCmdEnd.split("!");
//                            for (String strItem  : isbnParts) {
//                                Log.d(LogMsgID, "isbnParts: " + strItem);
//                            }
                        }
                    }
                });
                trThread.start();



            }
        };

        IntentFilter intFilt = new IntentFilter(BROADCAST_ACTION);

        registerReceiver(br, intFilt);

        dbHelper = new DBHelper(this);


    }

    private Countur getCurrentDetail(String stringCmdEnd){
        Countur currentCountur = new Countur();
        List<Lamp> lamp = new ArrayList<>();
        String[] a1 = stringCmdEnd.split(";");
        String[] a2 = a1[2].split(":");
        currentCountur.setTitle(a2[1]);
        String[] a3 = a1[4].split("!");
        a3[0].replace("05:","");
        for (String avar : a3) {
            String[] avararr = avar.split(",");
            lamp.add(new Lamp(avararr[0], Integer.parseInt(avararr[1]), false));
            currentCountur.setLamps(lamp);
        }
        //String[] a4 = a3[4].split(",");
       // currentCountur.setLamps();
        return currentCountur;
}


    private void setUpToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        getSupportActionBar().setTitle("Home Page!");
    }


    @Override
    public void onClick(View v) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        switch (v.getId()) {

            case R.id.button1:
                setFragment(new Fragment1());
                break;

            case R.id.button2:
                setFragment(new Fragment2());
                break;

            case R.id.btnConAndAdd:
                Toast.makeText(this, String.valueOf(insertLight(db, "LED", "Led light with rgb color", R.drawable.one, 0)), Toast.LENGTH_SHORT).show();
                updateButton();
                break;

            case R.id.btnAddCont:
                addNewCont();
                break;


        }
    }

    private void addNewCont() {
//        Countur tempCountur = new Countur("Lamp1","description",R.drawable.lamp_up);
        Countur tempCountur = new Countur();
        tempCountur.setTitle("Lamp1");
        tempCountur.setDescription("description");
        tempCountur.setImageId(R.drawable.lamp_up);
    }


    public void setFragment2(android.support.v4.app.Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();

        if (fragment != null && fragmentManager.findFragmentById(fragment.getId()) == null) {
            fragmentManager.beginTransaction()
                    .replace(R.id.frameLayout, fragment)
                    .addToBackStack(null)
                    .commit();
        }

    }

    private long insertLight(SQLiteDatabase db, String name, String description, int resourceID, int sw) {
        ContentValues lightValues = new ContentValues();
        lightValues.put("NAME", name);
        lightValues.put("DESCRIPTION", description);
        lightValues.put("IMAGE_RESOURCE_ID", resourceID);
        lightValues.put("SWITCH", sw);
        long id = db.insert("LIGHT", null, lightValues);
        return id;
    }

    private void setFragment(android.support.v4.app.Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();

        if (fragment != null && fragmentManager.findFragmentById(fragment.getId()) == null) {
            fragmentManager.beginTransaction()
                    .replace(R.id.frameLayout, fragment)
                    .commit();
        }


    }

    public void userAvaClicked(View view) {
        Toast.makeText(this, "clicked by frm", Toast.LENGTH_SHORT).show();
        // setFragment2(new FragmentRoom());


    }

    @Override
    public void itemClicked(long id) {
        FragmentLamp fragmentLamp = new FragmentLamp();
        fragmentLamp.setLamp(id);
        setFragment2(fragmentLamp);
    }

    @Override
    public void itemClickedLmp(long id) {
        FragmentLamp fragmentLamp = new FragmentLamp();
        fragmentLamp.setLamp(id);
        setFragment2(fragmentLamp);
    }


    private class DrawerItemClickListener implements android.widget.AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
            setActionBarTitle(position);
            DrawerLayout drawerLayout = ((DrawerLayout) findViewById(R.id.drawer_layout));
            drawerLayout.closeDrawer((leftMenu));
        }
    }

    private void setActionBarTitle(int position) {
        String title;
        if (position == 0) {
            title = getResources().getString(R.string.app_name);
        } else {
            title = titles[position];
        }

        getSupportActionBar().setTitle(title);
    }

    private void selectItem(int position) {
        switch (position) {
            case 0:
                setFragment(new Fragment1());

                break;
            case 1:
                setFragment(new Fragment2());
                break;
        }
        // Toast.makeText(this, "position is " + position, Toast.LENGTH_SHORT).show();
    }

    // Method to start the service
    private void startService7939() {
        Log.d(LogMsgID, "StartService");
       // startService(new Intent(getBaseContext(), Service7939.class));
        startService(new Intent(this, Service7939.class));
    }

    // Method to stop the service
    public void stopService7939() {
        Log.d(LogMsgID, "StopService");
        stopService(new Intent(this, Service7939.class));
        //finish();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        // updateButton(button);
    }

    private boolean serviceIsRunning() {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (SERVICE_CLASSNAME.equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }


    private void updateButton() {
        if (serviceIsRunning()) {
                    stopService7939();
        }
        else {
                    startService7939();
            }
        }

    boolean isFinalStr(String finalStr){

        if (finalStr.length() != 0) {
            Log.d(LogMsgID, "isFinalStr: " + finalStr);
            int A = finalStr.indexOf("NN") + 2;
            int B = finalStr.indexOf("NP") + 2;

            char indOne = finalStr.charAt(A);
            char indTwo = finalStr.charAt(B);
            Log.d(LogMsgID, "finalStr.charAt(A): " + indOne + ", " + "finalStr.charAt(B): " + indTwo);
            if (indOne == indTwo) {
                stringCmd.append(finalStr.substring(8));
                Log.d(LogMsgID, "Char equal: " + indOne + " and " + indTwo);
                stringCmdEnd =  stringCmd.toString();
                return true;
            }

            stringCmd.append(finalStr.substring(8));

        }

        return false;
    }
}


