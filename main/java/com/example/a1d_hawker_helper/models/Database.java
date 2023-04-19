package com.example.a1d_hawker_helper.models;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Database {
    // Database references:
    // final modifiers as the references do not change
    // main -> Full database
    // ingredients -> ingredients
    // recipes -> Recipes
    // currentOrders -> existing orders
    // history -> Full history
    // orderCountReference -> orderCounter
    private final DatabaseReference main = FirebaseDatabase.getInstance("https://infosys1d-75070-default-rtdb.asia-southeast1.firebasedatabase.app").getReference();
    private final DatabaseReference ingredients = main.child("ingredients");
    private final DatabaseReference recipes = main.child("recipes");
    private final DatabaseReference currentOrders = main.child("currentorders");
    private final DatabaseReference dailyCounterOrder = main.child("dailyordercounter");
    private final DatabaseReference history = main.child("history");
    private final DatabaseReference orderCountReference = main.child("dailyordercounter");

    // Array Lists for recipe, ingredient and order objects to be used in RecyclerViews and adapters
    // Lists cannot be static as multiple adapters are set to same reference.
    private ArrayList<Recipe> recipeList = new ArrayList<>();
    private ArrayList<Ingredient> ingredientList = new ArrayList<>();
    private ArrayList<Order> orderList = new ArrayList<>();

    // orderCounter =  orderCounter of the day, recentDate = date of last order placed
    private int orderCounter;
    private int recentDate;


    // Constructor Method: check if daily counter should be reset, set orderCounter as value saved in DB
    public Database(){
        Log.d("Database","initialised");
        resetDaily();
        getOrderCount();
    }

    // Retrieve daily order count from DB.
    public int getOrderCount(){
        Log.d("Database","getordercounter");
        dailyCounterOrder.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                orderCounter = Integer.parseInt(String.valueOf(task.getResult().getValue()));
            }
        });
        return orderCounter;
    }

    // check date and reset counter and orders if necessary
    private void resetDaily() {
        Log.d("Database","ComparingDates");
        //get current date
        LocalDate currDate = LocalDate.now();
        String date = currDate.format(DateTimeFormatter.ofPattern("ddMMyyyy"));
        main.child("recentdate").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                recentDate = Integer.valueOf(String.valueOf(snapshot.getValue()));

                if (Integer.valueOf(date) > recentDate) {
                    getDailyCounterOrder().setValue(0);
                    getOrderCount();

                    //Auto Delete previous day orders - Assume were not completed
                    currentOrders.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot order: snapshot.getChildren()){
                                Order orderobject = order.getValue(Order.class);
                                deleteOrder(String.valueOf(orderobject.getOrderNumber()));
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {}
                    });

                    Log.d("Database","Daily Reset Done");

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    // Set Event Listener for Recipe
    public void SetRecipeEventListener(RecyclerView.Adapter adapter){
        Log.d("Database","RecipeListener - Initialised");
        recipes.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Log.d("Database","RecipeListener - Added");
                // Update list and adapter when data changed
                recipeList.add(snapshot.getValue(Recipe.class));
                adapter.notifyItemInserted(recipeList.size() - 1);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                Log.d("Database","RecipeListener - Removed");
                Recipe deleted = snapshot.getValue(Recipe.class);

                for (int i = 0; i < recipeList.size(); i++) {
                    if (recipeList.get(i).getName().equals(deleted.getName())) {
                        recipeList.remove(i);
                        adapter.notifyItemRemoved(i);
                        adapter.notifyItemRangeChanged(i, adapter.getItemCount());
                        break;
                    }
                }
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    //Set Event Listener for Ingredient
    public void SetIngredientEventListener(RecyclerView.Adapter adapter){
        Log.d("Database","IngredientListener - Initialised");
        ingredients.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Log.d("Database","IngredientListener - Added");
                ingredientList.add(snapshot.getValue(Ingredient.class));
                Log.d("IngList", String.valueOf(ingredientList));
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                Log.d("Database","IngredientListener - Removed");
                Ingredient deleted = snapshot.getValue(Ingredient.class);

                for (int i = 0; i < ingredientList.size(); i++) {
                    if (ingredientList.get(i).getName().equals(deleted.getName())) {
                        ingredientList.remove(i);
                        adapter.notifyItemRemoved(i);
                        adapter.notifyItemRangeChanged(i, adapter.getItemCount());
                        break;
                    }
                }
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    //Set Event Listener for Order
    public void SetOrderEventListener(RecyclerView.Adapter adapter){
        Log.d("Database","OrderListener - Initialised");
        currentOrders.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Log.d("Database","OrderListener - Added");
                orderList.add(snapshot.getValue(Order.class));
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                Log.d("Database","OrderListener - Removed");
                Order deleted = snapshot.getValue(Order.class);

                for (int i = 0; i < orderList.size(); i++) {
                    if (orderList.get(i).getOrderNumber() == (deleted.getOrderNumber())) {
                        orderList.remove(i);
                        adapter.notifyItemRemoved(i);
                        adapter.notifyItemRangeChanged(i, adapter.getItemCount());
                        break;
                    }
                }
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    // Getter Methods
    public ArrayList<Recipe> getRecipeList() {
        return recipeList;
    }

    public ArrayList<Ingredient> getIngredientList() {
        return ingredientList;
    }

    public ArrayList<Order> getOrderList() {
        return orderList;
    }

    public DatabaseReference getCurrentOrders() {
        return currentOrders;
    }

    public DatabaseReference getDailyCounterOrder() {
        return dailyCounterOrder;
    }

    public DatabaseReference getHistory() {
        return history;
    }

    public DatabaseReference getIngredients() {
        return ingredients;
    }

    public DatabaseReference getRecipes() {
        return recipes;
    }

    public DatabaseReference getMain() {
        return main;
    }

    public DatabaseReference getOrderCountReference() {
        return orderCountReference;
    }

    // Setter method for recentdate
    public void setRecentDate(int recentDate) {
        Log.d("Database","recentdate set");
        main.child("recentdate").setValue(recentDate);
    }

    // Add Recipe Object into DB
    public void addRecipe(String name, ArrayList<Ingredient> ingredientList, double price, ArrayList<String> steps){
        DatabaseReference newRecipeRef = recipes.child(name);
        Recipe newRecipe = new Recipe(name,ingredientList,price,steps);
        newRecipeRef.setValue(newRecipe);
        Log.d("Database","Recipe Added: "+ name);
    }

    // Delete Recipe Object from DB
    public void deleteRecipe(String name){
        recipes.child(name).removeValue();
        Log.d("Database","Recipe Deleted: " + name);

    }

    // Add Ingredient Object into DB
    public void addIngredient(String name, float qty, String unit){
        DatabaseReference newIngredientRef = ingredients.child(name);
        Ingredient newIngredient = new Ingredient(name,qty,unit);
        newIngredientRef.setValue(newIngredient);
        Log.d("Database","Ingredient Added: "+name);
    }

    // Delete Ingredient Object from DB
    public void deleteIngredient(String name){
        ingredients.child(name).removeValue();
        Log.d("Database","Ingredient Deleted: "+name);

    }

    // Add Order Object into DB
    public void addOrder(HashMap<String,Integer> orderList){
        int count = getOrderCount();
        DatabaseReference newOrderRef = currentOrders.child(String.valueOf(count));
        Order newOrder = new Order(count,orderList);
        newOrderRef.setValue(newOrder);
        dailyCounterOrder.setValue(getOrderCount()+1);
        Log.d("Database","Order Added: "+ String.valueOf(count));


    }

    // Delete Order Object from DB
    public void deleteOrder(String orderNum){
        currentOrders.child(orderNum).removeValue();
        Log.d("Database","Order Deleted: "+ orderNum);
    }

    // Complete Order, Shift to History
    public void completeOrder(Order order) {
        LocalDate currDate = LocalDate.now();
        String currYear = String.valueOf(currDate.getYear());
        String currMonth = String.valueOf(currDate.getMonth().getValue());
        String date = currDate.format(DateTimeFormatter.ofPattern("ddMMyyyy"));
        HashMap<String,Integer> orderItems = order.getOrders();

        //For each order - Add to History (Add to qty if already existing), Add price point (At time of completion)
        for (Map.Entry<String, Integer> set: orderItems.entrySet()) {
            Log.d("Database","Order Completed: "+ set.getKey());
            history.child(currYear)
                    .child(currMonth)
                    .child(date)
                    .child(set.getKey())
                    .child("qty")
                    .setValue(ServerValue.increment(set.getValue()));
            recipes.child(set.getKey()).child("price").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    history.child(currYear)
                            .child(currMonth)
                            .child(date)
                            .child(set.getKey())
                            .child("price")
                            .setValue(Float.valueOf(String.valueOf(task.getResult().getValue())));
                }
            });

            // Update ingredients
            recipes.child(set.getKey()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    Recipe thisRecipe = task.getResult().getValue(Recipe.class);
                    for (Ingredient i:thisRecipe.getIngredientsList()) {
                        ingredients.child(i.getName())
                                .child("qty")
                                    .setValue(ServerValue
                                            .increment(-1 * set.getValue() * i.getQty()));
                        Log.d("Database","Ingredients deducted: "+ i.getName());
                    }
                }
            });
        }

    }





}



