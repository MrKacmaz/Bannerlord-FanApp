package com.example.term_project.Posts;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

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

public class NewsActivity extends AppCompatActivity {

    private TextView newsActivityTitle, newsActivityDesc, newsActivityUser, newsActivityDate;
    private ImageView newsActivityImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        newsActivityTitle = findViewById(R.id.news_activity_title);
        newsActivityDesc = findViewById(R.id.news_activity_desc);
        newsActivityImage = findViewById(R.id.news_activity_image);
        newsActivityUser = findViewById(R.id.news_activity_user);
        newsActivityDate = findViewById(R.id.news_activity_date);


        Intent getNewsIntent = getIntent();
        String title = getNewsIntent.getStringExtra("newsTitle");
        String desc = getNewsIntent.getStringExtra("newsDesc");
        String userUID = getNewsIntent.getStringExtra("newsUser");
        String date = getNewsIntent.getStringExtra("newsDates");
//        String img = getNewsIntent.getStringExtra("newsImg");

        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference("Users").child(userUID);
        mRef.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                newsActivityTitle.setText(title);
                newsActivityDesc.setText(desc);
                newsActivityUser.setText(Objects.requireNonNull(snapshot.child("userName").getValue()).toString() + " " + Objects.requireNonNull(snapshot.child("userSurname").getValue()).toString());
                newsActivityDate.setText(millisToDate(Long.parseLong(date)));
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });


    }

    private String millisToDate(long millis) {
        return DateFormat.getDateInstance(DateFormat.LONG).format(millis);
    }
}