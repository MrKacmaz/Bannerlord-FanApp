package com.example.term_project.Posts;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.term_project.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.text.DateFormat;
import java.util.Objects;

public class PostActivity extends AppCompatActivity {
    private TextView postActivityTitle, postActivityDesc, postActivityDate, postActivityUser;
//    private ImageView postActivityImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        postActivityTitle = findViewById(R.id.post_activity_title);
        postActivityDesc = findViewById(R.id.post_activity_desc);
        postActivityDate = findViewById(R.id.post_activity_date);
        postActivityUser = findViewById(R.id.post_activity_user);
//        postActivityImage = findViewById(R.id.post_activity_image);

        Intent getPostIntent = getIntent();
        String postTitle = getPostIntent.getStringExtra("postTitle");
        String postDesc = getPostIntent.getStringExtra("postDesc");
        String postDate = getPostIntent.getStringExtra("postDate");
        String postUser = getPostIntent.getStringExtra("postUser");

        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference("Users").child(postUser);
        mRef.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                postActivityTitle.setText(postTitle);
                postActivityDesc.setText(postDesc);
                postActivityDate.setText(millisToDate(Long.parseLong(postDate)));
                postActivityUser.setText(Objects.requireNonNull(snapshot.child("userName").getValue()).toString() + " " + Objects.requireNonNull(snapshot.child("userSurname").getValue()).toString());
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                Toast.makeText(PostActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private String millisToDate(long millis) {
        return DateFormat.getDateInstance(DateFormat.LONG).format(millis);
    }
}