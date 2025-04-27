package com.example.chat;

import java.util.ArrayList;
import java.util.List;

public class myBill {
    private String time;
    private List<singlepayitem> billlist;
    private double costsum;
    private double incomesum;
    public myBill(int year,int month,int day,List<singlepayitem> billlist){
        String textmonth=String.valueOf(month);
        String textday=String.valueOf(day);
        while(textmonth.length()<2){
            textmonth="0"+textmonth;
        }
        while(textday.length()<2){
            textday="0"+textday;
        }
        this.time=String.valueOf(year)+textmonth+textday;
        this.billlist=billlist;
        double sum1=0;
        double sum2=0;
        for(int i=0;i<billlist.size();i++){
            if(billlist.get(i).getCategory()==0){
                sum1+=billlist.get(i).getCost();
            }
            else {
                sum2+=billlist.get(i).getCost();
            }
        }
        this.incomesum=sum1;
        this.costsum=sum2;
    }

    public String getTime(){
        return this.time;
    }

    public double getCostsum() {
        return this.costsum;
    }

    public double getIncomesum(){
        return this.incomesum;
    }

    public List<singlepayitem> getBilllist(){
        return this.billlist;
    }
}
