package com.jf2mc1.a015004wtwitterclone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.shashank.sony.fancytoastlib.FancyToast;

public class LoginActivity extends AppCompatActivity implements
        View.OnClickListener {

    private EditText edtLIEmail, edtLIPassword;
    private Button btnLILogin, btnLISignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTitle("Login");

        edtLIEmail = findViewById(R.id.edt_desc);
        edtLIPassword = findViewById(R.id.edt_li_pword);
        btnLILogin = findViewById(R.id.btn_li_login);
        btnLISignUp = findViewById(R.id.btn_li_signup);
        btnLILogin.setOnClickListener(this::onClick);
        btnLISignUp.setOnClickListener(this::onClick);

        // keyword enter == login
        edtLIPassword.setOnKeyListener(new View.OnKeyListener() {
            //            What this means is that you've now told the edittext view that you want to be
//            informed every time the user presses a key while this EditText has the focus.
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent event) {
                if(keyCode == KeyEvent.KEYCODE_ENTER
                        && event.getAction() == KeyEvent.ACTION_DOWN) {
                    // sign up user
                    onClick(btnLILogin);
                }
                return false;
            }
        });



    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btn_li_login:
                logInUser();
                break;
            case R.id.btn_li_signup:
                backToSignUp();
                break;
            default:
                break;
        }


    }

    private void backToSignUp() {
        startActivity(new Intent(LoginActivity.this,
                MainActivity.class));
    }

    private void logInUser() {

        String email = edtLIEmail.getText().toString();
        String password = edtLIPassword.getText().toString();

        if(email.equals("") || password.equals("")) {
            FancyToast.makeText(LoginActivity.this,
            "Email & password is required :(",
                    FancyToast.LENGTH_LONG, FancyToast.ERROR, true).show();

        } else {

            ParseUser.logInInBackground(email, password,
                    new LogInCallback() {
                        @Override
                        public void done(ParseUser user, ParseException e) {
                            if(user != null && e == null) {
                                FancyToast.makeText(LoginActivity.this,
                                        user.getUsername() + " is logged in :)",
                                        FancyToast.LENGTH_LONG, FancyToast.SUCCESS, true).show();

                                goToTweetActvity();

                            } else {
                                FancyToast.makeText(LoginActivity.this,
                                        "Something went wrong :(" + e.getMessage(),
                                        FancyToast.LENGTH_LONG, FancyToast.ERROR, true).show();

                            }
                        }
                    });

        }

    }

    private void goToTweetActvity() {
        startActivity(new Intent(LoginActivity.this, TwitterUsers.class));


    }
}