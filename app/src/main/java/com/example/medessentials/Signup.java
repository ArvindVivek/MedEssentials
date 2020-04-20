package com.example.medessentials;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Signup extends AppCompatActivity {

    Button signUp;
    EditText firstName;
    EditText lastName;
    EditText email;
    EditText password;

    Spinner s;

    private FirebaseAuth mAuth;
    private DatabaseReference database;

    private static final String TAG = "Signup";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        String[] arraySpinner = new String[] {
                "Individual Donor", "Manufacturer Donor", "Medical Professional (Recipient)"
        };
        s = (Spinner) findViewById(R.id.persontype_dropdown);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, arraySpinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s.setAdapter(adapter);

        signUp = findViewById(R.id.fillout_signup);
        firstName = findViewById(R.id.firstname_textbox);
        lastName = findViewById(R.id.lastname_textbox);
        email = findViewById(R.id.email_textbox);
        password = findViewById(R.id.password_textbox);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance().getReference();
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signup();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
    }

    private void signup() {
        mAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                            String name = scrub(email.getText().toString());
                            String occupation = s.getSelectedItem().toString();

                            // database.child("users").child(name).child("Preferences").setValue(occupation);
                            String r = Integer.toString((int) (Math.random() * 10000));

                            database.child("users").child(name).setValue(new User(name, occupation));

                            if(occupation.equals("Medical Professional (Recipient)")) {
                                Intent intent = new Intent(getApplicationContext(), MapsHome.class);
                                intent.putExtra("Username", email.getText().toString());
                                startActivity(intent);
                            }
                            else {
                                Intent intent = new Intent(getApplicationContext(), Offer.class);
                                intent.putExtra("Username", email.getText().toString());
                                startActivity(intent);
                            }


                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(Signup.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                        // ...
                    }
                });
            //Log.d(TAG, email.getText().toString());
    }

    private String scrub(String str) {
        int loc = str.indexOf(".");
        return str.substring(0, loc) + str.substring(loc + 1);
    }
}
