package com.example.medessentials;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import android.widget.EditText;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Offer extends AppCompatActivity {

    EditText productName;
    EditText quant;
    EditText descrip;

    Button logout;
    Button offer;

    String user;

    private DatabaseReference database = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer);

        Intent intent = getIntent();
        user = intent.getStringExtra("Username");

        productName = findViewById(R.id.productname_textbox_off);
        quant = findViewById(R.id.quantity_textbox_off);
        descrip = findViewById(R.id.details_textbox_off);

        logout = findViewById(R.id.logout_off);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
            }
        });

        offer = findViewById(R.id.offer);
        offer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendOffer();
            }
        });
    }

    private void logout() {
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    public void onComplete(@NonNull Task<Void> task) {
                        Intent intent = new Intent(getApplicationContext(), Authentication.class);
                        startActivity(intent);
                    }
                });
    }

    private void sendOffer() {
        database.child("users").child(user).child("Product Name").setValue(productName.getText().toString());
        database.child("users").child(user).child("Quantity").setValue(quant.getText().toString());
        database.child("users").child(user).child("Description").setValue(descrip.getText().toString());

    }
}
