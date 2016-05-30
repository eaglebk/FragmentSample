package com.eagle.fragmenttypes;

import android.app.ListFragment;
import android.os.Bundle;
import android.widget.ArrayAdapter;

public class MyListFragment extends ListFragment {
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        String[] courseTitles = getResources().getStringArray(R.array.courseTitles);
        ArrayAdapter<String> courseTitleAdapter =
                new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_activated_1, courseTitles);
        setListAdapter(courseTitleAdapter);
    }
}