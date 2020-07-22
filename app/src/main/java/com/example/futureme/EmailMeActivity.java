package com.example.futureme;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class EmailMeActivity extends AppCompatActivity {

    private EditText message;
   // private TextView titleTv;
    private Button submitButton;
    private ImageButton backBtn;
    private String email;
    private String mail;
    private String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_me);
        message=findViewById(R.id.email);

        //titleTv=findViewById(R.id.titleId);
        backBtn=findViewById(R.id.backbtn);
        submitButton=findViewById(R.id.submitId);

        title = getIntent().getStringExtra("ButtonClicked");
        message.setHint(title);
        //titleTv.setText(title);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendEmail();
            }
        });


    }
    private void sendEmail() {

        mail=message.getText().toString().trim();
        String[] TO = {"anannadristy03@gmail.com"};
       // String[] CC = {FirebaseAuth.getInstance().getCurrentUser().getEmail()};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");


        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
       // emailIntent.putExtra(Intent.EXTRA_CC, CC);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, title);
        emailIntent.putExtra(Intent.EXTRA_TEXT, mail);

        try {
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            finish();
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(EmailMeActivity.this, "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }
     }
}