package com.example.a1d_hawker_helper.models;


import java.io.Serializable;
import java.util.ArrayList;

public class Recipe implements Serializable {
    //Recipe Attributes: Name, Ingredients list, price, steps
    private String name;
    private ArrayList<Ingredient> ingredientsList;
    private double price;
    private ArrayList<String> steps;


    // Constructor Methods
    public Recipe(){}

    public Recipe(String name, ArrayList<Ingredient> ingredientsList,double price,ArrayList<String> steps){
        setName(name);
        setIngredientsList(ingredientsList);
        setPrice(price);
        setSteps(steps);
    }


    // Getter and Setter Methods
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Ingredient> getIngredientsList() {
        return ingredientsList;
    }

    public void setIngredientsList(ArrayList<Ingredient> ingredientsList) {
        this.ingredientsList = ingredientsList;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public ArrayList<String> getSteps() {
        return steps;
    }

    public void setSteps(ArrayList<String> steps) {
        this.steps = steps;
    }


}

