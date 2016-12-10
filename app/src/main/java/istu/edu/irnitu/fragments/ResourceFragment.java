package istu.edu.irnitu.fragments;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import istu.edu.irnitu.IOPackage.Constants;
import istu.edu.irnitu.IOPackage.DateHelper;
import istu.edu.irnitu.IOPackage.HttpURLConnector;
import istu.edu.irnitu.IOPackage.InternetController;
import istu.edu.irnitu.R;
import istu.edu.irnitu.adapters.ResRVAdapter;
import istu.edu.irnitu.dataModels.ResourceModel;
import istu.edu.irnitu.database.DBHelper;

/**
 * NewFitGid
 * Created by Александр on 15.11.2016.
 * Contact on luck.alex13@gmail.com
 * Copyright Aleksandr Novikov 2016
 */

public class ResourceFragment extends AbstractTabFragment {
    private final String LOG_TAG = "LOG_TAG_RES";

    private static final int COLUMN_ID = 0;
    private static final int COLUMN_RES_NAME = 1;
    private static final int COLUMN_IMG_URL = 2;
    private static final int COLUMN_RES_URL = 3;
    private RecyclerView recyclerView;
    private View mProgressView;
    private View errorView;
    private TextView errorTV;
    private Button retryButton;
    private ResRVAdapter rvAdapter;
    private ArrayList<ResourceModel> resourceItems;
    private SwipeRefreshLayout swipeContainer;
    private ResourcesAsyncTask asyncTask;
    private long cacheSize;
    private String updTime;
    private String[] existingResource;

