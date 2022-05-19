package com.example.term_project.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.term_project.Adapters.profileAdapter;
import com.example.term_project.MainPageActivity;
import com.example.term_project.Profile.ProfileUpdateActivity;
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
import java.util.Objects;

public class FragmentProfile extends Fragment {
    private FirebaseUser mUser;

    private profileAdapter adapter;
    private ListView profileListView;
    private Intent profileUpdateIntent;

    private String[] titles;
    private String[] informations;
    private String[] groups;
    private int[] images;
    private final ArrayList<String> titleAL = new ArrayList<>();
    private final ArrayList<String> informationsAL = new ArrayList<>();
    private final ArrayList<String> groupsAL = new ArrayList<>();
    private final ArrayList<String> imgAL = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View temp = inflater.inflate(R.layout.fragment_bottomnav_profile, container, false);
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference("Posts");
        profileListView = temp.findViewById(R.id.bottomNavBarActivity_profile_listview);
        Button bottomNavBarActivity_profile_updateBtn = temp.findViewById(R.id.bottomNavBarActivity_profile_updateBtn);
        bottomNavBarActivity_profile_updateBtn.setOnClickListener(v -> {
            profileUpdateIntent = new Intent(getActivity(), ProfileUpdateActivity.class);
            profileUpdateIntent.putExtra("userUID", String.valueOf(MainPageActivity.userUID));
            startActivity(profileUpdateIntent);
        });

        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if (titles != null) {
                    for (int i = 0; i < titles.length; i++) {
                        titles[i] = null;
                        informations[i] = null;
                        groups[i] = null;
//                        images[i] = null;
                    }
                    titleAL.clear();
                    informationsAL.clear();
                    groupsAL.clear();
                    imgAL.clear();
                }
                for (DataSnapshot db : snapshot.getChildren()) {
                    if (Objects.requireNonNull(db.child("UserID").getValue()).equals(mUser.getUid())) {
                        titleAL.add(Objects.requireNonNull(db.child("Title").getValue()).toString());
                        informationsAL.add(Objects.requireNonNull(db.child("Information").getValue()).toString());
                        groupsAL.add(Objects.requireNonNull(db.child("Group").getValue()).toString());
                        System.out.println(db.child("Group").getValue());
                    }
                }
                titles = titleAL.toArray(new String[0]);
                informations = informationsAL.toArray(new String[0]);
                groups = groupsAL.toArray(new String[0]);

                adapter = new profileAdapter(titles, images, informations, getContext());
                profileListView.setAdapter(adapter);
                profileListView.setOnItemClickListener((parent, view, position, id) -> Toast.makeText(requireContext().getApplicationContext(), groups[position], Toast.LENGTH_SHORT).show());


            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                Toast.makeText(requireContext().getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


        return temp;
    }

}