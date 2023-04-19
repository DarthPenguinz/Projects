package com.example.a1d_hawker_helper.RecipeUI.AddRecipe;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.a1d_hawker_helper.R;
import com.example.a1d_hawker_helper.RecipeUI.RecipeListActivity;
import com.example.a1d_hawker_helper.models.Ingredient;

import java.util.ArrayList;
import java.util.HashMap;

public class AddRecipeNameActivity extends AppCompatActivity {

    private Button button_front, button_back;

    private EditText name, price;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipe_name);

        button_front = findViewById(R.id.button_front);
        button_back = findViewById(R.id.button_back);
        name = findViewById(R.id.name);
        price = findViewById(R.id.price);
        getSupportActionBar().setTitle("Name and Price");

        // Passing intents so if the user needs to change something he previously set, we save his current progress
        Intent last_intent = getIntent();
        ArrayList<String> steps = last_intent.getStringArrayListExtra("steps");
        HashMap<String, Ingredient> ingredients = (HashMap<String, Ingredient>) last_intent.getSerializableExtra("ingredients");
        if (last_intent.getStringExtra("name") != null){
            name.setText(last_intent.getStringExtra("name"));
        }
        if (last_intent.getDoubleExtra("price", -1) != -1){
            price.setText(Double.toString(last_intent.getDoubleExtra("price", -1)) );
        }
        button_front.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                double price_check = 0;
                try {
                    price_check = Double.parseDouble(price.getText().toString());
                    if (name.getText().toString().equals("")) {
                        Toast.makeText(AddRecipeNameActivity.this, "Name cannot be empty", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (price_check < 0) {
                        Toast.makeText(AddRecipeNameActivity.this, "Price cannot be less than 0", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    // pass on intent to save progress
                    Intent intent = new Intent(AddRecipeNameActivity.this, AddRecipeIngredientActivity.class);
                    intent.putExtra("name", name.getText().toString());
                    intent.putExtra("price", Double.valueOf(price.getText().toString()));
                    intent.putExtra("ingredients", ingredients);
                    intent.putExtra("steps", steps);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                } catch (NullPointerException | NumberFormatException e) {
                    Toast.makeText(AddRecipeNameActivity.this, "Invalid Price", Toast.LENGTH_SHORT).show();
                }
            }

        });
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Intent intent = new Intent(AddRecipeNameActivity.this, RecipeListActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                overridePendingTransition(android.R.anim.slide_out_right, android.R.anim.slide_out_right);
                startActivity(intent);
                finish();
            }
        });
        button_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddRecipeNameActivity.this, RecipeListActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();}
        });
    }
}
