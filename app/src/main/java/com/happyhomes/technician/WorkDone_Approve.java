package com.happyhomes.technician;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.AnimationDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class WorkDone_Approve extends AppCompatActivity {
ImageView workdone,call;
EditText a,b,c,d,e;
String uid,id,pid,date;
    String techname,techmob,phoneNo,message;
    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS =0 ;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mFirebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    FirebaseUser user;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_done__approve);
        //animation code
        ConstraintLayout constraintLayout = findViewById(R.id.workcomp);
        AnimationDrawable animationDrawable = (AnimationDrawable) constraintLayout.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(4000);
        animationDrawable.start();
        //end
        mFirebaseAuth=FirebaseAuth.getInstance();
        firebaseDatabase=FirebaseDatabase.getInstance();
        user=FirebaseAuth.getInstance().getCurrentUser();
        uid=user.getUid();
        DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy, HH:mm");
        date = df.format(Calendar.getInstance().getTime());
        id=getIntent().getStringExtra("id");
        Calendar ct=Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy & HH:mm:ss");
        final String date  = dateFormat.format(new Date());
        a=findViewById(R.id.a);
        a.setText(getIntent().getStringExtra("name"));
        a.setKeyListener(null);
        b=findViewById(R.id.b);
        b.setText(getIntent().getStringExtra("pname"));
        b.setKeyListener(null);
        c=findViewById(R.id.c);
        c.setText(getIntent().getStringExtra("pdesc"));
        c.setKeyListener(null);
        d=findViewById(R.id.d);
        d.setText(getIntent().getStringExtra("address"));
        d.setKeyListener(null);
        e=findViewById(R.id.e);
        e.setText(getIntent().getStringExtra("number"));
        e.setKeyListener(null);
        call=findViewById(R.id.callimg);
        pid=getIntent().getStringExtra("pid");
        progressDialog = new ProgressDialog(WorkDone_Approve.this);
        workdone=findViewById(R.id.completeimg);
        workdone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setTitle("Updating Data....");
                progressDialog.show();
               //sendSMSMessage();
                //sendEmail();
                Map<String, Object> data = new HashMap<>();
                data.put("status", "Work Completed");
                data.put("TimeStamp",FieldValue.serverTimestamp());
                data.put("Date_Time",date);
                db.collection("Problem_Accepted").document(id).update(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        //sendSMSMessage();
                        //
                        Map<String, Object> data = new HashMap<>();
                        data.put("status", "Work Completed");
                        db.collection("Problem_Post").document(pid).update(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                sendSMSMessage();
                                progressDialog.dismiss();
                                Toast.makeText(WorkDone_Approve.this,"Work Completed",Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(WorkDone_Approve.this,HomeTech.class);
                                startActivity(i);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressDialog.dismiss();
                                Toast.makeText(WorkDone_Approve.this,"Error in ",Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(WorkDone_Approve.this,"Error",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        e.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                call();
            }
        });
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                call();
            }
        });
    }
    public void call()
    {
        String posted_by =getIntent().getStringExtra("number");
        String uri = "tel:" + posted_by.trim();
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse(uri));
        if (ActivityCompat.checkSelfPermission(WorkDone_Approve.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        startActivity(intent);
    }

    public void sendSMSMessage() {
        phoneNo =getIntent().getStringExtra("number");
        message ="Your Work Is Completed by "+techname +" At "+date+ " You Can Now Rate Technician";
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(phoneNo, null, message, null, null);
        Toast.makeText(getApplicationContext(), "SMS sent.",Toast.LENGTH_LONG).show();
    }
    protected void onStart() {

        super.onStart();
        DocumentReference documentReference1=db.collection("Technician").document(uid);
        documentReference1.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                techname=documentSnapshot.getString("fullname");
                techmob=documentSnapshot.getString("mobile");
            }
        });
    }
    public void checkconnection()
    {
        ConnectivityManager manager=(ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork=manager.getActiveNetworkInfo();

        if(null!=activeNetwork)
        {
            if(activeNetwork.getType()==manager.TYPE_WIFI){
                Toast.makeText(this,"Wifi Enabled",Toast.LENGTH_LONG).show();
            }
            else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                Toast.makeText(this,"Mobile Data Enabled",Toast.LENGTH_LONG).show();
            }

        }
        else {
            dialog();
            //Toast.makeText(this,"No Internet Connection",Toast.LENGTH_LONG).show();
        }
    }
    public void dialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(WorkDone_Approve.this);
        builder.setTitle("Network Connection Error")
                .setMessage("You Have No Internet Connection")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(WorkDone_Approve.this,"No Internet Connection",Toast.LENGTH_SHORT).show();
                    }
                });
        AlertDialog alertDialog=builder.create();
        alertDialog.show();
    }

}
