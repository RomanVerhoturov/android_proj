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
import android.os.Handler;
import android.os.Message;
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

import com.github.pwittchen.infinitescroll.library.InfiniteScrollListener;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import istu.edu.irnitu.IOPackage.Constants;
import istu.edu.irnitu.IOPackage.DateHelper;
import istu.edu.irnitu.IOPackage.HttpURLConnector;
import istu.edu.irnitu.IOPackage.InternetController;
import istu.edu.irnitu.R;
import istu.edu.irnitu.adapters.NewsRVAdapter;
import istu.edu.irnitu.dataModels.NewsModel;
import istu.edu.irnitu.database.DBHelper;

/**
 * NewFitGid
 * Created by Александр on 15.11.2016.
 * Contact on luck.alex13@gmail.com
 * Copyright Aleksandr Novikov 2016
 */

public class NewsFragment extends AbstractTabFragment {
    private final String LOG_TAG = "LOG_TAG_NEWS";
    private static final int COLUMN_ID = 0;
    private static final int COLUMN_NEWS_TEXT = 1;
    private static final int COLUMN_NEWS_IMAGES = 2;
    private static final int COLUMN_NEWS_IMAGE_HEADER = 3;
    private static final int MAX_ITEMS_PER_REQUEST = 10;

    private RecyclerView recyclerView;
    private NewsRVAdapter rvAdapter;
    private View progressView, errorView;
    private TextView errorTV;
    private Button retryButton;
    private ArrayList<NewsModel> newsItems, localList;
    private SwipeRefreshLayout swipeContainer;
    private NewsAsyncTask asyncTask;
    private long cacheSize;
    private String updTime;
    private String[] existingNews;
    private LinearLayoutManager layoutManager;
    private String nextUrl;
    private boolean running;
    private InfiniteScrollListener scrollListener;
    private int currentPage;

