package com.example.a1d_hawker_helper.RecipeUI.AddRecipe;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a1d_hawker_helper.R;
import com.example.a1d_hawker_helper.RecipeUI.RecipeListActivity;
import com.example.a1d_hawker_helper.RecipeUI.RecipeStepsAdapter;
import com.example.a1d_hawker_helper.models.Database;
import com.example.a1d_hawker_helper.models.Ingredient;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class AddRecipeStepsActivity extends AppCompatActivity {

    private Button nextButton, backButton;
    private FloatingActionButton add_button;
    private RecyclerView recipeStepsList;
    private RecipeStepsAdapter adapter;

    private ArrayList<String> finalSteps;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipe_steps);
        nextButton = findViewById(R.id.nextButton);
        backButton = findViewById(R.id.backButton);
        add_button = findViewById(R.id.addStepButton);
        recipeStepsList = findViewById(R.id.recipeStepsList);
        getSupportActionBar().setTitle("Steps");

        // Getting saved progress if any
        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        String description = intent.getStringExtra("description");
        Double price = intent.getDoubleExtra("price", -1);
        HashMap<String, Ingredient> ingredients = (HashMap<String, Ingredient>) intent.getSerializableExtra("ingredients");

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recipeStepsList.setLayoutManager(linearLayoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recipeStepsList.getContext(), linearLayoutManager.getOrientation());
        recipeStepsList.addItemDecoration(dividerItemDecoration);

        ArrayList<String> steps = new ArrayList<String>();
        if (intent.getStringArrayListExtra("steps")!= null){
            steps = intent.getStringArrayListExtra("steps");
        }
        adapter = new RecipeStepsAdapter(this, steps);
        recipeStepsList.setAdapter(adapter);

        finalSteps = steps;
        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finalSteps.add("");
                adapter.notifyItemInserted(finalSteps.size()-1);
                findViewById(R.id.focus_window).requestFocus();
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(finalSteps.size() == 0){
                    Toast.makeText(AddRecipeStepsActivity.this, "Cannot have no steps!", Toast.LENGTH_SHORT).show();
                }
                else{
                    Intent intent = new Intent(AddRecipeStepsActivity.this, RecipeListActivity.class);
                    Collection<Ingredient> values = ingredients.values();
                    ArrayList<Ingredient> listOfValues = new ArrayList<>(values);
                    Database db = new Database();
                    db.addRecipe(name, listOfValues, price, finalSteps);
                    Toast.makeText(AddRecipeStepsActivity.this, "Recipe added!", Toast.LENGTH_SHORT).show();
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(AddRecipeStepsActivity.this, AddRecipeIngredientActivity.class);
                intent1.putExtra("name", name);
                intent1.putExtra("description", description);
                intent1.putExtra("ingredients", ingredients);
                intent1.putExtra("price", price );
                intent1.putExtra("steps", finalSteps);
                intent1.addFlags(FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent1);
            }
        });

        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                Intent intent1 = new Intent(AddRecipeStepsActivity.this, AddRecipeIngredientActivity.class);
                intent1.putExtra("name", name);
                intent1.putExtra("description", description);
                intent1.putExtra("ingredients", ingredients);
                intent1.putExtra("price", price);
                intent1.putExtra("steps", finalSteps);
                intent1.addFlags(FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent1);
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);


    }
}

