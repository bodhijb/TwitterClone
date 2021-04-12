package com.jf2mc1.a015004wtwitterclone;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

public class MainActivity extends AppCompatActivity implements
        View.OnClickListener {

    private EditText edtSUEmail, edtSUUsername, edtSUPassword;
    private Button btnSUSignUp, btnSULogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Sign Up");

        ParseInstallation.getCurrentInstallation().saveInBackground();

        edtSUEmail = findViewById(R.id.edt_su_email);
        edtSUUsername = findViewById(R.id.edt_su_uname);
        edtSUPassword = findViewById(R.id.edt_su_pword);
        btnSUSignUp = findViewById(R.id.btn_su_signup);
        btnSULogin = findViewById(R.id.btn_su_login);

        btnSUSignUp.setOnClickListener(this::onClick);
        btnSULogin.setOnClickListener(this::onClick);

        // keyword enter == login
        edtSUPassword.setOnKeyListener(new View.OnKeyListener() {
            //            What this means is that you've now told the edittext view that you want to be
//            informed every time the user presses a key while this EditText has the focus.
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent event) {
                if(keyCode == KeyEvent.KEYCODE_ENTER
                        && event.getAction() == KeyEvent.ACTION_DOWN) {
                    // sign up user
                    onClick(btnSUSignUp);
                }
                return false;
            }
        });


    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btn_su_signup:
                signUpUser();
                break;

            case R.id.btn_su_login:
                goToLoginActivity();
                break;

            default:
                break;
        }



    }

    private void goToLoginActivity() {
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);


    }

    private void signUpUser() {

        String email = edtSUEmail.getText().toString();
        String username = edtSUUsername.getText().toString();
        String password = edtSUPassword.getText().toString();

        if(email.equals("") || username.equals("") || password.equals("")) {
            FancyToast.makeText(MainActivity.this,
                    "Email, username & password is required :(",
                    FancyToast.LENGTH_LONG, FancyToast.ERROR,
                    true).show();

        } else {

            ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setMessage("Signing up " + username);
            progressDialog.show();

            ParseUser newUser = new ParseUser();
            newUser.setEmail(email);
            newUser.setUsername(username);
            newUser.setPassword(password);

            newUser.signUpInBackground(new SignUpCallback() {
                @Override
                public void done(ParseException e) {
                    if(e == null) {

                        FancyToast.makeText(MainActivity.this,
                                "Signed up :)",
                                FancyToast.LENGTH_LONG, FancyToast.SUCCESS,
                                true).show();

                        goToTweetActvity();

                    } else {
                        FancyToast.makeText(MainActivity.this,
                                "Something went wrong :(" + e.getMessage(),
                                FancyToast.LENGTH_LONG, FancyToast.ERROR,
                                true).show();
                    }
                    progressDialog.dismiss();
                }
            });

        }






    }

    private void goToTweetActvity() {
        startActivity(new Intent(MainActivity.this, TwitterUsers.class));


    }
}