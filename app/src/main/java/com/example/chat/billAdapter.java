package com.example.chat;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class billAdapter extends ArrayAdapter<singlepayitem> {

    private int resourceId;
    public billAdapter(Context context, int textViewResourceId, List<singlepayitem> objects) {
        super(context, textViewResourceId,objects);
        resourceId=textViewResourceId;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        singlepayitem  singlepayitem=getItem(position);
        View view= LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
        TextView payname=view.findViewById(R.id.payname);
        TextView paycost=view.findViewById(R.id.paycost);

        FloatingActionButton pic=(FloatingActionButton)view.findViewById(R.id.bill_item_pic);
        NameMap nm=new NameMap();
        pic.setImageResource(nm.getIconInteger(singlepayitem.getName()));

        Resources resource=(Resources)getContext().getResources();
        ColorStateList csl=(ColorStateList)resource.getColorStateList(nm.getColorInteger(singlepayitem.getName()));

        pic.setBackgroundTintList(csl);

        payname.setText(singlepayitem.getName());
        if(singlepayitem.getCategory()==0){
            paycost.setText("+"+String.valueOf(singlepayitem.getCost()));
            paycost.setTextColor(Color.parseColor("#FF6347"));
        }
        else{
            paycost.setText("-"+String.valueOf(singlepayitem.getCost()));

        }
        return view;
    }
}
