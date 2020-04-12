package com.example.medessentials;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;

public class Authentication extends AppCompatActivity {


    public static final String TAG = "Authentication";
    int AUTHUI_REQUEST_CODE = 10001;
    Button signUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);

        signUp = findViewById(R.id.signup);
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), Signup.class);
                startActivity(intent);
            }
        });

        if(FirebaseAuth.getInstance().getCurrentUser() != null) {
            Intent intent = new Intent(getApplicationContext(), Placeholder.class);
            startActivity(intent);
        }


    }

    public void handleLoginRegister(View view) {
        List<AuthUI.IdpConfig> provider = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build()
        );
        Intent intent = AuthUI.getInstance().createSignInIntentBuilder()
                .setAvailableProviders(provider)
                .setTosAndPrivacyPolicyUrls("https://example.com", "https://example.com")
                .setLogo(R.drawable.logo)
                .build();

        startActivityForResult(intent, AUTHUI_REQUEST_CODE);
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == AUTHUI_REQUEST_CODE) {
            if(resultCode == RESULT_OK) {
                FirebaseUser user  = FirebaseAuth.getInstance().getCurrentUser();
                Log.d(TAG, "onActivityResult: " + user.getEmail());
                if(user.getMetadata().getCreationTimestamp() == user.getMetadata().getLastSignInTimestamp()) {
                    Toast.makeText(this, "Welcome new User", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(this, "Welcome back again", Toast.LENGTH_SHORT).show();
                }

                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                this.finish();
            }
            else {
                IdpResponse response = IdpResponse.fromResultIntent(data);
                if(response == null) {
                    Log.d(TAG, "onActivityResult: user has cancelled the sginin request");
                }
                else {
                    Log.e(TAG, "onActivityResult: ", response.getError());
                }
            }
        }
    }
}
