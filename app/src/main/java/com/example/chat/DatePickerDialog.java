package com.example.chat;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;

import java.lang.reflect.Field;
import java.util.Calendar;

public class DatePickerDialog extends Dialog {

    private DatePicker dp;

    private Context context;

    private String text="";

    private Button sure,cancel;
    /**
     * 自定义Dialog监听器
     */
    public interface PriorityListener {
        /**
         * 回调函数，用于在Dialog的监听事件触发后刷新Activity的UI显示
         */
        void setDateText(int year,int month);
    }
    private PriorityListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflate = LayoutInflater.from(context);
        View view = inflate.inflate(R.layout.choose_date_layout, null);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(view);

        WindowManager.LayoutParams params = getWindow().getAttributes();
        if(params!=null){
            //获取属性可能失败 为空
            params.gravity= Gravity.CENTER_HORIZONTAL|Gravity.BOTTOM;
            getWindow().setAttributes(params);
        }

        //初始化界面控件
        initView();

        int yy=Integer.parseInt(text.substring(0,4));
        int mm=Integer.parseInt(text.substring(5));
        dp.init(yy, mm - 1, 1, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

            }
        });

        int daySpinnerId = Resources.getSystem().getIdentifier("day", "id", "android");
        if (daySpinnerId != 0) {
            View daySpinner = dp.findViewById(daySpinnerId);
            if (daySpinner != null) {
                daySpinner.setVisibility(View.GONE);
            }
        }

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int Year=dp.getYear();
                int Month=dp.getMonth()+1;
                listener.setDateText(Year,Month);
                dismiss();
            }
        });
    }

    public DatePickerDialog(Context context, String last_text, PriorityListener listener) {
        super(context);//加载dialog的样式
        this.text=last_text;
        this.context=context;
        this.listener = listener;
    }

    @Override
    public void show() {
        super.getWindow().setWindowAnimations(R.style.BottomDialog_Animation);
        super.show();
    }

    /**
     * 初始化界面控件
     */
    private void initView() {
        dp=(DatePicker)findViewById(R.id.datepicker);
        cancel=(Button)findViewById(R.id.date_cancel);
        sure=(Button)findViewById(R.id.date_true);
        dp.setMaxDate(Calendar.getInstance().getTimeInMillis());
    }

    public String getRemarkText() {
        return text;
    }
}
