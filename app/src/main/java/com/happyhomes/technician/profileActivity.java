package com.happyhomes.technician;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
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

public class profileActivity extends AppCompatActivity {
    TextView name,number,email,emailverify;
    EditText a,b,c,d;
    Button editprofile;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    FirebaseUser user;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    String uid;
    DatabaseReference reff;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        //animation code
        ConstraintLayout constraintLayout = findViewById(R.id.viewprofile);
        AnimationDrawable animationDrawable = (AnimationDrawable) constraintLayout.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(4000);
        animationDrawable.start();
        //end
        checkconnection();
        name=findViewById(R.id.name);
        number=findViewById(R.id.number);
        email=findViewById(R.id.email);
        emailverify=findViewById(R.id.emailverify);
        a=findViewById(R.id.a);
        b=findViewById(R.id.b);
        c=findViewById(R.id.c);
        d=findViewById(R.id.d);
        editprofile=findViewById(R.id.edtprofile);
        mFirebaseAuth=FirebaseAuth.getInstance();
        firebaseDatabase=FirebaseDatabase.getInstance();
        user=FirebaseAuth.getInstance().getCurrentUser();
        uid=user.getUid();
        if(user.isEmailVerified())
        {
            emailverify.setText("");
        }
        else
        {
            emailverify.setText("Email Is Not Verified Click Here To Verify");
            emailverify.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(profileActivity.this,"Verification Link Send To Registered Email-id",Toast.LENGTH_LONG).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(profileActivity.this,"Error While Sending Verification Link",Toast.LENGTH_LONG).show();
                        }
                    });
                }
            });
        }
        DocumentReference documentReference=db.collection("Technician").document(uid);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                a.setKeyListener(null);
                b.setKeyListener(null);
                c.setKeyListener(null);
                d.setKeyListener(null);
                a.setText(documentSnapshot.getString("fullname"));
                b.setText(documentSnapshot.getString("mobile"));
                c.setText(documentSnapshot.getString("mail"));
                d.setText(documentSnapshot.getString("category"));
            }
        });
        editprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(profileActivity.this,EditProfile.class);
                i.putExtra("name",a.getText().toString());
                i.putExtra("number",b.getText().toString());
                i.putExtra("email",c.getText().toString());
                startActivity(i);
            }
        });
    }
    protected void onStart() {
        super.onStart();

        /*reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String fullname=dataSnapshot.child("fullname").getValue().toString();
                String mobile=dataSnapshot.child("mobile").getValue().toString();
                String mail=dataSnapshot.child("mail").getValue().toString();
                String category=dataSnapshot.child("category").getValue().toString();
                a.setText(fullname);
                b.setText(mobile);
                c.setText(mail);
                d.setText(category);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });*/
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
               // Toast.makeText(this,"Mobile Data Enabled",Toast.LENGTH_LONG).show();
            }

        }
        else {
            dialog();
            //Toast.makeText(this,"No Internet Connection",Toast.LENGTH_LONG).show();
        }
    }
    public void dialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(profileActivity.this);
        builder.setTitle("Network Connection Error")
                .setMessage("You Have No Internet Connection")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(profileActivity.this,"No Internet Connection",Toast.LENGTH_SHORT).show();
                    }
                });
        AlertDialog alertDialog=builder.create();
        alertDialog.show();

    }
}
