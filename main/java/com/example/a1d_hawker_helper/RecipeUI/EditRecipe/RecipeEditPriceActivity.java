package com.example.a1d_hawker_helper.RecipeUI.EditRecipe;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.a1d_hawker_helper.R;
import com.example.a1d_hawker_helper.RecipeUI.RecipeListActivity;
import com.example.a1d_hawker_helper.models.Database;
import com.example.a1d_hawker_helper.models.Recipe;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class RecipeEditPriceActivity extends AppCompatActivity {
    //Edit name and price page
    private String recipeName;
    private EditText name, price;
    private Button button_front, button_back;
    private String originalName;
    private Menu optionsMenu;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipe_name);

        recipeName = getIntent().getStringExtra("recipe");

        // Make the buttons disappear since they are not needed
        button_front = findViewById(R.id.button_front);
        button_back = findViewById(R.id.button_back);
        button_front.setVisibility(View.GONE);
        button_back.setVisibility(View.GONE);
        name = findViewById(R.id.name);
        price = findViewById(R.id.price);
        DatabaseReference thisRecipe = new Database().getRecipes().child(recipeName);
        thisRecipe.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                // Set recipe original name and price
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                } else {
                    for (DataSnapshot i : task.getResult().getChildren()) {
                        if (Objects.equals(i.getKey(), "name")) {
                            String value = (String) i.getValue();
                            name.setText(value);
                            originalName = value;
                        } else if (Objects.equals(i.getKey(), "price")) {
                            if (i.getValue().getClass() == Double.class) {
                                Double value = (Double) i.getValue();
                                price.setText(value.toString());
                            } else if (i.getValue().getClass() == Long.class) {
                                Long value = (Long) i.getValue();
                                price.setText(value.toString());
                            }
                        }
                    }
                }
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.save_item_menu, menu);
        optionsMenu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        int save = R.id.saveItem;

        if (save == id) {
            if (originalName.equals(String.valueOf(name.getText()))) {
                // If name is the same, we just update price
                updatePrice();
            } else {
                DatabaseReference recipes = new Database().getRecipes();
                recipes.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            Recipe oldRecipe = dataSnapshot.getValue(Recipe.class);
                            if (oldRecipe.getName().equals(originalName)) {
                                // delete previous recipe object and create a new one with new name
                                // as we are referencing by Key value in firebase which can't be changed
                                oldRecipe.setName(String.valueOf(name.getText()));
                                recipeName = String.valueOf(name.getText());
                                recipes.child(String.valueOf(name.getText())).setValue(oldRecipe);
                                recipes.child(originalName).removeValue();
                                // then we update price
                                updatePrice();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {}
                });
            }
        }
            return false;
        }
        private void updatePrice(){
            try {
                double price_check = Double.parseDouble(price.getText().toString());
                if (price_check <= 0) {
                    Toast.makeText(getApplicationContext(), "Price cannot be less than or equal to 0", Toast.LENGTH_SHORT).show();
                } else {
                    Double new_price = Double.valueOf(price.getText().toString());
                    DatabaseReference recipeReference = new Database().getRecipes().child(recipeName);
                    Map<String, Object> updates = new HashMap<>();
                    updates.put("price", new_price);
                    recipeReference.updateChildren(updates);
                    Toast.makeText(this, "saved!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RecipeEditPriceActivity.this, RecipeListActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }

            } catch (NullPointerException | NumberFormatException e) {
                Toast.makeText(getApplicationContext(), "Invalid Price", Toast.LENGTH_SHORT).show();
            }
        }
    }

