package com.example.andromeda;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class forgot_password extends AppCompatActivity implements View.OnClickListener{

    private EditText email;
    private Button Submit;
    private ProgressBar progressBar;

    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        email = findViewById(R.id.e);
        Submit = findViewById(R.id.submit);
        mAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.progressBar);

        Submit.setOnClickListener(this);

    }
    private void sendmail(){
        String Email = email.getText().toString().trim();

        if (TextUtils.isEmpty(Email)) {
            Toast.makeText(getApplicationContext(), R.string.please_enter_email, Toast.LENGTH_SHORT).show();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);

        mAuth.sendPasswordResetEmail(Email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), R.string.reset_password, Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), R.string.fail_to_send_mail, Toast.LENGTH_SHORT).show();
                }

                progressBar.setVisibility(View.GONE);

            }
        });
    }

    @Override
    public void onClick(View v) {
        if(v == Submit){
            sendmail();
        }
    }
}
