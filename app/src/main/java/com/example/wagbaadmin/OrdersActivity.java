package com.example.wagbaadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.example.wagbaadmin.Adaptors.OrdersAdaptor;
import com.example.wagbaadmin.Models.OrdersModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class OrdersActivity extends AppCompatActivity {

    private RecyclerView.Adapter ordersAdapter;
    private RecyclerView recyclerOrdersList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);

        initiate();
        recyclerOrders();
    }

    private void initiate(){
        recyclerOrdersList = findViewById(R.id.recyclerOrdersList);
    }

    private void recyclerOrders(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerOrdersList = findViewById(R.id.recyclerOrdersList);
        recyclerOrdersList.setLayoutManager(linearLayoutManager);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ordersRef = database.getReference("Orders");

        ArrayList<OrdersModel> ordersItems = new ArrayList<>();

        ordersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ordersItems.clear();
                for (DataSnapshot postSnapshot : snapshot.getChildren()){
                    for (DataSnapshot nextSnapshot : postSnapshot.getChildren()){
                        ordersItems.add(nextSnapshot.getValue(OrdersModel.class));
                    }
                }
                ordersAdapter = new OrdersAdaptor(ordersItems);
                recyclerOrdersList.setAdapter(ordersAdapter);
                recyclerOrdersList.setLayoutManager(linearLayoutManager);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("Failure", "Failed");
            }
        });
    }
}