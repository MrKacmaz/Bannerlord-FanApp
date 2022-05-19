package com.example.term_project.Groups;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.term_project.Adapters.groupsClickAdapter;
import com.example.term_project.R;
import com.google.firebase.database.DatabaseReference;

public class GroupJoinPageActivity extends AppCompatActivity {

    private TextView title, description;

    private String STitle, SDescription;

    private DatabaseReference gReference;
    private DatabaseReference gImage;

    private groupsClickAdapter groupsClickAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_join_page);

        Intent getArraysIntent = getIntent();
        STitle = getArraysIntent.getStringExtra("title");
        SDescription = getArraysIntent.getStringExtra("desc");

        title = findViewById(R.id.group_join_title_textView);
        description = findViewById(R.id.group_join_description_textView);
        ListView listView = findViewById(R.id.group_join_listView);

        setTextsToLayout();

    }

    private void setTextsToLayout() {
        title.setText(STitle);
        description.setText(SDescription);
    }

}