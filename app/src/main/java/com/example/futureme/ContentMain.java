package com.example.futureme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class ContentMain extends AppCompatActivity  {

    private static final String TAG = "ContentMain";
    private EditText editTextMessage;
    private EditText editTextTitle;
    private Button buttonSend;
    private ImageView btnDate;
    private ImageButton btnBack;
    private TextView textDate;
    private TextView textTime;
    private ProgressDialog pd;
    private String currentDate;
    private String yearS;
    private String monthS;
    private String dayS;
    private String email;
    private String message;
    private String title;
    private String arrivalDate;
    private String Id;
    private String mailId;
    private String dateText="";
    int cnt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content_main);
        Log.d(TAG, "onCreate: ");
        editTextMessage=findViewById(R.id.email);
        editTextTitle=findViewById(R.id.email_title);
        buttonSend=findViewById(R.id.sendId);
        btnBack=findViewById(R.id.menuButton);
        btnDate=findViewById(R.id.dateId);
        textDate=findViewById(R.id.textviewId);
        pd = new ProgressDialog(this,R.style.DialogTheme);
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
        textDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleDateButton();
            }
        });
        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                message = editTextMessage.getText().toString().trim();
                arrivalDate=textDate.getText().toString().trim();
                title=editTextTitle.getText().toString().trim();

                if(title.length()<1)
                {
                    editTextTitle.setError("This field shouldn't be empty!");
                    editTextTitle.setFocusable(true);
                }else if(message.length()<1)
                {
                    editTextMessage.setError("This field shouldn't be empty!");
                    editTextMessage.setFocusable(true);
                }else if(dateText.length()<1)
                {
                   Toast.makeText(getApplicationContext(),"Choose your the date of receiving email!",Toast.LENGTH_LONG).show();
                } else
                {
                    pd.setTitle("Sending mail to the future...");
                    pd.show();
                    processTheCounter();
                    editTextMessage.setText("");
                    editTextTitle.setText("");
                    textDate.setText("Date of receiving email");
                }

            }
        });
    }


    //increase the counter as a mail is written and storing the topic of the user
    private void processTheCounter() {

        email= Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail();

        DocumentReference dr = FirebaseFirestore.getInstance().collection("Users").document(email);
        dr.update("MailCounter", FieldValue.increment(1));

       /* dr.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    if(document.exists()){
                        userTopic=document.getString("Topic");
                    }
                }
            }
        });
*/
        dr.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();

                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        cnt = document.getLong("MailCounter").intValue();
                        Log.d(TAG, "DocumentSnapshot data: " + cnt);
                        Id = Integer.toString(cnt);
                        mailId=email+Id;
                        addToMailDatabase();
                    }
                }
            }
        });
    }

    // updates to the MailDatabase
    private void addToMailDatabase() {


        Map<String,Object> map= new HashMap<>();
        Map<String,Object> mapp= new HashMap<>();
        mapp.put("SentAllMails",false);
        map.put("Email",email);
        map.put("Message",message);
        map.put("Title",title);
        map.put("Date",arrivalDate);
        //map.put("Topic",userTopic);

        FirebaseFirestore.getInstance().collection("MailDatabase").document(yearS).collection(monthS)
                .document(dayS).set(mapp)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Toast.makeText(ContentMain.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                });


        FirebaseFirestore.getInstance().collection("MailDatabase").document(yearS).collection(monthS)
                .document(dayS).collection("Emails")
                .document(mailId).set(map)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                           addToDatabase();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Toast.makeText(ContentMain.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // updates to the Mails
    private void addToDatabase() {


        Calendar calendar= Calendar.getInstance();
        int YEAR = calendar.get(Calendar.YEAR);
        int MONTH = calendar.get(Calendar.MONTH);
        int DATE = calendar.get(Calendar.DATE);
        long a= YEAR*100+MONTH;
        a= a*100+DATE;

        MONTH++;
        String mmS = ((MONTH<10)?"0"+MONTH:MONTH+"");
        String ddS = ((DATE<10)?"0"+DATE:DATE+"");
        currentDate = ddS+"."+mmS+"."+YEAR;

        Map<String,Object> map1= new HashMap<>();
        map1.put("Email",email);
        map1.put("Message",message);
        map1.put("Title",title);
        map1.put("Date",arrivalDate);
        map1.put("Priority",a);
        map1.put("CurrentDate",currentDate);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Mails").document(email).collection("Details").document(Id)
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
                });

    }

//arrival of mail
    private void handleDateButton() {
        final Calendar calendar = Calendar.getInstance();
        int YEAR = calendar.get(Calendar.YEAR);
        int MONTH = calendar.get(Calendar.MONTH);
        int DATE = calendar.get(Calendar.DATE);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,  R.style.DialogTheme,new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int date) {

                Calendar calendar1 = Calendar.getInstance();
                calendar1.set(Calendar.YEAR, year);
                calendar1.set(Calendar.MONTH, month);
                calendar1.set(Calendar.DATE, date);

                dateText = DateFormat.format("EEEE, MMM d, yyyy", calendar1).toString();
                yearS = DateFormat.format("yyyy",calendar1).toString();
                monthS = DateFormat.format("MM",calendar1).toString();
                dayS = DateFormat.format("dd",calendar1).toString();
                textDate.setText(dateText);

            }
        }, YEAR, MONTH, DATE);
        datePickerDialog.getDatePicker().setMinDate(Calendar.getInstance().getTimeInMillis()+1000*60*60*24);  //+1000*60*60*24
        datePickerDialog.show();
    }



}
