package com.example.term_project.Groups;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.term_project.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

public class GroupCreateActivity extends AppCompatActivity {

    private EditText title, description;
    private ImageView imageView;

    private String STitle, SDescription;

    private static final int GALLERY_REQUEST_CODE = 123;

    private HashMap<String, Object> gData;
    private DatabaseReference gReference;

    private FirebaseStorage storage;
    private StorageReference storageReference;
    private Uri imageData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_create);


        Button chooseImageBtn = findViewById(R.id.fragment_groupsCreate_imageChooseBtn);
        Button createBtn = findViewById(R.id.fragment_groupsCreate_createBtn);
        Button backBtn = findViewById(R.id.fragment_groupsCrate_backBtn);
        title = findViewById(R.id.fragment_groupsCreate_Title);
        description = findViewById(R.id.fragment_groupsCreate_Description);
        imageView = findViewById(R.id.fragment_groupsCreate_image);

        // Go Back to groups activity
        backBtn.setOnClickListener(v -> finish());


        // Choose an image from gallery
        chooseImageBtn.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Pick an image"), GALLERY_REQUEST_CODE);

        });


        // Create new group
        createBtn.setOnClickListener(v -> {

            STitle = title.getText().toString();
            SDescription = description.getText().toString();

            gReference = FirebaseDatabase.getInstance().getReference();

            storage = FirebaseStorage.getInstance();
            storageReference = storage.getReference();


            if (!TextUtils.isEmpty(STitle) && !TextUtils.isEmpty(SDescription)) {

                // Group Info
                String gUID = UUID.randomUUID().toString();
                gData = new HashMap<>();
                gData.put("groupID", gUID);
                gData.put("groupName", STitle);
                gData.put("groupDescription", SDescription);

                // Group Image Info
                StorageReference ref = storageReference.child("GroupsImage").child(gUID);
                ref.putFile(imageData).addOnSuccessListener(taskSnapshot -> Toast.makeText(GroupCreateActivity.this, "Image Uploaded", Toast.LENGTH_SHORT).show()).addOnFailureListener(e -> Toast.makeText(GroupCreateActivity.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show());
                gReference.child("Groups").child(gUID).setValue(gData).addOnCompleteListener(GroupCreateActivity.this, task1 -> {
                    if (task1.isSuccessful()) {
                        Toast.makeText(GroupCreateActivity.this, "Group Successfully Created !", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(GroupCreateActivity.this, Objects.requireNonNull(task1.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });


            } else {
                Toast.makeText(GroupCreateActivity.this, "Title and Description can not be empty", Toast.LENGTH_SHORT).show();
            }

        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            imageData = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageData);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}