package com.example.futureme;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseError;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ContentMain extends AppCompatActivity  {

    private static final String TAG = "ContentMain";
    private EditText editTextMessage;
    private Button buttonSend;
    private ImageView btnDate;
    private ImageButton btnBack;
    private TextView textDate;
    private TextView textTime;
    private ProgressDialog pd;
    private String dateId,cnt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content_main);
        Log.d(TAG, "onCreate: ");
        editTextMessage=findViewById(R.id.email);
        buttonSend=findViewById(R.id.sendId);
        btnBack=findViewById(R.id.menuButton);
        btnDate=findViewById(R.id.dateId);
        textDate=findViewById(R.id.textviewId);
        pd = new ProgressDialog(this);
        pd.setCancelable(false);
        btnBack.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        onBackPressed();
    }
});
        btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleDateButton();
            }
        });

        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd.setTitle("Sending mail to the future...");
                pd.show();
                addToDatabase();
            }
        });
    }

    private void addToDatabase() {

        String message = editTextMessage.getText().toString().trim();
        String arrivalDate=textDate.getText().toString().trim();
        String email= Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail();

       /* Map<String,Object> map1= new HashMap<>();
        map1.put("Email",email);
        map1.put("Message",message);
        map1.put("Date",arrivalDate);*/

        FirebaseFirestore db1 = FirebaseFirestore.getInstance();
        db1.collection("Users").document(email)
                             .update("MailCounter", FieldValue.increment(1));

        DocumentReference dr = FirebaseFirestore.getInstance().collection("Users").document(email);
        dr.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        cnt = document.getString("MailCounter");
                        Log.d(TAG, "DocumentSnapshot data: " + cnt);
                    } else {

                    }

                } else {

                }
            }
        });



       /* FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Mails").document(email)
                .set(map1)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        pd.dismiss();

                        Toast.makeText(ContentMain.this,"The mail has taken off for the future!",Toast.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        pd.dismiss();
                        Toast.makeText(ContentMain.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                });*/
        Map<String,Object> map= new HashMap<>();
        map.put("Email",email);
        map.put("Message",message);
        map.put("Date",arrivalDate);
        String id= email+cnt;

        FirebaseFirestore.getInstance().collection("MailDatabase").document(dateId).collection(id)
                .document("Details").set(map)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        pd.dismiss();

                        Toast.makeText(ContentMain.this,"The mail has taken off for the future!",Toast.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        pd.dismiss();
                        Toast.makeText(ContentMain.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private void handleDateButton() {
        final Calendar calendar = Calendar.getInstance();
        int YEAR = calendar.get(Calendar.YEAR);
        int MONTH = calendar.get(Calendar.MONTH);
        int DATE = calendar.get(Calendar.DATE);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int date) {

                Calendar calendar1 = Calendar.getInstance();
                calendar1.set(Calendar.YEAR, year);
                calendar1.set(Calendar.MONTH, month);
                calendar1.set(Calendar.DATE, date);

                String dateText = DateFormat.format("EEEE, MMM d, yyyy", calendar1).toString();
                dateId = DateFormat.format("yyyy.MM.dd",calendar1).toString();
                textDate.setText(dateText);

            }
        }, YEAR, MONTH, DATE);

        datePickerDialog.show();
    }

    /*private void sendEmail() {
        String email= Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail();
         String message=editTextMessage.getText().toString().trim();

         SendMail sendMail=new SendMail(this,email,message);
         sendMail.execute();
         editTextMessage.setText("");

     }*/

}
