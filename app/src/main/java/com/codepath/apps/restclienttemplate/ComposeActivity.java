package com.codepath.apps.restclienttemplate;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class ComposeActivity extends AppCompatActivity {

    private final int MAX_NUM_CHAR = 140;

    TwitterClient client;
    EditText userMessage;
    String message;
    Tweet tweet;
    TextView charCount;
    Boolean isReply;
    ImageButton homeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        client = TwitterApplication.getRestClient(this);
        setContentView(R.layout.activity_compose);

        isReply = getIntent().getBooleanExtra("isReply", false);

        userMessage = (EditText) findViewById(R.id.etCompose);
        charCount = (TextView) findViewById(R.id.tvCharCount);
        homeButton = (ImageButton) findViewById(R.id.ibHome);
        userMessage.addTextChangedListener(textEditorWatcher);
    }


    public void onSubmit(View v) {
        if (!isReply) {
            message = userMessage.getText().toString();
            client.sendTweet(message, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    try {
                        tweet = Tweet.fromJSON(response);
                        Intent i = new Intent();
                        //sending data back once it finishes
                        i.putExtra("Tweet", tweet);
                        setResult(RESULT_OK, i);
                        finish();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    throwable.printStackTrace();
                }

            });
        } else {
            message = userMessage.getText().toString();
            long userId = getIntent().getLongExtra("uid", 0);
            client.replyTweet(message, userId, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    try {
                        tweet = Tweet.fromJSON(response);
                        Intent i = new Intent();
                        //sending data back once it finishes
                        i.putExtra("Tweet", tweet);
                        setResult(RESULT_OK, i);
                        Toast.makeText(ComposeActivity.this, "Replied!", Toast.LENGTH_LONG).show();
                        finish();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    throwable.printStackTrace();
                    Toast.makeText(ComposeActivity.this, "Failed to pass message", Toast.LENGTH_LONG).show();
                }
            });
        }

    }

    private final TextWatcher textEditorWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            int numCharactersLeft = MAX_NUM_CHAR - s.length();
            charCount.setText(String.valueOf(numCharactersLeft));
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    public void onClickHome (View v) {
        Intent goHome = new Intent(ComposeActivity.this, TimelineActivity.class);
        startActivityForResult(goHome, 3);
    }

}
