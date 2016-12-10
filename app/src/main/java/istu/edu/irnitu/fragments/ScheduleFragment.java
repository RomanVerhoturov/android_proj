/*
 *  Aleksandr Novikov luck.alex@gmail.com Copyright (c) 2016.
 */

package istu.edu.irnitu.fragments;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;

import istu.edu.irnitu.IOPackage.Constants;
import istu.edu.irnitu.IOPackage.InternetController;
import istu.edu.irnitu.R;



public class ScheduleFragment extends AbstractTabFragment {
    private final String LOG_TAG = "LOG_TAG_WEBVIEW";
    private View progressView, errorView;
    private TextView errorTV;
    private SwipeRefreshLayout swipeContainer;
    private WebView mWebView;
    private String currentUrl;
    private Button retryButton;
    private FloatingActionButton fabBack, fabUpdate;


    public static ScheduleFragment getInstance(Context context) {
        Bundle args = new Bundle();
        ScheduleFragment fragment = new ScheduleFragment();
        fragment.setArguments(args);
        fragment.setActivityContext(context);
        fragment.setTitle(context.getString(R.string.tab_item_schedule));
        return fragment;
    }

    @SuppressLint("JavascriptInterface")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(LOG_TAG, "ResourceFragment onCreateView()");
        view = inflater.inflate(R.layout.shedule_webview, container, false);
        mWebView = (WebView) view.findViewById(R.id.webViewSchedule);
        progressView = view.findViewById(R.id.progressSchedule);//прогрессбар
        errorView = view.findViewById(R.id.errorViewSchedule);
        errorTV = (TextView) view.findViewById(R.id.schedule_errorTV);
        retryButton = (Button) view.findViewById(R.id.buttonRetrySchedule);
        retryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadUrl(currentUrl);
            }
        });
        fabBack = (FloatingActionButton) view.findViewById(R.id.fab_back);
        fabBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mWebView.canGoBack()) {
                    mWebView.goBack();
                } else {

                }
            }
        });
        fabUpdate = (FloatingActionButton) view.findViewById(R.id.fab_update);
        fabUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadUrl(currentUrl);
            }
        });
        /*swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainerSchedule);
        swipeContainer.setColorSchemeResources(R.color.colorButtonDisabled);

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadUrl(currentUrl);
            }
        });*/
        //масштабирование карты
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setAllowUniversalAccessFromFileURLs(true);
        // add javascript interface
        mWebView.addJavascriptInterface(new AjaxHandler(getContext()), "ajaxHandler");
        mWebView.setWebViewClient(new MyWebViewClient());

        // HTML5 API flags
        mWebView.getSettings().setAppCacheEnabled(true);
        mWebView.getSettings().setDatabaseEnabled(true);
        mWebView.getSettings().setDomStorageEnabled(true);
        currentUrl = Constants.URL_SCHEDULE;
        loadUrl(currentUrl);
        return view;
    }

    // Inject CSS method: read style.css from assets folder
    // Append stylesheet to document head
    private void injectCSS() {
        try {
            InputStream inputStream = getContext().getAssets().open("style.css");
            byte[] buffer = new byte[inputStream.available()];
            inputStream.read(buffer);
            inputStream.close();
            String encoded = Base64.encodeToString(buffer, Base64.NO_WRAP);
            mWebView.loadUrl("javascript:(function() {" +
                    "var parent = document.getElementsByTagName('head').item(0);" +
                    "var style = document.createElement('style');" +
                    "style.type = 'text/css';" +
                    // Tell the browser to BASE64-decode the string into your script !!!
                    "style.innerHTML = window.atob('" + encoded + "');" +
                    "parent.appendChild(style)" +
                    "})()");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showErrorView(final boolean show, int textError) {
        Log.d(LOG_TAG, "showErrorView()");
        int shortAnimTime = getResources().getInteger(android.R.integer.config_mediumAnimTime);
        if (show) {
            if (mWebView.getVisibility() == View.VISIBLE) {
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
                mWebView.setVisibility(View.GONE);
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

    public void goBrouserBack(){
        if (mWebView.canGoBack()) {
            mWebView.goBack();
        }
    }

    public WebView getmWebView() {
        return mWebView;
    }

    private void loadUrl(String url){
        if (InternetController.hasConnection(view.getContext())) {
            Log.d(LOG_TAG, "Has Connection");
            showErrorView(false,0);
            mWebView.loadUrl(url);
        }else {
            Log.d(LOG_TAG, "NoConnection");
            showErrorView(true,R.string.error_nointernet);
            swipeContainer.setRefreshing(false);
        }
    }

    //переопределим класс WebViewClient и позволим нашему приложению обрабатывать ссылки
    private class MyWebViewClient extends WebViewClient {

//        @Override
//        public boolean shouldOverrideUrlLoading(WebView view, String url) {
//            view.loadUrl(url);
//            return true;
//        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            // TODO Auto-generated method stub

                showProgress(true);
                currentUrl = url;
                super.onPageStarted(view, url, favicon);


        }

        @Override
        public void onPageFinished(WebView view, String url) {
            // TODO Auto-generated method stub
            injectCSS();
            showProgress(false);
            Log.d(LOG_TAG, " onPageFinished() " + url);

        }
    }

    private void showProgress(final boolean show) {
        int shortAnimTime = getResources().getInteger(android.R.integer.config_mediumAnimTime);

        // mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        mWebView.animate().setDuration(shortAnimTime).alpha(
                show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mWebView.setVisibility(show ? View.GONE : View.VISIBLE);
            }
        });

        progressView.animate().setDuration(shortAnimTime).alpha(
                show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                progressView.setVisibility(show ? View.VISIBLE : View.GONE);
            }
        });

    }

    // our JavascriptInterface
    private class AjaxHandler {
        private final Context context;

        public AjaxHandler(Context context) {
            this.context = context;
        }

        public void ajaxBegin() {
            Log.d(LOG_TAG, "AJAX Begin");
            Toast.makeText(context, "AJAX Begin", Toast.LENGTH_SHORT).show();
        }

        public void ajaxDone() {
            Log.d(LOG_TAG, "AJAX Done");
            Toast.makeText(context, "AJAX Done", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDestroy() {
        if (mWebView != null)
            mWebView.destroy();
        super.onDestroy();
    }


}
