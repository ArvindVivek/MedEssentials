package com.example.medessentials;

import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class Signup extends AppCompatActivity {

    Button signUp;
    EditText firstName;
    EditText lastName;
    EditText email;
    EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        String[] arraySpinner = new String[] {
                "Individual Donor", "Manufacturer Donor", "Medical Professional (Recipient)"
        };
        Spinner s = (Spinner) findViewById(R.id.persontype_dropdown);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, arraySpinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s.setAdapter(adapter);

        signUp = findViewById(R.id.fillout_signup);
        firstName = findViewById(R.id.firstname_textbox);
        lastName = findViewById(R.id.lastname_textbox);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
    }

    private void signup() {

    }
}
