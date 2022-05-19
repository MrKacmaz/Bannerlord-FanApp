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

public class newsAdapter extends ArrayAdapter<String> {

    private final String[] newsTitle;
    private final int[] newsImage;
    private final String[] newsDescription;

    private final Context context;

    private ImageView imageImageView;

    public newsAdapter(String[] newsTitle, int[] newsImage, String[] newsDescription, Context context1) {
        super(context1, R.layout.adapter_news, newsTitle);
        this.newsTitle = newsTitle;
        this.newsImage = newsImage;
        this.newsDescription = newsDescription;
        this.context = context1;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        @SuppressLint({"ViewHolder", "InflateParams"}) View view = LayoutInflater.from(context).inflate(R.layout.adapter_news, null);

        if (view != null) {
            TextView titleTextView = view.findViewById(R.id.news_adapter_title);
            TextView descriptionTextView = view.findViewById(R.id.news_adapter_description);
            imageImageView = view.findViewById(R.id.news_adapter_image);


            titleTextView.setText(newsTitle[position]);
            descriptionTextView.setText(newsDescription[position]);
//            imageImageView.setBackgroundResource(newsImage[position]);


        }
        return view;
    }
}
