package com.example.chat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.media.Image;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class detail extends AppCompatActivity {
    ImageButton backbtn;
    TextView payname;
    TextView paycost;
    TextView paytime;
    TextView payremark;
    TextView paymethod;
    ImageButton delete_now;
    LinearLayout updatecost;
    LinearLayout updatetime;
    LinearLayout updatepaymethod;
    LinearLayout updateremark;
    double realcost;
    int realday,realmonth,realyear;
    int realpaymethod;
    String realremark;

    private RemarkDialog rd;
    private CalendarActivity ca;
    private PaymentDialog pd;
    private CommonDialog cd;

    private LinearLayout keyboard,kong;
    private Button num_1,num_2,num_3,num_4,num_5,num_6,num_7,num_8,num_9,num_0,delete,point,sure_equal,plus,minus;
    private ImageButton delete_one;
    private TextView current_cal;

    private java.text.DecimalFormat df = new java.text.DecimalFormat("#0.00");

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


    @Override
    public void onBackPressed() {
        Intent intent=new Intent();
        intent.putExtra("delete",true);
        setResult(RESULT_OK,intent);
        detail.this.finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail);
        Intent intent = getIntent();
        singlepayitem temp = (singlepayitem)intent.getSerializableExtra("item");


        //id
        backbtn=findViewById(R.id.back);
        payname=findViewById(R.id.name);
        paycost=findViewById(R.id.cost);
        paytime=findViewById(R.id.time);
        payremark=findViewById(R.id.remark);
        paymethod=findViewById(R.id.paymethod);
        delete_now=findViewById(R.id.delete_now);
        updatecost=findViewById(R.id.updatecost);
        updatetime=findViewById(R.id.updatetime);
        updatepaymethod=findViewById(R.id.updatepaymethod);
        updateremark=findViewById(R.id.updateremark);
        keyboard=findViewById(R.id.keyboard);
        kong=findViewById(R.id.kong);
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
        current_cal=findViewById(R.id.cost);


        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.putExtra("delete",true);
                setResult(RESULT_OK,intent);
                detail.this.finish();
            }
        });

        delete_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    URL url = new URL("http://47.94.221.238/data/deleteBytime.php?Time="+temp.getTime().replace(" ","+"));
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setConnectTimeout(3*1000);
                    InputStream in = conn.getInputStream();
                    conn.disconnect();
                } catch (ProtocolException e) {
                    e.printStackTrace();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Intent intent=new Intent();
                intent.putExtra("delete",true);
                setResult(RESULT_OK,intent);
                detail.this.finish();
            }
        });

        payname.setText(temp.getName());
        paycost.setText(String.valueOf(temp.getCost()));
        paytime.setText(getstandardDateWithline(temp.getYear(),temp.getMonth(),temp.getDay()));
        paymethod.setText(CommonUtil.paymethodTranslate(temp.getPaymethod()));
        payremark.setText(temp.getRemark());

        FloatingActionButton pic=(FloatingActionButton)findViewById(R.id.detail_pic);
        NameMap nm=new NameMap();
        pic.setImageResource(nm.getIconInteger(temp.getName()));
        Resources resource=(Resources)getBaseContext().getResources();
        ColorStateList csl=(ColorStateList)resource.getColorStateList(nm.getColorInteger(temp.getName()));

        pic.setBackgroundTintList(csl);


        //更新当前cost
        updatecost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                keyboard.setVisibility(View.VISIBLE);
                keyboard.setAnimation(AnimationUtil.moveToViewLocation());
            }
        });

        //更新当前time
        updatetime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal=new GregorianCalendar(temp.getYear(),temp.getMonth()-1,temp.getDay());
                ca = new CalendarActivity(detail.this,cal, new CalendarActivity.PriorityListener() {
                    @Override
                    public void setActivityDate(int Year,int Month,int Day) {
                        realyear=Year;
                        realmonth=Month+1;
                        realday=Day;
                        paytime.setText(getstandardDateWithline(realyear,realmonth,realday));//更新界面的time
                        try {
                            URL url = new URL("http://47.94.221.238/data/updateymd.php?Year="+String.valueOf(realyear)+"&Month="+String.valueOf(realmonth)+"&Day="+String.valueOf(realday)+"&Time="+temp.getTime());
                            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                            conn.setRequestMethod("GET");
                            conn.setConnectTimeout(3*1000);
                            InputStream in = conn.getInputStream();
                            conn.disconnect();
                        } catch (ProtocolException e) {
                            e.printStackTrace();
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
                ca.getWindow().setBackgroundDrawableResource(R.drawable.big_layout);
                ca.show();
            }
        });

        //更新当前paymethod
        updatepaymethod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd = new PaymentDialog(detail.this, new PaymentDialog.PriorityListener() {
                    @Override
                    public void setActivityPayment(String payment) {
                        switch (payment){
                            case "现金":
                                realpaymethod=0;
                                break;
                            case "储蓄卡":
                                realpaymethod=1;
                                break;
                            case "支付宝":
                                realpaymethod=2;
                                break;
                            case "微信支付":
                                realpaymethod=3;
                                break;
                        }
                        paymethod.setText(CommonUtil.paymethodTranslate(realpaymethod));//更新界面的paymethod
                        try {
                            URL url = new URL("http://47.94.221.238/data/updatepaymethod.php?Paymethod="+String.valueOf(realpaymethod)+"&Time="+temp.getTime());
                            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                            conn.setRequestMethod("GET");
                            conn.setConnectTimeout(3*1000);
                            InputStream in = conn.getInputStream();
                            conn.disconnect();
                        } catch (ProtocolException e) {
                            e.printStackTrace();
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
                pd.getWindow().setBackgroundDrawableResource(R.drawable.big_layout);
                pd.show();
            }
        });

        //更新当前remark
        updateremark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rd = new RemarkDialog(detail.this, temp.getRemark(), new RemarkDialog.PriorityListener() {
                    @Override
                    public void setActivityText(String string) {
                        realremark=string;
                        //realremark为输出
                        payremark.setText(realremark);//更新界面的remark
                        try {
                            URL url = new URL("http://47.94.221.238/data/updateremark.php?Remark="+realremark+"&Time="+temp.getTime());
                            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                            conn.setRequestMethod("GET");
                            conn.setConnectTimeout(3*1000);
                            InputStream in = conn.getInputStream();
                            conn.disconnect();
                        } catch (ProtocolException e) {
                            e.printStackTrace();
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
                rd.show();
            }
        });

        kong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tmp_s=current_cal.getText().toString();
                if(sure_equal.getText().toString().equals("=")){
                    cd = new CommonDialog(detail.this,"运算还没结束哦");
                    WindowManager.LayoutParams lp=cd.getWindow().getAttributes();
                    lp.alpha=0.8f;
                    cd.getWindow().setAttributes(lp);
                    cd.show();
                    detail.TimeCount timer = new detail.TimeCount(1500, 500);//具体时间自定
                    timer.start();
                }else{
                    if(tmp_s.equals("0.00")||tmp_s.equals("0")||tmp_s.equals("0.0")||tmp_s.equals("0.")){
                        cd = new CommonDialog(detail.this,"金额不能为 0");
                        WindowManager.LayoutParams lp=cd.getWindow().getAttributes();
                        lp.alpha=0.8f;
                        cd.getWindow().setAttributes(lp);
                        cd.show();
                        detail.TimeCount timer = new detail.TimeCount(1500, 500);//具体时间自定
                        timer.start();
                    }else if(Double.parseDouble(tmp_s)>99999999){
                        cd = new CommonDialog(detail.this,"金额太大放不下啦!");
                        WindowManager.LayoutParams lp=cd.getWindow().getAttributes();
                        lp.alpha=0.8f;
                        cd.getWindow().setAttributes(lp);
                        cd.show();
                        detail.TimeCount timer = new detail.TimeCount(1500, 500);//具体时间自定
                        timer.start();
                    }else if(tmp_s.charAt(0)=='-'){
                        cd = new CommonDialog(detail.this,"金额不能小于 0");
                        WindowManager.LayoutParams lp=cd.getWindow().getAttributes();
                        lp.alpha=0.8f;
                        cd.getWindow().setAttributes(lp);
                        cd.show();
                        detail.TimeCount timer = new detail.TimeCount(1500, 500);//具体时间自定
                        timer.start();
                    }
                    else{
                        keyboard.setVisibility(View.INVISIBLE);
                        keyboard.setAnimation(AnimationUtil.moveToViewBottom());
                        current_cal.setText(df.format(Double.parseDouble(tmp_s)));
                        realcost=Double.parseDouble(tmp_s);//更新界面的cost
                        try {
                            URL url = new URL("http://47.94.221.238/data/updatecost.php?Cost="+realcost+"&Time="+temp.getTime());
                            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                            conn.setRequestMethod("GET");
                            conn.setConnectTimeout(3*1000);
                            InputStream in = conn.getInputStream();
                            conn.disconnect();
                        } catch (ProtocolException e) {
                            e.printStackTrace();
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });

        num_0.setOnClickListener(new detail.OnClickEvent());
        num_1.setOnClickListener(new detail.OnClickEvent());
        num_2.setOnClickListener(new detail.OnClickEvent());
        num_3.setOnClickListener(new detail.OnClickEvent());
        num_4.setOnClickListener(new detail.OnClickEvent());
        num_5.setOnClickListener(new detail.OnClickEvent());
        num_6.setOnClickListener(new detail.OnClickEvent());
        num_7.setOnClickListener(new detail.OnClickEvent());
        num_8.setOnClickListener(new detail.OnClickEvent());
        num_9.setOnClickListener(new detail.OnClickEvent());

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
                        cd = new CommonDialog(detail.this,"输入金额不能为 0");
                        WindowManager.LayoutParams lp=cd.getWindow().getAttributes();
                        lp.alpha=0.8f;
                        cd.getWindow().setAttributes(lp);
                        cd.show();
                        detail.TimeCount timer = new detail.TimeCount(1500, 500);//具体时间自定
                        timer.start();
                    }else if(Double.parseDouble(tmp_s)>99999999){
                        cd = new CommonDialog(detail.this,"金额太大放不下啦!");
                        WindowManager.LayoutParams lp=cd.getWindow().getAttributes();
                        lp.alpha=0.8f;
                        cd.getWindow().setAttributes(lp);
                        cd.show();
                        detail.TimeCount timer = new detail.TimeCount(1500, 500);//具体时间自定
                        timer.start();
                    }else if(tmp_s.charAt(0)=='-'){
                        cd = new CommonDialog(detail.this,"输入金额不能小于 0");
                        WindowManager.LayoutParams lp=cd.getWindow().getAttributes();
                        lp.alpha=0.8f;
                        cd.getWindow().setAttributes(lp);
                        cd.show();
                        detail.TimeCount timer = new detail.TimeCount(1500, 500);//具体时间自定
                        timer.start();
                    }
                    else{
                        keyboard.setVisibility(View.INVISIBLE);
                        keyboard.setAnimation(AnimationUtil.moveToViewBottom());
                        current_cal.setText(df.format(Double.parseDouble(tmp_s)));
                        realcost=Double.parseDouble(tmp_s);//更新界面的cost
                        try {
                            URL url = new URL("http://47.94.221.238/data/updatecost.php?Cost="+realcost+"&Time="+temp.getTime());
                            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                            conn.setRequestMethod("GET");
                            conn.setConnectTimeout(3*1000);
                            InputStream in = conn.getInputStream();
                            conn.disconnect();
                        } catch (ProtocolException e) {
                            e.printStackTrace();
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
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
}
