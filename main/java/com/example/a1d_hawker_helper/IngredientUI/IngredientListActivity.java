package com.example.a1d_hawker_helper.IngredientUI;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a1d_hawker_helper.R;
import com.example.a1d_hawker_helper.models.Database;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class IngredientListActivity extends AppCompatActivity {
    private RecyclerView ingredientListView;
    private IngredientAdapter adapter;
    private FloatingActionButton addNewIngredientButton;
    private Database db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredient_page);
        db = new Database();

        ingredientListView = findViewById(R.id.IngredientList);

        // Init Layout Manager and Add Lines between List Entries
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        ingredientListView.setLayoutManager(linearLayoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(ingredientListView.getContext(), linearLayoutManager.getOrientation());
        ingredientListView.addItemDecoration(dividerItemDecoration);

        adapter = new IngredientAdapter(this, db.getIngredientList());
        ingredientListView.setAdapter(adapter);
        db.SetIngredientEventListener(adapter);

        // Button to add new ingredient
        addNewIngredientButton = findViewById(R.id.addNewIngredientButton);
        addNewIngredientButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddIngredient addIngredientDialog = new AddIngredient();
                addIngredientDialog.show(getSupportFragmentManager(),"example");
                Log.d("IngredientUI","Open Add New Ingredient");
            }
        });


    }
}
