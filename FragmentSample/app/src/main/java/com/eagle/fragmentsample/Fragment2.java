package com.eagle.fragmentsample;


import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.eagle.fragmentsample.model.Countur;


/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment2 extends Fragment implements View.OnClickListener {

    private long lampID;
    private Switch sw;

    public Fragment2() {
        // Required empty public constructor
    }


    @Override
    public void onStart() {
        super.onStart();

        View view = getView();
        if (view != null){
            TextView title = (TextView) view.findViewById(R.id.titleLamp);
            Countur countur = Countur.countur[(int)lampID];
            title.setText(countur.getTitle());
            TextView description = (TextView) view.findViewById(R.id.descrLamp);
            description.setText(countur.getDescription());
            ImageView img = (ImageView) view.findViewById(R.id.imgLamp);
            img.setImageResource(countur.getImageId());
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fragment_fragment2, container, false);
        sw = ((Switch) layout.findViewById(R.id.switchBtn));
        sw.setOnClickListener(this);
        return layout;
    }

    public void setLamp(long id){
        this.lampID = id;
    }



    @Override
    public void onClick(View v) {
        //Toast.makeText(getContext(), "clicked: " + v.getId(), Toast.LENGTH_SHORT).show();
        if (v.getId()==R.id.switchBtn){
            Toast.makeText(getContext(), "clicked", Toast.LENGTH_SHORT).show();
            int lmpNo = (int)lampID;
            ContentValues lampValues = new ContentValues();
            lampValues.put("SWITCH", sw.isChecked());
            SQLiteOpenHelper sqLiteOpenHelper = new DBHelper(getContext());
            SQLiteDatabase db = sqLiteOpenHelper.getWritableDatabase();
            db.update("LIGHT", lampValues,
                    "_id = ?", new String[]{Integer.toString(lmpNo)});
            Toast.makeText(getContext(), "ID: " + lmpNo + " Switch: " + sw.isChecked(), Toast.LENGTH_SHORT).show();
            db.close();
      }
    }
}
