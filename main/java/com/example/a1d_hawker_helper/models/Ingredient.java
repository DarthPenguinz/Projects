package com.example.a1d_hawker_helper.models;

import java.io.Serializable;

public class Ingredient implements Serializable {
    // Ingredient Attributes: name, qty, unit
    private String name;
    private float qty;
    private String unit;

    // Constructor Methods
    public Ingredient(){}

    public Ingredient(String name, float qty, String unit){
        this.name = name;
        this.qty = qty;
        this.unit = unit;

    }

    // Getter and Setter Methods
    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setQty(float qty) {
        this.qty = qty;
    }

    public float getQty() {
        return qty;
    }

    public void setUnit(String units) {
        this.unit = units;
    }

    public String getUnit() {
        return unit;
    }
}
