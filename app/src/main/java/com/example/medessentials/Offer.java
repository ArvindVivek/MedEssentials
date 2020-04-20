package com.example.medessentials;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Offer extends AppCompatActivity {

    private static final String TAG = "Offer";

    EditText companyName;
    Spinner productName;
    //EditText productName;
    EditText type;
    EditText quant;
    EditText descrip;
    EditText email;
    EditText latitude;
    EditText longitude;

    Button logout;
    Button offer;
    Button show_request;

    String user;

    private DatabaseReference database = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference requestRef = FirebaseDatabase.getInstance().getReference("Request");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer);

        Intent intent = getIntent();
        user = intent.getStringExtra("Username");
        Log.d(TAG, "user: " + user);

        companyName = findViewById(R.id.company_textbox_off);

        String[] arraySpinner = new String[] {"Face Shields", "Gloves", "Goggles", "Gowns", "Head Covers", "Masks", "Respirators", "Shoe Covers", "Other"};
        productName = (Spinner)findViewById(R.id.productname_spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arraySpinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        productName.setAdapter(adapter);
        //productName = findViewById(R.id.productname_textbox_off);

        type = findViewById(R.id.product_type_textbox_off);
        quant = findViewById(R.id.quantity_textbox_off);
        descrip = findViewById(R.id.details_textbox_off);
        email = findViewById(R.id.email_textbox_off);
        longitude = findViewById(R.id.longitude_textbox_off);
        latitude = findViewById(R.id.latitude_textbox_off);


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
                Toast.makeText(getApplicationContext(), "Offer has been sent!!!", Toast.LENGTH_SHORT).show();
                sendOffer();
            }
        });

        show_request = findViewById(R.id.show_request_button);
        show_request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ShowRequests.class);
                startActivity(intent);
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
        offerData offer = new offerData(user, companyName.getText().toString(), productName.getSelectedItem().toString(), type.getText().toString(),quant.getText().toString(), descrip.getText().toString(), email.getText().toString(), longitude.getText().toString(), latitude.getText().toString());

        String r = Integer.toString((int) (Math.random() * 10000));

        database.child("Offer").child(r).setValue(offer);


        //database.child("users").child(user).child("Offer").setValue(data);
    }

    private void readData() {
        requestRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren()) {
                    HashMap<String, String> request = (HashMap)dataSnapshot1.getValue();

                    String name = request.get("user");
                    String productName = request.get("quantity");
                    String quantity = request.get("quant");
                    String description = request.get("descrip");
                    String latitude = request.get("lat");
                    String longitude = request.get("longi");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d(TAG, "Data Access failed: " + databaseError.getMessage());
            }
        });
    }
}
