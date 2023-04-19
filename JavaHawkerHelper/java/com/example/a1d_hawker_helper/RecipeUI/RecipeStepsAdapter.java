package com.example.a1d_hawker_helper.RecipeUI;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a1d_hawker_helper.R;

import java.util.ArrayList;

public class RecipeStepsAdapter extends RecyclerView.Adapter<RecipeStepsAdapter.RecipeStepsViewHolder>{
    private Context context;
    private ArrayList<String> stepsList;
    private RecyclerView parent;

    public class RecipeStepsViewHolder extends RecyclerView.ViewHolder {
        private TextView step_no, options;
        private EditText step;
        private textListener listener;
        private View focus_window;

        public RecipeStepsViewHolder(@NonNull View itemView) {
            super(itemView);
            step_no = itemView.findViewById(R.id.step_no);
            step = itemView.findViewById(R.id.step);
            options = itemView.findViewById(R.id.text_options);
            listener = new textListener();
            focus_window = itemView.findViewById(R.id.focus_window_individual);
            step.addTextChangedListener(listener);
            step.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    view.setFocusableInTouchMode(true);
                    return false;
                }
            });
        }

        private class textListener implements TextWatcher {
            private int position;

            private void updatePosition(int position){
                this.position = position;
            }

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                stepsList.set(position, charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        }
    }

    public RecipeStepsAdapter(Context c,ArrayList<String> list){
        context = c;
        stepsList = list;
    }

    public RecipeStepsAdapter(){}

    @NonNull
    @Override
    public RecipeStepsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View stepsViewHolder = inflater.inflate(R.layout.viewholder_recipe_step,parent,false);
        return new RecipeStepsViewHolder(stepsViewHolder);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeStepsViewHolder holder, int position) {
        holder.listener.updatePosition(position);
        String step = stepsList.get(position);
        holder.step.setText(step);
        holder.step_no.setText(Integer.toString(position + 1));
        holder.options.setOnClickListener(view -> {
            PopupMenu popupMenu = new PopupMenu(context,holder.options);
            popupMenu.inflate(R.menu.remove_item_menu);
            popupMenu.setOnMenuItemClickListener(item-> {
                switch(item.getItemId())
                {
                    case R.id.remove_item:
                        stepsList.remove(position);
                        notifyDataSetChanged();
                        holder.focus_window.requestFocus();
                }
                return false;
            });
            popupMenu.show();
        });
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        parent = recyclerView;
    }

    public int getItemCount() {
        return stepsList.size();
    }
}
