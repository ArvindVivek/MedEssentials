package com.example.medessentials;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

import java.util.ArrayList;
import java.util.List;

public class Offer extends AppCompatActivity {

    private static final String TAG = "Offer";

    EditText companyName;
    EditText productName;
    EditText quant;
    EditText descrip;
    EditText email;
    EditText latitude;
    EditText longitude;

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
        Log.d(TAG, "user: " + user);

        companyName = findViewById(R.id.company_textbox_off);
        productName = findViewById(R.id.productname_textbox_off);
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
        offerData offer = new offerData(companyName.getText().toString(), productName.getText().toString(), quant.getText().toString(), descrip.getText().toString(), email.getText().toString(), longitude.getText().toString(), latitude.getText().toString());

        List<offerData> data = new ArrayList<>();
        data.add(offer);

        database.child("Offer").setValue(data);
        //database.child("users").child(user).child("Offer").setValue(data);
    }
}
