package istu.edu.irnitu.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import istu.edu.irnitu.R;
import istu.edu.irnitu.dataModels.ResourceModel;


/**
 * NewFitGid
 * Created by Александр on 04.06.2016.
 * Contact on luck.alex13@gmail.com
 * © Aleksandr Novikov 2016
 */
public class ResRVAdapter extends RecyclerView.Adapter<ResRVAdapter.ResViewHolder> {
    private static final String LOG_TAG = "LOG_TAG_RES";
    private static final int DIALOG = 1;
    private static ArrayList<ResourceModel> resourceList;
    private String currentDate;
    private Context adapterContext;


    public ResRVAdapter(ArrayList<ResourceModel> data, Context context) {
        this.resourceList = data;
        this.adapterContext = context;
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        this.currentDate = sdf.format(cal.getTime());

    }

    public static class ResViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView resName;
        ImageView resIV;
        View progressView;
        String resLink;
        ViewGroup parent;

        public ResViewHolder(View itemView, ViewGroup viewGroup) {
            super(itemView);
            parent = viewGroup;
            resName = (TextView) itemView.findViewById(R.id.resource_name);
            resIV = (ImageView) itemView.findViewById(R.id.resource_image);
            progressView = itemView.findViewById(R.id.item_resource_progress);

            //Log.d(LOG_TAG, "ViewHolder Constructor");

            itemView.setOnClickListener(this);
            //itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onClick(View v) {
            //Toast.makeText(v.getContext(), resLink, Toast.LENGTH_SHORT).show();
            Intent intent= new Intent(Intent.ACTION_VIEW,Uri.parse(resLink));
            v.getContext().startActivity(intent);
        }

    }

    @Override
    public ResViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_resource, parent, false);
        //Log.d(LOG_TAG, "onCreateViewHolder ");
        return new ResViewHolder(view, parent);
    }

    @Override
    public void onViewRecycled(ResViewHolder holder) {
        //Log.d(LOG_TAG, "onViewRecycled before"+holder.eventName.getText().toString() +" "+holder.newsContent.getText().toString());
        super.onViewRecycled(holder);
    }

    @Override
    public void onBindViewHolder(final ResViewHolder holder, final int position) {
        holder.resName.setText(resourceList.get(position).getResName());
        holder.resLink = resourceList.get(position).getResLink();
        Picasso.Builder builder = new Picasso.Builder(holder.parent.getContext());
        builder.listener(new Picasso.Listener() {
            @Override
            public void onImageLoadFailed(Picasso picasso, Uri uri, Exception ex) {
                Log.d(LOG_TAG, " onImageLoadFailed " + ex);

                ex.printStackTrace();
            }
        });

        builder.build()
                .load(resourceList.get(position).getImageUrl())
                .error(R.drawable.ic_golos)
                .into(holder.resIV, new Callback() {
                    @Override
                    public void onSuccess() {
                        holder.progressView.setVisibility(View.GONE);
                        Log.d(LOG_TAG, " onSuccess");
                    }

                    @Override
                    public void onError() {
                        holder.progressView.setVisibility(View.GONE);
                        Log.d(LOG_TAG, " onSuccess");

                    }
                });

    }


    // Clean all elements of the recycler
    public void clear() {
        resourceList.clear();
        notifyDataSetChanged();
    }

    // Add a list of items
    public void addAll(ArrayList<ResourceModel> list) {
        resourceList.addAll(list);
        notifyDataSetChanged();
    }

    public void updateAll(ArrayList<ResourceModel> list) {
        resourceList.clear();
        resourceList.addAll(list);
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return resourceList.size();
    }


}
