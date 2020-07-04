package com.example.ipark.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ipark.Misc.Permission;
import com.example.ipark.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

public class FirstActivity extends AppCompatActivity {
    TextView signin;
    Button google,facebook;
    GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen);

        signin = (TextView) findViewById(R.id.login);
        google = (Button)findViewById(R.id.google);
        facebook = (Button)findViewById(R.id.facebook);

        if(!Permission.check_GPS(FirstActivity.this)){
            Permission.Request_GPS(FirstActivity.this,22);
        }

        if(!Permission.check_Coarse(FirstActivity.this)){
            Permission.Request_Coarse(FirstActivity.this,23);
        }

        if(!Permission.check_Camera(FirstActivity.this)){
            Permission.Request_Camera(FirstActivity.this,24);
        }

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FirstActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestProfile()
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent,0);
            }
        });

        facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FirstActivity.this,SignupActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==0){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask){
        try{
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            Intent intent = new Intent(FirstActivity.this,SignupActivity.class);
            intent.putExtra("Email ID",account.getEmail());
            intent.putExtra("Fname",account.getGivenName());
            intent.putExtra("Lname",account.getFamilyName());
            mGoogleSignInClient.signOut();
            startActivity(intent);
        }catch (ApiException e){
            Toast.makeText(this, "Try Again Later!", Toast.LENGTH_SHORT).show();
            Log.w("Error", "signInResult:failed code=" + e.getStatusCode());
        }
    }
}
