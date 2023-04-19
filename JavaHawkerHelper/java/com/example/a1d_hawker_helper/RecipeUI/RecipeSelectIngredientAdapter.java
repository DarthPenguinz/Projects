package com.example.a1d_hawker_helper.RecipeUI;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a1d_hawker_helper.R;

import java.util.ArrayList;

public class RecipeSelectIngredientAdapter extends RecyclerView.Adapter<RecipeSelectIngredientAdapter.RecipeSelectIngredientViewHolder> {


    public static class RecipeSelectIngredientViewHolder extends RecyclerView.ViewHolder {

        private CheckBox checkBox;
        private TextView ingredientName;
        private androidx.constraintlayout.widget.ConstraintLayout ConstraintLayout;

        public RecipeSelectIngredientViewHolder(View itemView) {
            super(itemView);
            // Find the checkbox view in the layout
            ingredientName = itemView.findViewById(R.id.recipeAddIngredientName);
            checkBox = itemView.findViewById(R.id.checkBox);
            ConstraintLayout = itemView.findViewById(R.id.ConstraintLayout);
        }

        void bind(String s, Boolean w) {
            // Set the text
            ingredientName.setText(s);
            checkBox.setChecked(w);

            // Listen to changes (i.e. when the user checks or unchecks the box)
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    // Invoke the callback
                    if(callback != null) callback.onCheckedChanged(s, isChecked);
                }
            });
        }
    }
    private ArrayList<String> items;
    private ArrayList<String> checked_items;
    // A callback that gets invoked when an item is checked (or unchecked)
    private static Callback callback;

    public RecipeSelectIngredientAdapter(ArrayList<String> items, ArrayList<String> checked_items){
        this.checked_items = checked_items;
        this.items = items;
    }
    // Call this to add strings to the adapter
    public void addItem(String item) {
        items.add(item);
    }

    @Override
    public RecipeSelectIngredientViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater Inflater = LayoutInflater.from(parent.getContext());
        View ingredientViewHolder = Inflater.inflate(R.layout.viewholder_recipe_add_ingredient,parent,false);
        return new RecipeSelectIngredientViewHolder(ingredientViewHolder);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeSelectIngredientViewHolder holder, int position) {
        holder.bind(items.get(position), checked_items.contains(items.get(position)));
        holder.ConstraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.bind(items.get(position), !holder.checkBox.isChecked());
            }
        });

    }

    // Sets the callback
    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    // Callback interface, used to notify when an item's checked status changed
    public interface Callback {
        void onCheckedChanged(String item, boolean isChecked);
    }
}
