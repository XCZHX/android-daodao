package com.example.chat;

import android.graphics.Color;

import java.util.HashMap;
import java.util.Map;
import java.util.*;

public class NameMap {
    private Map icon_map;

    private String[][] str={{"1","工资","早餐","购物","交通","教育","娱乐"},{"2","收入","午餐","生活用品","加油","学习","电影"},
            {"3","投资收入","晚餐","服饰","停车费","书籍","游戏"},{"4","生活费","零食","护肤彩妆","修车养车","学费","KTV"},
            {"5","红包","蔬果","饰品","公交地铁","辅导班","旅游"}};

    private Integer[][] pic={{1,R.drawable.salary,R.drawable.egg,R.drawable.shopping,R.drawable.traffic,R.drawable.education,R.drawable.entertainment},
            {2,R.drawable.comein,R.drawable.lunch,R.drawable.living,R.drawable.oil,R.drawable.study,R.drawable.movie},
            {3,R.drawable.investment,R.drawable.dinner,R.drawable.cloth,R.drawable.parking,R.drawable.book,R.drawable.game},
            {4,R.drawable.living_exp,R.drawable.sugar,R.drawable.skin_care,R.drawable.repair_car,R.drawable.tuition,R.drawable.ktv},
            {5,R.drawable.hongbao,R.drawable.vegetable,R.drawable.decoration,R.drawable.bus,R.drawable.tutorial,R.drawable.vacation}};

    private int[] colorString={R.color.c1,R.color.c2,R.color.c3,R.color.c4,R.color.c5};

    public NameMap() {
    }

    public int getIconInteger(String ss){
        for(int i=0;i<=4;i++){
            for(int j=1;j<=6;j++){
                if(ss.equals(str[i][j])){
                    return pic[i][j].intValue();
                }
            }
        }
        return 0;
    }

    public int getColorInteger(String ss){
        for(int i=0;i<=4;i++){
            for(int j=1;j<=6;j++){
                if(ss.equals(str[i][j])){
                    return colorString[i];
                }
            }
        }
        return 0;
    }
}
