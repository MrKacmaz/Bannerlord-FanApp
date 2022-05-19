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

public class groupsClickAdapter extends ArrayAdapter<String> {

    private final String[] postsTitles;
    private final String[] postsDescriptions;
    private final String[] postImagesLinks;

    private ImageView imageView;
    private final Context context;

    public groupsClickAdapter(String[] postsTitles, String[] postsDescriptions, String[] postImagesLinks, Context context1) {
        super(context1, R.layout.adapter_groups_click, postsTitles);
        this.postsTitles = postsTitles;
        this.postsDescriptions = postsDescriptions;
        this.postImagesLinks = postImagesLinks;
        this.context = context1;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        @SuppressLint({"ViewHolder", "InflateParams"}) View temp = LayoutInflater.from(context).inflate(R.layout.adapter_groups_click, null);

        if (temp != null) {
            imageView = temp.findViewById(R.id.adapter_groups_listView_image);
            TextView title = temp.findViewById(R.id.adapter_groups_listView_title_textview);
            TextView desc = temp.findViewById(R.id.adapter_groups_listView_desc_textview);

            title.setText(postsTitles[position]);
            desc.setText(postsDescriptions[position]);

//            Picasso.get().load(postImagesLinks[position]).into(imageView);
        }

        return temp;
    }
}
