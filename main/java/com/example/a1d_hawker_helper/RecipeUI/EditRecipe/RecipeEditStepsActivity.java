package com.example.a1d_hawker_helper.RecipeUI.EditRecipe;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a1d_hawker_helper.R;
import com.example.a1d_hawker_helper.RecipeUI.RecipeListActivity;
import com.example.a1d_hawker_helper.RecipeUI.RecipeStepsAdapter;
import com.example.a1d_hawker_helper.models.Database;
import com.example.a1d_hawker_helper.models.Recipe;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

public class RecipeEditStepsActivity extends AppCompatActivity {

    private RecyclerView recipeStepsList;
    private RecipeStepsAdapter adapter;
    private FloatingActionButton addStepButton;
    private Button nextButton, backButton;
    private Menu optionsMenu;
    private ArrayList<String> finalSteps;
    private boolean done;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!done){
            setContentView(R.layout.activity_add_recipe_steps);

            Intent intent = getIntent();
            String recipeName = intent.getStringExtra("recipe");
            recipeStepsList = findViewById(R.id.recipeStepsList);
            addStepButton = findViewById(R.id.addStepButton);
            backButton = findViewById(R.id.backButton);
            backButton.setVisibility(View.GONE);
            nextButton = findViewById(R.id.nextButton);
            nextButton.setVisibility(View.GONE);

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
            recipeStepsList.setLayoutManager(linearLayoutManager);
            DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recipeStepsList.getContext(), linearLayoutManager.getOrientation());
            recipeStepsList.addItemDecoration(dividerItemDecoration);

            ArrayList<String> steps = new ArrayList<>();
            adapter = new RecipeStepsAdapter(this, steps);
            recipeStepsList.setAdapter(adapter);
            finalSteps = steps;
            addStepButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finalSteps.add("");
                    adapter.notifyDataSetChanged();
                    findViewById(R.id.focus_window).requestFocus();
                }
            });
            DatabaseReference recipes = new Database().getRecipes();
            recipes.orderByChild("name").equalTo(recipeName).addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    if (!done){
                        Recipe recipe = snapshot.getValue(Recipe.class);
                        steps.addAll(recipe.getSteps());
                        adapter.notifyDataSetChanged();
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
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.save_item_menu, menu);
        optionsMenu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        String name = getIntent().getStringExtra("recipe");
        if (id == R.id.saveItem) {
            // Save final steps and changes (if any)
            DatabaseReference recipes = new Database().getRecipes();
            RecipeEditStepsActivity.this.done = false;
            recipes.child(name).child("steps").setValue(finalSteps);
            Toast.makeText(getApplicationContext(), "saved!", Toast.LENGTH_SHORT).show();
            RecipeEditStepsActivity.this.done = true;
            Intent intent = new Intent(RecipeEditStepsActivity.this, RecipeListActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            RecipeEditStepsActivity.this.finish();
        }
        return false;
    }
}