    public static ResourceFragment getInstance(Context context, String cookies) {
        Bundle args = new Bundle();
        ResourceFragment fragment = new ResourceFragment();
        fragment.setArguments(args);
        fragment.setActivityContext(context);
        fragment.setTitle(context.getString(R.string.tab_item_resources));
        fragment.setCookiesLogin(cookies);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(LOG_TAG, "ResourceFragment onCreateView()");
        view = inflater.inflate(R.layout.content_resources, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerViewResrc);
        mProgressView = view.findViewById(R.id.resrc_progress);//прогрессбар
        errorView = view.findViewById(R.id.errorViewResrc);
        errorTV = (TextView) view.findViewById(R.id.resrc_errorTV);
        retryButton = (Button) view.findViewById(R.id.buttonRetryResrc);
        retryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(LOG_TAG, "onClick() ");
                swipeContainer.setVisibility(View.VISIBLE);

                startTask();
            }
        });

        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainerResrc);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (asyncTask == null) {
                    startTask();
                } else if (asyncTask.getStatus() == AsyncTask.Status.FINISHED) {
                    startTask();
                }

            }
        });
        swipeContainer.setColorSchemeResources(R.color.colorButtonDisabled);


        startTask();
        return view;
    }

    private void initRecView(ArrayList<ResourceModel> list) {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        rvAdapter = new ResRVAdapter(list, getContext());
        recyclerView.setAdapter(rvAdapter);
    }

    private void startTask() {
        if (InternetController.hasConnection(getContext())) {
            Log.d(LOG_TAG, "ContestRegister hasConnection ");
            asyncTask = new ResourcesAsyncTask(Constants.URL_RESOURCES, ResourcesAsyncTask.TASK_WEB);
            asyncTask.execute();

        } else {
            Log.d(LOG_TAG, "NoConnection");

            swipeContainer.setRefreshing(false);
            if (rvAdapter == null) {
                asyncTask = new ResourcesAsyncTask(ResourcesAsyncTask.TASK_ONLY_CACHE);
                asyncTask.execute();
            } else if(rvAdapter.getItemCount() > 0){
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                recyclerView.setAdapter(rvAdapter);
                Toast.makeText(getContext(), R.string.error_nointernet, Toast.LENGTH_LONG).show();
                //Log.d(LOG_TAG, "rvAdapter.getItemCount() " + rvAdapter.getItemCount());
            }
        }

    }

    private class ResourcesAsyncTask extends AsyncTask<Void, Void, String> {
        private static final int TASK_WEB = 0;
        private static final int TASK_ONLY_CACHE = 1;
        int currentTask;
        String urlLink;

        public ResourcesAsyncTask(String url, int task) {
            super();
            this.currentTask = task;
            this.urlLink = url;
        }

        public ResourcesAsyncTask(int task) {
            super();
            this.currentTask = task;
        }

        @Override
        protected void onPreExecute() {
            Log.d(LOG_TAG, "NewsAsyncTask onPreExecute ");
            if (!swipeContainer.isRefreshing()) {
                swipeContainer.setRefreshing(true);
            }
            //swipeContainer.setEnabled(false);
            showErrorView(false, 0);
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... params) {
            String response = null, jsonResponse;
            switch (currentTask) {
                case TASK_WEB: {
                    response = webTask(urlLink);

                    break;
                }
                case TASK_ONLY_CACHE: {
                    response = readDB();
                    break;
                }
            }


            return response;
        }

        @Override
        protected void onPostExecute(String response) {
            Log.d(LOG_TAG, "onPostExecute() " + response);
            if (resourceItems != null) {
                if (resourceItems.size() > 0) {
                    initRecView(resourceItems);
                    recyclerView.setVisibility(View.VISIBLE);

                } else {
                    Log.d(LOG_TAG, "onPostExecute() resourceItems.size() == 0");
                    showErrorView(true, R.string.error_nores);
                }
            } else {
                Log.d(LOG_TAG, "onPostExecute() resourceItems == null");
                showErrorView(true, R.string.error_nodata);
            }
            //swipeContainer.setEnabled(true);
            swipeContainer.setRefreshing(false);
            super.onPostExecute(response);
        }
    }

    private String readDB() {
        String response = null;
        try {
            DBHelper dbHelper = new DBHelper(getContext());
            Cursor cursor = dbHelper.getAllEntries(DBHelper.TABLE_NAME_RESOURCES);
            if (cursor.moveToFirst()) {
                resourceItems = new ArrayList<>();
                do {
                    int indName = cursor.getColumnIndex(DBHelper.COLUMN_RES_NAME);
                    int indResLink = cursor.getColumnIndex(DBHelper.COLUMN_RES_URL);
                    int indImgLink = cursor.getColumnIndex(DBHelper.COLUMN_IMG_URL);
                    resourceItems.add(new ResourceModel(cursor.getString(indName),
                            cursor.getString(indImgLink), cursor.getString(indResLink)));

                } while (cursor.moveToNext());
                response = Constants.SUCCESS_READ_DB;
            } else {
                resourceItems = null;
                response = Constants.NO_DATA_IN_DB;
            }
        } catch (SQLiteException ex) {
            ex.printStackTrace();
            resourceItems = null;
            response = "NewsAsyncTask " + ex;
        }

        return response;
    }

    private String webTask(String urlLink) {
        String jsonResponse, response;
        try {
            URL url = new URL(urlLink);
            HttpURLConnector urlConnector = new HttpURLConnector(url);
            jsonResponse = urlConnector.simpleGetRequest();
            JSONObject dataJsonObj = new JSONObject(jsonResponse);
            JSONArray resourcesJSONArr = dataJsonObj.getJSONArray(Constants.JSON_FIELD_RESOURCES);
            resourceItems = new ArrayList<>();

            DBHelper dbHelper = new DBHelper(getContext());
            SQLiteDatabase database = dbHelper.getWritableDatabase();
            cacheSize = DatabaseUtils.queryNumEntries(database, DBHelper.TABLE_NAME_RESOURCES);
            if (cacheSize > resourcesJSONArr.length()) {
                Log.d(LOG_TAG, "cacheSize " + cacheSize + " > JSONArr.length() " + resourcesJSONArr.length());
            }
            ContentValues contentValues = new ContentValues();
            updTime = DateHelper.getCurrentTime();
            for (int i = 0; i < resourcesJSONArr.length(); i++) {
                JSONObject resourceJSON = resourcesJSONArr.getJSONObject(i);
                ResourceModel resource = new ResourceModel(resourceJSON.getString("updTimeDate"),
                        resourceJSON.getString("resName"), resourceJSON.getString("imageUrl"), resourceJSON.getString("resLink"));
                //Log.d(LOG_TAG, "JSONObject " + resourceJSON);
                contentValues.clear();
                contentValues.put(DBHelper.COLUMN_UPDATE_TIME_DATE, resource.getUpdTimeDate());
                contentValues.put(DBHelper.COLUMN_UPDATE_TIME, updTime);
                contentValues.put(DBHelper.COLUMN_RES_NAME, resource.getResName());
                contentValues.put(DBHelper.COLUMN_IMG_URL, resource.getImageUrl());
                contentValues.put(DBHelper.COLUMN_RES_URL, resource.getResLink());
                if (!isResourceExistInDB(resource, database, DBHelper.TABLE_NAME_RESOURCES)) {
                    database.insert(DBHelper.TABLE_NAME_RESOURCES, null, contentValues);
                    Log.d(LOG_TAG, "database.insert()  " + contentValues);
                } else if (cacheSize > resourcesJSONArr.length()) {
                    Log.d(LOG_TAG, " database.update() " + contentValues);
                    database.update(DBHelper.TABLE_NAME_NEWS, contentValues,
                            DBHelper.COLUMN_ID + " = ?", new String[]{existingResource[this.COLUMN_ID]});
                }else {
                    if (existingResource[this.COLUMN_RES_NAME].equals(resource.getResName()) &
                            existingResource[this.COLUMN_IMG_URL].equals(resource.getImageUrl()) &
                            existingResource[this.COLUMN_RES_URL].equals(resource.getResLink())) {
                        Log.d(LOG_TAG, "Row in DB equals " + resource.getResName() + " " + resource.getResLink());

                    } else {
                        Log.d(LOG_TAG, "database.update()" + existingResource[this.COLUMN_ID] + " " + resource.getResName());
                        database.update(DBHelper.TABLE_NAME_RESOURCES, contentValues,
                                "_id = ?", new String[]{existingResource[this.COLUMN_ID]});
                        Log.d(LOG_TAG, "database.update()  " + contentValues);
                    }
                }
                resourceItems.add(resource);


            }
            dbHelper.close();
            response = Constants.SUCCESS_PARSING_JSON;
        } catch (SQLiteException ex) {
            ex.printStackTrace();
            resourceItems = null;
            response = "NewsAsyncTask " + ex;
        } catch (MalformedURLException e) {
            resourceItems = null;
            response = "NewsAsyncTask " + e;
            // Log.d(LOG_TAG, response);
            e.printStackTrace();
        } catch (JSONException e) {
            resourceItems = null;
            response = "JSONException " + e;
            e.printStackTrace();
            //Log.d(LOG_TAG, response);
        } catch (NullPointerException nEx) {
            resourceItems = null;
            response = "NullPointerException " + nEx;
            nEx.printStackTrace();
        }
        return response;
    }

    private boolean isResourceExistInDB(ResourceModel resource, SQLiteDatabase database, String tableName) {
        String[] columns = new String[]{DBHelper.COLUMN_ID, DBHelper.COLUMN_RES_NAME, DBHelper.COLUMN_IMG_URL, DBHelper.COLUMN_RES_URL};
        String selection = DBHelper.COLUMN_RES_NAME + " = ? AND " + DBHelper.COLUMN_IMG_URL + " = ? AND " + DBHelper.COLUMN_RES_URL + " = ? ";
        String[] selectionArgs = new String[]{resource.getResName(), resource.getImageUrl(), resource.getResLink()};
        Cursor cursor;
        try {

            cursor = database.query(tableName, columns, selection, selectionArgs, null, null, null);
            if (cursor.moveToFirst()) {
                existingResource = new String[]{cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3)};
                cursor.close();
                return true;
            } else {
                cursor.close();
                return false;
            }
        } catch (SQLiteException ex) {
            ex.printStackTrace();
            Log.d(LOG_TAG, "SQLiteException " + tableName);
            Log.d(LOG_TAG, ex.toString());
            return false;
        }
    }

    private void showErrorView(final boolean show, int textError) {
        Log.d(LOG_TAG, "showErrorView()");
        int shortAnimTime = getResources().getInteger(android.R.integer.config_mediumAnimTime);

        if (show) {

            if (rvAdapter != null) {
                Toast.makeText(getContext(), textError, Toast.LENGTH_LONG).show();
            } else {
                errorTV.setText(textError);
                errorView.animate().setDuration(shortAnimTime).alpha(
                        show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        errorView.setVisibility(show ? View.VISIBLE : View.GONE);
                    }
                });
                recyclerView.setVisibility(View.GONE);

            }

        } else {
            errorView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    errorView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        }
        //errorView.setVisibility(show ? View.VISIBLE : View.GONE);


    }

    @Override
    public void onStop() {
        Log.d(LOG_TAG, "onStop()");
        super.onStop();
    }

    @Override
    public void onDestroy() {
        Log.d(LOG_TAG, "onDestroy()");
        super.onDestroy();
    }

    @Override
    public void onStart() {
        Log.d(LOG_TAG, "onStart()");
        super.onStart();
    }

    @Override
    public void onResume() {
        Log.d(LOG_TAG, "onResume()");
        super.onResume();
    }
}
