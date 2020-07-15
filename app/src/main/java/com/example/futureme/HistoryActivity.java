package com.example.futureme;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class HistoryActivity extends AppCompatActivity {

    RecyclerView recyclerView;


    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private CollectionReference ref = FirebaseFirestore.getInstance().collection("Mails").document(FirebaseAuth.getInstance().getCurrentUser().getEmail())
            .collection("Details");

    private HistoryAdapter historyAdapter;

    private ImageButton btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        recyclerView=findViewById(R.id.recyclerView);
        setupRecView();
        btnBack=findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void setupRecView() {

       Query query = ref.orderBy("Date", Query.Direction.DESCENDING);


        FirestoreRecyclerOptions<History> options= new FirestoreRecyclerOptions.Builder<History>()
                .setQuery(query,History.class)
                .build();
        historyAdapter = new HistoryAdapter(options);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(historyAdapter);
    }
    @Override
    protected void onStart() {
        super.onStart();
        historyAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        historyAdapter.stopListening();
    }
}
