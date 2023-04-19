package com.example.a1d_hawker_helper.RecipeUI;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a1d_hawker_helper.R;
import com.example.a1d_hawker_helper.RecipeUI.EditRecipe.RecipeEditActivity;
import com.example.a1d_hawker_helper.models.Database;
import com.example.a1d_hawker_helper.models.Ingredient;
import com.example.a1d_hawker_helper.models.Recipe;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

public class RecipeDetailActivity extends AppCompatActivity {

    private TextView RecipeName, RecipePrice, RecipeIngredients;
    private Menu optionsMenu;
    private RecyclerView recipeStepsList;
    private ArrayList<String> steps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_details);
        Intent intent = getIntent();
        Database db = new Database();

        String message = intent.getStringExtra("recipe");
        steps = new ArrayList<>();
        RecipeName = findViewById(R.id.RecipeName);
        RecipeName.setText(message);
        RecipePrice = findViewById(R.id.RecipePrice);
        RecipeIngredients = findViewById(R.id.RecipeIngredients);
        recipeStepsList = findViewById(R.id.RecipeSteps);

        // Init Layout Manager and Add Lines between List Entries
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recipeStepsList.setLayoutManager(linearLayoutManager);
        androidx.recyclerview.widget.DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recipeStepsList.getContext(), linearLayoutManager.getOrientation());
        recipeStepsList.addItemDecoration(dividerItemDecoration);

        DatabaseReference recipes = db.getRecipes();
        RecipeDetailStepsAdapter adapter = new RecipeDetailStepsAdapter(this, steps);
        recipeStepsList.setAdapter(adapter);
        recipes.orderByChild("name").equalTo(message).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if (snapshot.exists()) {
                    Recipe recipe = snapshot.getValue(Recipe.class);
                    RecipePrice.setText("$" + String.valueOf(recipe.getPrice()));
                    String text = "";
                    for (Ingredient i : recipe.getIngredientsList()) {
                        text += i.getName() + ": " + i.getQty() + " " + i.getUnit() + "\n";
                    }
                    RecipeIngredients.setText(text);
                    steps.clear();
                    for (int i = 0; i < recipe.getSteps().size(); i++) {
                        steps.add(recipe.getSteps().get(i));
                    }
                    adapter.notifyDataSetChanged();
                    /*
                    String text = "Price:\n$" + String.valueOf(recipe.getPrice()) + "\n\n";
                    text += "Ingredients needed: \n";
                    for (Ingredient i : recipe.getIngredientsList()) {
                        text += i.getName() + ": " + i.getQty() + " " + i.getUnit() + "\n";
                    }
                    text += "\nSteps:\n\n";
                    for (int i = 0; i < recipe.getSteps().size(); i++) {
                        text += "Step " + (i + 1) + ":" + recipe.getSteps().get(i) + "\n\n";
                    }
                    RecipeDescription.setText(text);
                     */
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if (snapshot.exists()) {
                    Recipe recipe = snapshot.getValue(Recipe.class);
                    RecipePrice.setText("$" + String.valueOf(recipe.getPrice()));
                    String text = "";
                    for (Ingredient i : recipe.getIngredientsList()) {
                        text += i.getName() + ": " + i.getQty() + " " + i.getUnit() + "\n";
                    }
                    RecipeIngredients.setText(text);
                    steps.clear();
                    for (int i = 0; i < recipe.getSteps().size(); i++) {
                        steps.add(recipe.getSteps().get(i));
                    }
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {}

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Intent intent = getIntent();
        Boolean from_order = intent.getBooleanExtra("from_order", false);
        if (from_order){
            // if viewing from order page, no option to edit recipe
            return false;
        }
        else{
            getMenuInflater().inflate(R.menu.recipe_detail_menu, menu);
            optionsMenu = menu;
            return true;
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.edit) {
            Intent intent_edit_recipe = new Intent(RecipeDetailActivity.this, RecipeEditActivity.class);
            intent_edit_recipe.putExtra("recipe", getIntent().getStringExtra("recipe"));
            startActivity(intent_edit_recipe);
        }
        else if (id == R.id.delete) {
            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which){
                        case DialogInterface.BUTTON_POSITIVE:
                            Database db = new Database();
                            Intent intent = getIntent();
                            db.deleteRecipe(intent.getStringExtra("recipe"));
                            Intent intent_return_to_list = new Intent(RecipeDetailActivity.this, RecipeListActivity.class);
                            Toast.makeText(RecipeDetailActivity.this, "Successfully deleted!", Toast.LENGTH_SHORT).show();
                            intent_return_to_list.addFlags(FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent_return_to_list);
                            finish();
                            break;

                        case DialogInterface.BUTTON_NEGATIVE:
                            break;
                    }
                }
            };
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setMessage("Are you sure you want to delete this recipe?").setPositiveButton("Yes", dialogClickListener)
                    .setNegativeButton("No", dialogClickListener).show();
        }
        return super.onOptionsItemSelected(item);
    }

}