package com.example.term_project.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.term_project.Adapters.postAdapter;
import com.example.term_project.Posts.PostActivity;
import com.example.term_project.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Objects;

public class FragmentHome extends Fragment {

    private postAdapter adapter;
    private ListView homeListView;

    private static String[] titles;
    private static String[] desc;
    private static String[] dates;
    private static String[] users;
    private static int[] img;

    private static final ArrayList<String> titleAL = new ArrayList<>();
    private static final ArrayList<String> descAL = new ArrayList<>();
    private static final ArrayList<String> dateAL = new ArrayList<>();
    private static final ArrayList<String> userAL = new ArrayList<>();

    private Intent postIntent;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View temp = inflater.inflate(R.layout.fragment_bottomnav_home, container, false);
        homeListView = temp.findViewById(R.id.bottomNavBarActivity_home);
        DatabaseReference mReference = FirebaseDatabase.getInstance().getReference("Posts");
        mReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if (titles != null) {
                    for (int i = 0; i < titles.length; i++) {
                        titles[i] = null;
                        desc[i] = null;
                        dates[i] = null;
                        users[i] = null;
                    }
                    titleAL.clear();
                    descAL.clear();
                    dateAL.clear();
                    userAL.clear();
                }
                for (DataSnapshot db : snapshot.getChildren()) {
                    if (!Objects.requireNonNull(db.child("Group").getValue()).equals("News")) {
                        titleAL.add(Objects.requireNonNull(db.child("Title").getValue()).toString());
                        descAL.add(Objects.requireNonNull(db.child("Information").getValue()).toString());
                        dateAL.add(Objects.requireNonNull(db.child("Date").getValue()).toString());
                        userAL.add(Objects.requireNonNull(db.child("UserID").getValue()).toString());
                    }
                }

                titles = titleAL.toArray(new String[0]);
                desc = descAL.toArray(new String[0]);
                dates = dateAL.toArray(new String[0]);
                users = userAL.toArray(new String[0]);


                adapter = new postAdapter(titles, desc, dates, users, img, getContext());
                homeListView.setAdapter(adapter);

                homeListView.setOnItemClickListener((parent, view, position, id) -> {
                    postIntent = new Intent(getActivity(), PostActivity.class);
                    postIntent.putExtra("postTitle", titles[position]);
                    postIntent.putExtra("postDesc", desc[position]);
                    postIntent.putExtra("postDate", dates[position]);
                    postIntent.putExtra("postUser", users[position]);
                    clearAllArray();
                    startActivity(postIntent);
                });
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                Toast.makeText(requireContext().getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        return temp;
    }

    private void clearAllArray() {
        for (int i = 0; i < titles.length; i++) {
            titles[i] = null;
            desc[i] = null;
            dates[i] = null;
            users[i] = null;
        }
    }
}