package com.example.wagbaadmin.Adaptors;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.wagbaadmin.Models.OrdersModel;
import com.example.wagbaadmin.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class OrdersAdaptor extends RecyclerView.Adapter<OrdersAdaptor.ViewHolder> {

    ArrayList<OrdersModel> ordersModels;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ordersRef = database.getReference("Orders");


    public OrdersAdaptor(ArrayList<OrdersModel> ordersModels) {
        this.ordersModels = ordersModels;
    }

    @NonNull
    @Override
    public OrdersAdaptor.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_order ,parent,false);
        return new ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull OrdersAdaptor.ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        holder.userMail.setText(ordersModels.get(position).getUserMail());
        holder.orderDescription.setText(String.valueOf(ordersModels.get(position).getOrderDescription()));
        holder.orderTotal.setText(String.valueOf(ordersModels.get(position).getOrderTotalPrice()));
        holder.orderStatus.setText(String.valueOf(ordersModels.get(position).getOrderStatus()));
        holder.statusButton.setText(String.valueOf(ordersModels.get(position).getStatusButtonText()));

        String userID = ordersModels.get(position).getUserID();
        String orderDescription = ordersModels.get(position).getOrderDescription();

        int drawableResourceID = holder.itemView.getContext().getResources().getIdentifier(ordersModels.get(position).getOrderRestaurantPicture(), "drawable", holder.itemView.getContext().getPackageName());
        Glide.with(holder.itemView.getContext())
                .load(drawableResourceID)
                .into(holder.restaurantPictureOrder );

        holder.statusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ((holder.statusButton.getText().toString()).matches("Accept Order")){
//                    holder.statusButton.setText("Prepare Order");
                    updateFirebase("Accepted", userID, orderDescription, "Prepare Order");
                    System.out.println(holder.statusButton.getText());
                }
                else if((holder.statusButton.getText().toString()).matches("Prepare Order")){
//                    holder.statusButton.setText("Deliver Order");
                    updateFirebase("Preparing", userID, orderDescription, "Deliver Order");
                    System.out.println(holder.statusButton.getText());
                }
                else if((holder.statusButton.getText().toString()).matches("Deliver Order")){
//                    holder.statusButton.setText("Order Delivered");
                    updateFirebase("Delivering", userID, orderDescription, "Order Delivered");
                    System.out.println(holder.statusButton.getText());
                }
                else if((holder.statusButton.getText().toString()).matches("Order Delivered")){
                    updateFirebase("Delivered", userID, orderDescription, "Done");
                    System.out.println(holder.statusButton.getText());
                }
            }
        });
    }

    private void updateFirebase(String newStatus, String userID, String orderDescription, String newStatusButtonText){
        ordersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ordersRef.child(userID).child(orderDescription).child("orderStatus").setValue(newStatus);
                ordersRef.child(userID).child(orderDescription).child("statusButtonText").setValue(newStatusButtonText);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("Failure", "Failed");
            }
        });
    }

    @Override
    public int getItemCount() {
        return ordersModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView userMail, orderDescription;
        ImageView restaurantPictureOrder;
        TextView orderTotal, orderStatus;
        TextView statusButton;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            userMail = itemView.findViewById(R.id.clientMail);
            orderDescription = itemView.findViewById(R.id.orderDescription);
            restaurantPictureOrder = itemView.findViewById(R.id.restaurantPictureOrder);
            orderTotal = itemView.findViewById(R.id.orderTotal);
            orderStatus = itemView.findViewById(R.id.orderStatus);
            statusButton = itemView.findViewById(R.id.statusButton);
        }
    }
}
