package com.example.a1d_hawker_helper.RecipeUI.EditRecipe;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.a1d_hawker_helper.R;

public class RecipeEditActivity extends AppCompatActivity {
    private Button editIngredientsButton;
    private Button editStepsButton;
    private Button editPriceButton;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_edit);

        editIngredientsButton = findViewById(R.id.editIngredientsButton);
        editStepsButton = findViewById(R.id.editStepsButton);
        editPriceButton = findViewById(R.id.editPriceButton);
        String recipe = getIntent().getStringExtra("recipe");

        editPriceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(RecipeEditActivity.this, RecipeEditPriceActivity.class);
                intent1.putExtra("recipe", recipe);
                startActivity(intent1);
            }
        });
        editIngredientsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(RecipeEditActivity.this, RecipeEditIngredientActivity.class);
                intent1.putExtra("recipe", recipe);
                startActivity(intent1);
            }
        });
        editStepsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(RecipeEditActivity.this, RecipeEditStepsActivity.class);
                intent1.putExtra("recipe", recipe);
                startActivity(intent1);
            }
        });
    }


}

