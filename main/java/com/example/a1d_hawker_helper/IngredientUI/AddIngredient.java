package com.example.a1d_hawker_helper.IngredientUI;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.a1d_hawker_helper.R;
import com.example.a1d_hawker_helper.models.Database;
import com.google.firebase.database.DatabaseReference;

public class AddIngredient extends AppCompatDialogFragment {

    private EditText addIngredientName, addIngredientQty, addIngredientUnit;

    // Create Popup Dialog for Adding Ingredient
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.add_ingredient,null);
        Database db = new Database();

        addIngredientName = view.findViewById(R.id.addIngredientName);
        addIngredientQty = view.findViewById(R.id.addIngredientQty);
        addIngredientUnit = view.findViewById(R.id.addIngredientUnit);

        builder.setView(view)
                .setTitle("New Ingredient")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {}
                })
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Do Not Allow any field to be empty, error message will appear
                        if (String.valueOf(addIngredientUnit.getText()).isEmpty() ||
                                String.valueOf(addIngredientQty.getText()).isEmpty() ||
                                String.valueOf(addIngredientName.getText()).isEmpty()){
                            Toast.makeText(getContext(), "Adding Failed - Missing Field", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        db.addIngredient(
                                String.valueOf(addIngredientName.getText()),
                                Float.valueOf(String.valueOf(addIngredientQty.getText())),
                                String.valueOf(addIngredientUnit.getText())
                        );
                        Log.d("IngredientUI", "Ingredient Added: " + addIngredientName.getText());
                    }
                });

        return builder.create();

    }
}
