/*
 *  Aleksandr Novikov luck.alex@gmail.com Copyright (c) 2016.
 */

package istu.edu.irnitu;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.klinker.android.link_builder.Link;
import com.klinker.android.link_builder.LinkBuilder;

import static android.content.Intent.ACTION_SENDTO;
import static android.content.Intent.EXTRA_SUBJECT;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_about);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        TextView progTV = (TextView) findViewById(R.id.programmer);
        TextView managTV = (TextView) findViewById(R.id.manager);
        TextView consTV = (TextView) findViewById(R.id.consult);

// create the link builder object add the link rule
        LinkBuilder.on(progTV)
                .addLink(new Link("Александр Новиков")
                        .setTextColor(ContextCompat.getColor(this,R.color.colorAccent))                  // optional, defaults to holo blue;
                        .setTextColorOfHighlightedLink(ContextCompat.getColor(this,R.color.colorAccent)) // optional, defaults to holo blue
                        .setHighlightAlpha(.3f)                                     // optional, defaults to .15f
                        .setOnClickListener(new Link.OnClickListener() {
                            @Override
                            public void onClick(String clickedText) {
                                sendMail(getString(R.string.proger_mail));
                            }
                        }))
                .build();
        LinkBuilder.on(managTV)
                .addLink(new Link("Алексей Говорков")
                        .setTextColor(ContextCompat.getColor(this,R.color.colorAccent))                  // optional, defaults to holo blue;
                        .setTextColorOfHighlightedLink(ContextCompat.getColor(this,R.color.colorAccent)) // optional, defaults to holo blue
                        .setHighlightAlpha(.3f)                                     // optional, defaults to .15f
                        .setOnClickListener(new Link.OnClickListener() {
                            @Override
                            public void onClick(String clickedText) {
                                sendMail(getString(R.string.manager_mail));
                            }
                        }))
                .build();
        LinkBuilder.on(consTV)
                .addLink(new Link("Антон Жиляев")
                        .setTextColor(ContextCompat.getColor(this,R.color.colorAccent))                  // optional, defaults to holo blue;
                        .setTextColorOfHighlightedLink(ContextCompat.getColor(this,R.color.colorAccent)) // optional, defaults to holo blue
                        .setHighlightAlpha(.3f)                                     // optional, defaults to .15f
                        .setOnClickListener(new Link.OnClickListener() {
                            @Override
                            public void onClick(String clickedText) {
                                sendMail(getString(R.string.consult_mail));
                            }
                        }))
                .build();

    }

    private void sendMail(String email){

        Intent emailIntent = new Intent(ACTION_SENDTO);
        emailIntent.setType("message/rfc822");
        emailIntent.setData(Uri.parse("mailto:" + email));
        emailIntent.putExtra(EXTRA_SUBJECT, getString(R.string.mail_subject));
        startActivity(Intent.createChooser(emailIntent, getString(R.string.send_mail_via)));
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
