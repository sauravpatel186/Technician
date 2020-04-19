package com.happyhomes.technician;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;

public class feedbackAdapter extends FirestoreRecyclerAdapter<feedback,feedbackAdapter.feedbackviewholder>{
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    public feedbackAdapter(FirestoreRecyclerOptions<feedback> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(final feedbackviewholder feedbackviewholder, final int i, final feedback feedback) {

        Log.d("a",feedback.getC_name());
        feedbackviewholder.a.setText(feedback.getC_name());
        feedbackviewholder.b.setText(feedback.getPname());
        feedbackviewholder.c.setText(feedback.getPdesc());
        String rating=feedback.getRating();
        float rating1 =Float.parseFloat(String.valueOf(rating));
        feedbackviewholder.rating.setRating(rating1);
        feedbackviewholder.comment.setText(feedback.getComment());
    }

    @NonNull
    @Override
    public feedbackviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_feedback_recycler_, parent, false);
        return new feedbackviewholder(view);
    }

    public class feedbackviewholder extends RecyclerView.ViewHolder {
        TextView name,problemname,des;
        RatingBar rating;
        EditText comment,a,b,c;
        public feedbackviewholder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.custnamefeedback);
            problemname=itemView.findViewById(R.id.compnamefeedback);
            des=itemView.findViewById(R.id.compdetailfeedback);
           rating=itemView.findViewById(R.id.ratingBar);
            comment=itemView.findViewById(R.id.comment);
            //comment.setFocusable(false);
            comment.setKeyListener(null);
            a=itemView.findViewById(R.id.a);
            a.setKeyListener(null);
            b=itemView.findViewById(R.id.b);
            b.setKeyListener(null);
            c=itemView.findViewById(R.id.c);
            c.setKeyListener(null);
        }
    }
}
