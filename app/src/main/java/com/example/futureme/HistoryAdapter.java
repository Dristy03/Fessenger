package com.example.futureme;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class HistoryAdapter extends FirestoreRecyclerAdapter<History,HistoryAdapter.MyViewHolder> {
    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public HistoryAdapter(@NonNull FirestoreRecyclerOptions<History> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull HistoryAdapter.MyViewHolder holder, int position, @NonNull History model) {

        holder.textArrivalDate.setText(model.getDate());
    }

    @NonNull
    @Override
    public HistoryAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.history_row,parent,false);
        return new MyViewHolder(view);
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

       TextView textArrivalDate;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            textArrivalDate=itemView.findViewById(R.id.dateId);
        }
    }

}