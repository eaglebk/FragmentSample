package com.eagle.fragmentsample;


import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.eagle.fragmentsample.model.Countur;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment1 extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private SQLiteDatabase db;
    private Cursor cursor;
    private boolean firstRun = true;
    ListView listViewLamp;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(false);
                ArrayAdapter<String> listAdapter = new ArrayAdapter<>(
                        getContext(),
                        android.R.layout.simple_list_item_1,
                        Countur.getTitles());
                listViewLamp.setAdapter(listAdapter);
            }
        }, 1000);
    }


    interface LampListListener {
        void itemClicked(long id);
    }

    private LampListListener listener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.listener = (LampListListener) activity;
    }

    public Fragment1() {
        // Required empty public constructor
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (!firstRun) {
            cursor.close();
            db.close();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mSwipeRefreshLayout = new SwipeRefreshLayout(getContext());
        ArrayList<Countur> counturNames = new ArrayList<>();
        View theView = inflater.inflate(R.layout.fragment_fragment1, container, false);
        mSwipeRefreshLayout = (SwipeRefreshLayout) theView.findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setOnRefreshListener(this);


//
        AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               if (listener != null){
                   listener.itemClicked(id);

               }
               }
            };


        listViewLamp = (ListView) theView.findViewById(R.id.listView_lamp);
        if (!firstRun) {
            try {
                SQLiteOpenHelper dBHelper = new DBHelper(getContext());
                db = dBHelper.getReadableDatabase();

                cursor = db.query("LIGHT", new String[]{"_ID", "NAME"},
                        null, null, null, null, null);

                CursorAdapter listAdapter = new SimpleCursorAdapter(getContext(),
                        android.R.layout.simple_list_item_1,
                        cursor,
                        new String[]{"NAME"},
                        new int[]{R.id.listView_lamp},
                        0);

                firstRun = false;
                listViewLamp.setAdapter(listAdapter);


            } catch (SQLiteException e) {
                Toast.makeText(getContext(), "Database unavailable", Toast.LENGTH_SHORT).show();
            }
        } else {

//            List<String> listNames = new ArrayList<>();
//            for (int i = 0; i < 3; i++) {
//                listNames.add()
//
//            }



        ArrayAdapter<String> listAdapter = new ArrayAdapter<>(
                getContext(),
                android.R.layout.simple_list_item_1,
                Countur.getTitles());
            listViewLamp.setAdapter(listAdapter);
           // mSwipeRefreshLayout.addView(listViewLamp);
        }


            listViewLamp.setOnItemClickListener(itemClickListener);


        return theView;
    }

}
