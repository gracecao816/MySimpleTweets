package com.codepath.apps.restclienttemplate;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.models.Tweet;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class DetailActivity extends AppCompatActivity {
    //list out the attributes
    ImageView ivProfileImage;
    TextView tvUserName;
    TextView tvName;
    TextView tvTime;
    TextView tvBody;
    Tweet tweet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ivProfileImage = (ImageView) findViewById(R.id.ivProfileImage);
        tvUserName = (TextView) findViewById(R.id.tvUserName);
        tvName = (TextView) findViewById(R.id.tvName);
        tvTime = (TextView) findViewById(R.id.tvTime);
        tvBody = (TextView) findViewById(R.id.tvBody);
        //sending data back once it finishes

        tweet = (Tweet) getIntent().getSerializableExtra("tweet");

        tvUserName.setText(tweet.user.name);
        tvName.setText(tweet.user.screenName);
        tvTime.setText(tweet.time);
        tvBody.setText(tweet.body);

        //load image using glide
        Glide.with(DetailActivity.this).load(tweet.user.profileImageUrl)
                .into(ivProfileImage);

    }

    public static String getRelativeTimeAgo(String rawJsonDate) {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        String relativeDate = "";
        try {
            long dateMillis = sf.parse(rawJsonDate).getTime();
            relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return relativeDate;
    }

    public void onClickHome (View v) {
        Intent goHome = new Intent(DetailActivity.this, TimelineActivity.class);
        startActivityForResult(goHome, 3);
    }

}
