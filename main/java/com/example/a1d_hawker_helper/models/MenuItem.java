package com.example.a1d_hawker_helper.models;

import android.util.Log;

import androidx.annotation.Nullable;

public class MenuItem {

    //MenuItem Attributes: Name, Qty, Price
    private String MenuItemName;
    private Integer MenuItemQty;
    private Float MenuItemPrice;


    // Constructor Method
    public MenuItem(String MenuItemName, Integer MenuItemQty, Float MenuItemPrice) {
        this.MenuItemName = MenuItemName;
        this.MenuItemQty = MenuItemQty;
        this.MenuItemPrice = MenuItemPrice;
    }

    // Getter and Setter Methods
    public String getMenuItemName() {
        return MenuItemName;
    }

    public void setMenuItemName(String menuItemName) {
        MenuItemName = menuItemName;
    }

    public Integer getMenuItemQty() {
        return MenuItemQty;
    }

    public void setMenuItemQty(Integer menuItemQty) {
        MenuItemQty = menuItemQty;
    }

    public Float getMenuItemPrice() {
        return MenuItemPrice;
    }

    public void setMenuItemPrice(Float menuItemPrice) {
        MenuItemPrice = menuItemPrice;
    }

    // Comparing MenuItem Objects
    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj.getClass() != this.getClass()) {
            return false;
        }
        final MenuItem other = (MenuItem) obj;
        Log.d("menutqyITEM",this.MenuItemName);
        Log.d("menutqyITEM",other.MenuItemName);
        Log.d("menutqyITEM", String.valueOf(this.MenuItemName == other.MenuItemName));
        return this.MenuItemName.equals(other.MenuItemName);
    }
}
