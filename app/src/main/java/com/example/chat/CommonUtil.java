package com.example.chat;

import android.content.Context;

public class CommonUtil {
    public static int dp2px(Context context, float dpValue) {
        if (null == context) {
            return 0;
        }
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

//...

    public static int getScreenWidth(Context context) {
        if (null == context) {
            return 0;
        }
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    public static String paymethodTranslate(int id) {
        switch (id){
            case 0:
                return "现金";

            case 1:
                return "储蓄卡";

            case 2:
                return "支付宝";

            case 3:
                return "微信支付";
        }
        return "";
    }
}
