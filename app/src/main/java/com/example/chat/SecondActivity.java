package com.example.chat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Parcelable;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chat.ui.main.SectionsPagerAdapter;
import com.github.mikephil.charting.charts.PieChart;
import com.google.android.material.tabs.TabLayout;
import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.CalendarView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SecondActivity extends AppCompatActivity {
    CalendarView calendar ;
    ImageButton previous;
    ImageButton next;
    TextView readingMonth;
    int nowmonth;
    LinearLayout shownothing;
    LinearLayout bill;
    Map<String,myBill> countlist=new HashMap<>();
    Map<String,List<singlepayitem>> everydayList=new HashMap<>();
    TextView sumcost;
    TextView sumincome;
    ListView mybilllist;
    TextView  billdate;
    ImageButton backbtn;
    double daybudget=50;
    LinearLayout setbudget;
    int selectday;
    int selectMonth;
    int selectyear;

    ImageButton add;

    private java.text.DecimalFormat df = new java.text.DecimalFormat("#0.00");

    private LinearLayout lll;
    private LinearLayout kong;
    private TextView current_cal;
    private TextView current_item;
    private LinearLayout keyboard;
    private Button num_1,num_2,num_3,num_4,num_5,num_6,num_7,num_8,num_9,num_0,delete,point,sure_equal,plus,minus;
    private ImageButton delete_one;
    private Button remark;
    private Button date_button;
    private Button payment_button;

    private CommonDialog cd;
    private RemarkDialog rd;
    private CalendarActivity ca;
    private PaymentDialog pd;
    private String current_remark="";
    private java.util.Calendar current_date= java.util.Calendar.getInstance();
    private String current_month;
    private String current_day;
    private String current_payment="现金";

    @Override
    protected void onRestart() {
        super.onRestart();
        getbudget();
        updatecalendar();
        calendar.scrollToSelectCalendar();
    }

    String getstandardDate(int year,int month,int day){
        String textmonth=String.valueOf(month);
        String textday=String.valueOf(day);
        while(textmonth.length()<2){
            textmonth="0"+textmonth;
        }
        while(textday.length()<2){
            textday="0"+textday;
        }
        return String.valueOf(year)+textmonth+textday;
    }

    String getstandardDateWithpoint(int year,int month,int day){
        String textmonth=String.valueOf(month);
        String textday=String.valueOf(day);
        while(textmonth.length()<2){
            textmonth="0"+textmonth;
        }
        while(textday.length()<2){
            textday="0"+textday;
        }
        return String.valueOf(year)+"."+textmonth+"."+textday;
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    if(data.getBooleanExtra("delete",false)){
                        updatecalendar();
                        calendar.scrollToSelectCalendar();
                    }
                }
                break;
            default:
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        InitView();

        //id
        calendar=findViewById(R.id.calendarView);
        previous=findViewById(R.id.previous);
        next=findViewById(R.id.next);
        readingMonth=findViewById(R.id.readingmonth);
        shownothing=findViewById(R.id.shownothing);
        bill=findViewById(R.id.bill);
        sumcost=findViewById(R.id.sumcost);
        sumincome=findViewById(R.id.sumincome);
        mybilllist=findViewById(R.id.mybilllist);
        billdate=findViewById(R.id.billdate);
        backbtn=findViewById(R.id.back);
        setbudget=findViewById(R.id.setbudget);


        //设置区间
        int curyear=calendar.getCurYear();
        selectday=calendar.getCurDay();
        int curmonth=calendar.getCurMonth();
        selectMonth=calendar.getCurMonth();
        int curday=calendar.getCurDay();
        selectyear=calendar.getCurYear();
        Calendar today=new Calendar();
        Calendar startday=new Calendar();
        today.setDay(curday);
        today.setMonth(curmonth);
        today.setYear(curyear);
        startday.setDay(1);
        startday.setMonth(1);
        startday.setYear(curyear);
        calendar.setRange(curyear,1,1,curyear,curmonth,curday);



        //设置初始浏览的月份
        String showmonth=String.valueOf(curmonth);
        while(showmonth.length()<2){
            showmonth="0"+showmonth;
        }
        readingMonth.setText(String.valueOf(curyear)+"."+showmonth);
        nowmonth=curmonth;

        //点击上一个月
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(nowmonth-1>=1){
                    nowmonth--;
                    String showmonth=String.valueOf(nowmonth);
                    while(showmonth.length()<2){
                        showmonth="0"+showmonth;
                    }
                    readingMonth.setText(String.valueOf(curyear)+"."+showmonth);
                    calendar.scrollToPre();
                }
            }
        });

        //点击下一个月
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(nowmonth+1<=curmonth){
                    nowmonth++;
                    String showmonth=String.valueOf(nowmonth);
                    while(showmonth.length()<2){
                        showmonth="0"+showmonth;
                    }
                    readingMonth.setText(String.valueOf(curyear)+"."+showmonth);
                    calendar.scrollToNext();
                }
            }
        });

        //滑动月份事件
        calendar.setOnMonthChangeListener(new CalendarView.OnMonthChangeListener() {
            @Override
            public void onMonthChange(int year, int month) {
                nowmonth=month;
                String showmonth=String.valueOf(month);
                while(showmonth.length()<2){
                    showmonth="0"+showmonth;
                }
                readingMonth.setText(String.valueOf(curyear)+"."+showmonth);
            }
        });

        //日历收支更新
        updatecalendar();


        //初始化底部
        if(countlist.containsKey(getstandardDate(curyear,curmonth,curday))){
            shownothing.setVisibility(View.INVISIBLE);
            bill.setVisibility(View.VISIBLE);
            myBill tempmyBill=countlist.get(getstandardDate(curyear,curmonth,curday));
            billAdapter adapter=new billAdapter(SecondActivity.this,R.layout.bill_item,tempmyBill.getBilllist());
            mybilllist.setAdapter(adapter);
            mybilllist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    singlepayitem tempsinglepayitem=tempmyBill.getBilllist().get(position);
                    Intent intent= new Intent(SecondActivity.this,detail.class);
                    intent.putExtra("item", tempsinglepayitem);
                    startActivityForResult(intent,1);
                }
            });
            sumcost.setText(String.valueOf(tempmyBill.getCostsum()));
            sumincome.setText(String.valueOf(tempmyBill.getIncomesum()));
            billdate.setText(getstandardDateWithpoint(curyear,curmonth,curday));
        }
        else {
            bill.setVisibility(View.INVISIBLE);
            shownothing.setVisibility(View.VISIBLE);
        }


        //日期点击事件
        calendar.setOnCalendarSelectListener(new CalendarView.OnCalendarSelectListener() {
            @Override
            public void onCalendarOutOfRange(Calendar calendar) {

            }

            @Override
            public void onCalendarSelect(Calendar calendarday, boolean isClick) {
                selectday=calendarday.getDay();
                selectMonth=calendarday.getMonth();
                selectyear=calendarday.getYear();
                int color=calendarday.getSchemeColor();
                if(color==0){
                    bill.setVisibility(View.INVISIBLE);
                    shownothing.setVisibility(View.VISIBLE);
                }
                else{
                    shownothing.setVisibility(View.INVISIBLE);
                    bill.setVisibility(View.VISIBLE);
                }

                //获取某天的收支情况

                if(countlist.containsKey(getstandardDate(calendarday.getYear(),calendarday.getMonth(),calendarday.getDay()))){
                    myBill tempmyBill=countlist.get(getstandardDate(calendarday.getYear(),calendarday.getMonth(),calendarday.getDay()));
                    billAdapter adapter=new billAdapter(SecondActivity.this,R.layout.bill_item,tempmyBill.getBilllist());
                    mybilllist.setAdapter(adapter);
                    mybilllist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            singlepayitem tempsinglepayitem=tempmyBill.getBilllist().get(position);
                            Intent intent= new Intent(SecondActivity.this,detail.class);
                            intent.putExtra("item", tempsinglepayitem);
                            startActivityForResult(intent,1);
                        }
                    });
                    sumcost.setText(String.valueOf(tempmyBill.getCostsum()));
                    sumincome.setText(String.valueOf(tempmyBill.getIncomesum()));
                    billdate.setText(getstandardDateWithpoint(calendarday.getYear(),calendarday.getMonth(),calendarday.getDay()));
                }

            }
        });

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent= new Intent(SecondActivity.this,search.class);
                //startActivity(intent);
                finish();
            }
        });

        setbudget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(SecondActivity.this,setbudget.class);
                intent.putExtra("budget",df.format(daybudget));
                startActivity(intent);
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lll.setVisibility(View.VISIBLE);
                lll.setAnimation(AnimationUtil.moveToViewLocation());
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

        num_0.setOnClickListener(new SecondActivity.OnClickEvent());
        num_1.setOnClickListener(new SecondActivity.OnClickEvent());
        num_2.setOnClickListener(new SecondActivity.OnClickEvent());
        num_3.setOnClickListener(new SecondActivity.OnClickEvent());
        num_4.setOnClickListener(new SecondActivity.OnClickEvent());
        num_5.setOnClickListener(new SecondActivity.OnClickEvent());
        num_6.setOnClickListener(new SecondActivity.OnClickEvent());
        num_7.setOnClickListener(new SecondActivity.OnClickEvent());
        num_8.setOnClickListener(new SecondActivity.OnClickEvent());
        num_9.setOnClickListener(new SecondActivity.OnClickEvent());

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
                        cd = new CommonDialog(SecondActivity.this,"输入金额不能为 0");
                        WindowManager.LayoutParams lp=cd.getWindow().getAttributes();
                        lp.alpha=0.8f;
                        cd.getWindow().setAttributes(lp);
                        cd.show();
                        SecondActivity.TimeCount timer = new SecondActivity.TimeCount(1500, 500);//具体时间自定
                        timer.start();
                    }else if(Double.parseDouble(tmp_s)>99999999){
                        cd = new CommonDialog(SecondActivity.this,"金额太大放不下啦!");
                        WindowManager.LayoutParams lp=cd.getWindow().getAttributes();
                        lp.alpha=0.8f;
                        cd.getWindow().setAttributes(lp);
                        cd.show();
                        SecondActivity.TimeCount timer = new SecondActivity.TimeCount(1500, 500);//具体时间自定
                        timer.start();
                    }else if(tmp_s.charAt(0)=='-'){
                        cd = new CommonDialog(SecondActivity.this,"输入金额不能小于 0");
                        WindowManager.LayoutParams lp=cd.getWindow().getAttributes();
                        lp.alpha=0.8f;
                        cd.getWindow().setAttributes(lp);
                        cd.show();
                        SecondActivity.TimeCount timer = new SecondActivity.TimeCount(1500, 500);//具体时间自定
                        timer.start();
                    }
                    else{
                        int position = tabs.getSelectedTabPosition();

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

                        lll.setVisibility(View.INVISIBLE);
                        lll.setAnimation(AnimationUtil.moveToViewBottom());
                        keyboard.setVisibility(View.INVISIBLE);
                        keyboard.setAnimation(AnimationUtil.moveToViewBottom());
                        current_cal.setText("0.00");

                        current_remark="";
                        remark.setText("添加备注");

                        updatecalendar();
                        calendar.scrollToSelectCalendar();
                    }
                }
            }
        });
        remark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rd = new RemarkDialog(SecondActivity.this, current_remark, new RemarkDialog.PriorityListener() {
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

        date_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ca = new CalendarActivity(SecondActivity.this,current_date, new CalendarActivity.PriorityListener() {
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
                pd = new PaymentDialog(SecondActivity.this, new PaymentDialog.PriorityListener() {
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
    }

    void updatecalendar(){
        //获得收支总和
        try {
            everydayList.clear();
            countlist.clear();
            calendar.clearSchemeDate();
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
                int year=object.optInt("year");
                int month=object.optInt("month");
                int day=object.optInt("day");
                double cost = object.optDouble("cost");
                int category = object.optInt("category");
                int paymethod = object.optInt("paymethod");
                String time=object.getString("time");
                String remark=object.getString("remark");
                singlepayitem tempitem=new singlepayitem(cost,name,year,month,day,paymethod,category,time,remark);
                if(everydayList.containsKey(getstandardDate(year,month,day))){
                    everydayList.get(getstandardDate(year,month,day)).add(tempitem);
                }
                else{
                    List<singlepayitem>templist=new ArrayList<>();
                    templist.add(tempitem);
                    everydayList.put(getstandardDate(year,month,day),templist);
                }
            }
            conn.disconnect();

            for (Map.Entry<String, List<singlepayitem>> entry : everydayList.entrySet()) {
                myBill tempmybill=new myBill(entry.getValue().get(0).getYear(),entry.getValue().get(0).getMonth(),entry.getValue().get(0).getDay(),entry.getValue());
                countlist.put(tempmybill.getTime(),tempmybill);
            }

            for (Map.Entry<String, myBill> entry : countlist.entrySet()){
                Calendar specialday=new Calendar();
                specialday.setDay(entry.getValue().getBilllist().get(0).getDay());
                specialday.setMonth(entry.getValue().getBilllist().get(0).getMonth());
                specialday.setYear(entry.getValue().getBilllist().get(0).getYear());
                if(entry.getValue().getCostsum()-entry.getValue().getIncomesum()>daybudget){
                    specialday.setSchemeColor(Color.parseColor("#EF7B64"));
                    specialday.setScheme(entry.getValue().getIncomesum()-entry.getValue().getCostsum()>0?"收":"支");
                }
                else {
                    specialday.setSchemeColor(Color.parseColor("#7599E7"));
                    specialday.setScheme(entry.getValue().getIncomesum()-entry.getValue().getCostsum()>0?"收":"支");
                }
                calendar.addSchemeDate(specialday);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
        }
    }
    private void InitView(){
        add=findViewById(R.id.add_button);

        lll=(LinearLayout)findViewById(R.id.lll);
        kong=(LinearLayout)findViewById(R.id.kong);
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
        remark=(Button) findViewById(R.id.remark);
        date_button=(Button)findViewById(R.id.date);
        payment_button=(Button)findViewById(R.id.payment);

        int month=current_date.get(java.util.Calendar.MONTH)+1;
        int day=current_date.get(java.util.Calendar.DAY_OF_MONTH);
        int year=current_date.get(java.util.Calendar.YEAR);
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
        getbudget();
        //Toast.makeText(getBaseContext(),daybudget+"",Toast.LENGTH_SHORT).show();
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
    private void addchat(String item, Double sum, String remark, int reply, java.util.Calendar date, int category, int paymethod){
        try {
            int year=date.get(java.util.Calendar.YEAR);
            int month=date.get(java.util.Calendar.MONTH)+1;
            int day=date.get(java.util.Calendar.DAY_OF_MONTH);
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

    void getbudget(){
        //获得收支总和
        try {
            URL url = new URL("http://47.94.221.238/budget/getbudget.php");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(3*1000);
            InputStream in = conn.getInputStream();
            JSONArray ja=StreamToJson(in);
            //StringBuffer buffer = new StringBuffer();
            JSONObject object = ja.getJSONObject(0);
            daybudget=object.optDouble("number");
            conn.disconnect();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
        }
    }
}
