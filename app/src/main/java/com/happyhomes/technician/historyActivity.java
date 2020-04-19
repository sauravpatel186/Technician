package com.happyhomes.technician;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.AnimationDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
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

public class historyActivity extends AppCompatActivity {
RecyclerView history;
private historyAdapter adapter;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference workRef = db.collection("Problem_Accepted");
    private FirebaseAuth mFirebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    FirebaseUser user;
    String uid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        //animation code
        ConstraintLayout constraintLayout = findViewById(R.id.his);
        AnimationDrawable animationDrawable = (AnimationDrawable) constraintLayout.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(2000);
        animationDrawable.start();
        //end
        checkconnection();
        history=findViewById(R.id.history);
        history.setLayoutManager(new LinearLayoutManager(this));
        mFirebaseAuth=FirebaseAuth.getInstance();
        firebaseDatabase=FirebaseDatabase.getInstance();
        user=FirebaseAuth.getInstance().getCurrentUser();
        uid=user.getUid();
        String cate=getIntent().getStringExtra("type");
        Query query = workRef.whereEqualTo("tech_id",uid).whereEqualTo("status","Work Completed").orderBy("Date_Time");
        FirestoreRecyclerOptions<work> options =new FirestoreRecyclerOptions.Builder<work>().setQuery(query,work.class).build();
        adapter = new historyAdapter(options);
        history.setAdapter(adapter);
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
        AlertDialog.Builder builder = new AlertDialog.Builder(historyActivity.this);
        builder.setTitle("Network Connection Error")
                .setMessage("You Have No Internet Connection")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(historyActivity.this,"No Internet Connection",Toast.LENGTH_SHORT).show();
                    }
                });
        AlertDialog alertDialog=builder.create();
        alertDialog.show();

    }
}
