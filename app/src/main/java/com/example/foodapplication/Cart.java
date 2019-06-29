package com.example.foodapplication;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.foodapplication.Database.Database;
import com.example.foodapplication.ViewHolder.cartAdapter;
import com.example.foodapplication.common.common;
import com.example.foodapplication.model.Order;
import com.example.foodapplication.model.Request;
import com.google.android.gms.common.internal.service.Common;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Cart extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference requests;

    TextView totalPrice;
    Button placeOrder;


    List<Order> cart = new ArrayList<>();

    cartAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);


        database = FirebaseDatabase.getInstance();
        requests = database.getReference("Requests");

        recyclerView = findViewById(R.id.listCart);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        totalPrice = findViewById(R.id.total);
        placeOrder = findViewById(R.id.bttnPlaceOrder);

        placeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //create new request

                showAlertDialog();


            }
        });

        loadListFood();


    }

    private void showAlertDialog() {

        AlertDialog.Builder alertdialog = new AlertDialog.Builder(Cart.this);
        alertdialog.setTitle("One more step!");
        alertdialog.setMessage("Enter your address: ");

        final EditText editAddress = new EditText(Cart.this);

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        );

        editAddress.setLayoutParams(lp);
        alertdialog.setView(editAddress);
        alertdialog.setIcon(R.drawable.ic_shopping_cart_black_24dp);
        alertdialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                // create new request

                Request request = new Request(
                        common.current_user.getPhone(),
                        common.current_user.getName(),
                        editAddress.getText().toString(),
                        totalPrice.getText().toString(),
                        cart
                );

                //submit to firebase

                requests.child(String.valueOf(System.currentTimeMillis()))
                        .setValue(request);

                //delete cart

                new Database(getBaseContext()).cleanCart();
                Toast.makeText(Cart.this,"Thank you, Order Place",Toast.LENGTH_LONG).show();
                finish();

            }
        });

        alertdialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


                dialog.dismiss();

            }
        });
        alertdialog.show();



    }

    private void loadListFood() {

        cart = new Database(this).getCarts();
        adapter = new cartAdapter(cart,this);
        recyclerView.setAdapter(adapter);

        //calculate total price

        int total =0;
        for (Order order:cart)
            total +=(Integer.parseInt(order.getPrice()))*(Integer.parseInt(order.getQuantity()));

        Locale locale = new Locale("en","US");
        NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);

        totalPrice.setText(fmt.format(total));


    }
}
