package com.example.a1d_hawker_helper.RecipeUI.AddRecipe;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a1d_hawker_helper.R;
import com.example.a1d_hawker_helper.RecipeUI.RecipeIngredientsAdapter;
import com.example.a1d_hawker_helper.models.Ingredient;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashMap;

public class AddRecipeIngredientActivity extends AppCompatActivity {
    private Button buttonBack, buttonFront;
    private RecyclerView currentIngredientList;
    private RecipeIngredientsAdapter adapter;
    private HashMap ingredients;
    private FloatingActionButton addNewIngredientButton;
    private String name, description;
    private ArrayList<String> steps;
    private Double price;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_edit_ingredient);

        currentIngredientList = findViewById(R.id.currentIngredientList);
        buttonBack = findViewById(R.id.buttonBack);
        buttonFront = findViewById(R.id.buttonFront);
        addNewIngredientButton = findViewById(R.id.addRecipeIngredientButton);
        getSupportActionBar().setTitle("Ingredients List");

        // Receiving intent to check current progress in adding recipe
        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        description = intent.getStringExtra("description");
        steps = intent.getStringArrayListExtra("steps");
        price = intent.getDoubleExtra("price", -1);
        ingredients = (HashMap<String, Ingredient>) intent.getSerializableExtra("ingredients");

        if (ingredients == null){
            // If null, no saved progress, so init new HashMap
            ingredients = new HashMap<String, Ingredient>();
        }

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        currentIngredientList.setLayoutManager(linearLayoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(currentIngredientList.getContext(), linearLayoutManager.getOrientation());
        currentIngredientList.addItemDecoration(dividerItemDecoration);

        ArrayList<String> ingredientList = new ArrayList<>(ingredients.keySet());
        adapter = new RecipeIngredientsAdapter(this,ingredientList,ingredients);
        currentIngredientList.setAdapter(adapter);

        addNewIngredientButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddRecipeIngredientActivity.this, AddRecipeAddIngredientActivity.class);
                intent.putExtra("name", name);
                intent.putExtra("description", description);
                intent.putExtra("ingredients", ingredients);
                intent.putExtra("steps", steps);
                intent.putExtra("price", price);
                startActivity(intent);
            }
        });


        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Pass on intent to save progress
                Intent intent1 = new Intent(AddRecipeIngredientActivity.this, AddRecipeNameActivity.class);
                intent1.putExtra("name", name);
                intent1.putExtra("description", description);
                intent1.putExtra("ingredients", ingredients);
                intent1.putExtra("steps", steps);
                intent1.putExtra("price", price);
                intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent1);
                finish();
            }
        });

        buttonFront.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ingredients.keySet().size() == 0){
                    Toast.makeText(AddRecipeIngredientActivity.this, "You need to add at least 1 ingredient!", Toast.LENGTH_SHORT).show();
                }
                else{
                    Intent intent2 = new Intent(AddRecipeIngredientActivity.this, AddRecipeStepsActivity.class);
                    intent2.putExtra("name", name);
                    intent2.putExtra("description", description);
                    intent2.putExtra("ingredients", ingredients);
                    intent2.putExtra("steps", steps);
                    intent2.putExtra("price", price);
                    startActivity(intent2);
                }

            }
        });

        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                Intent intent1 = new Intent(AddRecipeIngredientActivity.this, AddRecipeNameActivity.class);
                intent1.putExtra("name", name);
                intent1.putExtra("description", description);
                intent1.putExtra("ingredients", ingredients);
                intent1.putExtra("steps", steps);
                intent1.putExtra("price", price);
                intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent1);
                finish();
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);

    }
}