package com.example.a1d_hawker_helper.ViewOrderUI;

import android.content.Context;
import android.content.Intent;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a1d_hawker_helper.R;
import com.example.a1d_hawker_helper.RecipeUI.RecipeDetailActivity;
import com.example.a1d_hawker_helper.models.Database;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;

public class OrderItemAdapter extends RecyclerView.Adapter<OrderItemAdapter.OrderItemViewHolder> {

    private Context context;
    private HashMap<String, Integer> orderItems = new HashMap<String, Integer>();
    private ArrayList<String> orderItemsList;



    public class OrderItemViewHolder extends RecyclerView.ViewHolder {
        private TextView orderItemName, orderItemQty;
        private ConstraintLayout layout;

        public OrderItemViewHolder(@NonNull View itemView) {
            super(itemView);
            orderItemName = itemView.findViewById(R.id.orderItemName);
            orderItemQty = itemView.findViewById(R.id.orderItemQty);
            layout = itemView.findViewById(R.id.order_recipelist_layout);
        }
    }


    public OrderItemAdapter(Context context, HashMap<String, Integer> orderItems){
        this.context = context;
        this.orderItems = orderItems;
        this.orderItemsList = new ArrayList<String>(orderItems.keySet());
        Log.d("Tag", String.valueOf(orderItemsList));
    }

    @NonNull
    @Override
    public OrderItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new OrderItemViewHolder(
                LayoutInflater.from(context)
                        .inflate(R.layout.viewholder_order_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull OrderItemViewHolder holder, int position) {
        String recipe = orderItemsList.get(position);
        holder.orderItemName.setText(orderItemsList.get(position));
        holder.orderItemQty.setText(String.valueOf(orderItems.get(orderItemsList.get(position))));

        holder.layout.setOnClickListener(new View.OnClickListener() {
            // If clicked, view recipe details
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), RecipeDetailActivity.class);
                intent.putExtra("recipe", recipe);
                intent.putExtra("from_order", true);
                context.startActivity(intent);
            }
        });



    }

    @Override
    public int getItemCount() {
        return orderItemsList.size();
    }
}

