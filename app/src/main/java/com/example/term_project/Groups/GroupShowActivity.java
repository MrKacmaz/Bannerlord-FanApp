package com.example.term_project.Groups;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.term_project.Adapters.groupsAdapter;
import com.example.term_project.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class GroupShowActivity extends AppCompatActivity {

    private DatabaseReference gReferenceForGroupJoined;
    private final HashMap<String, String> gData = new HashMap<>();


    private final ArrayList<String> IDInFireBase = new ArrayList<>();
    private final ArrayList<String> titleInFireBase = new ArrayList<>();
    private final ArrayList<String> descInFireBase = new ArrayList<>();
    private final ArrayList<String> imgInFireBase = new ArrayList<>();

    private groupsAdapter groupsAdapter;
    private ListView listView;

    private String[] title;
    private String[] desc;
    private String[] img;

    private Button backBtn, createBtn;
    private ImageView gImageView;
    private Intent joinIntent, createBtnIntent;
    private static boolean isJoin = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_show);

        DatabaseReference gImage = FirebaseDatabase.getInstance().getReference("GroupsImages");
        gImage.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                for (DataSnapshot db : snapshot.getChildren()) {
                    String link = db.getValue(String.class);
                    System.out.println(link);
                    imgInFireBase.add(link);
                }
                img = imgInFireBase.toArray(new String[0]);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                Toast.makeText(GroupShowActivity.this, "Error Loading Image", Toast.LENGTH_SHORT).show();
            }
        });

        DatabaseReference gReference = FirebaseDatabase.getInstance().getReference("Groups");
        gReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                for (DataSnapshot db : snapshot.getChildren()) {
                    IDInFireBase.add((Objects.requireNonNull(db.child("groupID").getValue())).toString());
                    titleInFireBase.add((Objects.requireNonNull(db.child("groupName").getValue())).toString());
                    descInFireBase.add(Objects.requireNonNull(db.child("groupDescription").getValue()).toString());
                }
                title = titleInFireBase.toArray(new String[0]);
                desc = descInFireBase.toArray(new String[0]);

                backBtn = findViewById(R.id.fragment_groups_backBtn);
                createBtn = findViewById(R.id.fragment_groups_createBtn);
                listView = findViewById(R.id.fragment_groups_listView);

                groupsAdapter = new groupsAdapter(title, img, getApplicationContext());
                listView = findViewById(R.id.fragment_groups_listView);
                listView.setAdapter(groupsAdapter);


                // When press the group items
                setListView();


                // When Create Button pressed
                setCreateBtn();
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                Toast.makeText(GroupShowActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();

            }
        });

    }

    public void setCreateBtn() {
        createBtn.setOnClickListener(v -> {
            createBtnIntent = new Intent(GroupShowActivity.this, GroupCreateActivity.class);
            startActivity(createBtnIntent);
        });

    }

    public void setListView() {

        listView.setOnItemClickListener((parent, view, position, id) -> alertDialog(title[position], desc[position], position));
    }

    public void backBtnForGroup(View v) {
        finish();
    }

    public void alertDialog(String title, String desc, int position) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle(title);
        alert.setMessage(desc);
        alert.setCancelable(false);
        alert.setIcon(R.drawable.ic_add_post_back);
        alert.setPositiveButton("Not Interested", (dialog, which) -> dialog.dismiss());

        alert.setNegativeButton("Join", (dialog, which) -> {

            isJoin = true;
            addDatabase(title, position);

            joinIntent = new Intent(GroupShowActivity.this, GroupJoinPageActivity.class);
            joinIntent.putExtra("title", title);
            joinIntent.putExtra("desc", desc);
            startActivity(joinIntent);
        });
        alert.show();
    }

    private void addDatabase(String title, int position) {
        if (isJoin) {

            FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
            gData.put(title, IDInFireBase.get(position));

            assert mUser != null;
            gReferenceForGroupJoined = FirebaseDatabase.getInstance().getReference("GroupsJoined").child(mUser.getUid());
            gReferenceForGroupJoined.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                    for (DataSnapshot db : snapshot.getChildren()) {
                        gData.put(db.getKey(), (String) db.getValue());
                    }
                    gReferenceForGroupJoined.setValue(gData).addOnCompleteListener(GroupShowActivity.this, task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(GroupShowActivity.this, "Successfully Joined !", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(GroupShowActivity.this, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                @Override
                public void onCancelled(@NonNull @NotNull DatabaseError error) {
                    Toast.makeText(GroupShowActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                }
            });

        }
    }
}