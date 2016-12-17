package istu.edu.irnitu;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
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
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.sufficientlysecure.htmltextview.HtmlHttpImageGetter;
import org.sufficientlysecure.htmltextview.HtmlTextView;

import istu.edu.irnitu.IOPackage.Constants;

public class NewsActivity extends AppCompatActivity {
    private final String LOG_TAG = "LOG_TAG_NEWS";

    private String title, theme, date, content, imagesIntent, headImage;
    private String[] imagesUrls;
    private TextView themeTV, dateTV, appbarHeaderTV, charNewsTV;
    private SliderLayout sliderShowView;
    private View circleView, progressView;
    private ImageView appbarIV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        title = getIntent().getStringExtra("title");
        theme = getIntent().getStringExtra("theme");
        date = getIntent().getStringExtra("date");
        content = getIntent().getStringExtra("content");
        imagesIntent = getIntent().getStringExtra("images");
        headImage = getIntent().getStringExtra("head_image");


        Log.d(LOG_TAG, "headImage " + headImage);
        Log.d(LOG_TAG, "imagesIntent " + imagesIntent);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_news);
        toolbar.setTitle(title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        progressView = findViewById(R.id.appbar_progress);
        CollapsingToolbarLayout mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        // mCollapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);
        // mCollapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);
        themeTV = (TextView) findViewById(R.id.news_themeTV);
        appbarIV = (ImageView) findViewById(R.id.appbar_image);
        dateTV = (TextView) findViewById(R.id.news_dateTV);
        appbarHeaderTV = (TextView) findViewById(R.id.appbar_header);
        charNewsTV = (TextView) findViewById(R.id.circle_view_char_news);
        themeTV.setText(theme);
        dateTV.setText(date);
        appbarHeaderTV.setText(title);
        charNewsTV.setText(theme.substring(0, 1));
        HtmlTextView newsContent = (HtmlTextView) findViewById(R.id.news_content);

// loads html from string and displays http://www.example.com/cat_pic.png from the Internet
        newsContent.setHtml(content,
                new HtmlHttpImageGetter(newsContent, Constants.URL_ISTU, true));
        sliderShowView = (SliderLayout) findViewById(R.id.images_slider);
        if (imagesIntent.length() > 0) {
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


        Picasso.with(NewsActivity.this)
                .load(headImage)
                .error(R.mipmap.ic_file_image)
                .into(appbarIV, new Callback() {
                    @Override
                    public void onSuccess() {
                        progressView.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError() {
                        progressView.setVisibility(View.GONE);
                    }
                });

        circleView = findViewById(R.id.circle_view_news);
        switch (theme) {
            case Constants.THEME_ABITUR:
                circleView.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.circle_green_light_layout));
                break;
            case Constants.THEME_ABOUT:
                circleView.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.circle_blue_dark_layout));
                break;
            case Constants.THEME_COMMON:
                circleView.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.circle_orange_layout));
                break;
            case Constants.THEME_EDUCATION:
                circleView.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.circle_blue_layout));
                break;
            case Constants.THEME_INNOVATION:
                circleView.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.circle_red_layout));
                break;
            case Constants.THEME_INTERNATIONAL:
                circleView.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.circle_yellow_layout));
                break;
            case Constants.THEME_PRESS:
                circleView.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.circle_grey_layout));
                break;
            case Constants.THEME_PUBLIC:
                circleView.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.circle_green_layout));
                break;
            case Constants.THEME_SCINCE:
                circleView.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.circle_blue_layout));
                break;
            case Constants.THEME_SHEDULE:
                circleView.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.circle_violet_layout));
                break;
            case Constants.THEME_TECH:
                circleView.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.circle_grey_layout));
                break;
            case Constants.THEME_STAFF:
                circleView.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.circle_violet_layout));
                break;
            case Constants.THEME_STUDENTS:
                circleView.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.circle_yellow_layout));
                break;
            case Constants.THEME_SPORT:
                circleView.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.circle_orange_layout));
                break;
            case Constants.THEME_GRADUETES:
                circleView.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.circle_red_layout));
                break;
            default:
                circleView.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.circle_orange_layout));

                break;
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

    @Override
    protected void onStop() {
        sliderShowView.stopAutoCycle();
        super.onStop();
    }
}
