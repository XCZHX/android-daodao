package com.example.chat;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

public class ChatAdapter extends BaseAdapter {
    private Context context;
    private List<PersonChat> lists;

    public ChatAdapter(Context context, List<PersonChat> lists) {
        super();
        this.context = context;
        this.lists = lists;
    }

    /**
     * 是否是自己发送的消息
     *
     * @author cyf
     *
     */
    public static interface IMsgViewType {
        int IMVT_COM_MSG = 0;// 收到对方的消息
        int IMVT_TO_MSG = 1;// 自己发送出去的消息
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return lists.size();
    }

    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return lists.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return arg0;
    }

    /**
     * 得到Item的类型，是对方发过来的消息，还是自己发送出去的
     */
    public int getItemViewType(int position) {
        PersonChat entity = lists.get(position);

        if (entity.isMeSend()) {// 收到的消息
            return IMsgViewType.IMVT_COM_MSG;
        } else {// 自己发送的消息
            return IMsgViewType.IMVT_TO_MSG;
        }
    }

    @Override
    public View getView(int arg0, View arg1, ViewGroup arg2) {
        // TODO Auto-generated method stub
        HolderView holderView = null;
        PersonChat entity = lists.get(arg0);
        boolean isMeSend = entity.isMeSend();
        boolean addtime = entity.addTime();
        if (holderView == null) {
            holderView = new HolderView();
            if(addtime){
                arg1 = View.inflate(context, R.layout.time_item,
                        null);
                holderView.time_message = (TextView) arg1
                        .findViewById(R.id.time_text);
                holderView.time_message.setText(entity.getChatMessage());
            }else{
                if (isMeSend) {
                    arg1 = View.inflate(context, R.layout.message_item_right,
                            null);
                    holderView.tv_chat_me_message = (TextView) arg1
                            .findViewById(R.id.right_text);
                    holderView.tv_chat_me_message.setText(entity.getChatMessage());
                } else {
                    arg1 = View.inflate(context, R.layout.message_item_left,
                            null);
                    holderView.tv_chat_you_message = (TextView) arg1
                            .findViewById(R.id.left_text);
                    holderView.tv_chat_you_message.setText(entity.getChatMessage());
                }
            }

            arg1.setTag(holderView);
        } else {
            holderView = (HolderView) arg1.getTag();
        }

        return arg1;
    }

    class HolderView {
        TextView tv_chat_me_message;
        TextView tv_chat_you_message;
        TextView time_message;
        RelativeLayout lay;
    }

    @Override
    public boolean isEnabled(int position) {
        return false;
    }
}
