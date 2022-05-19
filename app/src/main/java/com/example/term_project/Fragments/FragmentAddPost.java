package com.example.term_project.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;


public class FragmentAddPost extends Fragment {

    private ImageView addPostImageView;
    private Button addBtn, chooseImageBtn;
    private Spinner spinner;
    private EditText addPostTitle, addPostInformation;

    private FirebaseUser mUser;
    private DatabaseReference mPostsReference;

    private final List<String> categories = new ArrayList<>();
    private final HashMap<String, Object> mData = new HashMap<>();
    private final String PostUID = UUID.randomUUID().toString();


    private ArrayAdapter<String> dataAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View temp = inflater.inflate(R.layout.fragment_bottomnaw_addpost, container, false);

        addPostTitle = temp.findViewById(R.id.menuItemsAddPost_editText_title);
        addPostInformation = temp.findViewById(R.id.menuItemsAddPost_editText_information);

        addBtn = temp.findViewById(R.id.menuItemsAddPost_add_btn);
        chooseImageBtn = temp.findViewById(R.id.menuItemsAddPost_imageView_btn);
        spinner = temp.findViewById(R.id.menuItemsAddPost_spinner);


        mUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference mReference = FirebaseDatabase.getInstance().getReference("GroupsJoined").child(mUser.getUid());
        mPostsReference = FirebaseDatabase.getInstance().getReference("Posts").child(PostUID);

        categories.add("News");

        mReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                for (DataSnapshot db : snapshot.getChildren()) {
                    categories.add(db.getKey());
                }
                dataAdapter = new ArrayAdapter<>(requireContext().getApplicationContext(), android.R.layout.simple_spinner_item, categories);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(dataAdapter);


                addBtn.setOnClickListener(v -> mPostsReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot1) {
                        Date currDate = new Date();
                        mData.put("UserID", mUser.getUid());
                        mData.put("Title", addPostTitle.getText().toString());
                        mData.put("Information", addPostInformation.getText().toString());
                        mData.put("Group", spinner.getSelectedItem().toString());
                        mData.put("Date", currDate.getTime());
                        mPostsReference.setValue(mData)
                                .addOnSuccessListener(unused -> Toast.makeText(requireContext().getApplicationContext(), "Successfully added to " + spinner.getSelectedItem().toString(), Toast.LENGTH_SHORT).show())
                                .addOnFailureListener(e -> Toast.makeText(requireContext().getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show());
                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {
                        Toast.makeText(requireContext().getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }));
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                Toast.makeText(requireContext().getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        return temp;
    }

}