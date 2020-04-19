package com.happyhomes.technician;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class Password_reset extends AppCompatActivity {
    EditText email;
    Button reset;
    private FirebaseAuth mFirebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_reset);
        //animation code
        ConstraintLayout constraintLayout = findViewById(R.id.a);
        AnimationDrawable animationDrawable = (AnimationDrawable) constraintLayout.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(2000);
        animationDrawable.start();
        //end
        email=findViewById(R.id.email);
        reset=findViewById(R.id.resetbtn);
        mFirebaseAuth = FirebaseAuth.getInstance();
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String useremail=email.getText().toString().toLowerCase().trim();
                if(useremail.equals(""))
                {
                    Toast.makeText(Password_reset.this,"Please Enter Registered Email-ID",Toast.LENGTH_SHORT).show();
                }
                else {
                    mFirebaseAuth.sendPasswordResetEmail(useremail).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful())
                                {
                                    Toast.makeText(Password_reset.this,"Password Reset Link Sent To Email ID",Toast.LENGTH_SHORT).show();
                                    finish();
                                    startActivity(new Intent(Password_reset.this,MainActivity.class));
                                }
                                else
                                {
                                    Toast.makeText(Password_reset.this,"ERROR IN SENDING PASSWORD RESET LINK",Toast.LENGTH_SHORT).show();
                                }
                        }
                    });
                }
            }
        });
    }
}
