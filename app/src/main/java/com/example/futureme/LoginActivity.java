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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG ="LOGIN" ;
    private EditText login_email;
    private EditText login_password;
    private ImageButton back;
    private Button btnLogin;
    private TextView forget_pass;
    private FirebaseAuth mAuth;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login_email=findViewById(R.id.emailad_login);
        login_password=findViewById(R.id.password_login);
        back=findViewById(R.id.backbtn);
        btnLogin=findViewById(R.id.login_btn);
        forget_pass=findViewById(R.id.forgetpass);
        pd = new ProgressDialog(this,R.style.DialogTheme);
        pd.setTitle("Logging in...");
        pd.setCancelable(false);
        mAuth = FirebaseAuth.getInstance();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email= login_email.getText().toString().trim();
                String password= login_password.getText().toString().trim();
                if(!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches())
                {
                    login_email.setFocusable(true);
                    login_email.setError("Invalid Email Address");

                }else if(password.length()<6)
                {
                    login_password.setError("Length should at least be 6");
                    login_password.setFocusable(true);
                }else {
                    pd.show();
                    loginUser(email,password);
                }
            }
        });
        forget_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,RecoverPassActivity.class);
                startActivity(intent);
            }
        });
    }

    private void loginUser(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            pd.dismiss();
                            Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();
                        } else {
                            pd.dismiss();
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, task.getException().getLocalizedMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }



}
