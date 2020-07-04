package com.example.ipark.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ipark.Misc.Permission;
import com.example.ipark.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import me.zhanghai.android.materialprogressbar.MaterialProgressBar;

public class LoginActivity extends AppCompatActivity {
    TextView signup,login;
    TextView email,password;
    MaterialProgressBar progressBar;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        progressBar = findViewById(R.id.login_progress);

        if(!Permission.check_GPS(LoginActivity.this)){
            Permission.Request_GPS(LoginActivity.this,22);
        }

        if(!Permission.check_Coarse(LoginActivity.this)){
            Permission.Request_Coarse(LoginActivity.this,23);
        }

        if(!Permission.check_Camera(LoginActivity.this)){
            Permission.Request_Camera(LoginActivity.this,1);
        }

        signup = (TextView)findViewById(R.id.signup_again);
        login = (TextView)findViewById(R.id.login);

        mAuth = FirebaseAuth.getInstance();

        email = findViewById(R.id.username);
        password = findViewById(R.id.password);

        login.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                progressBar.setVisibility(View.INVISIBLE);
//                startActivity(intent);

                progressBar.setVisibility(View.VISIBLE);
                String u = email.getText().toString().trim();
                String p = password.getText().toString();
                int x=7;

                if (u.isEmpty() || p.isEmpty()) {
                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(LoginActivity.this, "Please enter Username and Password", Toast.LENGTH_SHORT).show();
                }


                else{
                    mAuth.signInWithEmailAndPassword(u, p)
                            .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {

//                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                                    progressBar.setVisibility(View.INVISIBLE);
//                                    startActivity(intent);
                                    if (task.isSuccessful()) {
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        progressBar.setVisibility(View.INVISIBLE);
                                        startActivity(intent);
                                    } else {
                                        Log.e("Firebase Problem","On Complete Failed: " + task.getException().getMessage());
                                        Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                        progressBar.setVisibility(View.INVISIBLE);
                                    }
                                }
                            });
                }

            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, FirstActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();
    }


}
