package com.example.a1d_hawker_helper.RecipeUI;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a1d_hawker_helper.R;
import com.example.a1d_hawker_helper.models.Database;
import com.example.a1d_hawker_helper.models.Ingredient;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class RecipeSelectIngredientActivity extends AppCompatActivity {
    private HashMap<String, Ingredient> checked_items;
    private Button back_Button;
    private RecyclerView AddIngredientList;
    private RecipeSelectIngredientAdapter adapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        String name = intent.getStringExtra("recipe");
        DatabaseReference ingredientReference = new Database().getIngredients();
        setContentView(R.layout.activity_recipe_add_ingredient);

        back_Button = findViewById(R.id.back_Button);
        AddIngredientList = findViewById(R.id.AddIngredientList);
        ArrayList<String> ingredientList = new ArrayList<>();

        if (intent.getSerializableExtra("ingredients") != null){
            checked_items = (HashMap<String,Ingredient>) intent.getSerializableExtra("ingredients");
        }
        else{
            checked_items = new HashMap<String,Ingredient>();
        }

        // Init Layout Manager and Add Lines between List Entries
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        AddIngredientList.setLayoutManager(linearLayoutManager);
        back_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("name", intent.getStringExtra("name"));
                intent.putExtra("ingredients", checked_items);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        
        adapter = new RecipeSelectIngredientAdapter(ingredientList, new ArrayList<String>(checked_items.keySet()));
        AddIngredientList.setAdapter(adapter);
        adapter.setCallback(new RecipeSelectIngredientAdapter.Callback() {
            @Override
            public void onCheckedChanged(String item, boolean isChecked) {
                if (isChecked){
                    ingredientReference.orderByKey().equalTo(item).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            HashMap temp = (HashMap) snapshot.child(item).getValue();
                            Ingredient temp1 = new Ingredient();
                            temp1.setName(temp.get("name").toString());
                            temp1.setQty(0);
                            temp1.setUnit(temp.get("unit").toString());
                            checked_items.put(item, temp1);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {}
                    });
                }
                else{
                    checked_items.remove(item);
                }
            }
        });

        ingredientReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Ingredient temp = snapshot.getValue(Ingredient.class);
                ingredientList.add(temp.getName());
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                Ingredient deleted = snapshot.getValue(Ingredient.class);
                ingredientList.remove(deleted.getName());
                adapter.notifyDataSetChanged();
            }
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });

        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                Intent intent = new Intent();
                intent.putExtra("name", intent.getStringExtra("name"));
                intent.putExtra("ingredients", checked_items);
                setResult(RESULT_OK, intent);
                finish();
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);
    }
}
