package com.example.chat;

import java.io.Serializable;

public class singlepayitem implements Serializable {
    private double cost;
    private String name;
    private int year;
    private  int month;
    private  int day;
    private int paymethod;
    private int category;
    private String  time;
    private String remark;
    public singlepayitem(double cost,String name,int year,int month,int day,int paymethod,int category,String time,String remark){
        this.cost=cost;
        this.name=name;
        this.year=year;
        this.month=month;
        this.day=day;
        this.paymethod=paymethod;
        this.category=category;
        this.time=time;
        this.remark=remark;
    }
    public double getCost(){
        return  this.cost;
    }
    public String getName(){
        return this.name;
    }

    public int getYear(){
        return this.year;
    }

    public int getMonth(){
        return this.month;
    }

    public int getDay(){
        return this.day;
    }

    public int getCategory(){
        return this.category;
    }

    public int getPaymethod(){
        return this.paymethod;
    }

    public String  getTime(){ return this.time;}

    public  String getRemark(){
        return this.remark;
    }
}
