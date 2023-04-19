package com.example.a1d_hawker_helper.ViewOrderUI;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a1d_hawker_helper.R;
import com.example.a1d_hawker_helper.RecipeUI.RecipeSelectIngredientAdapter;
import com.example.a1d_hawker_helper.models.Database;
import com.example.a1d_hawker_helper.models.Order;

import org.w3c.dom.Text;

import java.util.ArrayList;

import javax.security.auth.callback.Callback;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    private Context context;
    private ArrayList<Order> orders = new ArrayList<Order>();

    public class OrderViewHolder extends RecyclerView.ViewHolder {
        private TextView orderNumber, options;
        private RecyclerView orderItemsList;
        private Button completeOrderButton;
        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            orderNumber = itemView.findViewById(R.id.orderNumber);
            orderItemsList = itemView.findViewById(R.id.orderItemsList);
            completeOrderButton = itemView.findViewById(R.id.completeOrderButton);
            options = itemView.findViewById(R.id.optionsorder);
        }
    }

    public OrderAdapter(Context context, ArrayList<Order> orders ){
        this.context = context;
        this.orders = orders;
    }

    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new OrderViewHolder(
                LayoutInflater.from(context)
                        .inflate(R.layout.viewholder_order,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = orders.get(position);
        String orderNumber = String.valueOf(order.getOrderNumber());
        holder.orderNumber.setText(orderNumber);

        // Init Layout Manager and Add Lines between List Entries
        LinearLayoutManager layout= new LinearLayoutManager(context);
        holder.orderItemsList.setLayoutManager(layout);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(context, layout.getOrientation());
        holder.orderItemsList.addItemDecoration(dividerItemDecoration);

        // Setting up adapter and pass in order items for inner RecyclerView
        OrderItemAdapter adapter = new OrderItemAdapter(context,orders.get(position).getOrders());
        holder.orderItemsList.setAdapter(adapter);
        holder.orderItemsList.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
                int action = e.getAction();
                switch (action) {
                    case MotionEvent.ACTION_MOVE:
                        rv.getParent().requestDisallowInterceptTouchEvent(true);
                        break;
                }
                return false;
            }

            @Override
            public void onTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {}

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {}
        });

        Database db = new Database();
        holder.completeOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.completeOrder(order);
                db.deleteOrder(orderNumber);
            }
        });

        holder.options.setOnClickListener(view -> {
            PopupMenu popupMenu = new PopupMenu(context,holder.options);
            popupMenu.inflate(R.menu.remove_item_menu);
            popupMenu.setOnMenuItemClickListener(item-> {
                switch(item.getItemId())
                {
                    case R.id.remove_item:
                        Log.d("ViewOrderUI","Deleting Order: "+orderNumber);
                        db.deleteOrder(orderNumber);
                        break;
                }
                return false;
            });
            popupMenu.show();
        });
    }


    @Override
    public int getItemCount() {
            return orders.size();
        }
}


