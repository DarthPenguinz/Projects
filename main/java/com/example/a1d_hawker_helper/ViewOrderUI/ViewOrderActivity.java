package com.example.a1d_hawker_helper.ViewOrderUI;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a1d_hawker_helper.R;
import com.example.a1d_hawker_helper.RecipeUI.EditRecipe.RecipeEditActivity;
import com.example.a1d_hawker_helper.RecipeUI.RecipeDetailActivity;
import com.example.a1d_hawker_helper.RecipeUI.RecipeListActivity;
import com.example.a1d_hawker_helper.models.Database;
import com.example.a1d_hawker_helper.models.Order;

import java.util.ArrayList;

public class ViewOrderActivity extends AppCompatActivity {
    private RecyclerView orderList;
    private OrderAdapter adapter;
    private ArrayList<Order> orders;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_order);
        Database db = new Database();

        orderList = findViewById(R.id.orderList);

        // Init Layout Manager and Add Lines between List Entries
        orderList.setLayoutManager(new LinearLayoutManager(this));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(orderList.getContext(), new LinearLayoutManager(this).getOrientation());
        orderList.addItemDecoration(dividerItemDecoration);

        orders = db.getOrderList();
        adapter = new OrderAdapter(this ,orders);
        orderList.setAdapter(adapter);
        db.SetOrderEventListener(adapter);
    }

}
