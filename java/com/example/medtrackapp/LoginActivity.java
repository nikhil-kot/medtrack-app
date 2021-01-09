/*
LoginActivity
This class allows for the users to login into Firebase with their username and password or a Google account.
Version 1 and 6/06/2020
Daniel Sin, Hasan Almemari
All of the imports below the package statement are dependencies.
 */


package com.example.medtrackapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;

import com.firebase.client.Firebase;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private TextView mFormFeedback;
    private ProgressBar progressbar;
    private Firebase reference;
    private EditText email;
    private EditText password;
    private Button linkBtn;
    private static final int RC_SIGN_IN = 1;
    private GoogleSignInClient mGoogleSigninClient;
    private SignInButton signInButton;
    private String name;
    private String new_email;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        Firebase.setAndroidContext(getApplicationContext());
        reference = new Firebase("https://medtrack-31772.firebaseio.com");
        mAuth = FirebaseAuth.getInstance();
        mFormFeedback = findViewById(R.id.form_feedback);
        progressbar = findViewById(R.id.progressBar);
        user = mAuth.getCurrentUser();
        email = findViewById(R.id.textEmail);
        password = findViewById(R.id.textPassword);
        linkBtn = findViewById(R.id.linkbtn);

        Toolbar toolbar = findViewById(R.id.toolbar_2);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.baseline_arrow_back_black_18dp));

        toolbar.setNavigationOnClickListener(v -> {
            Intent back_return = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(back_return);
        });

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSigninClient = GoogleSignIn.getClient(this, gso);

        signInButton = findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);



        //Allows for users to sign in anonymously
        if (user == null) {
            mAuth.signInAnonymously().addOnCompleteListener(task -> {

                if (task.isSuccessful()) {
                    mFormFeedback.setVisibility(View.VISIBLE);
                    mFormFeedback.setText("Signed in Anonymously");
                } else {
                    mFormFeedback.setText("There was an error signing in.");
                }
                progressbar.setVisibility(View.INVISIBLE);
            });
        } else {
            mFormFeedback.setText("Already signed in.");
            progressbar.setVisibility(View.INVISIBLE);
        }

        //Allows users to sign in with their email.
        linkBtn.setOnClickListener(v -> {
            String new_email = email.getText().toString();
            String new_password = password.getText().toString();
            if (user != null) {
                if (!new_email.isEmpty() || !new_password.isEmpty()) {
                    progressbar.setVisibility(View.VISIBLE);
                    AuthCredential credential = EmailAuthProvider.getCredential(new_email, new_password);
                    user.linkWithCredential(credential).addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            mFormFeedback.setVisibility(View.VISIBLE);
                            mFormFeedback.setText("Signed in.");
                        } else {
                            mFormFeedback.setText("There was an error signing in.");
                        }
                        progressbar.setVisibility(View.INVISIBLE);
                    });
                }
            }
        });


        signInButton.setOnClickListener(v -> {
            switch (v.getId()) {
                case R.id.sign_in_button:
                    signIn();
                    break;
            }
        });

    }

    /**
     * Signs in the user
     */
    void signIn() {
        progressbar.setVisibility(View.VISIBLE);
        Intent signInIntent = mGoogleSigninClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);

    }

    /**
     * This method is used to help authenticating the user
     * @param requestCode Used in requesting signining in the user
     * @param resultCode  Used as a result of the action
     * @param data Retrieves the signInIntent to sign in the user's google account
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                if (account != null) {
                    firebaseAuthWithGoogle(account);
                }
            } catch (ApiException e) {
                e.printStackTrace();
            }

        }
    }

    /**
     * Used in authenticating the user with Google Sign In Account
     * Suggested and Created by Google API
     * @param account The Google account that is being authenticated
     */
    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        Log.d("TAG", "firebaseAuthWithGoogle:" + account.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.getCurrentUser().linkWithCredential(credential).addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        mFormFeedback.setText("Successful Authentication.");
                        progressbar.setVisibility(View.INVISIBLE);
                        FirebaseUser user = task.getResult().getUser();
                        updateUI(user);
                    } else {
                        mFormFeedback.setText("Failed Authentication.");
                        Toast.makeText(LoginActivity.this, "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                        updateUI(null);
                    }
                }
        );
    }

    /**
     * Updates the features of the UI
     * @param user FirebaseUser object that references the user's account
     */
    private void updateUI(FirebaseUser user) {
        if (user != null){
            name = user.getDisplayName();
            new_email = user.getEmail();
        }
        else {
            name = null;
            new_email = null;
        }
    }
}
