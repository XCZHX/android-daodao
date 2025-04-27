package com.example.chat;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;

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

public class PaymentDialog extends Dialog {
    private LinearLayout zfb,wx,card,cash;
    private TextView zfb_text,wx_text,card_text,cash_text;
    private Context context;
    private java.text.DecimalFormat df = new java.text.DecimalFormat("#0.00");
    /**
     * 自定义Dialog监听器
     */
    public interface PriorityListener {
        /**
         * 回调函数，用于在Dialog的监听事件触发后刷新Activity的UI显示
         */
        void setActivityPayment(String payment);
    }
    private PaymentDialog.PriorityListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater inflate = LayoutInflater.from(context);
        View view = inflate.inflate(R.layout.payment_layout, null);
        setContentView(view);

        InitView();

        updateWallet();

        zfb.setOnClickListener(new PaymentDialog.OnClickEvent());
        wx.setOnClickListener(new PaymentDialog.OnClickEvent());
        card.setOnClickListener(new PaymentDialog.OnClickEvent());
        cash.setOnClickListener(new PaymentDialog.OnClickEvent());
    }

    public PaymentDialog(Context context,PaymentDialog.PriorityListener listener) {
        super(context);//加载dialog的样式
        this.context=context;
        this.listener = listener;
    }

    @Override
    public void show() {
        super.show();
    }

    private void InitView(){
        zfb=(LinearLayout)findViewById(R.id.zfb);
        wx=(LinearLayout)findViewById(R.id.wx);
        card=(LinearLayout)findViewById(R.id.card);
        cash=(LinearLayout)findViewById(R.id.cash);
        zfb_text=(TextView)findViewById(R.id.zfb_t);
        wx_text=(TextView)findViewById(R.id.wx_t);
        card_text=(TextView)findViewById(R.id.card_t);
        cash_text=(TextView)findViewById(R.id.cash_t);
    }

    private class OnClickEvent implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.zfb:
                    listener.setActivityPayment("支付宝");
                    dismiss();
                    break;
                case R.id.wx:
                    listener.setActivityPayment("微信支付");
                    dismiss();
                    break;
                case R.id.card:
                    listener.setActivityPayment("储蓄卡");
                    dismiss();
                    break;
                case R.id.cash:
                    listener.setActivityPayment("现金");
                    dismiss();
                    break;
                default:
                    break;
            }
        }
    }

    private void updateWallet(){
        try {
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

            cash_text.setText("余额: "+df.format(cash));
            card_text.setText("余额: "+df.format(card));
            zfb_text.setText("余额: "+df.format(zfb));
            wx_text.setText("余额: "+df.format(wx));

            conn.disconnect();

        }catch (MalformedURLException e){

        }catch (IOException e){

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
}
