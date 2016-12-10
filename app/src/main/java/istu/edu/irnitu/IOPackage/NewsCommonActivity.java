/*
 *  Aleksandr Novikov luck.alex@gmail.com Copyright (c) 2016.
 */

package istu.edu.irnitu.IOPackage;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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
        Log.d(LOG_TAG, "headImage " + headImage);
        Log.d(LOG_TAG, "imagesIntent " + imagesIntent);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_news_comm);
        toolbar.setTitle(title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        themeTV = (TextView) findViewById(R.id.news_themeTV);
        appbarIV = (ImageView) findViewById(R.id.appbar_image);
        dateTV = (TextView) findViewById(R.id.news_dateTV);
        appbarHeaderTV = (TextView) findViewById(R.id.appbar_header);
        charNewsTV = (TextView) findViewById(R.id.circle_view_char_news);
        themeTV.setText(theme);
        dateTV.setText(date);
        appbarHeaderTV.setText(title);
        charNewsTV.setText(theme.substring(0,1));
        HtmlTextView newsContent = (HtmlTextView) findViewById(R.id.news_content);
        newsContent.setHtml(content,
                new HtmlHttpImageGetter(newsContent, Constants.URL_ISTU, true));
        sliderShowView = (SliderLayout) findViewById(R.id.images_slider);

        progressView = findViewById(R.id.appbar_progress);

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
                sliderShowView.setDuration(3000);
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

}
