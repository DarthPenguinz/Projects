package com.example.a1d_hawker_helper.RecipeUI;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a1d_hawker_helper.R;
import com.example.a1d_hawker_helper.models.Ingredient;

import java.util.ArrayList;
import java.util.HashMap;

public class RecipeIngredientsAdapter extends RecyclerView.Adapter<RecipeIngredientsAdapter.RecipeIngredientViewHolder> {

    private Context context;
    private ArrayList<String> ingredientList;
    private HashMap<String, Ingredient> ingredientHashMap;

    public class RecipeIngredientViewHolder extends RecyclerView.ViewHolder{
        private TextView ingredient, qty, unit;
        private Button setQtyButton;
        private EditText recipeSetIngredientQty;

        public RecipeIngredientViewHolder(@NonNull View itemView) {
            super(itemView);
            ingredient = itemView.findViewById(R.id.recipeIngredientName);
            setQtyButton = itemView.findViewById(R.id.setQtyButton);
            qty = itemView.findViewById(R.id.recipeIngredientQuantity);
            recipeSetIngredientQty = itemView.findViewById(R.id.recipeSetIngredientQty);
            unit = itemView.findViewById(R.id.recipeIngredientUnit);
        }
    }

    public RecipeIngredientsAdapter(Context c, ArrayList<String> list, HashMap<String, Ingredient> dict){
        context = c;
        ingredientList = list;
        ingredientHashMap = dict;
    }

    public RecipeIngredientsAdapter() {}

    @Override
    public RecipeIngredientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater Inflater = LayoutInflater.from(context);
        View tempView = Inflater.inflate(R.layout.viewholder_recipe_ingredient,parent,false);
        return new RecipeIngredientViewHolder(tempView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeIngredientViewHolder holder, int position) {
        String thisIngredient = ingredientList.get(position);
        Log.d("adaptercheck1",String.valueOf(this.ingredientList));
        Log.d("adaptercheck2",String.valueOf(this.ingredientHashMap));

        Float qty = ingredientHashMap.get(thisIngredient).getQty();
        holder.ingredient.setText(thisIngredient);
        holder.qty.setText(qty.toString());
        holder.unit.setText(ingredientHashMap.get(thisIngredient).getUnit());

        holder.setQtyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Float newQty = Float.valueOf(String.valueOf(holder.recipeSetIngredientQty.getText()));
                String ingredient = holder.ingredient.getText().toString();
                ingredientHashMap.get(thisIngredient).setQty(newQty);
                holder.qty.setText(newQty.toString());
            }
        });
    }

    public void updateAll(ArrayList<String> list, HashMap<String, Ingredient> mapping){
        ingredientList = list;
        ingredientHashMap = mapping;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return ingredientList.size();
    }
}