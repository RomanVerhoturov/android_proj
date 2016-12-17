package istu.edu.irnitu.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import istu.edu.irnitu.IOPackage.Constants;
import istu.edu.irnitu.NewsActivity;
import istu.edu.irnitu.R;
import istu.edu.irnitu.dataModels.NewsModel;


/**
 * NewFitGid
 * Created by Александр on 04.06.2016.
 * Contact on luck.alex13@gmail.com
 * © Aleksandr Novikov 2016
 */
public class NewsRVAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String LOG_TAG = "LOG_TAG_NewsRVAdapter";
    private static final int DIALOG = 1;
    private static ArrayList<NewsModel> newsList;
    private String currentDate;
    private Context adapterContext;
    private final int COMMON = 0, PROGRESS = 1;

    public NewsRVAdapter(ArrayList<NewsModel> data, Context context) {
        this.newsList = data;
        this.adapterContext = context;
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        this.currentDate = sdf.format(cal.getTime());

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        switch (viewType) {
            case COMMON:
                View v1 = inflater.inflate(R.layout.item_news, viewGroup, false);
                viewHolder = new NewsViewHolder(v1,viewGroup);
                break;
            case PROGRESS:
                View v2 = inflater.inflate(R.layout.item_progress, viewGroup, false);
                viewHolder = new ProgressViewHolder(v2,viewGroup);
                break;
            default:
                View view = inflater.inflate(R.layout.item_news, viewGroup, false);
                viewHolder = new NewsViewHolder(view,viewGroup);
                break;
        }
        return viewHolder;
    }


    @Override
    public int getItemViewType(int position) {
        if (newsList.get(position).getItemType() == COMMON) {
            return COMMON;
        } else if (newsList.get(position).getItemType() == PROGRESS) {
            return PROGRESS;
        }
        return -1;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case COMMON:

                NewsViewHolder newsViewHolder = (NewsViewHolder) holder;
                newsViewHolder.newsItem = newsList.get(position);
                newsViewHolder.newsTheme.setText(newsList.get(position).getTheme());
                newsViewHolder.newsDate.setText(newsList.get(position).getPublishDate());
                newsViewHolder.themeChar.setText(newsList.get(position).getTheme().substring(0,1));
                newsViewHolder.newsContent.setText(newsList.get(position).getNewsTitle());
                switch (newsList.get(position).getTheme()){
                    case Constants.THEME_ABITUR:
                        newsViewHolder.circleView.setBackground(ContextCompat.getDrawable(adapterContext, R.drawable.circle_green_light_layout));
                        break;
                    case Constants.THEME_ABOUT:
                        newsViewHolder.circleView.setBackground(ContextCompat.getDrawable(adapterContext, R.drawable.circle_blue_dark_layout));
                        break;
                    case Constants.THEME_COMMON:
                        newsViewHolder.circleView.setBackground(ContextCompat.getDrawable(adapterContext, R.drawable.circle_orange_layout));
                        break;
                    case Constants.THEME_EDUCATION:
                        newsViewHolder.circleView.setBackground(ContextCompat.getDrawable(adapterContext, R.drawable.circle_blue_layout));
                        break;
                    case Constants.THEME_INNOVATION:
                        newsViewHolder.circleView.setBackground(ContextCompat.getDrawable(adapterContext, R.drawable.circle_red_layout));
                        break;
                    case Constants.THEME_INTERNATIONAL:
                        newsViewHolder.circleView.setBackground(ContextCompat.getDrawable(adapterContext, R.drawable.circle_yellow_layout));
                        break;
                    case Constants.THEME_PRESS:
                        newsViewHolder.circleView.setBackground(ContextCompat.getDrawable(adapterContext, R.drawable.circle_grey_layout));
                        break;
                    case Constants.THEME_PUBLIC:
                        newsViewHolder.circleView.setBackground(ContextCompat.getDrawable(adapterContext, R.drawable.circle_green_layout));
                        break;
                    case Constants.THEME_SCINCE:
                        newsViewHolder.circleView.setBackground(ContextCompat.getDrawable(adapterContext, R.drawable.circle_blue_layout));
                        break;
                    case Constants.THEME_SHEDULE:
                        newsViewHolder.circleView.setBackground(ContextCompat.getDrawable(adapterContext, R.drawable.circle_violet_layout));
                        break;
                    case Constants.THEME_TECH:
                        newsViewHolder.circleView.setBackground(ContextCompat.getDrawable(adapterContext, R.drawable.circle_grey_layout));
                        break;
                    case Constants.THEME_STAFF:
                        newsViewHolder.circleView.setBackground(ContextCompat.getDrawable(adapterContext, R.drawable.circle_violet_layout));
                        break;
                    case Constants.THEME_STUDENTS:
                        newsViewHolder.circleView.setBackground(ContextCompat.getDrawable(adapterContext, R.drawable.circle_yellow_layout));
                        break;
                    case Constants.THEME_SPORT:
                        newsViewHolder.circleView.setBackground(ContextCompat.getDrawable(adapterContext, R.drawable.circle_orange_layout));
                        break;
                    case Constants.THEME_GRADUETES:
                        newsViewHolder.circleView.setBackground(ContextCompat.getDrawable(adapterContext, R.drawable.circle_red_layout));
                        break;
                    default:
                        newsViewHolder.circleView.setBackground(ContextCompat.getDrawable(adapterContext, R.drawable.circle_orange_layout));

                        break;
                }
                break;
            case PROGRESS:
                ProgressViewHolder vh2 = (ProgressViewHolder) holder;
                break;
            default:
                newsViewHolder = (NewsViewHolder) holder;
                newsViewHolder.newsItem = newsList.get(position);
                newsViewHolder.newsTheme.setText(newsList.get(position).getTheme());
                newsViewHolder.newsDate.setText(newsList.get(position).getPublishDate());
                newsViewHolder.themeChar.setText(newsList.get(position).getTheme().substring(0,1));
                newsViewHolder.newsContent.setText(newsList.get(position).getNewsTitle());
                break;

        }
    }

    // Clean all elements of the recycler
    public void clear() {
        newsList.clear();
        notifyDataSetChanged();
    }

    // Add a list of items
    public void addAll(ArrayList<NewsModel> list) {
        newsList.remove(newsList.size()-1);
        newsList.addAll(list);
        notifyDataSetChanged();
    }

    public void updateAll(ArrayList<NewsModel> list) {
        newsList.clear();
        newsList.addAll(list);
        notifyDataSetChanged();
    }

    public void removeItem(int pos){
        newsList.remove(pos);
        notifyDataSetChanged();
    }
    public void removeItemScroll(int pos){
        if(newsList.get(pos).getItemType() == NewsModel.PROGRESS) {
            newsList.remove(pos);
            notifyDataSetChanged();
        }
    }
    @Override
    public int getItemCount() {
        return newsList.size();
    }

}
