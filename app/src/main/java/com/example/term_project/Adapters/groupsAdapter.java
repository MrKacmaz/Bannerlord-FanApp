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
import com.squareup.picasso.Picasso;

public class groupsAdapter extends ArrayAdapter<String> {

    private final String[] groupsName;
    private final String[] groupsImage;

    private final Context context;

    public groupsAdapter(String[] groupsName, String[] groupsImage, Context context1) {
        super(context1, R.layout.adapter_groups, groupsName);
        this.groupsName = groupsName;
        this.groupsImage = groupsImage;
        this.context = context1;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        @SuppressLint({"ViewHolder", "InflateParams"}) View view = LayoutInflater.from(context).inflate(R.layout.adapter_groups, null);
        if (view != null) {

            ImageView fragment_groups_listView_image = view.findViewById(R.id.fragment_groups_listView_image);
            TextView fragment_groups_listView_text = view.findViewById(R.id.fragment_groups_listView_title);

            fragment_groups_listView_text.setText(groupsName[position]);
            Picasso.get().load(groupsImage[position]).into(fragment_groups_listView_image);

        }

        return view;
    }
}
