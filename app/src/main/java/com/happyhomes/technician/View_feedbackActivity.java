package com.happyhomes.technician;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.AnimationDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class View_feedbackActivity extends AppCompatActivity {private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference workRef = db.collection("Feedback");
    private FirebaseAuth mFirebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    FirebaseUser user;
    String uid,rate;
    RecyclerView feedback1;
    private feedbackAdapter adapter;
    Spinner spinner;
    int pos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_feedback);
        //animation code
        ConstraintLayout constraintLayout = findViewById(R.id.feedback);
        AnimationDrawable animationDrawable = (AnimationDrawable) constraintLayout.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(2000);
        animationDrawable.start();
        //end
        spinner=findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter1=ArrayAdapter.createFromResource(this,R.array.rating,android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter1);
        feedback1=findViewById(R.id.feedback1);
        feedback1.setLayoutManager(new LinearLayoutManager(this));
        mFirebaseAuth=FirebaseAuth.getInstance();
        firebaseDatabase=FirebaseDatabase.getInstance();
        user=FirebaseAuth.getInstance().getCurrentUser();
        uid=user.getUid();
        String cate=getIntent().getStringExtra("type");
        Query query = workRef.whereEqualTo("tech_id",uid);
        FirestoreRecyclerOptions<feedback> options =new FirestoreRecyclerOptions.Builder<feedback>().setQuery(query,feedback.class).build();
        adapter = new feedbackAdapter(options);
        feedback1.setAdapter(adapter);
        /*spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                pos=position;
                rate=spinner.getItemAtPosition(position).toString();
                if(rate.equals("1 Star"))
                {
                    one();
                }
                if(rate.equals("2 Star")){
                    two();
                }
                if(rate.equals("3 Star")){
                    three();
                }
                if(rate.equals("4 Star")){
                    four();
                }
                if(rate.equals("5 Star")){
                    five();
                }
                if(rate.equals("All"))
                {
                    all();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
            */

    }
    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
    public void checkconnection()
    {
        ConnectivityManager manager=(ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork=manager.getActiveNetworkInfo();

        if(null!=activeNetwork)
        {
            if(activeNetwork.getType()==manager.TYPE_WIFI){
                // Toast.makeText(this,"Wifi Enabled",Toast.LENGTH_SHORT).show();
            }
            else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                //Toast.makeText(this,"Mobile Data Enabled",Toast.LENGTH_SHORT).show();
            }

        }
        else {
            dialog();
            //Toast.makeText(this,"No Internet Connection",Toast.LENGTH_LONG).show();
        }
    }
    public void dialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(View_feedbackActivity.this);
        builder.setTitle("Network Connection Error")
                .setMessage("You Have No Internet Connection")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(View_feedbackActivity.this,"No Internet Connection",Toast.LENGTH_SHORT).show();
                    }
                });
        AlertDialog alertDialog=builder.create();
        alertDialog.show();

    }
    public void one()
    {
        Toast.makeText(this,"1 Star",Toast.LENGTH_SHORT).show();
        Query query1 = workRef.whereEqualTo("tech_id",uid).whereEqualTo("ratinng","1");
        FirestoreRecyclerOptions<feedback> options =new FirestoreRecyclerOptions.Builder<feedback>().setQuery(query1,feedback.class).build();
        adapter = new feedbackAdapter(options);
        feedback1.setAdapter(adapter);
    }
    public void two()
    {
        Toast.makeText(this,"2 Star",Toast.LENGTH_SHORT).show();
        Query query2 = workRef.whereEqualTo("tech_id",uid).whereEqualTo("ratinng","2");
        FirestoreRecyclerOptions<feedback> options =new FirestoreRecyclerOptions.Builder<feedback>().setQuery(query2,feedback.class).build();
        adapter = new feedbackAdapter(options);
        feedback1.setAdapter(adapter);
    }
    public void  three()
    {
        Query query3 = workRef.whereEqualTo("tech_id",uid).whereEqualTo("ratinng","3");
        FirestoreRecyclerOptions<feedback> options =new FirestoreRecyclerOptions.Builder<feedback>().setQuery(query3,feedback.class).build();
        adapter = new feedbackAdapter(options);
        feedback1.setAdapter(adapter);
    }
    public void four()
    {
        Query query4 = workRef.whereEqualTo("tech_id",uid).whereEqualTo("ratinng","4");
        FirestoreRecyclerOptions<feedback> options =new FirestoreRecyclerOptions.Builder<feedback>().setQuery(query4,feedback.class).build();
        adapter = new feedbackAdapter(options);
        feedback1.setAdapter(adapter);
    }
    public void five()
    {
        Query query5 = workRef.whereEqualTo("tech_id",uid).whereEqualTo("ratinng","5");
        FirestoreRecyclerOptions<feedback> options =new FirestoreRecyclerOptions.Builder<feedback>().setQuery(query5,feedback.class).build();
        adapter = new feedbackAdapter(options);
        feedback1.setAdapter(adapter);
    }
    public void all(){
        Query querya = workRef.whereEqualTo("tech_id",uid);
        FirestoreRecyclerOptions<feedback> options =new FirestoreRecyclerOptions.Builder<feedback>().setQuery(querya,feedback.class).build();
        adapter = new feedbackAdapter(options);
        feedback1.setAdapter(adapter);
    }
}
