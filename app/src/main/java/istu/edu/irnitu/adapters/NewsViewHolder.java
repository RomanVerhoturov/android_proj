package istu.edu.irnitu.adapters;

import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import istu.edu.irnitu.IOPackage.Constants;
import istu.edu.irnitu.IOPackage.NewsCommonActivity;
import istu.edu.irnitu.NewsActivity;
import istu.edu.irnitu.R;
import istu.edu.irnitu.dataModels.NewsModel;

/**
 * NewFitGid
 * Created by Александр on 08.12.2016.
 * Contact on luck.alex13@gmail.com
 * Copyright Aleksandr Novikov 2016
 */

public class NewsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    CardView cardView;
    NewsModel newsItem;
    TextView newsTheme, newsContent, themeChar, newsDate;
    View circleView;
    ViewGroup parent;

    public NewsViewHolder(View itemView, ViewGroup viewGroup) {
        super(itemView);
        parent = viewGroup;
        newsTheme = (TextView) itemView.findViewById(R.id.news_theme);
        newsContent = (TextView) itemView.findViewById(R.id.news_TV);
        themeChar = (TextView) itemView.findViewById(R.id.circle_view_char);
        newsDate = (TextView) itemView.findViewById(R.id.news_dateTV);
        circleView = itemView.findViewById(R.id.circle_view);
        //Log.d(LOG_TAG, "ViewHolder Constructor");

        itemView.setOnClickListener(this);
        //itemView.setOnCreateContextMenuListener(this);
    }

    @Override
    public void onClick(View v) {
        try {
            if (!newsItem.getHeaderImageUrl().contains(Constants.NULL)) {
                Intent intent = new Intent(v.getContext(), NewsActivity.class);
                intent.putExtra("title", newsItem.getNewsTitle());
                intent.putExtra("theme", newsItem.getTheme());
                intent.putExtra("date", newsItem.getPublishDate());
                intent.putExtra("content", newsItem.getNewsText());
                intent.putExtra("images", newsItem.getImagesUrls());
                intent.putExtra("head_image", newsItem.getHeaderImageUrl());
                v.getContext().startActivity(intent);
            } else {
                Intent intent = new Intent(v.getContext(), NewsCommonActivity.class);
                intent.putExtra("title", newsItem.getNewsTitle());
                intent.putExtra("theme", newsItem.getTheme());
                intent.putExtra("date", newsItem.getPublishDate());
                intent.putExtra("content", newsItem.getNewsText());
                intent.putExtra("images", newsItem.getImagesUrls());
                v.getContext().startActivity(intent);
            }
        } catch (NullPointerException ex) {
            ex.printStackTrace();
            Toast.makeText(v.getContext(), "Произошла ошибка: NullPointerException. Обратитесь к разработчику.", Toast.LENGTH_LONG).show();
        }
        //Toast.makeText(v.getContext(), newsItem.getNewsLink(), Toast.LENGTH_SHORT).show();
    }

}
