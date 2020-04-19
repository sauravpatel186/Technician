package com.happyhomes.technician;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class workdoneAdapter extends FirestoreRecyclerAdapter<work,workdoneAdapter.workdoneviewholder>{
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private workAdapter.OnItemClickListener listener;
    public workdoneAdapter(FirestoreRecyclerOptions<work> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(final workdoneviewholder workdoneviewholder, final int i, final work work) {
        workdoneviewholder.name.setText("Name: "+work.getFname());
        workdoneviewholder.problemname.setText("Problem Name: "+work.getPname());
    }

    @NonNull
    @Override
    public workdoneviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.workdone_xml, parent, false);

        return new workdoneviewholder(view);
    }

    public class workdoneviewholder extends RecyclerView.ViewHolder {
        EditText name,problemname;
        public workdoneviewholder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.custnamewd);
            problemname=itemView.findViewById(R.id.compnamewd);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && listener != null) {
                        listener.onItemClick(getSnapshots().getSnapshot(position), position);
                    }
                }
            });
        }
    }
    public interface OnItemClickListener {
        void onItemClick(DocumentSnapshot documentSnapshot, int position);
    }

    public void setOnItemClickListener(workAdapter.OnItemClickListener listener) {
        this.listener = listener;
    }
}
