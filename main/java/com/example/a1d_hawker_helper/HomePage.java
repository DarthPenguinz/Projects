package com.example.a1d_hawker_helper;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.a1d_hawker_helper.IngredientUI.IngredientListActivity;
import com.example.a1d_hawker_helper.RecipeUI.RecipeListActivity;
import com.example.a1d_hawker_helper.AddOrderUI.AddOrderActivity;
import com.example.a1d_hawker_helper.TransactionHistoryUI.ViewTransactionHistoryActivity;
import com.example.a1d_hawker_helper.ViewOrderUI.ViewOrderActivity;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class HomePage extends AppCompatActivity {

    private Button addIngredientPageButton;
    private Button addOrderPageButton;
    private Button viewRecipePageButton;
    private Button viewOrderPageButton;
    private Button viewHistoryButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Main Page of app: Buttons to enter specific activities
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addIngredientPageButton = findViewById(R.id.addIngredientPageButton);
        addOrderPageButton = findViewById(R.id.addOrderPageButton);
        viewRecipePageButton = findViewById(R.id.viewRecipePageButton);
        viewOrderPageButton = findViewById(R.id.viewOrderPageButton);
        viewHistoryButton = findViewById(R.id.viewHistoryButton);

        addIngredientPageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomePage.this, IngredientListActivity.class);
                startActivity(intent);
            }
        });

        viewRecipePageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomePage.this, RecipeListActivity.class);
                startActivity(intent);
            }
        });

        addOrderPageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomePage.this, AddOrderActivity.class);
                startActivity(intent);
            }
        });

        viewOrderPageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomePage.this, ViewOrderActivity.class);
                startActivity(intent);
            }
        });

        viewHistoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomePage.this, ViewTransactionHistoryActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}