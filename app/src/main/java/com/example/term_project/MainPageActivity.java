package com.example.term_project;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.term_project.Fragments.FragmentAddPost;
import com.example.term_project.Fragments.FragmentHome;
import com.example.term_project.Fragments.FragmentNews;
import com.example.term_project.Fragments.FragmentProfile;
import com.example.term_project.FragmentsMenu.FragmenSettings;
import com.example.term_project.FragmentsMenu.FragmentGroups;
import com.example.term_project.FragmentsMenu.FragmentLogout;
import com.example.term_project.FragmentsMenu.FragmentMain;
import com.example.term_project.FragmentsMenu.Fragment_menubar_Profile;
import com.example.term_project.Groups.GroupShowActivity;
import com.example.term_project.Profile.ProfileUpdateActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class MainPageActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    private String userName;
    private String userSurname;
    private String userMail;
    public static String userUID;

    private DrawerLayout drawerLayout;

    private Intent profileUpdateIntent, groupShowIntent, friendIntent;

    public MainPageActivity() {
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser mUser = mAuth.getCurrentUser();
        assert mUser != null;
        DatabaseReference mReference = FirebaseDatabase.getInstance().getReference("Users").child(mUser.getUid());

        mReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NotNull DataSnapshot snapshot) {

                for (DataSnapshot db : snapshot.getChildren()) {
                    switch (Objects.requireNonNull(db.getKey())) {
                        case "userMail":
                            userMail = (String) db.getValue();
                        case "userName":
                            userName = (String) db.getValue();
                        case "userSurname":
                            userSurname = (String) db.getValue();

                    }
                }
                showUserInfoInTabLayout();
                System.out.println(userMail + " " + userName + " " + userSurname);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                Toast.makeText(MainPageActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        // Fragments for navigation view created
        FragmentMain fragmentMain = new FragmentMain();
        FragmentGroups fragmentGroups = new FragmentGroups();
        FragmenSettings fragmenSettings = new FragmenSettings();
        Fragment_menubar_Profile fragmentMenubarProfile = new Fragment_menubar_Profile();
        FragmentLogout fragmentLogout = new FragmentLogout();

        // Fragments for bottom navigation bar created
        FragmentHome fragmentHome = new FragmentHome();
        FragmentNews fragmentNews = new FragmentNews();
        FragmentAddPost fragmentAddPost = new FragmentAddPost();
        FragmentProfile fragmentProfile = new FragmentProfile();

        // Created a toolbar in place of the default bar. And added 3 rows of action buttons
        drawerLayout = findViewById(R.id.mainPageDrawerLayout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        Toolbar toolbar = findViewById(R.id.mainPageToolBar);

        setSupportActionBar(toolbar);


        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.OPEN, R.string.CLOSE);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();


        navigationView.setNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {

                case R.id.fragment_main:
                    Toast.makeText(getApplicationContext(), "MAIN", Toast.LENGTH_SHORT).show();
                    setFragments(fragmentHome);
                    drawerLayout.closeDrawer(GravityCompat.START);
                    return true;

                case R.id.fragment_groups:
                    Toast.makeText(getApplicationContext(), "GROUPS", Toast.LENGTH_SHORT).show();
                    //setFragments(fragmentGroups);
                    groupShowIntent = new Intent(MainPageActivity.this, GroupShowActivity.class);
                    drawerLayout.closeDrawer(GravityCompat.START);
                    startActivity(groupShowIntent);
                    return true;


                case R.id.fragment_settings:
                    Toast.makeText(getApplicationContext(), "SETTINGS", Toast.LENGTH_SHORT).show();
                    setFragments(fragmenSettings);
                    drawerLayout.closeDrawer(GravityCompat.START);
                    return true;

                case R.id.fragment_menubar_profile:
                    Toast.makeText(getApplicationContext(), "PROFILE", Toast.LENGTH_SHORT).show();
                    profileUpdateIntent = new Intent(MainPageActivity.this, ProfileUpdateActivity.class);
                    //setFragments(fragmentMenubarProfile);
                    drawerLayout.closeDrawer(GravityCompat.START);
                    startActivity(profileUpdateIntent);
                    return true;

                case R.id.fragment_logout:
                    alertDialogForLogout();
                    return true;

                default:
                    return false;
            }
        });


        // Bottom navigation bar catch
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavBarActivity);

        // Default choose Home Fragment
        setFragments(fragmentHome);

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.menuItemsHome:
                    setFragments(fragmentHome);
                    return true;
                case R.id.menuItemsNews:
                    setFragments(fragmentNews);
                    return true;
                case R.id.menuItemsAddPost:
                    setFragments(fragmentAddPost);
                    return true;
                case R.id.menuItemsProfile:
                    setFragments(fragmentProfile);
                    return true;
                default:
                    return false;
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    public void setFragments(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.nav_host_fragment, fragment);
        transaction.commit();
    }

    public void alertDialogForLogout() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("M&B 2: Bannerlord FanApp");
        alert.setMessage("Are you sure to logout?");
        alert.setCancelable(false);
        alert.setIcon(R.drawable.ic_menu_home);
        alert.setPositiveButton("No", (dialog, which) -> dialog.dismiss());
        alert.setNegativeButton("Yes", (dialog, which) -> logoutFun());
        alert.show();
    }

    public void logoutFun() {
        mAuth.signOut();
        Intent goToLoginIntent = new Intent(MainPageActivity.this, MainActivity.class);
        startActivity(goToLoginIntent);
        finish();
    }

    @SuppressLint("SetTextI18n")
    public void showUserInfoInTabLayout() {
        TextView textView = findViewById(R.id.fragmentMenuBarHeaderTextView);
        textView.setText("Welcome " + userName);
        System.out.println(textView.getText().toString());
    }
}