package com.example.a1d_hawker_helper.RecipeUI;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a1d_hawker_helper.R;
import com.example.a1d_hawker_helper.models.Recipe;

import java.util.ArrayList;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder>{

    private ArrayList<Recipe> recipeList;
    public static class RecipeViewHolder extends RecyclerView.ViewHolder {
        private TextView RecipeDescription;
        private ConstraintLayout relativeLayout;
        public RecipeViewHolder(View itemView) {
            super(itemView);
            this.RecipeDescription = (TextView) itemView.findViewById(R.id.RecipeListName);
            relativeLayout = (ConstraintLayout)itemView.findViewById(R.id.relativeLayout);
        }
    }

    public RecipeAdapter(ArrayList<Recipe> recipeList) {
        this.recipeList = recipeList;
    }

    @Override
    public RecipeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View recipeItem= layoutInflater.inflate(R.layout.viewholder_recipe_list, parent, false);
        RecipeViewHolder viewHolder = new RecipeViewHolder(recipeItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecipeViewHolder holder, int position) {
        holder.RecipeDescription.setText(recipeList.get(position).getName());
        holder.relativeLayout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                // View recipe details
                Intent intent = new Intent(view.getContext(), RecipeDetailActivity.class);
                intent.putExtra("recipe", recipeList.get(position).getName());
                view.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return recipeList.size();
    }
}
