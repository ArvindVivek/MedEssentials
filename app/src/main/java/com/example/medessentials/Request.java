package com.example.medessentials;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Request extends AppCompatActivity {

    private static final String TAG = "Request";

    Button logout;
    Button request;

    Spinner productName;
    EditText productType;
    EditText quantity;
    EditText descrip;
    EditText latitude;
    EditText longitude;

    String email;

    private DatabaseReference database = FirebaseDatabase.getInstance().getReference();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);

        Intent intent = getIntent();
        email = intent.getStringExtra("Email");

        logout = findViewById(R.id.logout_req);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
            }
        });

        productName = findViewById(R.id.request_productname_spinner);
        String[] arraySpinner = new String[] {"Face Shields", "Gloves", "Goggles", "Gowns", "Head Covers", "Masks", "Respirators", "Shoe Covers", "Other"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arraySpinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        productName.setAdapter(adapter);

        productType = findViewById(R.id.request_product_type_textbox_off);
        quantity = findViewById(R.id.quantity_textbox_req);
        descrip = findViewById(R.id.details_textbox_req);
        latitude = findViewById(R.id.lat_textbox_req);
        longitude = findViewById(R.id.long_textbox_req);


        request = findViewById(R.id.create_req_button);
        request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendRequest();

                Toast.makeText(getApplicationContext(), "Request has been sent!!!", Toast.LENGTH_SHORT).show();
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

    private void sendRequest() {
        RequestData req = new RequestData(email, productName.getSelectedItem().toString(), productType.getText().toString(), quantity.getText().toString(), descrip.getText().toString(), latitude.getText().toString(), longitude.getText().toString());

        String r = Integer.toString((int)(Math.random() * 10000));

        database.child("Request").child(r).setValue(req);
    }
}
