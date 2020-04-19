package com.happyhomes.technician;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.List;

public class HomeTech extends AppCompatActivity {
    ImageView work,workdone,user,contactus,exit,history,review;
    TextView work1,workdone1,user1,contactus1,exit1,history1,welcome,custreview;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private FirebaseDatabase firebaseDatabase;
    FirebaseUser home;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    DatabaseReference reff;
    String uid,cat,id,name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        //animation code
        ConstraintLayout constraintLayout = findViewById(R.id.home);
        AnimationDrawable animationDrawable = (AnimationDrawable) constraintLayout.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(2000);
        animationDrawable.start();
        //end
        checkpermisson();
        checkconnection();
        mFirebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase=FirebaseDatabase.getInstance();
        home=FirebaseAuth.getInstance().getCurrentUser();
        uid=home.getUid();
        work=findViewById(R.id.work);
        workdone=findViewById(R.id.workdone);
        history=findViewById(R.id.history);
        contactus=findViewById(R.id.conctact);
        //welcome=findViewById(R.id.welcome);
        user=findViewById(R.id.user1);
        exit=findViewById(R.id.exit);
        work1=findViewById(R.id.worktv);
        workdone1=findViewById(R.id.WorkDone);
        history1=findViewById(R.id.History);
        contactus1=findViewById(R.id.conctactus);
        review=findViewById(R.id.review);
        custreview=findViewById(R.id.custreview);
        user1=findViewById(R.id.profile);
        exit1=findViewById(R.id.logout);
            work.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    worksfun();
                }
            });
            work1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    worksfun();
                }
            });
            workdone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    workdonefun();
                }
            });
            workdone1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    workdonefun();
                }
            });
            history.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    historyfun();
                }
            });
            history1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    historyfun();
                }
            });
            contactus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    contactusfun();
                }
            });
            contactus1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    contactusfun();
                }
            });
            user.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    profilefun();
                }
            });
            user1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    profilefun();
                }
            });
            review.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    reviewfun();
                }
            });
            custreview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    reviewfun();
                }
            });
            exit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    logoutfun();
                }
            });
            exit1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    logoutfun();
                }
            });
        }

    public void worksfun()
    {
        Intent i = new Intent(HomeTech.this,workActivity.class);
        i.putExtra("type",cat);
        startActivity(i);
    }
    public void workdonefun()
    {
        Intent i = new Intent(HomeTech.this,workdoneActivity.class);
        i.putExtra("type",cat);
        startActivity(i);
    }
    public void historyfun()
    {
        Intent i = new Intent(HomeTech.this,historyActivity.class);
        i.putExtra("type",cat);
        startActivity(i);
    }
    public void contactusfun()
    {
        Intent i = new Intent(HomeTech.this,contactusActivity.class);
        startActivity(i);
    }
    public void profilefun()
    {
        Intent i = new Intent(HomeTech.this,profileActivity.class);
        startActivity(i);
    }
    public void logoutfun()
    {
        mFirebaseAuth.signOut();
        finish();
        startActivity(new Intent(HomeTech.this,MainActivity.class));
    }
    public void reviewfun()
    {
        Intent i = new Intent(HomeTech.this,View_feedbackActivity.class);
        startActivity(i);
    }
    protected void onStart() {
        super.onStart();
        checkconnection();
        checkconnection();
        DocumentReference documentReference=db.collection("Technician").document(uid);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                cat=documentSnapshot.getString("category");
                name=documentSnapshot.getString("fullname").trim();
                //welcome.setText("Happy Homes Welcomes You " +name);
            }
        });
        db.collection("Problem_Post").whereEqualTo("status","n/a").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        id=document.getId();
                    }
                } else {
                    Toast.makeText(HomeTech.this,"Error",Toast.LENGTH_SHORT).show();
                }
            }
        });
       Task<QuerySnapshot> query=db.collection("Problem_Post").whereEqualTo("Categoey",cat).get();



        /*reff= FirebaseDatabase.getInstance().getReference().child("Technician").child(uid);
        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                cat=dataSnapshot.child("category").getValue().toString();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });*/
    }
    public void checkpermisson()
    {
        Dexter.withActivity(this).withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.CALL_PHONE,
                Manifest.permission.SEND_SMS,
                Manifest.permission.INTERNET,
                Manifest.permission.ACCESS_FINE_LOCATION).withListener(new MultiplePermissionsListener() {
            @Override
            public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                //
            }

            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                permissionToken.continuePermissionRequest();
            }
        }).check();
    }

    public void checkconnection()
    {
        ConnectivityManager manager=(ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork=manager.getActiveNetworkInfo();

        if(null!=activeNetwork)
        {
            if(activeNetwork.getType()==manager.TYPE_WIFI){
                //Toast.makeText(this,"Wifi Enabled",Toast.LENGTH_LONG).show();
            }
            else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                //Toast.makeText(this,"Mobile Data Enabled",Toast.LENGTH_LONG).show();
            }
        }
        else {
            dialog();
            //Toast.makeText(this,"No Internet Connection",Toast.LENGTH_LONG).show();
        }
    }
    public void dialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(HomeTech.this);
        builder.setTitle("Network Connection Error")
                .setMessage("You Have No Internet Connection")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(HomeTech.this,"No Internet Connection",Toast.LENGTH_SHORT).show();
                    }
                });
        AlertDialog alertDialog=builder.create();
        alertDialog.show();

    }

}

