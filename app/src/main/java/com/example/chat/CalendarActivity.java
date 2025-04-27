package com.example.chat;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class CalendarActivity extends Dialog {

    Calendar cal=Calendar.getInstance();
    CalendarView cv;
    private Context context;
    private Button date_b;
    private CommonDialog cd;

    /**
     * 自定义Dialog监听器
     */
    public interface PriorityListener {
        /**
         * 回调函数，用于在Dialog的监听事件触发后刷新Activity的UI显示
         */
        void setActivityDate(int Year,int Month,int Day);
    }
    private CalendarActivity.PriorityListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater inflate = LayoutInflater.from(context);
        View view = inflate.inflate(R.layout.calendar_layout, null);
        setContentView(view);

        date_b=(Button)view.findViewById(R.id.date);

        InitView();
        cv.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                long now = cv.getDate();

                long date_c = new GregorianCalendar(year, month, dayOfMonth).getTimeInMillis();
                long date_n = new Date().getTime();
                if(date_c>date_n){
                    cv.setDate(now);
                    cd = new CommonDialog(getContext(),"不能选择未来的时间哦");
                    //WindowManager.LayoutParams lp=cd.getWindow().getAttributes();
                    //lp.alpha=0.8f;
                    //cd.getWindow().setAttributes(lp);
                    cd.show();
                    CalendarActivity.TimeCount timer = new CalendarActivity.TimeCount(1000, 500);//具体时间自定
                    timer.start();
                }else{
                    listener.setActivityDate(year,month,dayOfMonth);
                    dismiss();
                }

            }
        });
    }

    public CalendarActivity(Context context,Calendar c_d,CalendarActivity.PriorityListener listener) {
        super(context);//加载dialog的样式
        this.context=context;
        this.listener = listener;
        this.cal=c_d;
    }

    @Override
    public void show() {
        super.show();
    }

    private void InitView(){
        cv=(CalendarView)findViewById(R.id.calenderView);
        cv.setDate(cal.getTimeInMillis());
    }

    public class TimeCount extends CountDownTimer
    {
        public TimeCount(long millisInFuture, long countDownInterval)
        {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {

        }

        @Override
        public void onFinish() {
            // TODO Auto-generated method stub
            cd.dismiss();//alertDialog是你的对话框
        }
    }
}
