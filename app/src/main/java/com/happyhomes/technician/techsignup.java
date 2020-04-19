package com.happyhomes.technician;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class techsignup extends AppCompatActivity {
    private EditText  r_pass, r_conpass, r_mobile, r_dob, r_mail, r_address, r_fullname;
    private String name, pass, conpass, mail, address, fullname, mobile;
    private static final String PASSWORD_PATTERN = "(^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=](?=\\S+$)).{7,}$)";
    private static final String PHONE_PATTERN = "^[7-9][0-9]{9}$";
    Button signup,pi,ci;;
    Button btnbrowse, btnupload;
    EditText txtdata ;
    Spinner spinner;
    ImageView imgview;
    Uri FilePathUri;
    String url;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    FirebaseUser user;
    String uid;
    StorageReference storageReference;
    DatabaseReference databaseReference;
    int Image_Request_Code = 7;
    ProgressDialog progressDialog ;
    RadioButton r1,r2;
    RadioGroup rg;
    TextView gender;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_techsignup);
        //animation code
        RelativeLayout constraintLayout = findViewById(R.id.techsignup);
        AnimationDrawable animationDrawable = (AnimationDrawable) constraintLayout.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(4000);
        animationDrawable.start();
        //end
        mFirebaseAuth = FirebaseAuth.getInstance();
        r_address=(EditText)findViewById(R.id.r_address);
        r_fullname=(EditText)findViewById(R.id.r_fname);
        r_dob=(EditText) findViewById(R.id.r_dob);
        gender=(TextView)findViewById(R.id.gender);
        rg=(RadioGroup)findViewById(R.id.r_grp);
        int selectedid=rg.getCheckedRadioButtonId();
        r1=(RadioButton)findViewById(R.id.radio1);
        r2=(RadioButton)findViewById(R.id.radio2);
        r_pass = (EditText) findViewById(R.id.r_password);
        r_conpass = (EditText) findViewById(R.id.r_confirmpassword);
        r_mobile = (EditText) findViewById(R.id.r_mobileno);
        r_mail = (EditText) findViewById(R.id.r_email);
        spinner=findViewById(R.id.techlist);
        firebaseDatabase=FirebaseDatabase.getInstance();
        signup = (Button) findViewById(R.id.signup_btn);
        storageReference = FirebaseStorage.getInstance().getReference("Certificates");
        databaseReference = FirebaseDatabase.getInstance().getReference("Certificates");
        btnbrowse = (Button)findViewById(R.id.btnbrowse);
        //txtdata = (EditText)findViewById(R.id.txtdata);
        imgview = (ImageView)findViewById(R.id.image_view);
        progressDialog = new ProgressDialog(techsignup.this);// context name as per your project name
        btnbrowse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Image"), Image_Request_Code);
            }
        });
        ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(this,R.array.techlist,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                register();

            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Image_Request_Code && resultCode == RESULT_OK && data != null && data.getData() != null) {

            FilePathUri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), FilePathUri);
                imgview.setImageBitmap(bitmap);
            }
            catch (IOException e) {

                e.printStackTrace();
            }
        }
    }


    public String GetFileExtension(Uri uri) {

        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri)) ;

    }


    public void UploadImage() {

        if (FilePathUri != null) {

            progressDialog.setTitle("Image is Uploading...");
            progressDialog.show();
            StorageReference storageReference2 = storageReference.child(System.currentTimeMillis() + "." + GetFileExtension(FilePathUri));
            storageReference2.putFile(FilePathUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            String TempImageName =r_fullname.getText().toString().trim();

                            Toast.makeText(getApplicationContext(), "Image Uploaded Successfully ", Toast.LENGTH_SHORT).show();
                            @SuppressWarnings("VisibleForTests")
                            uploadinfo imageUploadInfo = new uploadinfo(TempImageName,url=taskSnapshot.getUploadSessionUri().toString());
                            String ImageUploadId = databaseReference.child(uid).getKey();
                            databaseReference.child(ImageUploadId).setValue(imageUploadInfo);
                           Map<String, Object> docData = new HashMap<>();
                            docData.put("Certificate_name",TempImageName);
                            docData.put("Certificate_url",taskSnapshot.getUploadSessionUri().toString());
                            db.collection("Certificate").document(uid)
                                 .set(docData).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    progressDialog.dismiss();
                                }
                            });
                        }
                    });
        }
        else {

            Toast.makeText(techsignup.this, "Please Select Image or Add Image Name", Toast.LENGTH_LONG).show();

        }
    }
    public class uploadinfo {

        public String imageName;
        public String imageURL;
        public uploadinfo(){
        }

        public uploadinfo(String name, String url) {
            this.imageName = name;
            this.imageURL = url;
        }
        public String getImageName() {
            return imageName;
        }
        public String getImageURL() {
            return imageURL;
        }
    }

    public void register() {
        final String fullname=r_fullname.getText().toString().trim();
        final String address=r_address.getText().toString().trim();
        final String dob=r_dob.getText().toString().trim();
        final String status="pending";
        final String mobile=r_mobile.getText().toString().trim();
        final String mail=r_mail.getText().toString().trim();
        final String pass=r_conpass.getText().toString();
        //final String imagename=url;
        final String category=spinner.getSelectedItem().toString();

        intialize(); //initialize the input to  string variable

        if (!validate()) {
            Toast.makeText(this, "SignUp Has Failed", Toast.LENGTH_SHORT).show();
        } else {
            signup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    mFirebaseAuth.createUserWithEmailAndPassword(mail,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful())
                            {
                                progressDialog.setTitle("Registering........");
                                progressDialog.show();
                                user=FirebaseAuth.getInstance().getCurrentUser();
                                uid=user.getUid();
                                String fullname=r_fullname.getText().toString().trim();
                                String address=r_address.getText().toString().trim();
                                String dob=r_dob.getText().toString().trim();
                                String status="pending";
                                String mobile=r_mobile.getText().toString().trim();
                                String mail=r_mail.getText().toString().trim();
                                String pass=r_conpass.getText().toString();
                                //String imagename=getImageURL();
                                String category=spinner.getSelectedItem().toString();
                                /* Map<String, Object> technician = new HashMap<>();
                                technician.put("fullname",fullname);
                                technician.put("address",address);
                                technician.put("dob",dob);
                                technician.put("mobile",mobile);
                                technician.put("mail",mail);
                                technician.put("pass",pass);
                                technician.put("imagename",imagename);
                                technician.put("status",status);
                                technician.put("category",category);*/
                                UploadImage();
                                DocumentReference dbtech=db.collection("Technician").document(uid);
                                techniciandetails Technician=new techniciandetails(fullname,address,status,mobile,mail,pass,category,dob);
                                dbtech.set(Technician).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(techsignup.this, "Registration Successfull", Toast.LENGTH_LONG).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(techsignup.this, "Registration Unsuccessfull"+e, Toast.LENGTH_LONG).show();
                                    }
                                });
                                /*User user1 = new User(fullname, address, dob,mobile,mail,pass,imagename,status,category);
                                FirebaseDatabase.getInstance().getReference("Technician")
                                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(user1).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(techsignup.this, "Registration Successfull", Toast.LENGTH_LONG).show();
                                        } else {
                                            //display a failure message
                                            Toast.makeText(techsignup.this, "Registration Unsuccessfull", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });*/
                            }

                        }
                    });

                }
            });
        }

    }



    public boolean validate() {
        boolean valid = true;
        int isSelected=rg.getCheckedRadioButtonId();

        if (pass.isEmpty()) {
            r_pass.setError("Please Enter Password"); //Password is Empty
            valid = false;
        } else if (pass.length() < 7) {
            r_pass.setError("Password Cannot Be Less Than 8 Characters ");//Password Not Contained 7 Letters
            valid = false;
        } else {

            if (!pass.matches(PASSWORD_PATTERN)) {
                r_pass.setError("Password Should Contain atleast one Lowercase letter,one Uppercase,one Special Character and number ");
                valid = false;
            }
        }
        if (mail.isEmpty()) {//EMail is EMpty
            r_mail.setError("Email Cannot be Empty");
            valid = false;
        } else  {//EMail not Empty
            String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
            if (!mail.matches(emailPattern))//Check Pattern
            {
                r_mail.setError("Not Valid Email Address");
                valid = false;
            }
        }

        if (!conpass.equals(pass))//Password and Confirm password MAtch {
        {
            r_conpass.setError("Password Not Matched");
            valid = false;
        }
        if(!mobile.matches(PHONE_PATTERN))

        {
            r_mobile.setError("Mobile Number Cannot Less Than 10 or More Than 10");
            valid=false;
        }
        if(fullname.isEmpty())
        {
            r_fullname.setError("Cannot be Empty");
            valid=false;
        }
        if(address.isEmpty())
        {
            r_address.setError("Cannot Be Empty");
            valid=false;
        }
        if(isSelected==-1)
        {
            gender.setError("Select Any One Gender");
            valid=false;
        }
        return valid;

    }
    public void intialize()
    {
        address=r_address.getText().toString();
        fullname=r_fullname.getText().toString();
        pass=r_pass.getText().toString().trim();
        conpass=r_conpass.getText().toString().trim();
        mail=r_mail.getText().toString().trim();
        mobile=r_mobile.getText().toString().trim();
    }
}

