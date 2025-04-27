package com.example.chat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

public class setbudget extends AppCompatActivity {

    private java.text.DecimalFormat df = new java.text.DecimalFormat("#0.00");

    LinearLayout back;
    private Button num_1,num_2,num_3,num_4,num_5,num_6,num_7,num_8,num_9,num_0,delete,point,sure_equal,plus,minus;
    private ImageButton delete_one;
    private LinearLayout keyboard;
    private TextView current_cal;
    private LinearLayout save;
    private CommonDialog cd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.budget);


        InitView();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setbudget.this.finish();
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tmp_s = current_cal.getText().toString();
                if(sure_equal.getText().toString().equals("=")){
                    cd = new CommonDialog(setbudget.this,"运算还没结束哦");
                    WindowManager.LayoutParams lp=cd.getWindow().getAttributes();
                    lp.alpha=0.8f;
                    cd.getWindow().setAttributes(lp);
                    cd.show();
                    setbudget.TimeCount timer = new setbudget.TimeCount(1500, 500);//具体时间自定
                    timer.start();
                }else{
                    if(tmp_s.equals("0.00")||tmp_s.equals("0")||tmp_s.equals("0.0")||tmp_s.equals("0.")){
                        cd = new CommonDialog(setbudget.this,"预算要大于 0 哦");
                        WindowManager.LayoutParams lp=cd.getWindow().getAttributes();
                        lp.alpha=0.8f;
                        cd.getWindow().setAttributes(lp);
                        cd.show();
                        setbudget.TimeCount timer = new setbudget.TimeCount(1500, 500);//具体时间自定
                        timer.start();
                    }else if(Double.parseDouble(tmp_s)>99999999){
                        cd = new CommonDialog(setbudget.this,"预算太大了!");
                        WindowManager.LayoutParams lp=cd.getWindow().getAttributes();
                        lp.alpha=0.8f;
                        cd.getWindow().setAttributes(lp);
                        cd.show();
                        setbudget.TimeCount timer = new setbudget.TimeCount(1500, 500);//具体时间自定
                        timer.start();
                    }else if(tmp_s.charAt(0)=='-'){
                        cd = new CommonDialog(setbudget.this,"预算要大于 0 哦");
                        WindowManager.LayoutParams lp=cd.getWindow().getAttributes();
                        lp.alpha=0.8f;
                        cd.getWindow().setAttributes(lp);
                        cd.show();
                        setbudget.TimeCount timer = new setbudget.TimeCount(1500, 500);//具体时间自定
                        timer.start();
                    }else{
                        setnewbudget(current_cal.getText().toString());
                        /*在这里传值*/
                        setbudget.this.finish();
                    }
                }
            }
        });

        num_0.setOnClickListener(new setbudget.OnClickEvent());
        num_1.setOnClickListener(new setbudget.OnClickEvent());
        num_2.setOnClickListener(new setbudget.OnClickEvent());
        num_3.setOnClickListener(new setbudget.OnClickEvent());
        num_4.setOnClickListener(new setbudget.OnClickEvent());
        num_5.setOnClickListener(new setbudget.OnClickEvent());
        num_6.setOnClickListener(new setbudget.OnClickEvent());
        num_7.setOnClickListener(new setbudget.OnClickEvent());
        num_8.setOnClickListener(new setbudget.OnClickEvent());
        num_9.setOnClickListener(new setbudget.OnClickEvent());

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
                        cd = new CommonDialog(setbudget.this,"预算要大于 0 哦");
                        WindowManager.LayoutParams lp=cd.getWindow().getAttributes();
                        lp.alpha=0.8f;
                        cd.getWindow().setAttributes(lp);
                        cd.show();
                        setbudget.TimeCount timer = new setbudget.TimeCount(1500, 500);//具体时间自定
                        timer.start();
                    }else if(Double.parseDouble(tmp_s)>99999999){
                        cd = new CommonDialog(setbudget.this,"预算太大了!");
                        WindowManager.LayoutParams lp=cd.getWindow().getAttributes();
                        lp.alpha=0.8f;
                        cd.getWindow().setAttributes(lp);
                        cd.show();
                        setbudget.TimeCount timer = new setbudget.TimeCount(1500, 500);//具体时间自定
                        timer.start();
                    }else if(tmp_s.charAt(0)=='-'){
                        cd = new CommonDialog(setbudget.this,"预算要大于 0 哦");
                        WindowManager.LayoutParams lp=cd.getWindow().getAttributes();
                        lp.alpha=0.8f;
                        cd.getWindow().setAttributes(lp);
                        cd.show();
                        setbudget.TimeCount timer = new setbudget.TimeCount(1500, 500);//具体时间自定
                        timer.start();
                    }
                    else{
                        setnewbudget(current_cal.getText().toString());
                        /*在这里传值*/
                        setbudget.this.finish();
                    }
                }
            }
        });
    }

    private void InitView(){
        back=findViewById(R.id.budgetback);
        save=findViewById(R.id.budgetsave);
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

        Intent intent = getIntent();

        current_cal.setText(intent.getStringExtra("budget"));/*初始化预算*/
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
    void setnewbudget(String number){
        try {
            URL url = new URL("http://47.94.221.238/budget/updatebudget.php?Number="+number);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(3*1000);
            InputStream in = conn.getInputStream();
            //JSONArray ja=StreamToJson(in);
            //JSONObject object = ja.getJSONObject(0);
            //daybudget=object.optDouble("number");
            conn.disconnect();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
        }
    }
}
