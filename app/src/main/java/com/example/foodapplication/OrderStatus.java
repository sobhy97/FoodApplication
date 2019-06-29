package com.example.foodapplication;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.foodapplication.ViewHolder.MenuViewHolder;
import com.example.foodapplication.ViewHolder.OrderViewHolder;
import com.example.foodapplication.common.common;
import com.example.foodapplication.model.Category;
import com.example.foodapplication.model.Order;
import com.example.foodapplication.model.Request;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class OrderStatus extends AppCompatActivity {


    public RecyclerView recyclerView;
    public RecyclerView.LayoutManager layoutManager;

    FirebaseRecyclerAdapter<Request, OrderViewHolder> adapter;

    FirebaseDatabase database;
    DatabaseReference requests;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_status);

        // firebase

        database = FirebaseDatabase.getInstance();
        requests = database.getReference("Requests");

        recyclerView = findViewById(R.id.listOrders);
//        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        loadOrders(common.current_user.getPhone());


    }

    private void loadOrders(String phone) {

        FirebaseRecyclerOptions<Request> options = new FirebaseRecyclerOptions.Builder<Request>().setQuery(requests.orderByChild("phone").equalTo(phone),Request.class).build();

       adapter = new FirebaseRecyclerAdapter<Request, OrderViewHolder>(options) {
           @Override
           protected void onBindViewHolder(@NonNull OrderViewHolder holder, int position, @NonNull Request model) {

               holder.txtOrderId.setText(adapter.getRef(position).getKey());
               holder.txtOrderStatus.setText(convertCodeToStatus(model.getStatus()));
               holder.txtOrderAddress.setText(model.getAddress());
               holder.txtOrderPhone.setText(model.getPhone());

           }

           @NonNull
           @Override
           public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
               View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.order_layout,viewGroup,false);
               OrderViewHolder orderViewHolder = new OrderViewHolder(view);
               return orderViewHolder;
           }
       };

       recyclerView.setAdapter(adapter);
        adapter.startListening();

    }



    private String convertCodeToStatus(String status) {
        if (status.equals("0"))
            return "Placed";
        else if (status.equals("1"))
            return "On my way";
        else
            return "Shipped";




        //holder.txtOrderId.setText(adapter.getRef(position).getKey());
        //holder.txtOrderStatus.setText(convertCodeToStatus(model.getStatus()));
        //holder.txtOrderAddress.setText(model.getAddress());
        //holder.txtOrderPhone.setText(model.getPhone());

        //View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.order_layout,viewGroup,false);
        //OrderViewHolder orderViewHolder = new OrderViewHolder(view);
        //return orderViewHolder;


    }
    @Override
    protected void onPostResume() {
        super.onPostResume();
        loadOrders(common.current_user.getPhone());
    }
}
