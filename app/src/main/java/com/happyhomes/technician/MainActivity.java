package com.happyhomes.technician;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    Button login_btn;
    EditText email,upass;
    TextView signup,forgotpass;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    ProgressDialog progressDialog ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //animation code
        LinearLayout constraintLayout = findViewById(R.id.main);
        AnimationDrawable animationDrawable = (AnimationDrawable) constraintLayout.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(2000);
        animationDrawable.start();
        //end
        checkpermisson();
        checkconnection();
        mFirebaseAuth = FirebaseAuth.getInstance();
        email=(EditText)findViewById(R.id.email);
        upass=(EditText)findViewById(R.id.password);
        signup=(TextView) findViewById(R.id.txtsignup);
        login_btn=(Button)findViewById(R.id.login_btn);
        forgotpass=findViewById(R.id.forgetpass);
        forgotpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, Password_reset.class);
                startActivity(i);
            }
        });
        progressDialog = new ProgressDialog(MainActivity.this);
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser mFirebaseUser = mFirebaseAuth.getCurrentUser();
                if( mFirebaseUser != null ){
                    Toast.makeText(MainActivity.this,"You are logged in",Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(MainActivity.this, HomeTech.class);
                    startActivity(i);
                }
                else{
                    Toast.makeText(MainActivity.this,"Please Login",Toast.LENGTH_SHORT).show();
                }
            }
        };
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intSignUp = new Intent(MainActivity.this, techsignup.class);
                startActivity(intSignUp);
            }
        });
        login_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                String mail=email.getText().toString();
                String pwd=upass.getText().toString();
                progressDialog.setTitle("Verifying Details...");
                progressDialog.show();
                if((mail.isEmpty()))
                {
                    progressDialog.dismiss();
                    email.setError("Username Cannot be Empty");
                    email.requestFocus();

                }
                else if(pwd.isEmpty())
                {
                    progressDialog.dismiss();
                    upass.setError("Password Cannot be Empty");
                    upass.requestFocus();

                }

                else  if(!(email.getText().toString().isEmpty() && upass.getText().toString().isEmpty())) {
                    mFirebaseAuth.signInWithEmailAndPassword(mail, pwd).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {


                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                progressDialog.dismiss();
                                Toast.makeText(MainActivity.this, "Login Error, Please Login Again", Toast.LENGTH_SHORT).show();
                            } else {
                                progressDialog.dismiss();
                                finish();
                                Intent intToHome = new Intent(MainActivity.this, HomeTech.class);
                                startActivity(intToHome);
                            }
                        }
                    });
                }
                else{
                    progressDialog.dismiss();
                    Toast.makeText(MainActivity.this,"Error Occurred!",Toast.LENGTH_LONG).show();

                }

            }
        });
    };

    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mFirebaseAuth.getCurrentUser();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);

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
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Network Connection Error")
                .setMessage("You Have No Internet Connection")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(MainActivity.this,"No Internet Connection",Toast.LENGTH_SHORT).show();
                    }
                });
        AlertDialog alertDialog=builder.create();
        alertDialog.show();

    }

}

