package com.example.chat;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class searchAdapter extends ArrayAdapter<singlepayitem> {
    private int resourceId;
    public searchAdapter(Context context, int textViewResourceId, List<singlepayitem> objects) {
        super(context, textViewResourceId,objects);
        resourceId=textViewResourceId;
    }

    String getstandardDateWithline(int year,int month,int day){
        String textmonth=String.valueOf(month);
        String textday=String.valueOf(day);
        while(textmonth.length()<2){
            textmonth="0"+textmonth;
        }
        while(textday.length()<2){
            textday="0"+textday;
        }
        return String.valueOf(year)+"-"+textmonth+"-"+textday;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        singlepayitem  singlepayitem=getItem(position);
        View view= LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
        TextView payname=view.findViewById(R.id.payname);
        TextView paycost=view.findViewById(R.id.paycost);
        TextView paytime=view.findViewById(R.id.time);
        FloatingActionButton pic=(FloatingActionButton)view.findViewById(R.id.search_item_pic);
        NameMap nm=new NameMap();
        pic.setImageResource(nm.getIconInteger(singlepayitem.getName()));

        Resources resource=(Resources)getContext().getResources();
        ColorStateList csl=(ColorStateList)resource.getColorStateList(nm.getColorInteger(singlepayitem.getName()));

        pic.setBackgroundTintList(csl);

        payname.setText(singlepayitem.getName());
        paytime.setText(getstandardDateWithline(singlepayitem.getYear(),singlepayitem.getMonth(),singlepayitem.getDay()));
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
