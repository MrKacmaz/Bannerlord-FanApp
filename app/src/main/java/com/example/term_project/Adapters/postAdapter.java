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

import java.text.DateFormat;


public class postAdapter extends ArrayAdapter<String> {
    private final String[] Title;
    private final String[] Description;
    private final int[] Image;
    private final String[] dates;
    private final String[] users;

    private final Context Context;

    private ImageView postAdapterImage;

    public postAdapter(String[] title, String[] description, String[] dates, String[] user, int[] image, Context context) {
        super(context, R.layout.adapter_post, title);
        this.Title = title;
        this.Description = description;
        this.dates = dates;
        this.Image = image;
        this.users = user;
        this.Context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        @SuppressLint({"ViewHolder", "InflateParams"}) View view = LayoutInflater.from(Context).inflate(R.layout.adapter_post, null);

        if (view != null) {
            TextView postAdapterTitle = view.findViewById(R.id.post_adapter_title);
//            postAdapterImage = view.findViewById(R.id.post_adapter_image);
            TextView postAdapterDate = view.findViewById(R.id.post_adapter_interaction);
            TextView postAdapterDescription = view.findViewById(R.id.post_adapter_description);


            postAdapterTitle.setText(Title[position]);
//            postAdapterImage.setBackgroundResource(Image[position]);
            postAdapterDate.setText(String.valueOf(millisToDate(Long.parseLong(dates[position]))));
            postAdapterDescription.setText(Description[position]);


        }
        return view;
    }

    private String millisToDate(long millis) {
        return DateFormat.getDateInstance(DateFormat.LONG).format(millis);
    }
}
