package com.example.term_project.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.term_project.R;

public class profileAdapter extends ArrayAdapter<String> {

    private final String[] postsTitle;
    private final String[] postDescription;
    private final int[] postsImage;

    private final Context context;

    private TextView profileTitleTextView, profileDescriptionTextView, profileInteractiveTextView;
    private ImageView profileImageImageView;

    public profileAdapter(String[] postsTitle, int[] postsImage, String[] postDescription, Context context1) {
        super(context1, R.layout.adapter_profile, postsTitle);
        this.postsTitle = postsTitle;
        this.postsImage = postsImage;
        this.postDescription = postDescription;
        this.context = context1;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        @SuppressLint({"ViewHolder", "InflateParams"}) View view = LayoutInflater.from(context).inflate(R.layout.adapter_profile, null);

        if (view != null) {
            profileTitleTextView = view.findViewById(R.id.bottomNavBarActivity_profile_title);
            profileDescriptionTextView = view.findViewById(R.id.bottomNavBarActivity_profile_desc);
//            profileInteractiveTextView = view.findViewById(R.id.bottomNavBarActivity_profile_interactive);
            profileImageImageView = view.findViewById(R.id.bottomNavBarActivity_profile_postImage);


            profileTitleTextView.setText(postsTitle[position]);
            profileDescriptionTextView.setText(postDescription[position]);
//            profileInteractiveTextView.setText(String.valueOf(postInteract[position]));
//            profileImageImageView.setBackgroundResource(postsImage[position]);


        }

        return view;
    }
}
