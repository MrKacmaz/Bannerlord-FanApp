package com.example.term_project;


import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.HashMap;
import java.util.Objects;

public class SignInActivity extends AppCompatActivity {

    // Firebase Authentication
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    private DatabaseReference mReference;
    private HashMap<String, Object> mData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        mAuth = FirebaseAuth.getInstance();
        mReference = FirebaseDatabase.getInstance().getReference();

    }

    // Sign up button actions
    public void signUpButtonPressed(View view) {

        // User Name
        EditText editTextUserName = findViewById(R.id.userName);
        String userName = editTextUserName.getText().toString();

        // User Surname
        EditText editTextUserSurname = findViewById(R.id.userSurname);
        String userSurname = editTextUserSurname.getText().toString();

        // User mail address
        EditText editTextUserMail = findViewById(R.id.userMail);
        String userMail = editTextUserMail.getText().toString();

        // User password
        EditText editTextUserPass = findViewById(R.id.passwordFirst);
        String userPass = editTextUserPass.getText().toString();

        // User password for confirm
        EditText editTextUserPassConf = findViewById(R.id.passwordSecond);
        String userPassConf = editTextUserPassConf.getText().toString();

        Intent signUpIntent = new Intent(this, MainPageActivity.class);

        // Check user information's is empty
        if (!TextUtils.isEmpty(userMail) && !TextUtils.isEmpty(userPass) && !TextUtils.isEmpty(userPassConf)) {

            // Check passwords are the same
            if (!userPass.equals(userPassConf)) {
                Toast.makeText(SignInActivity.this, "Passwords must be the same", Toast.LENGTH_SHORT).show();
            } else {
                mAuth.createUserWithEmailAndPassword(userMail, userPass).addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {

                        mUser = mAuth.getCurrentUser();

                        mData = new HashMap<>();
                        mData.put("userID", mUser.getUid());
                        mData.put("userName", userName);
                        mData.put("userSurname", userSurname);
                        mData.put("userMail", userMail);
                        mData.put("userPassword", userPass);

                        mReference.child("Users").child(mUser.getUid()).setValue(mData).addOnCompleteListener(SignInActivity.this, task1 -> {
                            if (task1.isSuccessful()) {
                                Toast.makeText(SignInActivity.this, "Successfully Added !", Toast.LENGTH_SHORT).show();

                            } else {
                                Toast.makeText(SignInActivity.this, Objects.requireNonNull(task1.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

                        signUpIntent.putExtra("userMail", mUser.getEmail());
                        startActivity(signUpIntent);
                    } else {
                        Toast.makeText(SignInActivity.this, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        } else {
            Toast.makeText(SignInActivity.this, "Mail address and Passwords cannot be Empty", Toast.LENGTH_SHORT).show();
        }
    }

    // Back button actions
    public void backButtonPressed(View view) {
        Intent backIntent = new Intent(this, MainActivity.class);
        startActivity(backIntent);
    }
}