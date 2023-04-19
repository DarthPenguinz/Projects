package com.example.a1d_hawker_helper.RecipeUI;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a1d_hawker_helper.R;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class RecipeDetailStepsAdapter extends RecyclerView.Adapter<RecipeDetailStepsAdapter.RecipeDetailStepsViewHolder> {

    private Context context;
    private ArrayList<String> stepsList;

    public class RecipeDetailStepsViewHolder extends RecyclerView.ViewHolder {
        private TextView step_no, step;

        public RecipeDetailStepsViewHolder(@NonNull View itemView) {
            super(itemView);
            step = itemView.findViewById(R.id.RecipeDetailsStep);
            step_no = itemView.findViewById(R.id.RecipeDetailsStepNo);
        }
    }

    RecipeDetailStepsAdapter(){}

    RecipeDetailStepsAdapter(Context c, ArrayList<String> list){
        context = c;
        stepsList = list;
    }

    @NonNull
    @Override
    public RecipeDetailStepsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View stepsViewHolder = inflater.inflate(R.layout.viewholder_recipe_details_step,parent,false);
        return new RecipeDetailStepsViewHolder(stepsViewHolder);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeDetailStepsViewHolder holder, int position) {
        String step = stepsList.get(position);
        holder.step_no.setText(String.valueOf(position + 1) + ".");
        holder.step.setText(step);
    }

    @Override
    public int getItemCount() {
        return stepsList.size();
    }
}
