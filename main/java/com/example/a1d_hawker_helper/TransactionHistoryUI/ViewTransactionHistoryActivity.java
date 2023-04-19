package com.example.a1d_hawker_helper.TransactionHistoryUI;

import android.os.Bundle;

import androidx.core.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a1d_hawker_helper.R;
import com.example.a1d_hawker_helper.models.Database;
import com.example.a1d_hawker_helper.models.MenuItem;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class ViewTransactionHistoryActivity extends AppCompatActivity {
    private Button dailyButton, monthlyButton, setDateRangeButton;
    private TextView timePeriod, totalRevenueText;
    private RecyclerView report;
    private transactionHistoryViewAdapter adapter;
    private DatabaseReference historyList;
    private ArrayList<MenuItem> history = new ArrayList<>();
    private LocalDateTime holder1, holder2;
    private float totalRevenue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_transaction_history);
        Database db = new Database();

        dailyButton = findViewById(R.id.dailyButton);
        monthlyButton = findViewById(R.id.monthlyButton);
        setDateRangeButton = findViewById(R.id.datesButton);
        timePeriod = findViewById(R.id.timePeriod);
        report = findViewById(R.id.txnHistory);
        totalRevenueText = findViewById(R.id.totalRevenue);
        MaterialDatePicker materialDatePicker = MaterialDatePicker.Builder.dateRangePicker().setSelection(
                Pair.create(
                        MaterialDatePicker.thisMonthInUtcMilliseconds(),
                        MaterialDatePicker.todayInUtcMilliseconds()
                )).build();

        // Init Layout Manager and Add Lines between List Entries
        report.setLayoutManager(new LinearLayoutManager(this));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(report.getContext(), new LinearLayoutManager(this).getOrientation());
        report.addItemDecoration(dividerItemDecoration);

        historyList = db.getHistory();
        adapter = new transactionHistoryViewAdapter(this, history);
        report.setAdapter(adapter);

        // Set up current date for querying database
        LocalDate currDate = LocalDate.now();
        String currYear = String.valueOf(currDate.getYear());
        String currMonth = String.valueOf(Integer.valueOf(currDate.getMonth().getValue()));
        String date = currDate.format(DateTimeFormatter.ofPattern("ddMMyyyy"));

        dailyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                timePeriod.setText("Date:  " + currDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                historyList.child(currYear).child(currMonth).child(date).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        history.clear();
                        // Find all items and calculate totalRevenue
                        for (DataSnapshot postSnapshot : task.getResult().getChildren()) {
                            MenuItem menuItem = new MenuItem(postSnapshot.getKey(),
                                    postSnapshot.child("qty").getValue(Integer.class),
                                    postSnapshot.child("price").getValue(Float.class)
                            );
                            history.add(menuItem);
                        }
                        adapter.notifyDataSetChanged();
                        totalRevenue = 0;
                        for (MenuItem item: history) {
                            totalRevenue += item.getMenuItemQty() * item.getMenuItemPrice();
                        }
                        totalRevenueText.setText("Total Revenue: " + String.valueOf(String.format("%.2f",totalRevenue)));
                    }
                });
            }
        });
        monthlyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get current month
                LocalDate startDate = currDate.withDayOfMonth(1);
                LocalDate end = currDate.withDayOfMonth(1).plusMonths(1).minusDays(1);

                timePeriod.setText("Date:  " + startDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))+" to "+
                        end.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                historyList.child(currYear).child(currMonth).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        history.clear();
                        // Find all items and calculate totalRevenue
                        for (DataSnapshot postSnapshot : task.getResult().getChildren()) {
                            for (DataSnapshot day : postSnapshot.getChildren()) {

                                String item = day.getKey();
                                MenuItem menuItem = new MenuItem(day.getKey(),
                                        day.child("qty").getValue(Integer.class),
                                        day.child("price").getValue(Float.class)
                                );
                                boolean contains = false;
                                // If current item is already in our list, add to the current item instead of a new item
                                for (int i = 0; i < history.size(); i++) {
                                    if (history.get(i).equals(menuItem)) {
                                        menuItem.setMenuItemQty(
                                                menuItem.getMenuItemQty() + history.get(i).getMenuItemQty()
                                        );
                                        history.set(i, menuItem);
                                        contains = true;
                                        break;
                                    }
                                }
                                if (!contains) {
                                    history.add(menuItem);
                                }
                            }
                        }
                        adapter.notifyDataSetChanged();
                        totalRevenue = 0;
                        for (MenuItem item: history) {
                            totalRevenue += item.getMenuItemQty() * item.getMenuItemPrice();
                        }
                        totalRevenueText.setText("Total Revenue: " + String.valueOf(String.format("%.2f",totalRevenue)));
                    }
                });
            }
        });

        setDateRangeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                materialDatePicker.clearOnPositiveButtonClickListeners();
                materialDatePicker.show(getSupportFragmentManager(),"Tag_Picker");

                materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Pair<Long,Long>>() {
                    @Override
                    public void onPositiveButtonClick(Pair<Long,Long> selection) {
                        holder1 = LocalDateTime.ofInstant(Instant.ofEpochMilli(selection.first), ZoneId.of("UTC"));
                        holder2 = LocalDateTime.ofInstant(Instant.ofEpochMilli(selection.second), ZoneId.of("UTC"));

                        timePeriod.setText("Date:  " + holder1.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + " to " +
                                holder2.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));

                        holder2 = holder2.plusDays(1);
                        history.clear();

                        // Go through all the entries in this date range
                        while (holder1.isBefore(holder2)){
                            // Find the correct database entry
                            String date = String.valueOf(holder1);
                            date = date.split("T")[0];
                            String[] dateList = date.split("-");
                            String year = dateList[0];
                            String month = dateList[1];
                            String nmonth = month;
                            if (Integer.parseInt(nmonth)<10){
                                nmonth = nmonth.split("0")[1];
                            }
                            String day = dateList[2];
                            holder1 = holder1.plusDays(1);
                            // Find all items and calculate totalRevenue
                            historyList.child(year).child(nmonth).child(day+month+year).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DataSnapshot> task) {
                                    for (DataSnapshot day : task.getResult().getChildren()) {

                                        String item = day.getKey();
                                        MenuItem menuItem = new MenuItem(day.getKey(),
                                                day.child("qty").getValue(Integer.class),
                                                day.child("price").getValue(Float.class)
                                        );
                                        boolean contains = false;
                                        for (int i = 0; i < history.size(); i++) {
                                            if (history.get(i).equals(menuItem)) {
                                                menuItem.setMenuItemQty(
                                                        menuItem.getMenuItemQty() + history.get(i).getMenuItemQty()
                                                );
                                                history.set(i, menuItem);
                                                contains = true;
                                                break;
                                            }
                                        }
                                        if (!contains) {
                                            history.add(menuItem);
                                        }
                                    }
                                    adapter.notifyDataSetChanged();
                                    totalRevenue = 0;
                                    for (MenuItem item: history) {
                                        totalRevenue += item.getMenuItemQty() * item.getMenuItemPrice();
                                    }
                                    totalRevenueText.setText("Total Revenue: " + String.valueOf(String.format("%.2f",totalRevenue)));
                                }
                            });
                        }
                    }
                });
            }
        });

    }

}





