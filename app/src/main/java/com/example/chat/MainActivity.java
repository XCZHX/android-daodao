package com.example.chat;

import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.StrictMode;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.codingending.popuplayout.PopupLayout;
import com.example.chat.ui.main.SectionsPagerAdapter;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieEntry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.apache.commons.io.IOUtils;

public class MainActivity extends AppCompatActivity {

    private List<PersonChat> chatList = new ArrayList<>();
    private ListView clist;
    private Button open;
    private LinearLayout lll;
    private LinearLayout kong;
    private TabLayout tabs;
    private TextView current_cal;
    private TextView current_item;
    private TextView in_out_text;
    private LinearLayout keyboard;
    private Button num_1,num_2,num_3,num_4,num_5,num_6,num_7,num_8,num_9,num_0,delete,point,sure_equal,plus,minus;
    private ImageButton delete_one;
    private Button remark;
    private Button wallet_button;
    private Button statistics_button;
    private Button open_calendar;
    private Button date_button;
    private Button payment_button;
    private TextView sum_text,cash_text,card_text,zfb_text,wx_text;
    private TextView chart_date;
    private Button shift;
    private TextView chart_text;
    private LinearLayout no_detail;
    private TextView search_button;

    private LinearLayout tabboard;

    private CommonDialog cd;
    private RemarkDialog rd;
    private CalendarActivity ca;
    private PaymentDialog pd;
    private DatePickerDialog dpd;

    private double sum_in=0.00,sum_out=0.00;
    private String current_remark="";
    private Calendar current_date=Calendar.getInstance();
    private String current_month;
    private String current_day;
    private String current_payment="现金";

    private java.text.DecimalFormat df = new java.text.DecimalFormat("#0.00");

    private boolean main_select=true;
    private boolean wallet_select=false;
    private boolean statistics_select=false;
    private LinearLayout wallet_layout;
    private LinearLayout statistics_layout;

    private PieChart pieChart;
    private Date dddddd=new Date();
    private int chart_month=6;
    private int chart_year=2021;

    private String Reply[]={"你慢点吃嘛，不管再怎么急，也不可以不细品一下早餐的味道哦",
                            "吃的怎么样？一定没有我做的好吃吧。不要饿着自己哦~",
                            "记得多吃点蔬菜哦!",
                            "哇!今天辛苦了捏!",
                            "收到收到收到!",
                            "欧西给!"
                            };

