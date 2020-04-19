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
import android.widget.Button;
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
import java.util.HashMap;
import java.util.Map;

public class WorkAcceptActivity extends AppCompatActivity {
    EditText a, b, c,d,e;
    Button yes, no;
    ImageView call;
    String id, uid,date;
    String techname, techmob,phoneNo,message;
    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS =0 ;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mFirebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    FirebaseUser user;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_accept);
        //animation code
        ConstraintLayout constraintLayout = findViewById(R.id.workacc);
        AnimationDrawable animationDrawable = (AnimationDrawable) constraintLayout.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(4000);
        animationDrawable.start();
        //end
        mFirebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();
        DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy, HH:mm");
        date = df.format(Calendar.getInstance().getTime());
        id = getIntent().getStringExtra("id");
        a = findViewById(R.id.a);
        a.setText(getIntent().getStringExtra("name"));
        a.setKeyListener(null);
        b = findViewById(R.id.b);
        b.setText(getIntent().getStringExtra("pname"));
        b.setKeyListener(null);
        c = findViewById(R.id.c);
        c.setText(getIntent().getStringExtra("pdesc"));
        c.setKeyListener(null);
        d = findViewById(R.id.d);
        d.setText(getIntent().getStringExtra("address"));
        d.setKeyListener(null);
        e = findViewById(R.id.e);
        e.setText(getIntent().getStringExtra("number"));
        e.setKeyListener(null);
        yes = findViewById(R.id.accept);
        no = findViewById(R.id.deny);
        call = findViewById(R.id.callimg);
        progressDialog = new ProgressDialog(WorkAcceptActivity.this);
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setTitle("Work Accepting...");
                progressDialog.show();
                //sendSMSMessage();
                Map<String, Object> data = new HashMap<>();
                data.put("status", "Work Accepted");
                data.put("tech_Id", uid);
                db.collection("Problem_Post").document(id).update(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                       sendSMSMessage();
                        Map<String, Object> data = new HashMap<>();
                        data.put("user_id", getIntent().getStringExtra("userid"));
                        data.put("tech_id", uid);
                        data.put("tech_name",techname);
                        data.put("tech_mob",techmob);
                        data.put("pid",id);
                        data.put("fname", getIntent().getStringExtra("name"));
                        data.put("pname", getIntent().getStringExtra("pname"));
                        data.put("pdesc", getIntent().getStringExtra("pdesc"));
                        data.put("pname", getIntent().getStringExtra("pname"));
                        data.put("category", getIntent().getStringExtra("category"));
                        data.put("address", getIntent().getStringExtra("address"));
                        data.put("mobile", getIntent().getStringExtra("number"));
                        data.put("mail", getIntent().getStringExtra("emailid"));
                        data.put("status", "Work Accepted");
                        db.collection("Problem_Accepted")
                                .add(data).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                //sendSMSMessage();
                                progressDialog.dismiss();
                                Toast.makeText(WorkAcceptActivity.this, "Work Accepted", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(WorkAcceptActivity.this, HomeTech.class);
                                startActivity(i);
                            }
                        })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        progressDialog.dismiss();
                                        Toast.makeText(WorkAcceptActivity.this, "Error Or CheckInternet Connection", Toast.LENGTH_SHORT).show();
                                    }
                                });

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(WorkAcceptActivity.this, "Error Or CheckInternet Connection", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> data = new HashMap<>();
                data.put("Tech_Id", FieldValue.delete());
                db.collection("Problem_Post").document(id).update(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        Toast.makeText(WorkAcceptActivity.this, "Work Rejected", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(WorkAcceptActivity.this, HomeTech.class);
                        startActivity(i);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Toast.makeText(WorkAcceptActivity.this, "Error Or CheckInternet Connection", Toast.LENGTH_SHORT).show();
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

    public void call() {
        String posted_by = getIntent().getStringExtra("number");
        String uri = "tel:" + posted_by.trim();
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse(uri));
        if (ActivityCompat.checkSelfPermission(WorkAcceptActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        startActivity(intent);
    }

    protected void onStart() {

        super.onStart();
        DocumentReference documentReference1 = db.collection("Technician").document(uid);
        documentReference1.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                techname = documentSnapshot.getString("fullname");
                techmob = documentSnapshot.getString("mobile");
            }
        });
    }
    public void sendSMSMessage() {
        phoneNo =getIntent().getStringExtra("number");
        message ="Your Work Is Accepted by " + techname +" At "+date+ " Technician Contact Number Is " + techmob;
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(phoneNo, null, message, null, null);
        Toast.makeText(getApplicationContext(), "SMS sent.",Toast.LENGTH_LONG).show();
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
        AlertDialog.Builder builder = new AlertDialog.Builder(WorkAcceptActivity.this);
        builder.setTitle("Network Connection Error")
                .setMessage("You Have No Internet Connection")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(WorkAcceptActivity.this,"No Internet Connection",Toast.LENGTH_SHORT).show();
                    }
                });
        AlertDialog alertDialog=builder.create();
        alertDialog.show();

    }

}