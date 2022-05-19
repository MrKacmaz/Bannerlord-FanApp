package com.example.term_project.Profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.term_project.MainPageActivity;
import com.example.term_project.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Objects;

public class ProfileUpdateActivity extends AppCompatActivity {

    private EditText profile_update_name, profile_update_surname, profile_update_email;
    private ImageView profile_update_photo;

    private FirebaseUser mUser;

    private String userMail, userName, userSurname, userPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_update);

        profile_update_name = findViewById(R.id.profile_update_name);
        profile_update_surname = findViewById(R.id.profile_update_surname);
        profile_update_email = findViewById(R.id.profile_update_email);
        profile_update_photo = findViewById(R.id.profile_update_photo);

        mUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference mReference = FirebaseDatabase.getInstance().getReference("Users").child(mUser.getUid());

        mReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                for (DataSnapshot db : snapshot.getChildren()) {
                    switch (Objects.requireNonNull(db.getKey())) {
                        case "userMail":
                            userMail = (String) db.getValue();
                            break;
                        case "userName":
                            userName = (String) db.getValue();
                            break;
                        case "userPassword":
                            userPassword = (String) db.getValue();
                            break;
                        case "userSurname":
                            userSurname = (String) db.getValue();
                            break;
                    }
                }
                profile_update_name.setHint(userName);
                profile_update_surname.setHint(userSurname);
                profile_update_email.setHint(userMail);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

    }

    public void updateInfo(View view) {
        String newName, newSurname, newEmail;
        HashMap<String, String> mNewData = new HashMap<>();

        newName = profile_update_name.getText().toString();
        newSurname = profile_update_surname.getText().toString();
        newEmail = profile_update_email.getText().toString();

        DatabaseReference mReferenceNewVersion = FirebaseDatabase.getInstance().getReference("Users").child(mUser.getUid());

        mNewData.put("userID", mUser.getUid());
        mNewData.put("userMail", newEmail);
        mNewData.put("userName", newName);
        mNewData.put("userPassword", userPassword);
        mNewData.put("userSurname", newSurname);


        mUser.updateEmail(newEmail).addOnCompleteListener(ProfileUpdateActivity.this, task -> {
            if (task.isSuccessful()) {
                Toast.makeText(ProfileUpdateActivity.this, "Successfully EMAIL Updated !", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(ProfileUpdateActivity.this, "Failed EMAIL !", Toast.LENGTH_SHORT).show();
            }
        });

        mReferenceNewVersion.setValue(mNewData).addOnCompleteListener(ProfileUpdateActivity.this, task -> {
            if (task.isSuccessful()) {
                Toast.makeText(ProfileUpdateActivity.this, "Successfully Updated !", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(ProfileUpdateActivity.this, "Failed !", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void backBtnProfile(View view) {
        Intent backBtnIntent = new Intent(this, MainPageActivity.class);
        startActivity(backBtnIntent);
    }
}