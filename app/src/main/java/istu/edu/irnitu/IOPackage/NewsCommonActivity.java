/*
 *  Aleksandr Novikov luck.alex@gmail.com Copyright (c) 2016.
 */

package istu.edu.irnitu.IOPackage;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;

import org.sufficientlysecure.htmltextview.HtmlHttpImageGetter;
import org.sufficientlysecure.htmltextview.HtmlTextView;

import istu.edu.irnitu.R;

public class NewsCommonActivity extends AppCompatActivity {
    private final String LOG_TAG = "LOG_TAG_NEWS";

    private String title, theme,date,content, imagesIntent,headImage;
    private String[] imagesUrls;
    private TextView themeTV,dateTV,appbarHeaderTV,charNewsTV;
    private SliderLayout sliderShowView;
    private View circleView,progressView;
    private ImageView appbarIV;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_common);
        title = getIntent().getStringExtra("title");
        theme = getIntent().getStringExtra("theme");
        date = getIntent().getStringExtra("date");
        content = getIntent().getStringExtra("content");
        imagesIntent = getIntent().getStringExtra("images");
        Log.d(LOG_TAG, "news_content " + content);
        Log.d(LOG_TAG, "imagesIntent " + imagesIntent);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_news_comm);
        toolbar.setTitle(title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        themeTV = (TextView) findViewById(R.id.news_themeTV_common);
        dateTV = (TextView) findViewById(R.id.news_dateTV_common);
        //appbarHeaderTV = (TextView) findViewById(R.id.appbar_header);
        charNewsTV = (TextView) findViewById(R.id.circle_view_char_news_common);
        themeTV.setText(theme);
        dateTV.setText(date);
        //appbarHeaderTV.setText(title);
        charNewsTV.setText(theme.substring(0,1));
        circleView = findViewById(R.id.circle_view_news_common);
        switch (theme) {
            case Constants.THEME_ABITUR:
                circleView.setBackground(ContextCompat.getDrawable(NewsCommonActivity.this, R.drawable.circle_green_light_layout));
                break;
            case Constants.THEME_ABOUT:
                circleView.setBackground(ContextCompat.getDrawable(NewsCommonActivity.this, R.drawable.circle_blue_dark_layout));
                break;
            case Constants.THEME_COMMON:
                circleView.setBackground(ContextCompat.getDrawable(NewsCommonActivity.this, R.drawable.circle_orange_layout));
                break;
            case Constants.THEME_EDUCATION:
                circleView.setBackground(ContextCompat.getDrawable(NewsCommonActivity.this, R.drawable.circle_blue_layout));
                break;
            case Constants.THEME_INNOVATION:
                circleView.setBackground(ContextCompat.getDrawable(NewsCommonActivity.this, R.drawable.circle_red_layout));
                break;
            case Constants.THEME_INTERNATIONAL:
                circleView.setBackground(ContextCompat.getDrawable(NewsCommonActivity.this, R.drawable.circle_yellow_layout));
                break;
            case Constants.THEME_PRESS:
                circleView.setBackground(ContextCompat.getDrawable(NewsCommonActivity.this, R.drawable.circle_grey_layout));
                break;
            case Constants.THEME_PUBLIC:
                circleView.setBackground(ContextCompat.getDrawable(NewsCommonActivity.this, R.drawable.circle_green_layout));
                break;
            case Constants.THEME_SCINCE:
                circleView.setBackground(ContextCompat.getDrawable(NewsCommonActivity.this, R.drawable.circle_blue_layout));
                break;
            case Constants.THEME_SHEDULE:
                circleView.setBackground(ContextCompat.getDrawable(NewsCommonActivity.this, R.drawable.circle_violet_layout));
                break;
            case Constants.THEME_TECH:
                circleView.setBackground(ContextCompat.getDrawable(NewsCommonActivity.this, R.drawable.circle_grey_layout));
                break;
            case Constants.THEME_STAFF:
                circleView.setBackground(ContextCompat.getDrawable(NewsCommonActivity.this, R.drawable.circle_violet_layout));
                break;
            case Constants.THEME_STUDENTS:
                circleView.setBackground(ContextCompat.getDrawable(NewsCommonActivity.this, R.drawable.circle_yellow_layout));
                break;
            case Constants.THEME_SPORT:
                circleView.setBackground(ContextCompat.getDrawable(NewsCommonActivity.this, R.drawable.circle_orange_layout));
                break;
            case Constants.THEME_GRADUETES:
                circleView.setBackground(ContextCompat.getDrawable(NewsCommonActivity.this, R.drawable.circle_red_layout));
                break;
            default:
                circleView.setBackground(ContextCompat.getDrawable(NewsCommonActivity.this, R.drawable.circle_orange_layout));

                break;
        }
        HtmlTextView newsContent = (HtmlTextView) findViewById(R.id.news_content_common);
        newsContent.setHtml(content);
        sliderShowView = (SliderLayout) findViewById(R.id.images_slider_common);

        progressView = findViewById(R.id.appbar_progress);

        if (!imagesIntent.contains(Constants.NULL)) {
            imagesUrls = imagesIntent.split(";");
            if(imagesUrls.length != 1) {
                for (int i = 0; i < imagesUrls.length; i++) {
                    DefaultSliderView defaultSliderView = new DefaultSliderView(this);
                    defaultSliderView
                            .description(String.valueOf(i))
                            .setScaleType(BaseSliderView.ScaleType.FitCenterCrop)
                            .image(imagesUrls[i]);

                    sliderShowView.addSlider(defaultSliderView);
                }
                sliderShowView.setPresetTransformer(SliderLayout.Transformer.DepthPage);
                //sliderShowView.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
                sliderShowView.setCustomIndicator((PagerIndicator) findViewById(R.id.custom_indicator));
                sliderShowView.setDuration(4000);
            }else {
                DefaultSliderView defaultSliderView = new DefaultSliderView(this);
                defaultSliderView
                        .description(String.valueOf(0))
                        .setScaleType(BaseSliderView.ScaleType.FitCenterCrop)
                        .image(imagesUrls[0]);
                sliderShowView.addSlider(defaultSliderView);
                sliderShowView.stopAutoCycle();
            }
        } else {
            sliderShowView.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                // this takes the user 'back', as if they pressed the left-facing triangle icon on the main android toolbar.
                // if this doesn't work as desired, another possibility is to call `finish()` here.
                onBackPressed();
                break;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

}
