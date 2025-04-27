package com.example.chat;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.haibin.calendarview.Calendar;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class search extends AppCompatActivity {
    private AnimationNestedScrollView sv_view;
    private LinearLayout ll_search;
    private TextView tv_title;
    private float LL_SEARCH_MIN_TOP_MARGIN, LL_SEARCH_MAX_TOP_MARGIN, LL_SEARCH_MAX_WIDTH, LL_SEARCH_MIN_WIDTH, TV_TITLE_MAX_TOP_MARGIN;
    private ViewGroup.MarginLayoutParams searchLayoutParams, titleLayoutParams;
    ListView searchList;
    EditTextWithDel searched;
    List<singlepayitem> paylist=new ArrayList<>();
    LinearLayout resultplace;
    TextView paytype;
    TextView sum;
    LinearLayout back;
    String input;

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
                        if(input.length()==0){
                            resultplace.setVisibility(View.INVISIBLE);
                            return;
                        }
                        try {
                            double all=0;
                            paylist.clear();
                            URL url = new URL("http://47.94.221.238/data/selectpayByname.php?Name="+input);
                            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                            conn.setRequestMethod("GET");
                            conn.setConnectTimeout(3*1000);
                            InputStream in = conn.getInputStream();
                            JSONArray ja=StreamToJson(in);
                            if(ja.length()==0){
                                resultplace.setVisibility(View.INVISIBLE);
                            }
                            else{
                                resultplace.setVisibility(View.VISIBLE);
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
                                    if(category==0){
                                        paytype.setText("收入:");
                                    }
                                    else{
                                        paytype.setText("支出:");
                                    }
                                    all+=cost;
                                    paylist.add(tempitem);
                                }
                                sum.setText(String.valueOf(all));
                                searchAdapter adapter=new searchAdapter(search.this,R.layout.search_item,paylist);
                                searchList.setAdapter(adapter);
                                searchList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        singlepayitem tempsinglepayitem=paylist.get(position);
                                        Intent intent= new Intent(search.this,detail.class);
                                        intent.putExtra("item", tempsinglepayitem);
                                        startActivityForResult(intent,1);
                                    }
                                });
                            }

                            conn.disconnect();

                        } catch (IOException | JSONException e) {
                            e.printStackTrace();
                        } finally {
                        }
                    }
                }
                break;
            default:
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        initView();

        //id
        searchList=findViewById(R.id.search_list);
        searched=findViewById(R.id.search_tv_search);
        resultplace=findViewById(R.id.resultplace);
        paytype=findViewById(R.id.type);
        sum=findViewById(R.id.sum);
        back=findViewById(R.id.searchback);

        //查询
        searched.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String searchname=String.valueOf(s);
                input=String.valueOf(s);
                if(s.length()==0){
                    resultplace.setVisibility(View.INVISIBLE);
                    return;
                }
                try {
                    double all=0;
                    paylist.clear();
                    URL url = new URL("http://47.94.221.238/data/selectpayByname.php?Name="+searchname);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setConnectTimeout(3*1000);
                    InputStream in = conn.getInputStream();
                    JSONArray ja=StreamToJson(in);
                    if(ja.length()==0){
                        resultplace.setVisibility(View.INVISIBLE);
                    }
                    else{
                        resultplace.setVisibility(View.VISIBLE);
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
                            if(category==0){
                                paytype.setText("收入:");
                            }
                            else{
                                paytype.setText("支出:");
                            }
                            all+=cost;
                            paylist.add(tempitem);
                        }
                        sum.setText(String.valueOf(all));
                        searchAdapter adapter=new searchAdapter(search.this,R.layout.search_item,paylist);
                        searchList.setAdapter(adapter);
                        searchList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                singlepayitem tempsinglepayitem=paylist.get(position);
                                Intent intent= new Intent(search.this,detail.class);
                                intent.putExtra("item", tempsinglepayitem);
                                startActivityForResult(intent,1);
                            }
                        });
                    }

                    conn.disconnect();

                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                } finally {
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search.this.finish();
            }
        });
    }

    private void initView() {
        sv_view = findViewById(R.id.search_sv_view);
        ll_search = findViewById(R.id.search_ll_search);
        tv_title = findViewById(R.id.search_tv_title);
        searchLayoutParams = (ViewGroup.MarginLayoutParams) ll_search.getLayoutParams();
        titleLayoutParams = (ViewGroup.MarginLayoutParams) tv_title.getLayoutParams();

        LL_SEARCH_MIN_TOP_MARGIN = CommonUtil.dp2px(this, 4.5f);//布局关闭时顶部距离
        LL_SEARCH_MAX_TOP_MARGIN = CommonUtil.dp2px(this, 49f);//布局默认展开时顶部距离
        LL_SEARCH_MAX_WIDTH = CommonUtil.getScreenWidth(this) - CommonUtil.dp2px(this, 30f);//布局默认展开时的宽度
        LL_SEARCH_MIN_WIDTH = CommonUtil.getScreenWidth(this) - CommonUtil.dp2px(this, 122f);//布局关闭时的宽度
        TV_TITLE_MAX_TOP_MARGIN = CommonUtil.dp2px(this, 11.5f);

        sv_view.setOnAnimationScrollListener(new AnimationNestedScrollView.OnAnimationScrollChangeListener() {
            @Override
            public void onScrollChanged(float dy) {
                float searchLayoutNewTopMargin = LL_SEARCH_MAX_TOP_MARGIN - dy;
                float searchLayoutNewWidth = LL_SEARCH_MAX_WIDTH - dy * 3.0f;//此处 * 1.3f 可以设置搜索框宽度缩放的速率

                float titleNewTopMargin = (float) (TV_TITLE_MAX_TOP_MARGIN - dy * 0.5);

                //处理布局的边界问题
                searchLayoutNewWidth = searchLayoutNewWidth < LL_SEARCH_MIN_WIDTH ? LL_SEARCH_MIN_WIDTH : searchLayoutNewWidth;

                if (searchLayoutNewTopMargin < LL_SEARCH_MIN_TOP_MARGIN) {
                    searchLayoutNewTopMargin = LL_SEARCH_MIN_TOP_MARGIN;
                }

                if (searchLayoutNewWidth < LL_SEARCH_MIN_WIDTH) {
                    searchLayoutNewWidth = LL_SEARCH_MIN_WIDTH;
                }

                float titleAlpha = 255 * titleNewTopMargin / TV_TITLE_MAX_TOP_MARGIN;
                if (titleAlpha < 0) {
                    titleAlpha = 0;
                }

                //设置相关控件的LayoutParams  此处使用的是MarginLayoutParams，便于设置params的topMargin属性
                tv_title.setTextColor(tv_title.getTextColors().withAlpha((int) titleAlpha));
                titleLayoutParams.topMargin = (int) titleNewTopMargin;
                tv_title.setLayoutParams(titleLayoutParams);

                searchLayoutParams.topMargin = (int) searchLayoutNewTopMargin;
                searchLayoutParams.width = (int) searchLayoutNewWidth;
                ll_search.setLayoutParams(searchLayoutParams);
            }
        });
    }
}