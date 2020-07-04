package com.example.ipark.Activities;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.example.ipark.Misc.RegEx;
import com.example.ipark.Models.Members;
import com.example.ipark.Models.Vehicle;
import com.example.ipark.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;
import me.zhanghai.android.materialprogressbar.MaterialProgressBar;

public class SignupActivity extends AppCompatActivity{
    // For Animation
    Toolbar toolbar;
    LinearLayout linearLayout;
    CircleImageView imageView;
    CoordinatorLayout coordinatorLayout;
    CollapsingToolbarLayout collapsingToolbarLayout;
    AppBarLayout mappBarLayout;
    TextView textView, form_textview;
    EditText password, retype_password,car_number, car_model;
    String mail,fname,lname,uid,id;
    Button submit;
    Uri filepath;

    private boolean mIsTheTitleVisible          = false;
    private boolean mIsTheTitleContainerVisible = true;

    private static final float PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR  = 0.9f;
    private static final float PERCENTAGE_TO_HIDE_TITLE_DETAILS     = 0.3f;
    private static final int ALPHA_ANIMATIONS_DURATION              = 200;

    //For Sign up
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    FirebaseStorage storage;
    StorageReference storageReference;
    DatabaseReference reference;

    MaterialProgressBar progressBar;
    private final int PICK_IMAGE_REQUEST = 71;
    private RegEx regex;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        regex = new RegEx();

        toolbar = (Toolbar)findViewById(R.id.toolbar);
        imageView = (CircleImageView) findViewById(R.id.logo_image);
        linearLayout = (LinearLayout) findViewById(R.id.linear_layout1);
        coordinatorLayout = (CoordinatorLayout)findViewById(R.id.coordinator_layout);
        mappBarLayout = findViewById(R.id.appbar_layout);
        collapsingToolbarLayout = (CollapsingToolbarLayout)findViewById(R.id.collapsing_toolbar);
        progressBar = findViewById(R.id.signup_progressbar);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        firebaseAuth = FirebaseAuth.getInstance();

        textView = (TextView)findViewById(R.id.main_textview_title);
        form_textview = (TextView)findViewById(R.id.secondary_text_view);
        password = findViewById(R.id.signup_password);
        retype_password = findViewById(R.id.re_password);
        car_number = findViewById(R.id.Signup_Car_number);
        car_model = findViewById(R.id.Signup_Car_name);

        mail = getIntent().getStringExtra("Email ID");
        fname = getIntent().getStringExtra("Fname");
        lname = getIntent().getStringExtra("Lname");

        textView.setText("Hey, " + fname + " " + lname);
        form_textview.setText("Hey, " + fname + " " + lname);


        submit = findViewById(R.id.submit_button);
        submit.setOnClickListener(new View.OnClickListener() {
            Members member = new Members();

            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);

                if(password.getText().toString().compareTo(retype_password.getText().toString())!=0) {
                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(SignupActivity.this, "Password does not match", Toast.LENGTH_SHORT).show();
                }

                else if(password.getText().toString().isEmpty() || retype_password.getText().toString().isEmpty() || car_model.getText().toString().isEmpty() || car_number.getText().toString().isEmpty()) {
                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(SignupActivity.this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
                }
                else if((regex.validate(car_number.getText().toString())).equals("INVALID")) {
                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(SignupActivity.this, "Enter the right car number!", Toast.LENGTH_SHORT).show();
                }

                else{
                    progressBar.setVisibility(View.VISIBLE);
                    firebaseAuth.createUserWithEmailAndPassword(mail,password.getText().toString())
                            .addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()) {
                                        user = firebaseAuth.getCurrentUser();
                                        uid = user.getUid();

                                        if(user!=null){
                                            reference = FirebaseDatabase.getInstance().getReference().child("Members").child(uid);
                                            uploadImage();
                                            id = reference.push().getKey();
                                            member.setId(id);
                                            member.setFirst_name(fname);
                                            member.setLast_name(lname);
                                            member.setEmail(mail);
                                            member.setCar_name(car_model.getText().toString());
                                            member.setPlate_number(car_number.getText().toString());

                                            reference.child(id).setValue(member);
                                            Toast.makeText(SignupActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(SignupActivity.this,LoginActivity.class));
                                        }

                                        else{
                                            progressBar.setVisibility(View.INVISIBLE);
                                            Toast.makeText(SignupActivity.this, "Cannot find the current user", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    else{
                                        progressBar.setVisibility(View.INVISIBLE);
                                        Toast.makeText(SignupActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                    progressBar.setVisibility(View.INVISIBLE);
                                }
                            });



                }
            }
        });
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });
        mappBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                int maxScroll = appBarLayout.getTotalScrollRange();
                float percentage = (float) Math.abs(verticalOffset) / (float) maxScroll;

                handleAlphaOnTitle(percentage);
                handleToolbarTitleVisibility(percentage);
            }
        });

        startAlphaAnimation(textView, 0, View.INVISIBLE);
    }


    private void chooseImage(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Picture"),PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data!=null && data.getData()!=null)
        {
            imageView.setBorderColor(Color.WHITE);
            imageView.setBorderWidth(4);

            filepath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),filepath);
                imageView.setImageBitmap(bitmap);
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    private void uploadImage(){
        if(filepath!=null){
            StorageReference ref = storageReference.child(mail);
            ref.putFile(filepath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                            Toast.makeText(SignupActivity.this, "Uploaded!", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(SignupActivity.this, "Internet Issue "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private void handleToolbarTitleVisibility(float percentage){

        if (percentage >= PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR){
            if(!mIsTheTitleVisible){
                startAlphaAnimation(textView, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);
                mIsTheTitleVisible = true;
            }
        }else{
            if (mIsTheTitleVisible){
                startAlphaAnimation(textView, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);
                mIsTheTitleVisible = false;
            }
        }
    }

    private void handleAlphaOnTitle(float percentage){
        if (percentage >= PERCENTAGE_TO_HIDE_TITLE_DETAILS) {
            if(mIsTheTitleContainerVisible) {
                startAlphaAnimation(linearLayout, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);
                mIsTheTitleContainerVisible = false;
            }

        } else {

            if (!mIsTheTitleContainerVisible) {
                startAlphaAnimation(linearLayout, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);
                mIsTheTitleContainerVisible = true;
            }
        }
    }

    public static void startAlphaAnimation (View v, long duration, int visibility) {
        AlphaAnimation alphaAnimation = (visibility == View.VISIBLE)
                ? new AlphaAnimation(0f, 1f)
                : new AlphaAnimation(1f, 0f);

        alphaAnimation.setDuration(duration);
        alphaAnimation.setFillAfter(true);
        v.startAnimation(alphaAnimation);
    }

}
