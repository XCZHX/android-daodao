package com.example.chat;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class RemarkDialog extends Dialog {
    private EditText remark_text;
    private Button remark_sure;
    private TextView remark;
    /**
     * 显示的消息
     */
    private TextView messageTv ;
    private Context context;

    /**
     * 都是内容数据
     */
    private String text="";

    /**
     * 自定义Dialog监听器
     */
    public interface PriorityListener {
        /**
         * 回调函数，用于在Dialog的监听事件触发后刷新Activity的UI显示
         */
        void setActivityText(String string);
    }
    private PriorityListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflate = LayoutInflater.from(context);
        View view = inflate.inflate(R.layout.remark_dialog_layout, null);
        setContentView(view);

        //初始化界面控件
        initView();
        remark_text.setText(text);
        remark=(TextView) view.findViewById(R.id.remark);

        remark_sure.setOnClickListener(new AdapterView.OnClickListener() {
            @Override
            public void onClick(View v) {
                text=remark_text.getText().toString();
                listener.setActivityText(text);
                dismiss();
            }
        });
    }

    public RemarkDialog(Context context, String last_text, PriorityListener listener) {
        super(context, R.style.RemarkDialog);//加载dialog的样式
        this.text=last_text;
        this.context=context;
        this.listener = listener;
    }

    @Override
    public void show() {
        super.show();
    }

    /**
     * 初始化界面控件
     */
    private void initView() {
        remark_text=(EditText)findViewById(R.id.remark_text);
        remark_sure=(Button)findViewById(R.id.remark_sure);
    }

    public String getRemarkText() {
        return text;
    }

}
