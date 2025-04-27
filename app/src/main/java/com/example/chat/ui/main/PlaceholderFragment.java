package com.example.chat.ui.main;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.chat.AnimationUtil;
import com.example.chat.MainActivity;
import com.example.chat.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    private PageViewModel pageViewModel;

    private TextView cur_item;
    private FloatingActionButton pic_1,pic_2,pic_3,pic_4,pic_5;
    private TextView name_1,name_2,name_3,name_4,name_5;
    private LinearLayout keyboard;

    public static PlaceholderFragment newInstance(int index) {
        PlaceholderFragment fragment = new PlaceholderFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageViewModel = new ViewModelProvider(this).get(PageViewModel.class);
        int index = 1;
        if (getArguments() != null) {
            index = getArguments().getInt(ARG_SECTION_NUMBER);
        }
        pageViewModel.setIndex(index);
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_main, container, false);
        final TextView textView1 = root.findViewById(R.id.name_1);
        final TextView textView2 = root.findViewById(R.id.name_2);
        final TextView textView3 = root.findViewById(R.id.name_3);
        final TextView textView4 = root.findViewById(R.id.name_4);
        final TextView textView5 = root.findViewById(R.id.name_5);

        cur_item=(TextView) getActivity().findViewById(R.id.current_item);
        keyboard=(LinearLayout)getActivity().findViewById(R.id.keyboard);

        pic_1=(FloatingActionButton)root.findViewById(R.id.pic_1);
        pic_2=(FloatingActionButton)root.findViewById(R.id.pic_2);
        pic_3=(FloatingActionButton)root.findViewById(R.id.pic_3);
        pic_4=(FloatingActionButton)root.findViewById(R.id.pic_4);
        pic_5=(FloatingActionButton)root.findViewById(R.id.pic_5);
        name_1=(TextView)root.findViewById(R.id.name_1);
        name_2=(TextView)root.findViewById(R.id.name_2);
        name_3=(TextView)root.findViewById(R.id.name_3);
        name_4=(TextView)root.findViewById(R.id.name_4);
        name_5=(TextView)root.findViewById(R.id.name_5);

        pageViewModel.getText_1().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView1.setText(s);
            }
        });
        pageViewModel.getText_2().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView2.setText(s);
            }
        });
        pageViewModel.getText_3().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView3.setText(s);
            }
        });
        pageViewModel.getText_4().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView4.setText(s);
            }
        });
        pageViewModel.getText_5().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView5.setText(s);
            }
        });
        pageViewModel.getPic_1().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                pic_1.setImageResource(integer);
            }
        });
        pageViewModel.getPic_2().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                pic_2.setImageResource(integer);
            }
        });
        pageViewModel.getPic_3().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                pic_3.setImageResource(integer);
            }
        });
        pageViewModel.getPic_4().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                pic_4.setImageResource(integer);
            }
        });
        pageViewModel.getPic_5().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                pic_5.setImageResource(integer);
            }
        });
        return root;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);

        pic_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                cur_item.setText(name_1.getText().toString());
                keyboard.setVisibility(View.VISIBLE);
                keyboard.setAnimation(AnimationUtil.moveToViewLocation());

            }
        });
        pic_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                cur_item.setText(name_2.getText().toString());
                keyboard.setVisibility(View.VISIBLE);
                keyboard.setAnimation(AnimationUtil.moveToViewLocation());

            }
        });
        pic_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                cur_item.setText(name_3.getText().toString());
                keyboard.setVisibility(View.VISIBLE);
                keyboard.setAnimation(AnimationUtil.moveToViewLocation());

            }
        });
        pic_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                cur_item.setText(name_4.getText().toString());
                keyboard.setVisibility(View.VISIBLE);
                keyboard.setAnimation(AnimationUtil.moveToViewLocation());

            }
        });
        pic_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                cur_item.setText(name_5.getText().toString());
                keyboard.setVisibility(View.VISIBLE);
                keyboard.setAnimation(AnimationUtil.moveToViewLocation());

            }
        });
    }
}