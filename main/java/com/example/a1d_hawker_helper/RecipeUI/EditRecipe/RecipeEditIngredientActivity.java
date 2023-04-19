package com.example.a1d_hawker_helper.RecipeUI.EditRecipe;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a1d_hawker_helper.R;
import com.example.a1d_hawker_helper.RecipeUI.RecipeIngredientsAdapter;
import com.example.a1d_hawker_helper.RecipeUI.RecipeListActivity;
import com.example.a1d_hawker_helper.RecipeUI.RecipeSelectIngredientActivity;
import com.example.a1d_hawker_helper.models.Database;
import com.example.a1d_hawker_helper.models.Ingredient;
import com.example.a1d_hawker_helper.models.Recipe;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class RecipeEditIngredientActivity extends AppCompatActivity {
    private Button buttonBack, buttonFront;
    private RecyclerView currentIngredientList;
    private RecipeIngredientsAdapter adapter;
    private FloatingActionButton addNewIngredientButton;
    private HashMap ingredients;
    private Menu optionsmenu;
    private boolean done;
    private ArrayList<String> ingredlist = new ArrayList<>();
    private String name;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_edit_ingredient);

        currentIngredientList = findViewById(R.id.currentIngredientList);
        // Make the buttons disappear since they are not needed
        buttonBack = findViewById(R.id.buttonBack);
        buttonBack.setVisibility(View.GONE);
        buttonFront = findViewById(R.id.buttonFront);
        buttonFront.setVisibility(View.GONE);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        currentIngredientList.setLayoutManager(linearLayoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(currentIngredientList.getContext(), linearLayoutManager.getOrientation());
        currentIngredientList.addItemDecoration(dividerItemDecoration);
        addNewIngredientButton = findViewById(R.id.addRecipeIngredientButton);

        adapter = new RecipeIngredientsAdapter(this, ingredlist, ingredients);
        currentIngredientList.setAdapter(adapter);

        Intent intent = getIntent();
        name = intent.getStringExtra("recipe");
        Log.d("editrecipename",name);
        ingredients = (HashMap) intent.getSerializableExtra("ingredients");

        if (ingredients == null) {
            ingredients = new HashMap<String, Ingredient>();
            // Populate the current ingredients used
            DatabaseReference ingredients_list = new Database().getRecipes().child(name).child("ingredientsList");
            ingredients_list.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if (!task.isSuccessful()) {
                        Log.e("firebase", "Error getting data", task.getException());
                    }
                    else {
                        for (DataSnapshot i: task.getResult().getChildren()) {
                            Ingredient d = i.getValue(Ingredient.class);
                            ingredients.put(d.getName(), d);
                            ingredlist.add(d.getName());
                        }
                        adapter.updateAll(ingredlist,ingredients);
                    }
                }
            });

        }

        addNewIngredientButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RecipeEditIngredientActivity.this, RecipeSelectIngredientActivity.class);
                intent.putExtra("recipe", name);
                intent.putExtra("ingredients", ingredients);
                launch_ingredients_select.launch(intent);
            }
        });
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.save_item_menu, menu);
        optionsmenu = menu;
        return true;
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        String name = getIntent().getStringExtra("recipe");
        if (id == R.id.saveItem) {
            Database db = new Database();
            DatabaseReference recipes = db.getRecipes();
            RecipeEditIngredientActivity.this.done = false;
            recipes.orderByChild("name").equalTo(name).addChildEventListener(new ChildEventListener() {

                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    if (!RecipeEditIngredientActivity.this.done){
                        // Save current values once
                        Recipe recipe = snapshot.getValue(Recipe.class);
                        Collection<Ingredient> values = ingredients.values();
                        ArrayList<Ingredient> listOfValues = new ArrayList<>(values);
                        db.getRecipes().child(recipe.getName()).child("ingredientsList").setValue(listOfValues);
                        Toast.makeText(getApplicationContext(), "Saved!", Toast.LENGTH_SHORT).show();
                        RecipeEditIngredientActivity.this.done = true;
                        Intent intent = new Intent(RecipeEditIngredientActivity.this, RecipeListActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        RecipeEditIngredientActivity.this.finish();
                    }

                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}

                @Override
                public void onChildRemoved(@NonNull DataSnapshot snapshot) {}

                @Override
                public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}

                @Override
                public void onCancelled(@NonNull DatabaseError error) {}
            });
        }
        return false;
    }
    private ActivityResultLauncher<Intent> launch_ingredients_select = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        ingredients = (HashMap) data.getSerializableExtra("ingredients");
                        ingredlist.clear();
                        ingredlist.addAll(ingredients.keySet());
                        adapter.updateAll(ingredlist,ingredients);
                    }
                }
            }
    );
}
