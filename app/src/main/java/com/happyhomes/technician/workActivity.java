package com.happyhomes.technician;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

//import com.google.firebase.database.Query;

public class workActivity extends AppCompatActivity {
    private static final String TAG ="Firelogs" ;
    RecyclerView works;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    FirebaseUser user;
    DatabaseReference reff;
    String uid,cate,st;
    TextView cp,nowork;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference workRef = db.collection("Problem_Post");
   // private FirestoreRecyclerAdapter adapter;
    private workAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work);
        //animation code
        ConstraintLayout constraintLayout = findViewById(R.id.worklist);
        AnimationDrawable animationDrawable = (AnimationDrawable) constraintLayout.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(4000);
        animationDrawable.start();
        //end
        checkconnection();
        works = findViewById(R.id.work);
        //nowork=findViewById(R.id.nowork);
        //nowork.setText("Currently You Don`t have any Work Request");
        works.setLayoutManager(new LinearLayoutManager(this));
        works.setHasFixedSize(true);
        mFirebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();
        cp = findViewById(R.id.textView8);
        cate = getIntent().getStringExtra("type");
        Query query = workRef.whereEqualTo("category",cate).whereEqualTo("status","n/a").orderBy("date");
        FirestoreRecyclerOptions<work> options =new FirestoreRecyclerOptions.Builder<work>().setQuery(query,work.class).build();
        adapter = new workAdapter(options);
        /*if(adapter.getItemCount()==0){

            nowork.setText("");
            works.setAdapter(adapter);
        }
        else
        {

            nowork.setText("");
        }*/
        works.setAdapter(adapter);
        adapter.setOnItemClickListener(new workAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                work Work=documentSnapshot.toObject(work.class);
                String id = documentSnapshot.getId();
                String path = documentSnapshot.getReference().getPath();
                String name=documentSnapshot.getString("fname");
                String pname=documentSnapshot.getString("pname");
                String pdesc=documentSnapshot.getString("pdesc");
                String add=documentSnapshot.getString("address");
                String mobile=documentSnapshot.getString("mobile");
                String userid=documentSnapshot.getString("uid");
                String email=documentSnapshot.getString("mail");
                String category=documentSnapshot.getString("category");
                Intent i = new Intent(workActivity.this,WorkAcceptActivity.class);
                i.putExtra("id",id);
                i.putExtra("name",name);
                i.putExtra("pname",pname);
                i.putExtra("pdesc",pdesc);
                i.putExtra("address",add);
                i.putExtra("number",mobile);
                i.putExtra("userid",userid);
                i.putExtra("emailid",email);
                i.putExtra("category",category);
                startActivity(i);

            }
        });


    }

    private void setUpRecyclerView() {

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
                //Toast.makeText(this,"Wifi Enabled",Toast.LENGTH_SHORT).show();
            }
            else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
               // Toast.makeText(this,"Mobile Data Enabled",Toast.LENGTH_SHORT).show();
            }

        }
        else {
            dialog();
            //Toast.makeText(this,"No Internet Connection",Toast.LENGTH_LONG).show();
        }
    }
    public void dialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(workActivity.this);
        builder.setTitle("Network Connection Error")
                .setMessage("You Have No Internet Connection")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(workActivity.this,"No Internet Connection",Toast.LENGTH_SHORT).show();
                    }
                });
        AlertDialog alertDialog=builder.create();
        alertDialog.show();

    }
}
