package com.example.chat.ui.main;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.example.chat.R;

public class PageViewModel extends ViewModel {
    private String[] str_1={"1","工资","早餐","购物","交通","教育","娱乐"};
    private String[] str_2={"2","收入","午餐","生活用品","加油","学习","电影"};
    private String[] str_3={"3","投资收入","晚餐","服饰","停车费","书籍","游戏"};
    private String[] str_4={"4","生活费","零食","护肤彩妆","修车养车","学费","KTV"};
    private String[] str_5={"5","红包","蔬果","饰品","公交地铁","辅导班","旅游"};

    private Integer[] pic_1={1,R.drawable.salary,R.drawable.egg,R.drawable.shopping,R.drawable.traffic,R.drawable.education,R.drawable.entertainment};
    private Integer[] pic_2={2,R.drawable.comein,R.drawable.lunch,R.drawable.living,R.drawable.oil,R.drawable.study,R.drawable.movie};
    private Integer[] pic_3={3,R.drawable.investment,R.drawable.dinner,R.drawable.cloth,R.drawable.parking,R.drawable.book,R.drawable.game};
    private Integer[] pic_4={4,R.drawable.living_exp,R.drawable.sugar,R.drawable.skin_care,R.drawable.repair_car,R.drawable.tuition,R.drawable.ktv};
    private Integer[] pic_5={5,R.drawable.hongbao,R.drawable.vegetable,R.drawable.decoration,R.drawable.bus,R.drawable.tutorial,R.drawable.vacation};
    private MutableLiveData<Integer> mIndex = new MutableLiveData<>();
    private LiveData<String> mText_1 = Transformations.map(mIndex, new Function<Integer, String>() {
        @Override
        public String apply(Integer input) {
            return str_1[input];
        }
    });
    private LiveData<String> mText_2 = Transformations.map(mIndex, new Function<Integer, String>() {
        @Override
        public String apply(Integer input) {
            return str_2[input];
        }
    });
    private LiveData<String> mText_3 = Transformations.map(mIndex, new Function<Integer, String>() {
        @Override
        public String apply(Integer input) {
            return str_3[input];
        }
    });
    private LiveData<String> mText_4 = Transformations.map(mIndex, new Function<Integer, String>() {
        @Override
        public String apply(Integer input) {
            return str_4[input];
        }
    });
    private LiveData<String> mText_5 = Transformations.map(mIndex, new Function<Integer, String>() {
        @Override
        public String apply(Integer input) {
            return str_5[input];
        }
    });

    private LiveData<Integer> pInt_1 = Transformations.map(mIndex, new Function<Integer, Integer>() {
        @Override
        public Integer apply(Integer input) {
            return pic_1[input];
        }
    });
    private LiveData<Integer> pInt_2 = Transformations.map(mIndex, new Function<Integer, Integer>() {
        @Override
        public Integer apply(Integer input) {
            return pic_2[input];
        }
    });
    private LiveData<Integer> pInt_3 = Transformations.map(mIndex, new Function<Integer, Integer>() {
        @Override
        public Integer apply(Integer input) {
            return pic_3[input];
        }
    });
    private LiveData<Integer> pInt_4 = Transformations.map(mIndex, new Function<Integer, Integer>() {
        @Override
        public Integer apply(Integer input) {
            return pic_4[input];
        }
    });
    private LiveData<Integer> pInt_5 = Transformations.map(mIndex, new Function<Integer, Integer>() {
        @Override
        public Integer apply(Integer input) {
            return pic_5[input];
        }
    });

    public void setIndex(int index) {
        mIndex.setValue(index);
    }

    public LiveData<String> getText_1() {
        return mText_1;
    }
    public LiveData<String> getText_2() {
        return mText_2;
    }
    public LiveData<String> getText_3() {
        return mText_3;
    }
    public LiveData<String> getText_4() {
        return mText_4;
    }
    public LiveData<String> getText_5() {
        return mText_5;
    }
    public LiveData<Integer> getPic_1(){
        return pInt_1;
    }
    public LiveData<Integer> getPic_2(){
        return pInt_2;
    }
    public LiveData<Integer> getPic_3(){
        return pInt_3;
    }
    public LiveData<Integer> getPic_4(){
        return pInt_4;
    }
    public LiveData<Integer> getPic_5(){
        return pInt_5;
    }
}
