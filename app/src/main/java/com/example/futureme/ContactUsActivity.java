package com.example.futureme;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ContactUsActivity extends AppCompatActivity {
    private Button helpButton;
    private Button wrongButton;
    private Button remarksButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);

        helpButton=findViewById(R.id.helpBtnId);
        wrongButton=findViewById(R.id.wrongBtnId);
        remarksButton=findViewById(R.id.remarksBtnId);

        helpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), EmailMeActivity.class);
                String contactTitle="Suggestion";
                intent.putExtra("ButtonClicked",contactTitle);
                startActivity(intent);
            }
        });
        wrongButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), EmailMeActivity.class);
                String contactTitle="Complain";
                intent.putExtra("ButtonClicked",contactTitle);
                startActivity(intent);
            }
        });
        remarksButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), EmailMeActivity.class);
                String contactTitle="Comment";
                intent.putExtra("ButtonClicked",contactTitle);
                startActivity(intent);
            }
        });

    }
}