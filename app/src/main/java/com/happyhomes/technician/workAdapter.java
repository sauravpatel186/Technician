package com.happyhomes.technician;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class workAdapter extends FirestoreRecyclerAdapter<work,workAdapter.workviewholder>{
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private OnItemClickListener listener;
    public workAdapter(FirestoreRecyclerOptions<work> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(final workviewholder workviewholder, final int i, final work work) {

        workviewholder.name.setText("Name: "+work.getFname());
        workviewholder.problemname.setText("Problem Name: "+work.getPname());
       // workviewholder.des.setText("Description: "+work.getPdesc());
       // workviewholder.add.setText("Address: "+work.getAddress());


    }

    @NonNull
    @Override
    public workviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.work_view_layout, parent, false);

        return new workviewholder(view);
    }

    public class workviewholder extends RecyclerView.ViewHolder {
        EditText name,problemname;
        CardView work;
       //Button accept;
        public workviewholder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.custname);
            problemname=itemView.findViewById(R.id.compname);
            //des=itemView.findViewById(R.id.compdetail);
            //add=itemView.findViewById(R.id.address);
            //accept=itemView.findViewById(R.id.accept);
            work=itemView.findViewById(R.id.card);
            name.setKeyListener(null);
            problemname.setKeyListener(null);

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

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
