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

import com.example.term_project.Adapters.newsAdapter;
import com.example.term_project.Posts.NewsActivity;
import com.example.term_project.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Objects;

public class FragmentNews extends Fragment {

    private newsAdapter adapter;
    private ListView newsListView;

    private Intent newsIntent;

    private String[] titles;
    private String[] informations;
    private String[] dates;
    private String[] users;
    private int[] img;
    private final ArrayList<String> titleAL = new ArrayList<>();
    private final ArrayList<String> infoAL = new ArrayList<>();
    private final ArrayList<String> dateAL = new ArrayList<>();
    private final ArrayList<String> userAL = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View temp = inflater.inflate(R.layout.fragment_bottomnav_news, container, false);

        DatabaseReference mReference = FirebaseDatabase.getInstance().getReference("Posts");
        mReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if (titles != null) {
                    for (int i = 0; i < titles.length; i++) {
                        titles[i] = null;
                        informations[i] = null;
                        dates[i] = null;
                        users[i] = null;
                    }
                    titleAL.clear();
                    infoAL.clear();
                    dateAL.clear();
                    userAL.clear();
                }
                for (DataSnapshot db : snapshot.getChildren()) {
                    if (Objects.requireNonNull(db.child("Group").getValue()).equals("News")) {
                        titleAL.add(Objects.requireNonNull(db.child("Title").getValue()).toString());
                        infoAL.add(Objects.requireNonNull(db.child("Information").getValue()).toString());
                        dateAL.add(Objects.requireNonNull(db.child("Date").getValue()).toString());
                        userAL.add(Objects.requireNonNull(db.child("UserID").getValue()).toString());
                    }
                }
                titles = titleAL.toArray(new String[0]);
                informations = infoAL.toArray(new String[0]);
                dates = dateAL.toArray(new String[0]);
                users = userAL.toArray(new String[0]);

                adapter = new newsAdapter(titles, img, informations, getContext());
                newsListView = temp.findViewById(R.id.bottomNavBarActivity_news);
                newsListView.setAdapter(adapter);
                newsListView.setOnItemClickListener((parent, view, position, id) -> {
                    newsIntent = new Intent(getActivity(), NewsActivity.class);
                    newsIntent.putExtra("newsTitle", titles[position]);
                    newsIntent.putExtra("newsDesc", informations[position]);
                    newsIntent.putExtra("newsUser", users[position]);
                    newsIntent.putExtra("newsDates", dates[position]);
                    startActivity(newsIntent);
                    Toast.makeText(requireContext().getApplicationContext(), titles[position], Toast.LENGTH_SHORT).show();
                });
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                Toast.makeText(requireContext().getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


        return temp;
    }
}