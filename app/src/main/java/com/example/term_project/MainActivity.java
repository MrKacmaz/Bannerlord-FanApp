package com.example.term_project;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    // Firebase Authentication
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
    }


    // Log in button actions
    public void logInButtonPressed(View view) {

        // User mail address
        EditText editTextUserName = findViewById(R.id.editTextTextPersonName);
        String userMail = editTextUserName.getText().toString();

        // User password
        EditText editTextUserPass = findViewById(R.id.editTextTextPassword);
        String userPass = editTextUserPass.getText().toString();

        Intent loginIntent = new Intent(this, MainPageActivity.class);

        // Check user information
        if (!TextUtils.isEmpty(userMail) && !TextUtils.isEmpty(userPass)) {
            mAuth.signInWithEmailAndPassword(userMail, userPass).addOnCompleteListener(this, task -> {
                if (task.isSuccessful()) {

                    // Added user mail to `loginIntent` for show in MainPageActivity page.
                    loginIntent.putExtra("userInfo", mUser.getUid());
                    startActivity(loginIntent);
                }
            }).addOnFailureListener(this, e -> Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show());
        } else {
            Toast.makeText(MainActivity.this, "Mail address and Password cannot be Empty", Toast.LENGTH_SHORT).show();
        }


    }


    // Forgot password actions
    public void forgotPasswordTextPressed(View view) {
        TextView textViewForgotPass = (TextView) findViewById(R.id.textForgetPassword);

        Intent forgotPassIntent = new Intent(this, MainPageActivity.class);

        textViewForgotPass.setOnClickListener(v -> {
            forgotPassIntent.putExtra("userMail", "FORGOT PASSWORD LOG IN");
            startActivity(forgotPassIntent);
        });
    }


    // Sign in button actions
    public void signInButtonPressed(View view) {
        Intent signIntent = new Intent(this, SignInActivity.class);
        startActivity(signIntent);
    }
}