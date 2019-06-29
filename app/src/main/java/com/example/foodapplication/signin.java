package com.example.foodapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.foodapplication.common.common;
import com.example.foodapplication.model.User;
import com.google.android.gms.common.internal.service.Common;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

public class signin extends AppCompatActivity {

    MaterialEditText phone,pass;
    Button signIn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        phone = findViewById(R.id.editphone);
        pass = findViewById(R.id.editpass);
        signIn = findViewById(R.id.bttnsignin);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_user = database.getReference("User");

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final ProgressDialog progressDialog = new ProgressDialog(signin.this);
                progressDialog.setMessage("Please Wait....");
                progressDialog.show();

                table_user.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if (dataSnapshot.child(phone.getText().toString()).exists()) {


                            progressDialog.dismiss();
                            User user = dataSnapshot.child(phone.getText().toString()).getValue(User.class);
                            user.setPhone(phone.getText().toString());
                            if (user.getPassword().equals(pass.getText().toString()))
                            {
                                Intent home = new Intent(signin.this,home_content.class);
                                common.current_user=user;
                                startActivity(home);
                                finish();

                            } else {
                                Toast.makeText(signin.this, "Sign in is failed", Toast.LENGTH_SHORT).show();
                            }

                        }
                        else {
                            progressDialog.dismiss();
                            Toast.makeText(signin.this, "user not exist", Toast.LENGTH_SHORT).show();

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
    }
}
