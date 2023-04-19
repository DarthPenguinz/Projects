package com.example.a1d_hawker_helper.AddOrderUI;

import static com.example.a1d_hawker_helper.AddOrderUI.AddOrderActivity.orderMap;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a1d_hawker_helper.R;
import com.example.a1d_hawker_helper.models.Recipe;

import java.util.ArrayList;

public class RecipeListForOrderAdapter extends RecyclerView.Adapter<RecipeListForOrderAdapter.recipeListForOrderViewHolder> {

    private Context context;
    private ArrayList<Recipe> orders = new ArrayList<Recipe>();

    public class recipeListForOrderViewHolder extends RecyclerView.ViewHolder {

        private TextView recipeName,currentAmount;
        private Button plusAmount,minusAmount;

        public recipeListForOrderViewHolder(@NonNull View itemView) {
            super(itemView);
            recipeName = itemView.findViewById(R.id.recipeName);
            currentAmount = itemView.findViewById(R.id.currentAmount);
            plusAmount = itemView.findViewById(R.id.plusAmount);
            minusAmount = itemView.findViewById(R.id.minusAmount);

            // Add qty by 1
            plusAmount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int curVal = Integer.parseInt(currentAmount.getText().toString()) + 1;
                    currentAmount.setText(Integer.toString(curVal));
                    orderMap.put(recipeName.getText().toString(),curVal);
                    Log.d("AddOrderUI","+1");
                }
            });

            // Reduce qty by 1
            minusAmount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int curVal = Integer.parseInt(currentAmount.getText().toString());
                    if(curVal>0){
                        curVal--;
                        currentAmount.setText(Integer.toString(curVal));
                        orderMap.put(recipeName.getText().toString(),curVal);
                        if(curVal==0){
                            // If order amount is now 0, remove item from current Order
                            orderMap.remove(recipeName.getText().toString());
                            Log.d("AddOrderUI","-1");
                        }
                    }
                }
            });

        }
    }


    public RecipeListForOrderAdapter(Context context, ArrayList<Recipe> orders ){
        this.context = context;
        this.orders = orders;
    }

    @NonNull
    @Override
    public recipeListForOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new recipeListForOrderViewHolder(
                LayoutInflater.from(context)
                        .inflate(R.layout.viewholder_recipe_for_order,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull recipeListForOrderViewHolder holder, int position) {
        holder.recipeName.setText(orders.get(position).getName());
        holder.currentAmount.setText("0");
        Log.d("AddOrderUI","Set Recipe: "+ orders.get(position).getName());
    }

    @Override
    public int getItemCount() {
        Log.d("AddOrderUI","List Size: "+String.valueOf(orders.size()));
        return orders.size();
    }
}
