package com.example.futureme;



import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.FirebaseMessaging;

public class MainActivity extends AppCompatActivity {

 CardView cardSendMail;
 CardView cardContactUs;
 CardView cardHistory;
 CardView cardLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
          cardSendMail=findViewById(R.id.sendMail);
          cardHistory=findViewById(R.id.history);
          cardContactUs=findViewById(R.id.containUs);
          cardLogout=findViewById(R.id.logout);

          cardSendMail.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  Intent intent = new Intent(MainActivity.this,ContentMain.class);
                  startActivity(intent);
              }
          });
          cardHistory.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  Intent intent = new Intent(MainActivity.this,HistoryActivity.class);
                  startActivity(intent);
              }
          });
          cardLogout.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  confirm();
              }
          });
    }

    private void confirm() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.logout_dialog, null);

        Button yesButton = view.findViewById(R.id.btnYes);
        Button noButton = view.findViewById(R.id.btnNo);

        final AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setView(view)
                .create();

        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                logout();
            }
        });
        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(FirebaseAuth.getInstance().getCurrentUser()!=null) FirebaseMessaging.getInstance().subscribeToTopic((FirebaseAuth.getInstance().getCurrentUser().getEmail()).replace('@','_'));
    }

    private void logout() {
        FirebaseAuth.getInstance().signOut();
        if(FirebaseAuth.getInstance().getCurrentUser()!=null)FirebaseMessaging.getInstance().unsubscribeFromTopic((FirebaseAuth.getInstance().getCurrentUser().getEmail()).replace('@','_'));
        Intent intent = new Intent(MainActivity.this,WelcomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}
