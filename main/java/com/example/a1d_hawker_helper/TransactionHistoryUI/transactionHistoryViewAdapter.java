package com.example.a1d_hawker_helper.TransactionHistoryUI;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.a1d_hawker_helper.R;
import com.example.a1d_hawker_helper.models.MenuItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class transactionHistoryViewAdapter extends RecyclerView.Adapter<transactionHistoryViewAdapter.txnHistoryViewHolder> {
    public class txnHistoryViewHolder extends RecyclerView.ViewHolder {
        private TextView menuItem, menuItemQty, menuItemRevenue;
        private View view;

        txnHistoryViewHolder(View itemView)
        {
            super(itemView);
            menuItem = itemView.findViewById(R.id.menuItem);
            menuItemQty = itemView.findViewById(R.id.menuItemQty);
            menuItemRevenue = itemView.findViewById(R.id.menuItemRevenue);
            view  = itemView;
        }
    }
    private List<MenuItem> menuItemsList;

    public transactionHistoryViewAdapter(Context context, List<MenuItem> menuItemsList) {
        this.menuItemsList = menuItemsList;
    }

    @Override
    public txnHistoryViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.viewholder_transaction_history, viewGroup, false);
        return new txnHistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(txnHistoryViewHolder viewHolder, final int position) {
        Log.d("menutqyADAPTER", String.valueOf(menuItemsList));
        viewHolder.menuItem.setText(menuItemsList.get(position).getMenuItemName());
        viewHolder.menuItemQty.setText(menuItemsList.get(position).getMenuItemQty().toString());
        viewHolder.menuItemRevenue.setText(
                String.valueOf(String.format("%.2f",
                        menuItemsList.get(position).getMenuItemPrice() *
                                menuItemsList.get(position).getMenuItemQty())));
    }

    @Override
    public int getItemCount() {
        return menuItemsList.size();
    }
}