    public static NewsFragment getInstance(Context context, String cookies) {
        Bundle args = new Bundle();
        NewsFragment fragment = new NewsFragment();
        fragment.setArguments(args);
        fragment.setActivityContext(context);
        fragment.setTitle(context.getString(R.string.tab_item_news));
        fragment.setCookiesLogin(cookies);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(LOG_TAG, "NewsFragment onCreateView()");
        view = inflater.inflate(R.layout.content_news_fragment, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerViewNews);
        progressView = view.findViewById(R.id.news_progress);//прогрессбар
        errorView = view.findViewById(R.id.errorViewNews);
        errorTV = (TextView) view.findViewById(R.id.news_errorTV);
        retryButton = (Button) view.findViewById(R.id.buttonRetryNews);
        retryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(LOG_TAG, "onClick() ");
                swipeContainer.setVisibility(View.VISIBLE);

                startTask();
            }
        });

        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainerNews);
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
        //nextUrl = Constants.URL_ISTU + "/news/?PAGEN_1=2#nav_start";
        running = false;
        DBHelper dbHelper = new DBHelper(getContext());
        dbHelper.dropTable(dbHelper.getWritableDatabase(), DBHelper.TABLE_NAME_NEWS);
        dbHelper.getWritableDatabase().execSQL(DBHelper.SQL_CREATE_TABLE_NEWS);
        dbHelper.close();
        startTask();

        return view;
    }

    private void initRecView(ArrayList<NewsModel> list) {
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        rvAdapter = new NewsRVAdapter(list, getContext());
        recyclerView.setAdapter(rvAdapter);
        scrollListener = createInfiniteScrollListener();
        recyclerView.addOnScrollListener(scrollListener);
    }

    private void startTask() {
        if (InternetController.hasConnection(getContext())) {
            Log.d(LOG_TAG, "ContestRegister hasConnection ");
            asyncTask = new NewsAsyncTask(Constants.URL_NEWS, NewsAsyncTask.TASK_WEB_FIRST);
            asyncTask.execute();
            currentPage = 1;
        } else {
            Log.d(LOG_TAG, "NoConnection");

            swipeContainer.setRefreshing(false);
            if (rvAdapter != null) {
                Toast.makeText(getContext(), R.string.error_nointernet, Toast.LENGTH_LONG).show();
            } else {
                //showErrorView(true, R.string.error_nointernet);
                asyncTask = new NewsAsyncTask(NewsAsyncTask.TASK_ONLY_CACHE);
                asyncTask.execute();
            }
        }

    }

    private void startTask(String nextUrl, int endPosition) {
        if (InternetController.hasConnection(getContext())) {
            Log.d(LOG_TAG, "ContestRegister hasConnection ");
            asyncTask = new NewsAsyncTask(nextUrl, endPosition, NewsAsyncTask.TASK_WEB_FOLLOWING);
            asyncTask.execute();

        } else {
            Log.d(LOG_TAG, "NoConnection");
            newsItems.remove(newsItems.size() - 1);
            Toast.makeText(getContext(), R.string.error_nointernet, Toast.LENGTH_LONG).show();
           /* swipeContainer.setRefreshing(false);
            if (rvAdapter != null) {
                Toast.makeText(getContext(), R.string.error_nointernet, Toast.LENGTH_LONG).show();
            } else {
                //showErrorView(true, R.string.error_nointernet);
                asyncTask = new NewsAsyncTask(NewsAsyncTask.TASK_ONLY_CACHE);
                asyncTask.execute();
            }*/
        }

    }

    private InfiniteScrollListener createInfiniteScrollListener() {
        return new InfiniteScrollListener(MAX_ITEMS_PER_REQUEST, layoutManager) {
            @Override
            public void onScrolledToEnd(final int onScrolledToEnd) {
                Log.d(LOG_TAG, "onScrolledToEnd " + onScrolledToEnd);
                if (running == false) {
                    running = true;

                    //localList = newsItems;
                    startTask(nextUrl, onScrolledToEnd);
                }
            }
        };
    }

    private class NewsAsyncTask extends AsyncTask<Void, Void, String> {
        private static final String TASK_WEB_FIRST = "TASK_WEB_FIRST";
        private static final String TASK_WEB_FOLLOWING = "TASK_WEB_FOLLOWING";
        private static final String TASK_ONLY_CACHE = "TASK_ONLY_CACHE";
        private String currentTask;
        private String urlLink;
        private int endPosition;

        public NewsAsyncTask(String url, String task) {
            super();
            this.currentTask = task;
            this.urlLink = url;
        }

        public NewsAsyncTask(String url, int endPosition, String task) {
            super();
            this.currentTask = task;
            this.endPosition = endPosition;
            this.urlLink = url;
        }

        public NewsAsyncTask(String task) {
            super();
            this.currentTask = task;
        }

        @Override
        protected void onPreExecute() {
            Log.d(LOG_TAG, "NewsAsyncTask onPreExecute " + currentTask);
            if (currentTask == TASK_WEB_FOLLOWING) {

            } else {
                if (!swipeContainer.isRefreshing()) {
                    swipeContainer.setRefreshing(true);
                }
            }

            //swipeContainer.setEnabled(false);
            showErrorView(false, 0);
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... params) {
            String response = null, jsonResponse;
            switch (currentTask) {
                case TASK_WEB_FIRST: {
                    response = webTask(urlLink);
                    //response = webTask(Constants.URL_ISTU + "/news/?PAGEN_1=17#nav_start");
                    currentPage++;
                    nextUrl = Constants.URL_ISTU + "/news/?PAGEN_1=" + currentPage + "#nav_start";
                    Log.d(LOG_TAG, "nextUrl " + nextUrl);
                    break;
                }
                case TASK_WEB_FOLLOWING: {
                    response = webTask(urlLink);
                    currentPage++;
                    nextUrl = Constants.URL_ISTU + "/news/?PAGEN_1=" + currentPage + "#nav_start";
                    Log.d(LOG_TAG, "nextUrl " + nextUrl);
                    //localList.addAll(newsItems);

                   /* try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        Log.e("MainActivity", e.getMessage());
                    }*/
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
            if (currentTask == TASK_WEB_FOLLOWING) {
               /* localList = new ArrayList<>();
                localList.add(new NewsModel("Тема",0,"20.2.18","Заголовок новости",""));*/
                running = false;
                if (newsItems != null) {
                    if (newsItems.size() > 0) {
                        rvAdapter.addAll(newsItems);
                        recyclerView.scrollToPosition(endPosition);

                    } else {
                        Log.d(LOG_TAG, "onPostExecute() newsItems.size() == 0");
                        showErrorView(true, R.string.error_nores);
                    }
                } else {
                    //localList.remove(localList.size() - 1);
                    rvAdapter.removeItemScroll(rvAdapter.getItemCount() - 1);
                    Log.d(LOG_TAG, "onPostExecute() newsItems.size() == 0");
                    showErrorView(true, R.string.error_nores);
                }
            } else if (newsItems != null) {
                if (newsItems.size() > 0) {
                    initRecView(newsItems);
                    currentPage++;
                    recyclerView.setVisibility(View.VISIBLE);
                } else {
                    Log.d(LOG_TAG, "onPostExecute() newsItems.size() == 0");
                    showErrorView(true, R.string.error_nores);
                }
                /*if (cacheSize > newsItems.size()) {
                    Log.d(LOG_TAG, "cacheSize " + cacheSize +
                            " > newsItems.size() " + newsItems.size());
                    runDeleteThread();
                }*/
            } else {
                Log.d(LOG_TAG, "onPostExecute() newsItems == null");
                showErrorView(true, R.string.error_nodata);
            }
            //swipeContainer.setEnabled(true);
            swipeContainer.setRefreshing(false);
            super.onPostExecute(response);
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
    }

    private void showProgress(final boolean show) {
        int animTime = getResources().getInteger(android.R.integer.config_mediumAnimTime);

       /* // mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        recyclerView.animate().setDuration(animTime).alpha(
                show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                recyclerView.setVisibility(show ? View.GONE : View.VISIBLE);
            }
        });*/

        //mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        progressView.animate().setDuration(animTime).alpha(
                show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                progressView.setVisibility(show ? View.VISIBLE : View.GONE);
            }
        });
    }

    private String webTask(String urlLink) {
        String jsonResponse, response, newsLink, newsText;
        Elements newsRows, links;
        int newsID;
        try {
            URL url = new URL(urlLink);
            HttpURLConnector urlConnector = new HttpURLConnector(url);
            response = urlConnector.simpleGetRequest();
            newsRows = urlConnector.getJsoupDocument().select("div.newslist-item");
            newsItems = new ArrayList<>();

            DBHelper dbHelper = new DBHelper(getContext());
            SQLiteDatabase database = dbHelper.getReadableDatabase();
            //dbHelper.onUpgrade(database,1,1);
            cacheSize = DatabaseUtils.queryNumEntries(database, DBHelper.TABLE_NAME_NEWS);
            if (cacheSize > newsRows.size()) {
                Log.d(LOG_TAG, "cacheSize " + cacheSize + " > newsRows.size() " + newsRows.size());
            }
            ContentValues contentValues = new ContentValues();
            updTime = DateHelper.getCurrentTime();
            for (int i = 0; i < newsRows.size(); i++) {

                String date = newsRows.get(i).getElementsByClass("news-date").first().text();
                String cat = newsRows.get(i).getElementsByClass("new-razdel-item").first().text();
                Element tagA = newsRows.get(i).getElementsByTag("a").first();
                newsLink = Constants.URL_ISTU + tagA.attr("href");

                NewsModel newsModel = new NewsModel(cat, NewsModel.COMMON, date,
                        tagA.text(), newsLink);
                Log.d(LOG_TAG, "newsRow " + newsModel.getNewsTitle());

                newsModel.setHeaderImageUrl(Constants.URL_ISTU + newsRows.get(i).getElementsByTag("img").first().attr("src"));
                Log.d(LOG_TAG, Constants.URL_ISTU + newsRows.get(i).getElementsByTag("img").first().attr("src"));

                if (!isNewsExistInDB(newsModel, database, DBHelper.TABLE_NAME_NEWS)) {
                    urlConnector = new HttpURLConnector(new URL(newsLink));
                    urlConnector.simpleGetRequest();
                    Elements content = urlConnector.getJsoupDocument().getElementsByClass("content");
                    Elements pTags = content.first().getElementsByTag("p");
                    Elements imgTags = content.first().getElementsByTag("img");
                    pTags.select("img").remove();
                    StringBuffer buffer = new StringBuffer();

                    if(imgTags.size()>0) {
                        float width, height;
                        for (Element imgTag : imgTags) {
                            buffer.append(Constants.URL_ISTU + imgTag.attr("src") + ";");
                        }
                        newsModel.setImagesUrls(buffer.toString());
                        if (newsModel.getHeaderImageUrl() == null) {
                            newsModel.setHeaderImageUrl(Constants.NULL);
                        }
                    } else {
                        newsModel.setImagesUrls(Constants.NULL);
                        newsModel.setHeaderImageUrl(Constants.NULL);
                    }
                    newsModel.setNewsText(pTags.toString());

                    contentValues.clear();
                    contentValues.put(DBHelper.COLUMN_PUBLISH_DATE, newsModel.getPublishDate());
                    contentValues.put(DBHelper.COLUMN_UPDATE_TIME, updTime);
                    contentValues.put(DBHelper.COLUMN_NEWS_THEME, newsModel.getTheme());
                    contentValues.put(DBHelper.COLUMN_NEWS_TITLE, newsModel.getNewsTitle());
                    contentValues.put(DBHelper.COLUMN_NEWS_TEXT, newsModel.getNewsText());
                    contentValues.put(DBHelper.COLUMN_NEWS_LINK, newsModel.getNewsLink());
                    contentValues.put(DBHelper.COLUMN_NEWS_IMAGES, newsModel.getImagesUrls());
                    contentValues.put(DBHelper.COLUMN_NEWS_IMAGE_HEADER, newsModel.getHeaderImageUrl());
                    database.insert(DBHelper.TABLE_NAME_NEWS, null, contentValues);
                    Log.d(LOG_TAG, "database.insert() " + newsModel.getPublishDate() + "; theme:"
                            + newsModel.getTheme() + "; title:" + newsModel.getNewsTitle() + "; newslink: "
                            + newsModel.getNewsLink() + "; \n" + newsModel.getImagesUrls() + "; " + "; \n" + newsModel.getHeaderImageUrl() + "; ");
                } /*else if (cacheSize > newsRows.size()) {
                    newsModel.setId(existingNews[this.COLUMN_ID]);
                    newsModel.setNewsText(existingNews[this.COLUMN_NEWS_TEXT]);
                    newsModel.setImagesUrls(existingNews[this.COLUMN_NEWS_IMAGES]);
                    contentValues.clear();
                    contentValues.put(DBHelper.COLUMN_PUBLISH_DATE, newsModel.getPublishDate());
                    contentValues.put(DBHelper.COLUMN_UPDATE_TIME, updTime);
                    contentValues.put(DBHelper.COLUMN_NEWS_THEME, newsModel.getTheme());
                    contentValues.put(DBHelper.COLUMN_NEWS_TITLE, newsModel.getNewsTitle());
                    contentValues.put(DBHelper.COLUMN_NEWS_TEXT, newsModel.getNewsText());
                    contentValues.put(DBHelper.COLUMN_NEWS_LINK, newsModel.getNewsLink());
                    contentValues.put(DBHelper.COLUMN_NEWS_IMAGES, newsModel.getImagesUrls());
                    Log.d(LOG_TAG, "newsModel.getNewsTitle()  " + newsModel.getNewsTitle());

                    Log.d(LOG_TAG, " database.update() " + contentValues);
                    database.update(DBHelper.TABLE_NAME_NEWS, contentValues,
                            DBHelper.COLUMN_ID + " = ?", new String[]{existingNews[this.COLUMN_ID]});
                }*/ else {
                    Log.d(LOG_TAG, "Row in DB exist " + newsModel.getPublishDate() + "; "
                            + newsModel.getTheme() + "; " + newsModel.getNewsTitle() + "; "
                            + newsModel.getNewsLink() + "; \n" + existingNews[this.COLUMN_NEWS_IMAGES] + "; " + "; \n" + existingNews[this.COLUMN_NEWS_IMAGE_HEADER] + "; ");
                    newsModel.setId(existingNews[this.COLUMN_ID]);
                    newsModel.setNewsText(existingNews[this.COLUMN_NEWS_TEXT]);
                    newsModel.setImagesUrls(existingNews[this.COLUMN_NEWS_IMAGES]);
                    newsModel.setHeaderImageUrl(existingNews[this.COLUMN_NEWS_IMAGE_HEADER]);

                    /*if (existingNews[this.COLUMN_RES_NAME].equals(newsModel.getResName()) &
                            existingNews[this.COLUMN_IMG_URL].equals(newsModel.getImageUrl()) &
                            existingNews[this.COLUMN_RES_URL].equals(newsModel.getResLink())) {
                        Log.d(LOG_TAG, "Row in DB equals " + newsModel.getResName() + " " + newsModel.getResLink());

                    } else {
                        Log.d(LOG_TAG, "database.update()" + existingNews[this.COLUMN_ID] + " " + newsModel.getResName());
                        contentValues.clear();
                        contentValues.put(DBHelper.COLUMN_PUBLISH_DATE, newsModel.getPublishDate());
                        contentValues.put(DBHelper.COLUMN_UPDATE_TIME, updTime);
                        contentValues.put(DBHelper.COLUMN_NEWS_THEME, newsModel.getTheme());
                        contentValues.put(DBHelper.COLUMN_NEWS_TITLE, newsModel.getNewsTitle());
                        contentValues.put(DBHelper.COLUMN_NEWS_TEXT, newsModel.getNewsText());
                        contentValues.put(DBHelper.COLUMN_NEWS_LINK, newsModel.getNewsLink());
                        contentValues.put(DBHelper.COLUMN_NEWS_IMAGES, newsModel.getImagesUrls());
                        database.update(DBHelper.TABLE_NAME_RESOURCES, contentValues,
                                "_id = ?", new String[]{existingNews[this.COLUMN_ID]});
                        Log.d(LOG_TAG, "database.update()  " + contentValues);
                    }*/
                }

                newsItems.add(newsModel);
            }
            newsItems.add(new NewsModel(NewsModel.PROGRESS));
            dbHelper.close();
            response = Constants.SUCCESS_PARSING;
        } catch (SQLiteException ex) {
            ex.printStackTrace();
            newsItems = null;
            response = "NewsAsyncTask " + ex;
        } catch (MalformedURLException e) {
            newsItems = null;
            response = "NewsAsyncTask " + e;
            // Log.d(LOG_TAG, response);
            e.printStackTrace();
        } catch (NullPointerException nEx) {
            newsItems = null;
            response = "NullPointerException " + nEx;
            nEx.printStackTrace();
        } catch (NumberFormatException nEx) {
            newsItems = null;
            response = "NumberFormatException " + nEx;
            nEx.printStackTrace();
        }
        return response;
    }

    private boolean isNewsExistInDB(NewsModel newsModel, SQLiteDatabase database, String tableName) {
        String[] columns = new String[]{DBHelper.COLUMN_ID, DBHelper.COLUMN_NEWS_TEXT, DBHelper.COLUMN_NEWS_IMAGES, DBHelper.COLUMN_NEWS_IMAGE_HEADER};
        String selection = DBHelper.COLUMN_PUBLISH_DATE + " = ? AND " + DBHelper.COLUMN_NEWS_THEME
                + " = ? AND " + DBHelper.COLUMN_NEWS_TITLE + " = ? AND " + DBHelper.COLUMN_NEWS_LINK + " = ? ";
        String[] selectionArgs = new String[]{newsModel.getPublishDate(), newsModel.getTheme(),
                newsModel.getNewsTitle(), newsModel.getNewsLink()};
        Cursor cursor;
        try {

            cursor = database.query(tableName, columns, selection, selectionArgs, null, null, null);
            if (cursor.moveToFirst()) {
                existingNews = new String[]{cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3)};
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

    private String readDB() {
        String response = null;
        try {
            DBHelper dbHelper = new DBHelper(getContext());
            Cursor cursor = dbHelper.getAllEntries(DBHelper.TABLE_NAME_NEWS);
            if (cursor.moveToFirst()) {
                newsItems = new ArrayList<>();
                do {
                    int indID = cursor.getColumnIndex(DBHelper.COLUMN_ID);
                    int indPubDate = cursor.getColumnIndex(DBHelper.COLUMN_PUBLISH_DATE);
                    int indTHeme = cursor.getColumnIndex(DBHelper.COLUMN_NEWS_THEME);
                    int indTitle = cursor.getColumnIndex(DBHelper.COLUMN_NEWS_TITLE);
                    int indText = cursor.getColumnIndex(DBHelper.COLUMN_NEWS_TEXT);
                    int indLink = cursor.getColumnIndex(DBHelper.COLUMN_NEWS_LINK);
                    int indImages = cursor.getColumnIndex(DBHelper.COLUMN_NEWS_IMAGES);
                    int indHeadImage = cursor.getColumnIndex(DBHelper.COLUMN_NEWS_IMAGE_HEADER);
                    //Log.d(LOG_TAG, getString(indID));

                    newsItems.add(new NewsModel(cursor.getString(indID),
                            NewsModel.COMMON,
                            cursor.getString(indTHeme),
                            cursor.getString(indHeadImage),
                            cursor.getString(indPubDate),
                            cursor.getString(indTitle),
                            cursor.getString(indText),
                            cursor.getString(indLink),
                            cursor.getString(indImages)));

                } while (cursor.moveToNext());
                response = Constants.SUCCESS_READ_DB;
            } else {
                newsItems = null;
                response = Constants.NO_DATA_IN_DB;
            }
        } catch (SQLiteException ex) {
            ex.printStackTrace();
            newsItems = null;
            response = "NewsAsyncTask " + ex;
        }

        return response;
    }

    private void runDeleteThread() {
        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message message) {
                Log.d(LOG_TAG, "runDeleteThread handleMessage()");

            }
        };
        final Thread thread = new Thread() {
            @Override
            public void run() {
                Log.d(LOG_TAG, "runDeleteThread run()");

                try {
                    DBHelper dbHelper = new DBHelper(getContext());
                    SQLiteDatabase database = dbHelper.getWritableDatabase();
                    String selection = DBHelper.COLUMN_UPDATE_TIME + " != ?";
                    String[] selectionArgs = new String[]{updTime};
                    Cursor cursor = database.query(DBHelper.TABLE_NAME_NEWS, null, selection, selectionArgs, null, null, null);
                    if (cursor.moveToFirst()) {
                        int col = database.delete(DBHelper.TABLE_NAME_NEWS, selection, selectionArgs);
                        Log.d(LOG_TAG, "was deleted rows " + col);
                    } else {
                        Log.d(LOG_TAG, "cursor.getCount() " + cursor.getCount());
                    }
                    // Log.d(LOG_TAG, "cursor.getCount() " + cursor.getCount());
                } catch (SQLiteException ex) {
                    ex.printStackTrace();
                    Log.d(LOG_TAG, "SQLiteException " + DBHelper.TABLE_NAME_NEWS);
                    Log.d(LOG_TAG, ex.getMessage());

                }
                final Message message = handler.obtainMessage(1, "");
                handler.sendMessage(message);
            }
        };
        thread.setPriority(3);
        thread.start();
    }
}
