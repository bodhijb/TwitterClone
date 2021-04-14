package com.jf2mc1.a015004wtwitterclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.ArrayList;
import java.util.List;

public class TwitterUsers extends AppCompatActivity implements
        AdapterView.OnItemClickListener,
        AdapterView.OnItemLongClickListener {

    private ListView listViewTwitters;
    ArrayList<String> mArrayList;
    private ArrayAdapter mArrayAdapter;
    private String followedUser = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_twitter_users);
        setTitle(ParseUser.getCurrentUser().getUsername() + " is Following");

        listViewTwitters = findViewById(R.id.listview_twitters);
        mArrayList = new ArrayList<>();
        mArrayAdapter = new ArrayAdapter(TwitterUsers.this,
                android.R.layout.simple_list_item_checked, mArrayList);
        listViewTwitters.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);


        listViewTwitters.setOnItemClickListener(TwitterUsers.this);
        listViewTwitters.setOnItemLongClickListener(TwitterUsers.this);


        FancyToast.makeText(TwitterUsers.this,
                "Welcome " + ParseUser.getCurrentUser().getUsername(),
                FancyToast.LENGTH_LONG, FancyToast.INFO, true).show();


        showTwitterUsers();


    }


    private void showTwitterUsers() {

        try {

            ParseQuery<ParseUser> parseUserParseQuery = ParseUser.getQuery();
            if (ParseUser.getCurrentUser() != null) {
                parseUserParseQuery.whereNotEqualTo("username",
                        ParseUser.getCurrentUser().getUsername());

                parseUserParseQuery.findInBackground(new FindCallback<ParseUser>() {
                    @Override
                    public void done(List<ParseUser> users, ParseException e) {
                        if (users.size() > 0 && e == null) {

                            for (ParseUser twitterUser : users) {
                                mArrayList.add(twitterUser.getUsername());
                            }
                            listViewTwitters.setAdapter(mArrayAdapter);

                            // update each users screen for who they are following
                            if (ParseUser.getCurrentUser().getList("fanOf") != null) {
                                for (String user : mArrayList) {
                                    if (ParseUser.getCurrentUser().getList("fanOf").contains(user)) {

                                        followedUser += user + "\n";

                                        listViewTwitters.setItemChecked(mArrayList.indexOf(user), true);

                                        FancyToast.makeText(TwitterUsers.this,
                                                ParseUser.getCurrentUser().getUsername() +
                                                " is following \n" + followedUser,
                                                FancyToast.LENGTH_LONG, FancyToast.INFO, true).show();

                                    }
                                }
                            }

                        }
                    }
                });
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_item_logout_user:
                ParseUser.getCurrentUser().logOutInBackground(new LogOutCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            startActivity(new Intent(TwitterUsers.this, LoginActivity.class));
                            finish();
                        }
                    }
                });
                break;
            case R.id.menu_item_send_tweet:
                startActivity(new Intent(TwitterUsers.this, SendTweetActivity.class));

            default:
                break;

        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View viewThatHasBeenTapped,
                            int position, long id) {

        CheckedTextView checkedTextView = (CheckedTextView) viewThatHasBeenTapped;
        if (checkedTextView.isChecked()) {

            ParseUser.getCurrentUser().add("fanOf", mArrayList.get(position));

        } else {
            // remove the followedUser, get remaining followedUsers as a list, remove the B4A column, add it back from the list
            ParseUser.getCurrentUser().getList("fanOf").remove(mArrayList.get(position));

            // update fanOf info. download the list, amend it and upload a frsh copy
            List currentUserFanOfList = ParseUser.getCurrentUser()
                    .getList("fanOf");
            // replace the entire column
            ParseUser.getCurrentUser().remove("fanOf");
            ParseUser.getCurrentUser().put("fanOf", currentUserFanOfList);

        }
        ParseUser.getCurrentUser().saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    FancyToast.makeText(TwitterUsers.this,
                            "Saved",
                            FancyToast.LENGTH_LONG, FancyToast.SUCCESS, true).show();

                }
            }
        });

    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        return false;
    }
}