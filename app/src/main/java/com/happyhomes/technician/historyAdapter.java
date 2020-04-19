package com.happyhomes.technician;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;

public class historyAdapter extends FirestoreRecyclerAdapter<work,historyAdapter.historyviewholder>{
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    public historyAdapter(FirestoreRecyclerOptions<work> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(final historyviewholder historyviewholder, final int i, final work work) {
        historyviewholder.name.setText("Name: "+work.getFname());
        historyviewholder.problemname.setText("Problem Name: "+work.getPname());
        historyviewholder.des.setText("Description: "+work.getPdesc());
        historyviewholder.date.setText("Date: "+work.getDate_Time());
    }

    @NonNull
    @Override
    public historyviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.history_view, parent, false);

        return new historyviewholder(view);
    }

    public class historyviewholder extends RecyclerView.ViewHolder {
        EditText name,problemname,des,date;
        public historyviewholder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.custnamehistory);
            problemname=itemView.findViewById(R.id.compnamehistory);
            des=itemView.findViewById(R.id.compdetailhistory);
            date=itemView.findViewById(R.id.date);
            name.setKeyListener(null);
            problemname.setKeyListener(null);
            des.setKeyListener(null);
            date.setKeyListener(null);
        }
    }
}
