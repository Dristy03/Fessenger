package com.example.futureme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    private static final String TAG = "RegisterActivity";
    private EditText email_signup;
    private EditText password_signup;
    private EditText confirmPassword_signup;
    private ImageButton btnBck,btnRefresh;
    private Button btnSignup;
    private FirebaseAuth mAuth;
    String email;
    String password;
    String username;
    String confirmPassword;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        email_signup = findViewById(R.id.email_signup);
        password_signup = findViewById(R.id.password_signup);
        confirmPassword_signup = findViewById(R.id.conpassword_signup);
        btnBck = findViewById(R.id.backbtn);
        //btnRefresh=findViewById(R.id.refresbtn);
        btnSignup = findViewById(R.id.signup_btn);
        mAuth = FirebaseAuth.getInstance();
        pd = new ProgressDialog(this,R.style.DialogTheme);
        pd.setTitle("Signing up...");
        pd.setCancelable(false);

        btnBck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userSignup();
            }
        });
       /* btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(getIntent());
            }
        });*/
    }

    private void userSignup() {

         email = email_signup.getText().toString().trim();
         password = password_signup.getText().toString().trim();
         confirmPassword = confirmPassword_signup.getText().toString().trim();

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            email_signup.setFocusable(true);
            email_signup.setError("Invalid Email Address");

        } else if (password.length() < 6) {
            password_signup.setError("Length should at least be 6");
            password_signup.setFocusable(true);
        } else if (!password.equals(confirmPassword)) {
            confirmPassword_signup.setFocusable(true);
            confirmPassword_signup.setError("Password didn't match");
        } else {
            pd.show();
            RegisterUser(email, password);
        }
    }

    private void RegisterUser(String email, String password) {


        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                                //verification of the user
                                mAuth.getCurrentUser().sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        pd.dismiss();
                                        Toast.makeText(RegisterActivity.this,"Verification Email has been sent! Please verify your email and login.",Toast.LENGTH_LONG).show();
                                        Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(intent);
                                        finish();


                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {

                                    }
                                });

                            }


                         else {
                            // If sign in fails, display a message to the user.
                            pd.dismiss();
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(RegisterActivity.this, "Authentication failed.\nIf you already have an account with this email, please login.",
                                    Toast.LENGTH_SHORT).show();

                        }

                        // ...
                    }
                });


    }




}