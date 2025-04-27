package com.example.chat;

import java.util.Date;

public class DetailedItem {
    private String name;
    private double cost;
    private double percent;

    public DetailedItem(String name,double cost,double percent){
        this.name=name;
        this.cost=cost;
        this.percent=percent;
    }

    public String getName(){
        return name;
    }

    public double getCost(){
        return cost;
    }

    public double getPercent(){
        return percent;
    }
}
