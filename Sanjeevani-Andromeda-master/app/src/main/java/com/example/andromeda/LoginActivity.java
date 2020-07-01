package com.example.andromeda;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = LoginActivity.class.getName();
    private EditText email, password;
    private ProgressBar progressBar;
    private Button login_button;
    private SignInButton button;
    private TextView ln, forgot;
    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;
    private final static  int RC_SIGN_IN = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        progressBar = findViewById(R.id.progressBar);
        email = findViewById(R.id.eml);
        password = findViewById(R.id.pwd);
        login_button = findViewById(R.id.login);
        button = findViewById(R.id.google_signin);
        ln = findViewById(R.id.sp);
        forgot = findViewById(R.id.fpw);
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        mAuth = FirebaseAuth.getInstance();


        login_button.setOnClickListener(this);
        ln.setOnClickListener(this);
        forgot.setOnClickListener(this);
        button.setOnClickListener(this);
    }


    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    private void signIn() {
        String Email = email.getText().toString().trim();
        String Password = password.getText().toString().trim();

        Log.d(TAG, "signIn:" + email);
        if (!validateForm(Email, Password)) {
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        // [START sign_in_with_email]
        mAuth.signInWithEmailAndPassword(Email, Password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        }
                        else{
                            try
                            {
                                throw Objects.requireNonNull(task.getException());
                            }
                            // if user enters wrong email.
                            catch (FirebaseAuthInvalidUserException invalidEmail)
                            {
                                Log.d(TAG, "onComplete: invalid_email");
                                Toast.makeText(getApplicationContext(),R.string.invalid_email,Toast.LENGTH_SHORT).show();
                            }
                            // if user enters wrong password.
                            catch (FirebaseAuthInvalidCredentialsException wrongPassword)
                            {
                                Log.d(TAG, "onComplete: wrong_password");
                                Toast.makeText(getApplicationContext(),R.string.wrong_password,Toast.LENGTH_SHORT).show();
                            }
                            catch (Exception e)
                            {
                                Log.d(TAG, "onComplete: " + e.getMessage());
                                Toast.makeText(getApplicationContext(),R.string.could_not_signin,Toast.LENGTH_SHORT).show();
                            }
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
    }

    private void userSignin() {
        progressBar.setVisibility(View.VISIBLE);
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        progressBar.setVisibility(View.GONE);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                Toast.makeText(this,R.string.Auth_went_wrong,Toast.LENGTH_SHORT).show();
                // ...
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            //    Snackbar.make(findViewById(R.id.main_layout), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                            try
                            {
                                throw task.getException();
                            }
                            // if user enters wrong email.
                            catch (FirebaseAuthInvalidUserException invalidEmail)
                            {
                                Log.d(TAG, "onComplete: invalid_email");
                                Toast.makeText(getApplicationContext(),R.string.invalid_email,Toast.LENGTH_SHORT).show();
                            }
                            // if user enters wrong password.
                            catch (FirebaseAuthInvalidCredentialsException wrongPassword)
                            {
                                Log.d(TAG, "onComplete: wrong_password");
                                Toast.makeText(getApplicationContext(),R.string.wrong_password,Toast.LENGTH_SHORT).show();
                            }
                            catch (Exception e)
                            {
                                Log.d(TAG, "onComplete: " + e.getMessage());
                                Toast.makeText(getApplicationContext(),R.string.could_not_signin,Toast.LENGTH_SHORT).show();
                            }
                            //  updateUI(null);
                        }
                        // ...
                    }
                });
    }

    private boolean validateForm( String Email, String Password) {
        boolean valid = true;

        if (TextUtils.isEmpty(Email)) {
            Toast.makeText(this, R.string.please_enter_email, Toast.LENGTH_SHORT).show();
            valid = false;
        }

        if (TextUtils.isEmpty(Password)) {
            Toast.makeText(this, R.string.please_enter_password, Toast.LENGTH_SHORT).show();
            valid = false;
        }

        return valid;
    }

    private void updateUI(FirebaseUser user) {
        progressBar.setVisibility(View.GONE);
        if (user != null) {
            finish();
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }
    }


    @Override
    public void onClick(View v) {
        if(v == login_button){
            signIn();
        }
        else if(v == ln){
            startActivity(new Intent(getApplicationContext(), SignupActivity.class));
            finish();
        }
        else if(v == forgot){
            startActivity(new Intent(getApplicationContext(), forgot_password.class));
        }
        else if(v == button){
            userSignin();
        }
    }
}
