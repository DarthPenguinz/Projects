package com.example.a1d_hawker_helper.models;

import java.io.Serializable;
import java.util.HashMap;

public class Order implements Serializable {
    //Order Attributes: Number, Hashmap of orders(Name,Qty)
    private int orderNumber;
    private HashMap<String,Integer> orders;

    // Constructor Methods
    public Order(){}

    public Order(int orderNumber,HashMap<String,Integer> orders){
        setOrderNumber(orderNumber);
        setOrders(orders);
    }

    //Getter and Setter Methods
    public int getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(int orderNumber) {
        this.orderNumber = orderNumber;
    }

    public HashMap<String, Integer> getOrders() {
        return orders;
    }

    public void setOrders(HashMap<String, Integer> orders) {
        this.orders = orders;
    }

}

