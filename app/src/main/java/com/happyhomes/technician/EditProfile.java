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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.HashMap;
import java.util.Map;

public class EditProfile extends AppCompatActivity implements View.OnClickListener {
    private FirebaseAuth mFirebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    FirebaseUser user;
    String uid;
    DatabaseReference reff;
    EditText name,number,email,date1;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    Button update;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editprofile);
        //animation code
        ConstraintLayout constraintLayout = findViewById(R.id.edt);
        AnimationDrawable animationDrawable = (AnimationDrawable) constraintLayout.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(2000);
        animationDrawable.start();
        //end
        checkconnection();
        mFirebaseAuth=FirebaseAuth.getInstance();
        firebaseDatabase=FirebaseDatabase.getInstance();
        user=FirebaseAuth.getInstance().getCurrentUser();
        uid=user.getUid();
        name=findViewById(R.id.edtname);
        number=findViewById(R.id.edtmobile);
        email=findViewById(R.id.edtemail);
        date1=findViewById(R.id.edtdob);
        update=findViewById(R.id.btnupdate);
        update.setOnClickListener(this);
        name.setText(getIntent().getStringExtra("name"));
        number.setText(getIntent().getStringExtra("number"));
        email.setText(getIntent().getStringExtra("email"));
    }
    protected void onStart() {
        super.onStart();
        DocumentReference documentReference=db.collection("Technician").document(uid);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                date1.setText(documentSnapshot.getString("dob"));
            }
        });
        /*fIREBASE QUERY
        reff=FirebaseDatabase.getInstance().getReference().child("Technician").child(uid);
        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String date=dataSnapshot.child("dob").getValue().toString();
                date1.setText(date);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/
    }

    @Override
    public void onClick(View v) {
       DocumentReference documentReference=FirebaseFirestore.getInstance().collection("Technician").document(uid);
        //reff=firebaseDatabase.getInstance().getReference().child("Technician").child(uid);
        String fullname=name.getText().toString();
        String mobile=number.getText().toString();
        String mail=email.getText().toString();
        String dob=date1.getText().toString();
        Map<String,Object> objectMap=new HashMap<>();
        objectMap.put("fullname",fullname);
        objectMap.put("mobile",mobile);
        objectMap.put("mail",mail);
        objectMap.put("dob",dob);
        documentReference.update(objectMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(EditProfile.this,"Field Updated",Toast.LENGTH_SHORT).show();
                Intent i=new Intent(EditProfile.this,profileActivity.class);
                startActivity(i);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(EditProfile.this,"Failed Updated",Toast.LENGTH_SHORT).show();
            }
        });
        /*reff.updateChildren(objectMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(EditProfile.this,"Field Updated",Toast.LENGTH_SHORT).show();
                Intent i=new Intent(EditProfile.this,profileActivity.class);
                startActivity(i);
            }
        })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(EditProfile.this,"Failed Updated",Toast.LENGTH_SHORT).show();
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
        AlertDialog.Builder builder = new AlertDialog.Builder(EditProfile.this);
        builder.setTitle("Network Connection Error")
                .setMessage("You Have No Internet Connection")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(EditProfile.this,"No Internet Connection",Toast.LENGTH_SHORT).show();
                    }
                });
        AlertDialog alertDialog=builder.create();
        alertDialog.show();

    }
}