    @Override
    protected void onRestart() {
        super.onRestart();
        //Toast.makeText(MainActivity.this,"onRestart",Toast.LENGTH_SHORT).show();
        getchat();
        updateThisMonthText();
        ChatAdapter arrayAdapter = new ChatAdapter(MainActivity.this,chatList);
        clist.setAdapter(arrayAdapter);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        InitView();

        thisMonthOut();
        updateThisMonthText();
        //showNullChart();

        //sendnew("你好呀哈桑弗兰克沙俄欧梵我i额复合物哦嗯回复势力对抗肌肤萨勒夫和罗斯福和十分大");
        //sendnew_left("最近天气如何");

        getchat();
        updateWallet();

        ChatAdapter arrayAdapter = new ChatAdapter(MainActivity.this,chatList);
        clist.setAdapter(arrayAdapter);

        open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(main_select==false&&wallet_select==true){
                    main_select=true;
                    open.setBackgroundResource(R.drawable.open_selected);
                    wallet_layout.setVisibility(View.INVISIBLE);
                    wallet_select=false;
                    wallet_button.setBackgroundResource(R.drawable.wallet_button);
                }else if(main_select==false&&statistics_select==true){
                    main_select=true;
                    open.setBackgroundResource(R.drawable.open_selected);
                    statistics_layout.setVisibility(View.INVISIBLE);
                    statistics_select=false;
                    statistics_button.setBackgroundResource(R.drawable.statistics_button);
                }
                else{
                    lll.setVisibility(View.VISIBLE);
                    lll.setAnimation(AnimationUtil.moveToViewLocation());
                }
            }
        });

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

        kong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lll.setVisibility(View.INVISIBLE);
                lll.setAnimation(AnimationUtil.moveToViewBottom());
                keyboard.setVisibility(View.INVISIBLE);
                keyboard.setAnimation(AnimationUtil.moveToViewBottom());
                current_cal.setText("0.00");
                current_remark="";
                remark.setText("添加备注");
            }
        });

        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        current_cal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                keyboard.setVisibility(View.VISIBLE);
                keyboard.setAnimation(AnimationUtil.moveToViewLocation());
            }
        });

        current_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                keyboard.setVisibility(View.INVISIBLE);
                keyboard.setAnimation(AnimationUtil.moveToViewBottom());
            }
        });

        num_0.setOnClickListener(new OnClickEvent());
        num_1.setOnClickListener(new OnClickEvent());
        num_2.setOnClickListener(new OnClickEvent());
        num_3.setOnClickListener(new OnClickEvent());
        num_4.setOnClickListener(new OnClickEvent());
        num_5.setOnClickListener(new OnClickEvent());
        num_6.setOnClickListener(new OnClickEvent());
        num_7.setOnClickListener(new OnClickEvent());
        num_8.setOnClickListener(new OnClickEvent());
        num_9.setOnClickListener(new OnClickEvent());

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                current_cal.setText("0.00");
                sure_equal.setText("确定");
            }
        });
        delete_one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tmp_String=current_cal.getText().toString();
                int tmp=current_cal.getText().toString().length();
                if(tmp_String.equals("0.00")){
                }else if(tmp_String.length()==1){
                    current_cal.setText("0.00");
                }else{
                    if(tmp_String.charAt(tmp-1)=='+'||tmp_String.charAt(tmp-1)=='-'){
                        sure_equal.setText("确定");
                    }
                    current_cal.setText(tmp_String.substring(0,tmp-1));
                }
            }
        });
        point.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tmp_s=current_cal.getText().toString();
                if(tmp_s.equals("0.00")){
                    current_cal.setText("0.");
                }else if(tmp_s.indexOf(".")!=-1){
                    if(tmp_s.indexOf("+")!=-1||tmp_s.indexOf("-")!=-1){
                        current_cal.setText(tmp_s+".");
                    }
                }else{
                    current_cal.setText(tmp_s+".");
                }
            }
        });
        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sure_equal.setText("=");
                String tmp_String=current_cal.getText().toString();
                int tmp=current_cal.getText().toString().length();

                if(tmp_String.charAt(0)=='-'){
                    tmp_String=tmp_String.substring(1,tmp);
                    if(tmp_String.indexOf("+")==-1&&tmp_String.indexOf("-")==-1){
                        current_cal.setText("-"+tmp_String+"+");
                    }else if(tmp_String.charAt(tmp-2)=='+'||tmp_String.charAt(tmp-2)=='-'){
                        current_cal.setText("-"+tmp_String.substring(0,tmp-2)+"+");
                    }else if(tmp_String.indexOf("+")!=-1){
                        String num[]=tmp_String.split("[+]");
                        double num1=Double.parseDouble(num[0]);
                        double num2=Double.parseDouble(num[1]);
                        num1=num2-num1;
                        current_cal.setText(df.format(num1)+"+");
                    }else{
                        String num[]=tmp_String.split("[-]");
                        double num1=Double.parseDouble(num[0]);
                        double num2=Double.parseDouble(num[1]);
                        num1+=num2;
                        num1=0.0-num1;
                        current_cal.setText(df.format(num1)+"+");
                    }
                }else{
                    if(tmp_String.indexOf("+")==-1&&tmp_String.indexOf("-")==-1){
                        current_cal.setText(tmp_String+"+");
                    }else if(tmp_String.charAt(tmp-1)=='+'||tmp_String.charAt(tmp-1)=='-'){
                        current_cal.setText(tmp_String.substring(0,tmp-1)+"+");
                    }else if(tmp_String.indexOf("+")!=-1){
                        String num[]=tmp_String.split("[+]");
                        double num1=Double.parseDouble(num[0]);
                        double num2=Double.parseDouble(num[1]);
                        num1+=num2;
                        current_cal.setText(df.format(num1)+"+");
                    }else{
                        String num[]=tmp_String.split("[-]");
                        double num1=Double.parseDouble(num[0]);
                        double num2=Double.parseDouble(num[1]);
                        num1-=num2;
                        current_cal.setText(df.format(num1)+"+");
                    }
                }
            }
        });
        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sure_equal.setText("=");
                String tmp_String=current_cal.getText().toString();
                int tmp=current_cal.getText().toString().length();

                if(tmp_String.charAt(0)=='-'){
                    tmp_String=tmp_String.substring(1,tmp);
                    if(tmp_String.indexOf("+")==-1&&tmp_String.indexOf("-")==-1){
                        current_cal.setText("-"+tmp_String+"-");
                    }else if(tmp_String.charAt(tmp-2)=='+'||tmp_String.charAt(tmp-2)=='-'){
                        current_cal.setText("-"+tmp_String.substring(0,tmp-2)+"-");
                    }else if(tmp_String.indexOf("+")!=-1){
                        String num[]=tmp_String.split("[+]");
                        double num1=Double.parseDouble(num[0]);
                        double num2=Double.parseDouble(num[1]);
                        num1=num2-num1;
                        current_cal.setText(df.format(num1)+"-");
                    }else{
                        String num[]=tmp_String.split("[-]");
                        double num1=Double.parseDouble(num[0]);
                        double num2=Double.parseDouble(num[1]);
                        num1+=num2;
                        num1=0.0-num1;
                        current_cal.setText(df.format(num1)+"-");
                    }
                }else{
                    if(tmp_String.indexOf("+")==-1&&tmp_String.indexOf("-")==-1){
                        current_cal.setText(tmp_String+"-");
                    }else if(tmp_String.charAt(tmp-1)=='+'||tmp_String.charAt(tmp-1)=='-'){
                        current_cal.setText(tmp_String.substring(0,tmp-1)+"-");
                    }else if(tmp_String.indexOf("+")!=-1){
                        String num[]=tmp_String.split("[+]");
                        double num1=Double.parseDouble(num[0]);
                        double num2=Double.parseDouble(num[1]);
                        num1+=num2;
                        current_cal.setText(df.format(num1)+"-");
                    }else{
                        String num[]=tmp_String.split("[-]");
                        double num1=Double.parseDouble(num[0]);
                        double num2=Double.parseDouble(num[1]);
                        num1-=num2;
                        current_cal.setText(df.format(num1)+"-");
                    }
                }
            }
        });
        sure_equal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tmp_s = current_cal.getText().toString();
                int tmp=tmp_s.length();
                if(sure_equal.getText().toString().equals("=")){
                    if(tmp_s.charAt(0)=='-'){
                        tmp_s=tmp_s.substring(1,tmp);
                        if(tmp_s.indexOf("+")==-1&&tmp_s.indexOf("-")==-1){
                            current_cal.setText("-"+df.format(Double.parseDouble(tmp_s)));
                        }else if(tmp_s.charAt(tmp-2)=='+'||tmp_s.charAt(tmp-2)=='-'){
                            current_cal.setText("-"+df.format(Double.parseDouble(tmp_s.substring(0,tmp-2))));
                        }else if(tmp_s.indexOf("+")!=-1){
                            String num[]=tmp_s.split("[+]");
                            double num1=Double.parseDouble(num[0]);
                            double num2=Double.parseDouble(num[1]);
                            num1=num2-num1;
                            current_cal.setText(df.format(num1));
                        }else{
                            String num[]=tmp_s.split("[-]");
                            double num1=Double.parseDouble(num[0]);
                            double num2=Double.parseDouble(num[1]);
                            num1+=num2;
                            num1=0.0-num1;
                            current_cal.setText(df.format(num1));
                        }
                    }else{
                        if(tmp_s.indexOf("+")==-1&&tmp_s.indexOf("-")==-1){
                            current_cal.setText(df.format(Double.parseDouble(tmp_s))+"");
                        }else if(tmp_s.charAt(tmp-1)=='+'||tmp_s.charAt(tmp-1)=='-'){
                            current_cal.setText(df.format(Double.parseDouble(tmp_s.substring(0,tmp-1)))+"");
                        }else if(tmp_s.indexOf("+")!=-1){
                            String num[]=tmp_s.split("[+]");
                            double num1=Double.parseDouble(num[0]);
                            double num2=Double.parseDouble(num[1]);
                            num1+=num2;
                            current_cal.setText(df.format(num1));
                        }else{
                            String num[]=tmp_s.split("[-]");
                            double num1=Double.parseDouble(num[0]);
                            double num2=Double.parseDouble(num[1]);
                            num1-=num2;
                            current_cal.setText(df.format(num1));
                        }
                    }
                    sure_equal.setText("确定");
                }else{
                    if(tmp_s.equals("0.00")||tmp_s.equals("0")||tmp_s.equals("0.0")||tmp_s.equals("0.")){
                        //showDialog("输入金额不能为 0","提示");
                        cd = new CommonDialog(MainActivity.this,"输入金额不能为 0");
                        WindowManager.LayoutParams lp=cd.getWindow().getAttributes();
                        lp.alpha=0.8f;
                        cd.getWindow().setAttributes(lp);
                        cd.show();
                        TimeCount timer = new TimeCount(1500, 500);//具体时间自定
                        timer.start();
                    }else if(Double.parseDouble(tmp_s)>99999999){
                        cd = new CommonDialog(MainActivity.this,"金额太大放不下啦!");
                        WindowManager.LayoutParams lp=cd.getWindow().getAttributes();
                        lp.alpha=0.8f;
                        cd.getWindow().setAttributes(lp);
                        cd.show();
                        TimeCount timer = new TimeCount(1500, 500);//具体时间自定
                        timer.start();
                    }else if(tmp_s.charAt(0)=='-'){
                        cd = new CommonDialog(MainActivity.this,"输入金额不能小于 0");
                        WindowManager.LayoutParams lp=cd.getWindow().getAttributes();
                        lp.alpha=0.8f;
                        cd.getWindow().setAttributes(lp);
                        cd.show();
                        TimeCount timer = new TimeCount(1500, 500);//具体时间自定
                        timer.start();
                    }
                    else{
                        int position = tabs.getSelectedTabPosition();
                        if(position==0){
                            sum_in+=Double.parseDouble(tmp_s);
                        }else{
                            sum_out+=Double.parseDouble(tmp_s);
                        }
                        in_out_text.setText("6月 收 "+df.format(sum_in)+" / 支 "+df.format(sum_out));

                        int num[]={0,1,2,3,4};

                        int paymethod=0;
                        switch(current_payment){
                            case "现金":
                                paymethod=0;
                                break;
                            case "储蓄卡":
                                paymethod=1;
                                break;
                            case "支付宝":
                                paymethod=2;
                                break;
                            case "微信支付":
                                paymethod=3;
                                break;
                            default:
                                paymethod=0;
                                break;
                        }

                        Date last_chat_time = StringToDate(getlastchattime());
                        Date now = new Date();
                        if(now.getTime()-last_chat_time.getTime()>60000){
                            SimpleDateFormat sformat = new SimpleDateFormat("yyyy-MM-dd HH:mm");//日期格式
                            String tiem = sformat.format(now);
                            sendtime(tiem);
                        }

                        switch(current_item.getText().toString()){
                            case "早餐":
                                addchat(current_item.getText().toString(),Double.parseDouble(df.format(Double.parseDouble(tmp_s))),current_remark,num[0],current_date,position,paymethod);
                                break;
                            case "午餐":
                                addchat(current_item.getText().toString(),Double.parseDouble(df.format(Double.parseDouble(tmp_s))),current_remark,num[1],current_date,position,paymethod);
                                break;
                            case "晚餐":
                                addchat(current_item.getText().toString(),Double.parseDouble(df.format(Double.parseDouble(tmp_s))),current_remark,num[2],current_date,position,paymethod);
                                break;
                            case "工资":
                                addchat(current_item.getText().toString(),Double.parseDouble(df.format(Double.parseDouble(tmp_s))),current_remark,num[3],current_date,position,paymethod);
                                break;
                            default:
                                addchat(current_item.getText().toString(),Double.parseDouble(df.format(Double.parseDouble(tmp_s))),current_remark,num[4],current_date,position,paymethod);
                                break;
                        }

                        if(current_remark==""){
                            sendnew(current_item.getText().toString()+" "+df.format(Double.parseDouble(tmp_s)));
                        }else{
                            sendnew(current_item.getText().toString()+" "+df.format(Double.parseDouble(tmp_s))+"，"+current_remark);
                        }

                        switch(current_item.getText().toString()){
                            case "早餐":
                                sendnew_left(Reply[0]);
                                break;
                            case "午餐":
                                sendnew_left(Reply[1]);
                                break;
                            case "晚餐":
                                sendnew_left(Reply[2]);
                                break;
                            case "工资":
                                sendnew_left(Reply[3]);
                                break;
                            default:
                                sendnew_left(Reply[4]);
                                break;
                        }

                        //adddata(current_item.getText().toString(),Double.parseDouble(tmp_s),current_date,position,paymethod);

                        ChatAdapter arrayAdapter = new ChatAdapter(MainActivity.this,chatList);
                        clist.setAdapter(arrayAdapter);

                        scrollMyListViewToBottom(arrayAdapter);

                        lll.setVisibility(View.INVISIBLE);
                        lll.setAnimation(AnimationUtil.moveToViewBottom());
                        keyboard.setVisibility(View.INVISIBLE);
                        keyboard.setAnimation(AnimationUtil.moveToViewBottom());
                        current_cal.setText("0.00");

                        current_remark="";
                        remark.setText("添加备注");
                        updateThisMonthText();
                    }
                }
            }
        });
        remark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rd = new RemarkDialog(MainActivity.this, current_remark, new RemarkDialog.PriorityListener() {
                    @Override
                    public void setActivityText(String string) {
                        current_remark=string;
                        if(current_remark.equals("")){
                            remark.setText("添加备注");
                        }else if(getLength(current_remark)<=6){
                            remark.setText(current_remark);
                        }else{
                            String tmp=setsixString(current_remark);
                            remark.setText(tmp+"…");
                        }

                    }
                });
                rd.show();
            }
        });
        wallet_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int origin = getResources().getIdentifier("wallet_button", "drawable", getPackageName());
                int selected = getResources().getIdentifier("wallet_button_selected", "drawable", getPackageName());
                if(wallet_select==false){
                    wallet_layout.setVisibility(View.VISIBLE);
                    wallet_button.setBackground(changeColor(selected,R.color.orange));
                    statistics_layout.setVisibility(View.INVISIBLE);
                    statistics_button.setBackgroundResource(R.drawable.statistics_button);
                    open.setBackgroundResource(R.drawable.open);
                    wallet_select=true;
                    main_select=false;
                    statistics_select=false;
                    updateWallet();
                }
            }
        });
        statistics_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(statistics_select==false){
                    statistics_layout.setVisibility(View.VISIBLE);
                    statistics_button.setBackground(changeColor(R.drawable.statistics_button,R.color.orange));
                    wallet_layout.setVisibility(View.INVISIBLE);
                    wallet_button.setBackgroundResource(R.drawable.wallet_button);
                    open.setBackgroundResource(R.drawable.open);
                    statistics_select=true;
                    main_select=false;
                    wallet_select=false;
                    thisMonthOut();
                    chart_text.setText("总支出");
                }
            }
        });
        open_calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(MainActivity.this,SecondActivity.class);
                startActivity(intent);
            }
        });
        date_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ca = new CalendarActivity(MainActivity.this,current_date, new CalendarActivity.PriorityListener() {
                    @Override
                    public void setActivityDate(int Year,int Month,int Day) {
                        current_date.set(Year,Month,Day);
                        int month_c=Month+1;
                        if(month_c<10){
                            current_month="0"+month_c;
                        }else{
                            current_month=month_c+"";
                        }
                        if(Day<10){
                            current_day="0"+Day;
                        }else{
                            current_day=Day+"";
                        }
                        date_button.setText(current_month+"."+current_day);
                    }
                });
                ca.getWindow().setBackgroundDrawableResource(R.drawable.big_layout);
                ca.show();
            }
        });
        payment_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd = new PaymentDialog(MainActivity.this, new PaymentDialog.PriorityListener() {
                    @Override
                    public void setActivityPayment(String payment) {
                        current_payment=payment;
                        payment_button.setText(payment);
                    }
                });
                pd.getWindow().setBackgroundDrawableResource(R.drawable.big_layout);
                pd.show();
            }
        });
        shift.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ct = chart_text.getText().toString();
                if(ct.equals("总支出")){
                    thisMonthIn();
                    chart_text.setText("总收入");
                }else{
                    thisMonthOut();
                    chart_text.setText("总支出");
                }
            }
        });
        chart_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dpd = new DatePickerDialog(MainActivity.this, chart_date.getText().toString(), new DatePickerDialog.PriorityListener() {
                    @Override
                    public void setDateText(int year,int month) {
                        String mmm;
                        String yyy=year+"";
                        if(month<10){
                            mmm="0"+month;
                        }else{
                            mmm=""+month;
                        }
                        String tete=yyy+"."+mmm;
                        chart_date.setText(tete);
                        chart_month=month;
                        chart_year=year;
                        chart_text.setText("总支出");
                        thisMonthOut();
                    }
                });
                dpd.getWindow().setBackgroundDrawableResource(R.drawable.big_layout);

                dpd.show();
            }
        });
        search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(MainActivity.this,search.class);
                startActivity(intent);
            }
        });
    }

    private void InitView(){
        clist=(ListView)findViewById(R.id.chat_list);
        open=(Button)findViewById(R.id.send);
        lll=(LinearLayout)findViewById(R.id.lll);
        kong=(LinearLayout)findViewById(R.id.kong);
        tabs=(TabLayout)findViewById(R.id.tabs);
        current_cal=(TextView)findViewById(R.id.current_cal);
        keyboard=(LinearLayout)findViewById(R.id.keyboard);
        num_0=findViewById(R.id.num_0);
        num_1=findViewById(R.id.num_1);
        num_2=findViewById(R.id.num_2);
        num_3=findViewById(R.id.num_3);
        num_4=findViewById(R.id.num_4);
        num_5=findViewById(R.id.num_5);
        num_6=findViewById(R.id.num_6);
        num_7=findViewById(R.id.num_7);
        num_8=findViewById(R.id.num_8);
        num_9=findViewById(R.id.num_9);
        delete=findViewById(R.id.delete);
        delete_one=(ImageButton)findViewById(R.id.delete_one);
        point=findViewById(R.id.mun_point);
        sure_equal=findViewById(R.id.sure_equal);
        plus=findViewById(R.id.plus);
        minus=findViewById(R.id.minus);
        current_item=(TextView)findViewById(R.id.current_item);
        tabboard=(LinearLayout)findViewById(R.id.tabboard);
        in_out_text=(TextView)findViewById(R.id.in_out_text);
        remark=(Button) findViewById(R.id.remark);
        wallet_button=(Button)findViewById(R.id.wallet_button);
        wallet_layout=(LinearLayout)findViewById(R.id.wallet);
        statistics_button=(Button)findViewById(R.id.statistics_button);
        statistics_layout=(LinearLayout)findViewById(R.id.statistics);
        pieChart = (PieChart) findViewById(R.id.pie_chart);
        open_calendar=(Button)findViewById(R.id.open_calendar);
        date_button=(Button)findViewById(R.id.date);
        payment_button=(Button)findViewById(R.id.payment);
        sum_text=(TextView)findViewById(R.id.sum_text);
        cash_text=(TextView)findViewById(R.id.cash_text);
        card_text=(TextView)findViewById(R.id.card_text);
        zfb_text=(TextView)findViewById(R.id.zfb_text);
        wx_text=(TextView)findViewById(R.id.wx_text);
        chart_date=(TextView)findViewById(R.id.chart_date);
        shift=(Button)findViewById(R.id.shift);
        chart_text=(TextView)findViewById(R.id.chart_text);
        no_detail=(LinearLayout)findViewById(R.id.no_detail);
        search_button=(TextView)findViewById(R.id.search_button);

        int month=current_date.get(Calendar.MONTH)+1;
        int day=current_date.get(Calendar.DAY_OF_MONTH);
        int year=current_date.get(Calendar.YEAR);
        if(month<10){
            current_month="0"+month;
        }else{
            current_month=month+"";
        }
        if(day<10){
            current_day="0"+day;
        }else{
            current_day=day+"";
        }

        date_button.setText(current_month+"."+current_day);
        chart_date.setText(year+"."+current_month);
    }

    private class OnClickEvent implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            String tmp_s=current_cal.getText().toString();
            int tmp=tmp_s.length();
            switch (v.getId()) {
                case R.id.num_0:
                    if(tmp_s.equals("0.00")||tmp_s.toString().equals("0")){
                        current_cal.setText("0");
                    }else if(tmp>=3) {
                        if(tmp_s.charAt(tmp-3)!='.'){
                            current_cal.setText(tmp_s+"0");
                        }
                    }else{
                        current_cal.setText(tmp_s+"0");
                    }
                    break;
                case R.id.num_1:
                    //Toast.makeText(getApplicationContext(), tmp_s, Toast.LENGTH_LONG).show();
                    if(tmp_s.equals("0.00")||tmp_s.equals("0")){
                        current_cal.setText("1");
                    }else if(tmp>=3) {
                        if(tmp_s.charAt(tmp-3)!='.'){
                            current_cal.setText(tmp_s+"1");
                        }
                    }else{
                        current_cal.setText(tmp_s+"1");
                    }
                    break;
                case R.id.num_2:
                    if(tmp_s.equals("0.00")||tmp_s.equals("0")){
                        current_cal.setText("2");
                    }else if(tmp>=3) {
                        if(tmp_s.charAt(tmp-3)!='.'){
                            current_cal.setText(tmp_s+"2");
                        }
                    }else{
                        current_cal.setText(tmp_s+"2");
                    }
                    break;
                case R.id.num_3:
                    if(tmp_s.equals("0.00")||tmp_s.equals("0")){
                        current_cal.setText("3");
                    }else if(tmp>=3) {
                        if(tmp_s.charAt(tmp-3)!='.'){
                            current_cal.setText(tmp_s+"3");
                        }
                    }else{
                        current_cal.setText(tmp_s.toString()+"3");
                    }
                    break;
                case R.id.num_4:
                    if(tmp_s.equals("0.00")||tmp_s.equals("0")){
                        current_cal.setText("4");
                    }else if(tmp>=3) {
                        if(tmp_s.charAt(tmp-3)!='.'){
                            current_cal.setText(tmp_s+"4");
                        }
                    }else{
                        current_cal.setText(tmp_s+"4");
                    }
                    break;
                case R.id.num_5:
                    if(tmp_s.equals("0.00")||tmp_s.equals("0")){
                        current_cal.setText("5");
                    }else if(tmp>=3) {
                        if(tmp_s.charAt(tmp-3)!='.'){
                            current_cal.setText(tmp_s+"5");
                        }
                    }else{
                        current_cal.setText(tmp_s+"5");
                    }
                    break;
                case R.id.num_6:
                    if(tmp_s.equals("0.00")||tmp_s.equals("0")){
                        current_cal.setText("6");
                    }else if(tmp>=3) {
                        if(tmp_s.charAt(tmp-3)!='.'){
                            current_cal.setText(tmp_s+"6");
                        }
                    }else{
                        current_cal.setText(tmp_s+"6");
                    }
                    break;
                case R.id.num_7:
                    if(tmp_s.equals("0.00")||tmp_s.equals("0")){
                        current_cal.setText("7");
                    }else if(tmp>=3) {
                        if(tmp_s.charAt(tmp-3)!='.'){
                            current_cal.setText(tmp_s+"7");
                        }
                    }else{
                        current_cal.setText(tmp_s+"7");
                    }
                    break;
                case R.id.num_8:
                    if(tmp_s.equals("0.00")||tmp_s.equals("0")){
                        current_cal.setText("8");
                    }else if(tmp>=3) {
                        if(tmp_s.charAt(tmp-3)!='.'){
                            current_cal.setText(tmp_s+"8");
                        }
                    }else{
                        current_cal.setText(tmp_s+"8");
                    }
                    break;
                case R.id.num_9:
                    if(tmp_s.equals("0.00")||tmp_s.equals("0")){
                        current_cal.setText("9");
                    }else if(tmp>=3) {
                        if(tmp_s.charAt(tmp-3)!='.'){
                            current_cal.setText(tmp_s+"9");
                        }
                    }else{
                        current_cal.setText(tmp_s+"9");
                    }
                    break;
                default:
                    break;
            }
        }
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

    void sendnew(String text){
        PersonChat hh=new PersonChat(1,"陈征瀚",text,true,false);
        chatList.add(hh);
    }

    void sendnew_left(String text){
        PersonChat hh=new PersonChat(2,"张楚",text,false,false);
        chatList.add(hh);
    }

    void sendtime(String text){
        PersonChat hh=new PersonChat(3,"时间",text,true,true);
        chatList.add(hh);
    }


    private void scrollMyListViewToBottom(ChatAdapter adapter) {
        clist.post(new Runnable() {
            @Override
            public void run() {
                // Select the last row so it will scroll into view...
                clist.setSelection(adapter.getCount() - 1);
            }
        });
    }

    public static int getLength(String s) {
        int length = 0;
        for (int i = 0; i < s.length(); i++) {
            int ascii = Character.codePointAt(s, i);
            if (ascii >= 0 && ascii <= 255) {
                length++;
            } else {
                length += 2;
            }
        }
        return length;
    }

    public static String setsixString(String s) {
        int length = 0;
        int i = 0;
        String ss="";
        for (; i < s.length(); i++) {
            int ascii = Character.codePointAt(s, i);
            if (ascii >= 0 && ascii <= 255) {
                length++;
            } else {
                length += 2;
            }
            if(length==6){
                ss=s.substring(0,i+1);
                break;
            }else if(length>6){
                ss=s.substring(0,i);
            }
        }
        return ss;
    }

    private Drawable changeColor(@DrawableRes int drawableId, @ColorRes int colorId) {
        Drawable drawable = ContextCompat
                .getDrawable(this, drawableId)
                .mutate();
        Drawable wrappedDrawable = DrawableCompat.wrap(drawable);
        ColorStateList colors = ColorStateList.valueOf(ContextCompat.getColor(this, colorId));
        DrawableCompat.setTintList(wrappedDrawable, colors);
        return wrappedDrawable;
    }

    private void showNullChart() {
//设置每份所占数量
        List<PieEntry> yvals = new ArrayList<>();
        yvals.add(new PieEntry(100f,""));
// 设置每份的颜色
        List<Integer> colors = new ArrayList<Integer>();
        colors.add(Color.parseColor("#FCEFD0"));

        PieDataSet pieDataSet=new PieDataSet(yvals,"");
        PieData pieData=new PieData(pieDataSet);
        //pieData.setDrawValues(true);
        pieData.setDrawValues(false);
        pieData.setValueFormatter(new PercentFormatter());
        pieData.setValueTextSize(12f);

        pieChart.setData(pieData);
        pieChart.setBackgroundColor(Color.WHITE);

        pieDataSet.setColors(colors.get(0).intValue());
        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleRadius(60f);//设置中间洞的大小
        // 半透明圈
        pieChart.setTransparentCircleRadius(30f);
        pieChart.setDrawCenterText(true);
        pieChart.setCenterText("0.00"); //设置中间文字
        pieChart.setCenterTextColor(Color.parseColor("#000000")); //中间问题的颜色
        pieChart.setCenterTextSizePixels(40);  //中间文字的大小px

        pieChart.getDescription().setEnabled(false);

        //图例
        Legend legend=pieChart.getLegend();
        legend.setEnabled(false);    //是否显示图例

        pieChart.notifyDataSetChanged();
        pieChart.invalidate();
    }

    private void getchat(){
        chatList.clear();
        try {
            String last_time="";
            URL url = new URL("http://47.94.221.238/chat/getchat.php");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(3*1000);

            InputStream in = conn.getInputStream();

            JSONArray ja=StreamToJson(in);
            //StringBuffer buffer = new StringBuffer();

            for (int i = 0; i < ja.length(); i++) {
                JSONObject object = ja.getJSONObject(i);
                String item = object.getString("item");
                double sum = object.optDouble("sum");
                String remark = object.optString("remark");
                int reply = object.optInt("reply");
                String time = object.getString("time");

                if(last_time.equals("")){
                    sendtime(time.substring(0,time.length()-3));
                }else if(StringToDate(time).getTime()-StringToDate(last_time).getTime()>=(long)60000){
                    sendtime(time.substring(0,time.length()-3));
                }
                last_time=time;

                if(remark.equals("")){
                    sendnew(item + " " + df.format(sum));
                }else{
                    sendnew(item + " " + df.format(sum) + "，" + remark);
                }
                sendnew_left(Reply[reply]);
            }
            conn.disconnect();

        }catch (MalformedURLException e){

        }catch (IOException e){
            cd = new CommonDialog(MainActivity.this,"该设备未联网!");
            WindowManager.LayoutParams lp=cd.getWindow().getAttributes();
            lp.alpha=0.8f;
            cd.getWindow().setAttributes(lp);
            cd.show();
            TimeCountFinish timer = new TimeCountFinish(1500, 500);//具体时间自定
            timer.start();
        }catch (JSONException e){

        }
    }

    private JSONArray StreamToJson(InputStream in){
        JSONArray ja=new JSONArray();
        try {
            BufferedReader streamReader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            StringBuilder responseStrBuilder = new StringBuilder();

            String inputStr;
            while ((inputStr = streamReader.readLine()) != null)
                responseStrBuilder.append(inputStr);

            JSONArray jsonObject = new JSONArray(responseStrBuilder.toString());
            ja=jsonObject;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
        }
        return ja;
    }

    public class TimeCountFinish extends CountDownTimer
    {
        public TimeCountFinish(long millisInFuture, long countDownInterval)
        {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {

        }

        @Override
        public void onFinish() {
            // TODO Auto-generated method stub
            finish();
        }
    }

    private Date StringToDate(String str){
        DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date dd=new Date();
        try {
            dd = format1.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dd;
    }

    private void addchat(String item,Double sum,String remark,int reply,Calendar date,int category,int paymethod){
        try {
            int year=date.get(Calendar.YEAR);
            int month=date.get(Calendar.MONTH)+1;
            int day=date.get(Calendar.DAY_OF_MONTH);
            URL url = new URL("http://47.94.221.238/chat/addchat.php?Item="+item+"&Sum="+sum+"&Remark="+remark+"&Reply="+reply+"&Year="+year+"&Month="+month+"&Day="+day+"&Category="+category+"&Paymethod="+paymethod);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(3*1000);

            InputStream in = conn.getInputStream();
            //String result = IOUtils.toString(in, "UTF-8");
            conn.disconnect();

        }catch (MalformedURLException e){

        }catch (IOException e){

        }
    }

    private void updateWallet(){
        try {
            double sum=0.00;
            double cash=0.00;
            double card=0.00;
            double zfb=0.00;
            double wx=0.00;

            URL url = new URL("http://47.94.221.238/data/getdata.php");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(3*1000);

            InputStream in = conn.getInputStream();


            JSONArray ja=StreamToJson(in);
            //StringBuffer buffer = new StringBuffer();

            for (int i = 0; i < ja.length(); i++) {
                JSONObject object = ja.getJSONObject(i);
                String name = object.getString("name");
                double cost = object.optDouble("cost");
                int category = object.optInt("category");
                int paymethod = object.optInt("paymethod");

                if(category==0){
                    switch (paymethod){
                        case 0:
                            cash+=cost;
                            break;
                        case 1:
                            card+=cost;
                            break;
                        case 2:
                            zfb+=cost;
                            break;
                        case 3:
                            wx+=cost;
                            break;
                        default:
                            break;
                    }
                }else{
                    switch (paymethod){
                        case 0:
                            cash-=cost;
                            break;
                        case 1:
                            card-=cost;
                            break;
                        case 2:
                            zfb-=cost;
                            break;
                        case 3:
                            wx-=cost;
                            break;
                        default:
                            break;
                    }
                }
            }

            sum=cash+card+zfb+wx;
            sum_text.setText(df.format(sum));
            cash_text.setText(df.format(cash));
            card_text.setText(df.format(card));
            zfb_text.setText(df.format(zfb));
            wx_text.setText(df.format(wx));

            conn.disconnect();

        }catch (MalformedURLException e){

        }catch (IOException e){

        }catch (JSONException e){
        }
    }

    private void thisMonthOut(){
        try {
            List<DetailedItem> detail=new ArrayList<>();
            double ccc[]={0.00,0.00,0.00,0.00,0.00};
            String date=chart_date.getText().toString();
            int Year=chart_year;
            int Month=chart_month;
            //String ym[]=date.split(".");
            //int Year=Integer.parseInt(ym[0]);
            //int Month=Integer.parseInt(ym[1]);
            String ttt[]={"食品","购物","出行","教育","娱乐"};
            int count=0;

            URL url = new URL("http://47.94.221.238/data/getdata.php");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(3*1000);

            InputStream in = conn.getInputStream();

            JSONArray ja=StreamToJson(in);
            //StringBuffer buffer = new StringBuffer();

            for (int i = 0; i < ja.length(); i++) {
                JSONObject object = ja.getJSONObject(i);
                String name = object.getString("name");
                double cost = object.optDouble("cost");
                int category = object.optInt("category");
                int paymethod = object.optInt("paymethod");
                int year=object.optInt("year");
                int month=object.optInt("month");

                if(Year==year&&Month==month){
                    for(int j=0;j<5;j++){
                        if(category==(j+1)){
                            ccc[j]+=cost;
                            break;
                        }
                    }
                }

            }
            if(ccc[0]==0.00&&ccc[1]==0.00&&ccc[2]==0.00&&ccc[3]==0.00&&ccc[4]==0.00){
                showNullChart();
                no_detail.setVisibility(View.VISIBLE);
            }else{
                double sum=0.00;
                List<PieEntry> yyy=new ArrayList<>();
                for(int i=0;i<5;i++){
                    if(ccc[i]!=0.00){
                        sum+=ccc[i];
                        yyy.add(new PieEntry((float) ccc[i], ttt[i]));
                        count++;
                    }
                }
                for(int i=0;i<5;i++){
                    if(ccc[i]!=0.00){
                        detail.add(new DetailedItem(ttt[i],ccc[i],ccc[i]/sum*100));
                    }
                }
                showChart(yyy,count,sum,detail);
            }
            conn.disconnect();

        }catch (MalformedURLException e){

        }catch (IOException e){

        }catch (JSONException e){
        }
    }

    private void thisMonthIn(){
        try {
            List<DetailedItem> detail=new ArrayList<>();
            double ccc[]={0.00,0.00,0.00,0.00,0.00};
            String date=chart_date.getText().toString();
            int Year=chart_year;
            int Month=chart_month;
            //String ym[]=date.split(".");
            //int Year=Integer.parseInt(ym[0]);
            //int Month=Integer.parseInt(ym[1]);
            String ttt[]={"工资","收入","投资收入","生活费","红包"};
            int count=0;

            URL url = new URL("http://47.94.221.238/data/getdata.php");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(3*1000);

            InputStream in = conn.getInputStream();

            JSONArray ja=StreamToJson(in);
            //StringBuffer buffer = new StringBuffer();

            for (int i = 0; i < ja.length(); i++) {
                JSONObject object = ja.getJSONObject(i);
                String name = object.getString("name");
                double cost = object.optDouble("cost");
                int category = object.optInt("category");
                int paymethod = object.optInt("paymethod");
                int year=object.optInt("year");
                int month=object.optInt("month");

                if(Year==year&&Month==month){
                    if(category==0){
                        switch(name){
                            case "工资":
                                ccc[0]+=cost;
                                break;
                            case "收入":
                                ccc[1]+=cost;
                                break;
                            case "投资收入":
                                ccc[2]+=cost;
                                break;
                            case "生活费":
                                ccc[3]+=cost;
                                break;
                            case "红包":
                                ccc[4]+=cost;
                                break;
                        }
                    }
                }

            }
            if(ccc[0]==0.00&&ccc[1]==0.00&&ccc[2]==0.00&&ccc[3]==0.00&&ccc[4]==0.00){
                showNullChart();
                no_detail.setVisibility(View.VISIBLE);
            }else{
                double sum=0.00;
                List<PieEntry> yyy=new ArrayList<>();
                for(int i=0;i<5;i++){
                    if(ccc[i]!=0.00){
                        sum+=ccc[i];
                        yyy.add(new PieEntry((float) ccc[i], ttt[i]));
                        count++;
                    }
                }
                for(int i=0;i<5;i++){
                    if(ccc[i]!=0.00){
                        detail.add(new DetailedItem(ttt[i],ccc[i],ccc[i]/sum*100));
                    }
                }
                showChart(yyy,count,sum,detail);
            }
            conn.disconnect();

        }catch (MalformedURLException e){

        }catch (IOException e){

        }catch (JSONException e){
        }
    }

    private void showChart(List<PieEntry> yvals,int cnt,double sum,List<DetailedItem> detail) {

        List<Integer> colors = new ArrayList<Integer>();
        colors.add(Color.parseColor("#F7D68A"));
        colors.add(Color.parseColor("#89C4F8"));
        colors.add(Color.parseColor("#9093F5"));
        colors.add(Color.parseColor("#B0E29C"));
        colors.add(Color.parseColor("#87D6D7"));

        PieDataSet pieDataSet=new PieDataSet(yvals,"");
        PieData pieData=new PieData(pieDataSet);
        //pieData.setDrawValues(true);
        pieData.setDrawValues(true);
        pieChart.setUsePercentValues(true);
        pieData.setValueFormatter(new PercentFormatter(pieChart));
        pieData.setValueTextSize(12f);

        pieChart.setData(pieData);
        pieChart.setBackgroundColor(Color.WHITE);

        switch (cnt){
            case 1:
                pieDataSet.setColors(colors.get(0).intValue());
                break;
            case 2:
                pieDataSet.setColors(colors.get(0).intValue(),colors.get(1).intValue());
                break;
            case 3:
                pieDataSet.setColors(colors.get(0).intValue(),colors.get(1).intValue(),colors.get(2).intValue());
                break;
            case 4:
                pieDataSet.setColors(colors.get(0).intValue(),colors.get(1).intValue(),colors.get(2).intValue(),colors.get(3).intValue());
                break;
            case 5:
                pieDataSet.setColors(colors.get(0).intValue(),colors.get(1).intValue(),colors.get(2).intValue(),colors.get(3).intValue(),colors.get(4).intValue());
                break;
        }

        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleRadius(60f);//设置中间洞的大小
        // 半透明圈
        pieChart.setTransparentCircleRadius(30f);
        pieChart.setDrawCenterText(true);
        pieChart.setCenterText(df.format(sum)); //设置中间文字
        pieChart.setCenterTextColor(Color.parseColor("#000000")); //中间问题的颜色
        pieChart.setCenterTextSizePixels(40);  //中间文字的大小px
        
        pieChart.getDescription().setEnabled(false);

        //图例
        Legend legend=pieChart.getLegend();
        legend.setEnabled(false);    //是否显示图例


        pieChart.notifyDataSetChanged();
        pieChart.invalidate();

        ItemAdapter adapter1=new ItemAdapter(MainActivity.this,R.layout.detailed_item,detail);

        // 将适配器上的数据传递给listView
        ListView lV=findViewById(R.id.statistics_detailed);
        lV.setAdapter(adapter1);
        no_detail.setVisibility(View.INVISIBLE);
    }

    private String getlastchattime(){
        String time="";
        try {
            String last_time="";
            URL url = new URL("http://47.94.221.238/chat/getchat.php");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(3*1000);

            InputStream in = conn.getInputStream();

            JSONArray ja=StreamToJson(in);
            //StringBuffer buffer = new StringBuffer();

            JSONObject object = ja.getJSONObject(ja.length()-1);

            time = object.getString("time");

            conn.disconnect();

        }catch (MalformedURLException e){

        }catch (IOException e){

        }catch (JSONException e){

        }
        return time;
    }

    private void updateThisMonthText(){
        try {
            double ccc[]={0.00,0.00,0.00,0.00,0.00};
            String date=chart_date.getText().toString();
            int Year=chart_year;
            int Month=chart_month;
            String ttt[]={"食品","购物","出行","教育","娱乐"};

            double sum_in_now=0.00;
            double sum_out_now=0.00;

            URL url = new URL("http://47.94.221.238/data/getdata.php");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(3*1000);

            InputStream in = conn.getInputStream();

            JSONArray ja=StreamToJson(in);
            //StringBuffer buffer = new StringBuffer();

            for (int i = 0; i < ja.length(); i++) {
                JSONObject object = ja.getJSONObject(i);
                String name = object.getString("name");
                double cost = object.optDouble("cost");
                int category = object.optInt("category");
                int paymethod = object.optInt("paymethod");
                int year=object.optInt("year");
                int month=object.optInt("month");

                if(Year==year&&Month==month){
                    if(category==0){
                        sum_in_now+=cost;
                    }else{
                        sum_out_now+=cost;
                    }
                }

            }
            sum_out=sum_out_now;
            sum_in=sum_in_now;

            in_out_text.setText(Month+"月 收 "+df.format(sum_in)+" / 支 "+df.format(sum_out));
            conn.disconnect();

        }catch (MalformedURLException e){

        }catch (IOException e){

        }catch (JSONException e){
        }
    }
}