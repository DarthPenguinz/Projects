package com.example.a1d_hawker_helper.IngredientUI;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a1d_hawker_helper.R;
import com.example.a1d_hawker_helper.models.Database;
import com.example.a1d_hawker_helper.models.Ingredient;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.IngredientViewHolder>{

    private Context context;
    private ArrayList<Ingredient> ingredientList;

    public class IngredientViewHolder extends RecyclerView.ViewHolder{
        private EditText ingredientName, units, qty;
        private Button add;
        private TextView options;

        public IngredientViewHolder(@NonNull View itemView) {
            super(itemView);
            ingredientName = itemView.findViewById(R.id.ingredientnametextview);
            add = itemView.findViewById(R.id.addQtyButton);
            qty = itemView.findViewById(R.id.changeQtyEdit);
            units = itemView.findViewById(R.id.changeUnitEdit);
            options = itemView.findViewById(R.id.ingredientListOptions);

        }
    }

    public IngredientAdapter(Context context, ArrayList<Ingredient> ingredientList){
        this.context = context;
        this.ingredientList = ingredientList;
    }

    public IngredientAdapter() {}

    @Override
    public IngredientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater Inflater = LayoutInflater.from(context);
        View ingredientItem = Inflater.inflate(R.layout.viewholder_ingredient_list,parent,false);
        return new IngredientViewHolder(ingredientItem);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientViewHolder holder, int position) {
        // Initialise db and get the name
        Database db = new Database();
        Ingredient thisIngredient = ingredientList.get(position);
        holder.ingredientName.setText(thisIngredient.getName());
        Log.d("IngredientUI","Ingredient Shown: "+ thisIngredient.getName());

        // Initialise references - used for updating values of existing ingredient
        DatabaseReference ingredientReference = db.getIngredients().child(thisIngredient.getName());
        DatabaseReference qtyReference = ingredientReference.child("qty");
        DatabaseReference unitsReference = ingredientReference.child("unit");
        String currentIngredientName = thisIngredient.getName();

        // Display qty and unit.  Round off value to 2 dp then display
        holder.qty.setText(String.valueOf(String.format("%.2f",thisIngredient.getQty())));
        holder.units.setText(thisIngredient.getUnit());

        // Click plus button to update values: 2 main cases, check if name is changed or not, if so, need to re-initialise object and delete old
        // 4 Sub cases: check if qty or unit left blank. if so, do not update
        holder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("IngredientUI", "Ingredient Updated: " + thisIngredient.getName());
                //Name same, Qty not empty , Unit not empty
                if (String.valueOf(holder.ingredientName.getText()).equals(currentIngredientName) && !String.valueOf(holder.qty.getText()).equals("") && !String.valueOf(holder.units.getText()).equals("")) {
                    Float number = Float.valueOf(holder.qty.getText().toString());
                    qtyReference.setValue(number);
                    unitsReference.setValue(String.valueOf(holder.units.getText()));
                    thisIngredient.setQty(number);
                    thisIngredient.setUnit(String.valueOf(holder.units.getText()));
                    Log.d("Test","1");
                }
                //Name same, Qty not empty , Unit empty
                if (String.valueOf(holder.ingredientName.getText()).equals(currentIngredientName) && !String.valueOf(holder.qty.getText()).equals("") && String.valueOf(holder.units.getText()).equals("")) {
                    Float number = Float.valueOf(holder.qty.getText().toString());
                    qtyReference.setValue(number);
                    thisIngredient.setQty(number);
                    holder.units.setText(thisIngredient.getUnit());
                    Log.d("Test","2");
                }
                //Name same, Qty empty , Unit not empty
                if (String.valueOf(holder.ingredientName.getText()).equals(currentIngredientName) && String.valueOf(holder.qty.getText()).equals("") && !String.valueOf(holder.units.getText()).equals("")) {
                    unitsReference.setValue(String.valueOf(holder.units.getText()));
                    thisIngredient.setUnit(String.valueOf(holder.units.getText()));
                    holder.qty.setText(String.valueOf(String.format("%.2f", thisIngredient.getQty())));
                    Log.d("Test","3");
                }
                //Name same, Qty empty , Unit empty
                if (String.valueOf(holder.ingredientName.getText()).equals(currentIngredientName) && String.valueOf(holder.qty.getText()).equals("") && String.valueOf(holder.units.getText()).equals("")) {
                    holder.qty.setText(String.valueOf(String.format("%.2f", thisIngredient.getQty())));
                    holder.units.setText(thisIngredient.getUnit());
                    Log.d("Test","4");
                }
                //Name not same and not empty, Qty not empty, Unit not empty
                if (!String.valueOf(holder.ingredientName.getText()).equals("") && !String.valueOf(holder.ingredientName.getText()).equals(currentIngredientName) && !String.valueOf(holder.qty.getText()).equals("") && !String.valueOf(holder.units.getText()).equals("")) {
                    db.addIngredient(String.valueOf(holder.ingredientName.getText()), Float.valueOf(String.valueOf(holder.qty.getText())), String.valueOf(holder.units.getText()));
                    db.deleteIngredient(thisIngredient.getName());
                    Log.d("Test","5");
                }
                //Name not same and not empty, Qty empty, Unit not empty
                if (!String.valueOf(holder.ingredientName.getText()).equals("") && !String.valueOf(holder.ingredientName.getText()).equals(currentIngredientName) && String.valueOf(holder.qty.getText()).equals("") && !String.valueOf(holder.units.getText()).equals("")) {
                    db.addIngredient(String.valueOf(holder.ingredientName.getText()), thisIngredient.getQty(), String.valueOf(holder.units.getText()));
                    db.deleteIngredient(thisIngredient.getName());
                    Log.d("Test","6");
                }
                //Name not same and not empty, Qty not empty, Unit empty
                if (!String.valueOf(holder.ingredientName.getText()).equals("") && !String.valueOf(holder.ingredientName.getText()).equals(currentIngredientName) && !String.valueOf(holder.qty.getText()).equals("") && String.valueOf(holder.units.getText()).equals("")) {
                    db.addIngredient(String.valueOf(holder.ingredientName.getText()), Float.valueOf(String.valueOf(holder.qty.getText())), thisIngredient.getUnit());
                    db.deleteIngredient(thisIngredient.getName());
                    Log.d("Test","7");
                }
                //Name not same and not empty, Qty empty, Unit empty
                if (!String.valueOf(holder.ingredientName.getText()).equals("") && !String.valueOf(holder.ingredientName.getText()).equals(currentIngredientName) && String.valueOf(holder.qty.getText()).equals("") && String.valueOf(holder.units.getText()).equals("")) {
                    db.addIngredient(String.valueOf(holder.ingredientName.getText()), thisIngredient.getQty(), thisIngredient.getUnit());
                    db.deleteIngredient(thisIngredient.getName());
                    Log.d("Test","8");
                }
                //Name empty
                if (String.valueOf(holder.ingredientName.getText()).equals("")){
                    holder.ingredientName.setText(String.valueOf(thisIngredient.getName()));
                    holder.qty.setText(String.valueOf(String.format("%.2f", thisIngredient.getQty())));
                    holder.units.setText(thisIngredient.getUnit());
                    Log.d("Test","9");
                }
            }
        });

        // Options dropdown to delete ingredient
        holder.options.setOnClickListener(view -> {
            PopupMenu popupMenu = new PopupMenu(context,holder.options);
            popupMenu.inflate(R.menu.remove_item_menu);
            popupMenu.setOnMenuItemClickListener(item-> {
                switch(item.getItemId())
                {
                    case R.id.remove_item:
                        Log.d("IngredientUI","Ingredient Deleted: "+ thisIngredient.getName());
                        ingredientReference.removeValue();
                        break;
                }
                return false;
            });
            popupMenu.show();
        });


    }

    @Override
    public int getItemCount() {
        Log.d("IngredientUI","List Size: "+ String.valueOf(ingredientList.size()));
        return ingredientList.size();
    }
}
