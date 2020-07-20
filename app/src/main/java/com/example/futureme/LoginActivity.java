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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG ="LOGIN" ;
    private EditText login_email;
    private EditText login_password;
    private ImageButton back;
    private Button btnLogin;
    private TextView forget_pass;
    private FirebaseAuth mAuth;
    String email,password;
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
                Intent intent = new Intent(getApplicationContext(),WelcomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                email= login_email.getText().toString().trim();
                password= login_password.getText().toString().trim();
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


                            //Check whether the user is verified or not

                            if(mAuth.getCurrentUser().isEmailVerified())
                            {
                                addtoDatabase();

                            }
                            else
                            {
                                pd.dismiss();
                                Toast.makeText(LoginActivity.this,"Please verify your email id",Toast.LENGTH_LONG).show();
                                mAuth.signOut();
                            }

                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");

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

    private void addtoDatabase() {
        FirebaseFirestore.getInstance().collection("Users").document(email)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful())
                {
                    DocumentSnapshot document = task.getResult();

                    if (document.exists()) {
                        pd.dismiss();
                        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();

                    }else{
                        Map<String,Object> map= new HashMap<>();
                        map.put("Email",email);
                        map.put("Password",password);
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
                                Toast.makeText(LoginActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                            }

                        });
                    }
                }
            }
        });
    }




}
