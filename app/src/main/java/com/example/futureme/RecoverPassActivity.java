package com.example.futureme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class RecoverPassActivity extends AppCompatActivity {
    private EditText recoveryEmail;
    private Button recoverButton;
    private ImageButton backButton;
    FirebaseAuth mAuth;
    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recover_pass);

        mAuth = FirebaseAuth.getInstance();
        recoveryEmail=findViewById(R.id.email_recoveryId);
        recoverButton=findViewById(R.id.recover_btn);
        backButton=findViewById(R.id.backbtnId);
        progressBar = findViewById(R.id.progressBar);


        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        
        recoverButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String  email = recoveryEmail.getText().toString().trim();
               if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Enter your registered email!", Toast.LENGTH_SHORT).show();
                    return;
                }
                   progressBar.setVisibility(View.VISIBLE);
                   mAuth.sendPasswordResetEmail(email)
                           .addOnCompleteListener(new OnCompleteListener<Void>() {
                               @Override
                               public void onComplete(@NonNull Task<Void> task) {
                                   if(task.isSuccessful()){
                                       Toast.makeText(RecoverPassActivity.this, "Recovery Email Sent", Toast.LENGTH_SHORT).show();
                                   }else
                                   {
                                       Toast.makeText(RecoverPassActivity.this, "Sending Email failed..", Toast.LENGTH_SHORT).show();
                                   }
                                   progressBar.setVisibility(View.GONE);
                               }
                           }).addOnFailureListener(new OnFailureListener() {
                       @Override
                       public void onFailure(@NonNull Exception e) {
                           e.printStackTrace();
                       }
                   });
                }



        });
    }





}
