package com.eagle.fragmentsample;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Switch;

import com.eagle.fragmentsample.model.Countur;
import com.eagle.fragmentsample.model.Lamp;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentLamp.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class FragmentLamp extends Fragment implements View.OnClickListener {

    private long lampID;
    private Switch sw;
    private int type;
    ListView listViewLamp;

    @Override
    public void onClick(View v) {

    }

    interface LampListListenerFrLmp {
        void itemClickedLmp(long id);
    }

    private FragmentLamp.LampListListenerFrLmp listener;


    public FragmentLamp() {
        // Required empty public constructor
    }

    public void setLamp(long id){
        this.lampID = id;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View theView = inflater.inflate(R.layout.fragment_fragment_lamp, container, false);
        ArrayList<Lamp> lampNames = new ArrayList<>();
        listViewLamp = (ListView) theView.findViewById(R.id.listView_lamp);

        ArrayAdapter<String> listAdapter = new ArrayAdapter<>(
                getContext(),
                android.R.layout.simple_list_item_1,
                Lamp.getTitles());
        listViewLamp.setAdapter(listAdapter);


        // Inflate the layout for this fragment
        return theView;
    }


}
