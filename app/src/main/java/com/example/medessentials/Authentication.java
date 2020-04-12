package com.example.medessentials;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Authentication extends AppCompatActivity {


    public static final String TAG = "Authentication";
    //int AUTHUI_REQUEST_CODE = 10001;
    Button signUp;
    Button login;
    EditText email;
    EditText password;

    private FirebaseAuth mAuth;
    private DatabaseReference myRef;

    private HashMap<String, String> userData;
    String userName;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);

        mAuth = FirebaseAuth.getInstance();
        myRef = FirebaseDatabase.getInstance().getReference("users");

        email = findViewById(R.id.Username);
        password = findViewById(R.id.Password);

        signUp = findViewById(R.id.signup);
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), Signup.class);
                startActivity(intent);
            }
        });

        userData = new HashMap<>();

        login = findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null) {
            Intent intent = new Intent(getApplicationContext(), Offer.class);
            startActivity(intent);
        }
    }

    private void signIn() {
        mAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                            readData();

                            userName = scrub(email.getText().toString());

                            /*if(occupation.equals("Medical Professional (Recipient)")) {
                                Intent intent = new Intent(getApplicationContext(), MapsHome.class);
                                intent.putExtra("Username", scrub(email.getText().toString()));
                                startActivity(intent);
                            }
                            else {
                                Intent intent = new Intent(getApplicationContext(), Offer.class);
                                intent.putExtra("Username", scrub(email.getText().toString()));
                                startActivity(intent);
                            }*/

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(getApplicationContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            // ...
                        }

                        // ...
                    }
                });
    }

    private String scrub(String str) {
        int loc = str.indexOf(".");
        return str.substring(0, loc) + str.substring(loc + 1);
    }

    public void readData() {
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot datasnapShot1: dataSnapshot.getChildren()) {
                    HashMap<String, String> user = (HashMap)datasnapShot1.getValue();

                    String name = user.get("name");
                    if(name.equals(userName)) {
                        String occupation = user.get("preference");
                        Log.d(TAG, "User: " + user + " Preference:" + occupation);
                        if(occupation.equals("Medical Professional (Recipient)")) {
                            Intent intent = new Intent(getApplicationContext(), MapsHome.class);
                            intent.putExtra("Username", scrub(email.getText().toString()));
                            startActivity(intent);
                        }
                        else {
                            Intent intent = new Intent(getApplicationContext(), Offer.class);
                            intent.putExtra("Username", scrub(email.getText().toString()));
                            startActivity(intent);
                        }
                    }


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d(TAG, "Data Access failed: " + databaseError.getMessage());

            }
        });
    }

}
