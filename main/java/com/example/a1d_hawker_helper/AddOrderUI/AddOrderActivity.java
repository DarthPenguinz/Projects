package com.example.a1d_hawker_helper.AddOrderUI;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a1d_hawker_helper.R;
import com.example.a1d_hawker_helper.models.Database;
import com.example.a1d_hawker_helper.models.Recipe;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;

public class AddOrderActivity extends AppCompatActivity {
    private RecyclerView recipeListForOrder;
    private RecipeListForOrderAdapter adapter;
    private Button sendOrderButton;
    private TextView orderNumberText;
    private ArrayList<Recipe> recipes;
    static HashMap<String,Integer> orderMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_order);
        Database db = new Database();
        orderMap = new HashMap<>();

        sendOrderButton = findViewById(R.id.sendOrderButton);
        recipeListForOrder = findViewById(R.id.recipeListForOrder);
        orderNumberText = findViewById(R.id.orderNumberText);

        // Init Layout Manager and Add Lines between List Entries
        recipeListForOrder.setLayoutManager(new LinearLayoutManager(this));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recipeListForOrder.getContext(), new LinearLayoutManager(this).getOrientation());
        recipeListForOrder.addItemDecoration(dividerItemDecoration);

        // Retrieving recipes from Database
        recipes = db.getRecipeList();
        adapter = new RecipeListForOrderAdapter(this ,recipes);
        recipeListForOrder.setAdapter(adapter);
        db.SetRecipeEventListener(adapter);

        // Set Listener for dailyCounter in DB
        db.getOrderCountReference().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                orderNumberText.setText("Order Number: " + String.valueOf(snapshot.getValue()));
                Log.d("AddOrderUI","Order Number New: "+ String.valueOf(snapshot.getValue()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });

        // sendOrder Button
        sendOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (orderMap.isEmpty()){
                    Toast.makeText(getApplicationContext(),"Order cannot be empty",Toast.LENGTH_SHORT).show();
                    return;
                }
                db.addOrder(orderMap);
                finish();

                // Reset values in order to 0
                startActivity(getIntent());
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                LocalDate currDate = LocalDate.now();
                String date = currDate.format(DateTimeFormatter.ofPattern("ddMMyyyy"));
                db.setRecentDate(Integer.parseInt(date));
                Log.d("AddOrderUI","Order Sent"+String.valueOf(orderMap));
            }
        });
    }
}