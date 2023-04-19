package com.example.a1d_hawker_helper.RecipeUI;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a1d_hawker_helper.R;
import com.example.a1d_hawker_helper.RecipeUI.AddRecipe.AddRecipeNameActivity;
import com.example.a1d_hawker_helper.models.Database;
import com.example.a1d_hawker_helper.models.Recipe;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class RecipeListActivity extends AppCompatActivity {
    private RecyclerView recipeList;
    private Database db;
    private RecipeAdapter adapter;
    private FloatingActionButton addRecipeButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_page);
        db = new Database();

        recipeList = findViewById(R.id.recipeList);
        addRecipeButton = findViewById(R.id.addRecipeButton);

        // Init Layout Manager and Add Lines between List Entries
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recipeList.setLayoutManager(linearLayoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recipeList.getContext(), linearLayoutManager.getOrientation());
        recipeList.addItemDecoration(dividerItemDecoration);

        // Get all current recipes and pass it into adapter
        adapter = new RecipeAdapter(db.getRecipeList());
        recipeList.setLayoutManager(new LinearLayoutManager(this));
        recipeList.setAdapter(adapter);
        db.SetRecipeEventListener(adapter);

        addRecipeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RecipeListActivity.this, AddRecipeNameActivity.class);
                startActivity(intent);
            }
        });
    }

}
