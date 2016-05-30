package com.eagle.fragmenttypes;


import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextClock;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 */
public class MyDialogFragment extends DialogFragment implements View.OnClickListener {
    TextView txtBalda;


    public MyDialogFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View theView = inflater.inflate(R.layout.fragment_my_dialog, container, false);


        View doneBtn = theView.findViewById(R.id.btnDone);
        View doneBtn2 = theView.findViewById(R.id.btnDone2);
        txtBalda = (TextView) theView.findViewById(R.id.txtBalda);
        TextClock textClock = (TextClock) theView.findViewById(R.id.textClock);
        textClock.setFormat24Hour("k:mm");
        doneBtn.setOnClickListener(this);
        doneBtn2.setOnClickListener(this);
        doneBtn.requestFocus();

        return theView;
    }

    @Override
    public void onClick(View v) {
        int viewid = v.getId();
        switch (viewid){
            case R.id.btnDone:
                dismiss();
                break;
            case R.id.btnDone2:
                txtBalda.setText("dsfsdf");
                break;
        }


    }
}
