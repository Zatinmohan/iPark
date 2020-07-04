package com.example.ipark.Activities;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.ipark.Models.CustomAdapter;
import com.example.ipark.Models.Listmodel;
import com.example.ipark.R;

import java.util.ArrayList;

public class AboutmeActivity extends AppCompatActivity {
    ListView listView;
    ImageView instagram,github,facebook,linkedin;
    ArrayList<Listmodel>data;
    CardView special;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aboutme);

        listView = findViewById(R.id.list_view);
        instagram = findViewById(R.id.instagram);
        github = findViewById(R.id.github);
        facebook = findViewById(R.id.facebook);
        linkedin = findViewById(R.id.linkedin);
        special = findViewById(R.id.special_thanks);
        data = new ArrayList<>();

        data.add(new Listmodel("Jonathan Hedley"," jsoup Java HTML Parser","https://github.com/jhy/jsoup/"));
        data.add(new Listmodel("Apache","Android SQLite","None"));
        data.add(new Listmodel("Aidan Follestad","Material Dialogs","https://github.com/afollestad/material-dialogs"));
        data.add(new Listmodel("Henning Dodenhof","Circle Image View","https://github.com/hdodenhof/CircleImageView"));
        data.add(new Listmodel("Icon8","Icons","https://icons8.com/"));
        data.add(new Listmodel("Vijay Verma","Illustration","https://illlustrations.co/"));

        CustomAdapter adapter = new CustomAdapter(data,R.layout.list_items,this);
        listView.setAdapter(adapter);

        special.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("https://github.com/chandruscm");
                startActivity(new Intent(Intent.ACTION_VIEW,uri));
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Listmodel model = data.get(position);
                if(model.getLink().compareTo("None")!=0){
                    Uri uri = Uri.parse(model.getLink());
                    Intent i = new Intent(Intent.ACTION_VIEW,uri);
                    startActivity(i);
                }
                else{
                    Toast.makeText(AboutmeActivity.this, "None", Toast.LENGTH_SHORT).show();
                }
            }
        });
        instagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("https://www.instagram.com/o.my.god.someone_actually/");
                Intent i = new Intent(Intent.ACTION_VIEW,uri);
                i.setPackage("com.instagram.android");

                try{
                    startActivity(i);
                }catch (ActivityNotFoundException e){
                    startActivity(new Intent(Intent.ACTION_VIEW,uri));
                }
            }
        });

        facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("https://www.facebook.com/iamzatin");
                Intent i = new Intent(Intent.ACTION_VIEW,uri);
                i.setPackage("com.facebook.katana");

                try{
                    startActivity(i);
                }catch (ActivityNotFoundException e){
                    startActivity(new Intent(Intent.ACTION_VIEW,uri));
                }
            }
        });

        github.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("https://github.com/Zatinmohan");
                startActivity(new Intent(Intent.ACTION_VIEW,uri));
            }
        });

        linkedin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("https://www.linkedin.com/in/jatin-mohan/");
                Intent intent = new Intent(Intent.ACTION_VIEW,uri);
                intent.setPackage("com.linkedin.android");

                try{
                    startActivity(intent);
                }catch (ActivityNotFoundException e){
                    startActivity(new Intent(Intent.ACTION_VIEW,uri));
                }
            }
        });
    }
}
