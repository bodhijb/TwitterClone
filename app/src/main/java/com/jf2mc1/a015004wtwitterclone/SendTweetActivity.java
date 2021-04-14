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
import android.widget.SimpleAdapter;

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
import java.util.HashMap;
import java.util.List;

public class SendTweetActivity extends AppCompatActivity implements
View.OnClickListener {

    private EditText etSendTweet;
    private Button btnSendTweet;
    private Button btnShowOtherTweets;
    private ListView listViewOthersTweets;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_tweet);
        setTitle("Tweets " + ParseUser.getCurrentUser().getUsername() + "is Following");

        etSendTweet = findViewById(R.id.et_send_tweet);
        btnSendTweet = findViewById(R.id.btn_send_tweet);
        btnShowOtherTweets = findViewById(R.id.btn_show_other_tweets);
        listViewOthersTweets = findViewById(R.id.listview_others_tweets);

        btnSendTweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendTweet();
            }
        });

        btnShowOtherTweets.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        final ArrayList<HashMap<String, String>> tweetList = new ArrayList<>();

        final SimpleAdapter adapter = new SimpleAdapter(SendTweetActivity.this, tweetList,
                android.R.layout.simple_list_item_2, new String[]{"tweetUserName", "tweetValue"},
                new int[]{android.R.id.text1, android.R.id.text2});

        try {
            ParseQuery<ParseObject> parseQuery = ParseQuery.getQuery("MyTweet");
            parseQuery.whereContainedIn("user", ParseUser.getCurrentUser().getList("fanOf"));
            parseQuery.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> objects, ParseException e) {

                    if (objects.size() > 0 && e == null) {

                        for (ParseObject tweetObject : objects) {
                            HashMap<String, String> userTweet = new HashMap<>();
                            userTweet.put("tweetUserName", tweetObject.getString("user"));
                            userTweet.put("tweetValue", tweetObject.getString("tweet"));
                            tweetList.add(userTweet);

                        }

                        listViewOthersTweets.setAdapter(adapter);

                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
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