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

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    private static final String TAG = "RegisterActivity";
    private EditText email_signup;
    private EditText user_signup;
    private EditText password_signup;
    private EditText confirmPassword_signup;
    private ImageButton btnBck;
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
        user_signup = findViewById(R.id.username_signup);
        password_signup = findViewById(R.id.password_signup);
        confirmPassword_signup = findViewById(R.id.conpassword_signup);
        btnBck = findViewById(R.id.backbtn);
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

    }

    private void userSignup() {

         email = email_signup.getText().toString().trim();
         password = password_signup.getText().toString().trim();
         confirmPassword = confirmPassword_signup.getText().toString().trim();
         username = user_signup.getText().toString().trim();

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            email_signup.setFocusable(true);
            email_signup.setError("Invalid Email Address");

        } else if(user_signup.length()<1){
            user_signup.setFocusable(true);
            user_signup.setError("Username can't be empty");
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
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            addtoDatabase();


                        } else {
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

    private void addtoDatabase() {
        Log.d(TAG, "addtoDatabase: started");

        Map<String,Object> map= new HashMap<>();
        map.put("Email",email);
        map.put("Password",password);
        map.put("Name",username);
        map.put("MailCounter",1000);
        FirebaseFirestore.getInstance().collection("Users").document(email).set(map)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        pd.dismiss();
                        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                e.printStackTrace();
                Toast.makeText(RegisterActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }

        });
    }
}