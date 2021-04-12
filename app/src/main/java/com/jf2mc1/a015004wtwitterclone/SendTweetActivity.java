package com.jf2mc1.a015004wtwitterclone;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class SendTweetActivity extends AppCompatActivity {

    private EditText etSendTweet;
    private Button btnSendTweet;
    private Button btnShowOtherTweets;
    private ListView listViewOthersTweets;
    private ArrayAdapter simpleAdapter;
    private ArrayList<String> listOfOthersTweets;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_tweet);
        setTitle(ParseUser.getCurrentUser().getUsername() + "'s Tweets");

        etSendTweet = findViewById(R.id.et_send_tweet);
        btnSendTweet = findViewById(R.id.btn_send_tweet);
        btnShowOtherTweets = findViewById(R.id.btn_show_other_tweets);
        listViewOthersTweets = findViewById(R.id.listview_others_tweets);

        listOfOthersTweets = new ArrayList<>();
        simpleAdapter = new ArrayAdapter(SendTweetActivity.this,
                android.R.layout.simple_list_item_1, listOfOthersTweets);
        listViewOthersTweets.setAdapter(simpleAdapter);

        btnSendTweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendTweet();

            }
        });

        btnShowOtherTweets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showOtherTweets();
            }
        });

    }

    private void showOtherTweets() {
        // parse query to get all others tweete
        // pupulate the listview

        try {

            ParseQuery<ParseObject> parseQuery = ParseQuery.getQuery("MyTweet");
            if (ParseUser.getCurrentUser() != null) {
                parseQuery.whereNotEqualTo("user",
                        ParseUser.getCurrentUser().getUsername());

                parseQuery.findInBackground(new FindCallback<ParseObject>() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void done(List<ParseObject> tweets, ParseException e) {

                        if(tweets.size() > 0 && e == null) {

                            for(ParseObject tweet: tweets) {
                                FancyToast.makeText(SendTweetActivity.this,
                                        tweets.get(0).get("tweet").toString(),
                                        FancyToast.LENGTH_LONG, FancyToast.SUCCESS, true).show();
                                listOfOthersTweets.add(tweet.get("tweet").toString());
                            }
                            listViewOthersTweets.setAdapter(simpleAdapter);
                        }
                    }
                });
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }



    }


    private void sendTweet() {
        if(etSendTweet.getText() != null) {
            String currentUser = ParseUser.getCurrentUser().getUsername();
            String toastMessage = etSendTweet.getText().toString();

            // save to back4app
            ParseObject myTweet = new ParseObject("MyTweet");
            myTweet.put("tweet", toastMessage);
            myTweet.put("user", currentUser);

            final ProgressDialog progressDialog = new ProgressDialog(SendTweetActivity.this);
            progressDialog.setMessage("Loading...");
            progressDialog.show();
            myTweet.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if(e == null) {
                        FancyToast.makeText(SendTweetActivity.this,
                                currentUser + "'s tweet (" + toastMessage + ") is saved!!",
                                FancyToast.LENGTH_LONG, FancyToast.SUCCESS, true).show();
                    } else {
                        e.printStackTrace();
                    }
                    progressDialog.dismiss();
                }

            });


        } else {
            FancyToast.makeText(SendTweetActivity.this,
                    "Add some text..",
                    FancyToast.LENGTH_LONG, FancyToast.SUCCESS, true).show();
        }

    }

}