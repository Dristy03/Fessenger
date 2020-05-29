package com.example.futureme;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
    private EditText editTextEmail;
    private EditText editTextMessage;
    private Button buttonSend;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextEmail=findViewById(R.id.emailaddress);
        editTextMessage=findViewById(R.id.email);
        buttonSend=findViewById(R.id.buttonSendId);

        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendEmail();
            }
        });
    }

    private void sendEmail() {
        String email=editTextEmail.getText().toString().trim();
        String message=editTextMessage.getText().toString().trim();

        SendMail sendMail=new SendMail(this,email,message);
        sendMail.execute();
        editTextMessage.setText("");
        editTextEmail.setText("");
    }
}
