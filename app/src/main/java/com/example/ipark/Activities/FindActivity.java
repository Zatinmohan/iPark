package com.example.ipark.Activities;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.example.ipark.Database.databasehelper;
import com.example.ipark.Jsoup.FetchFromVahan;
import com.example.ipark.Jsoup.GetCaptcha;
import com.example.ipark.Interfaces.AsyncCaptchaResponse;
import com.example.ipark.Interfaces.AsyncResponse;
import com.example.ipark.Models.RecyclerViewAdapter;
import com.example.ipark.R;
import com.example.ipark.Misc.RegEx;
import com.example.ipark.Models.Vehicle;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

public class FindActivity extends AppCompatActivity {
    TextInputEditText edittext;
    RegEx regEx = new RegEx();
    EditText captchaInput;

    TextView owner_name, plate_number,V_Type,Model,regDate, Engine, Chassis, Fuel, Insurance;

    private final int OK = 200;
    private final int SOCKET_TIMEOUT = 408;
    private final int CAPTCHA_FAILED = 999;
    private final int TECHNICAL_DIFFICULTY = 888;
    public final int ACTION_NULL = -1;

    List<Vehicle>everyone;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find);
        edittext = findViewById(R.id.searching);

        edittext.requestFocus();                                                                    //EditText Focus

        recyclerView = findViewById(R.id.recycler_view_content);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager manager = new LinearLayoutManager((FindActivity.this));
        recyclerView.setLayoutManager(manager);

        databasehelper db = new databasehelper(FindActivity.this);
        everyone = db.getEveryone();
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(everyone);
        recyclerView.setAdapter(adapter);



        String CAR_NUMBER = getIntent().getStringExtra("car_number");

        if(CAR_NUMBER!=null && CAR_NUMBER.length()!=0){
            edittext.setText(CAR_NUMBER);
        }

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);          //Keyboard Opens

             edittext.setOnKeyListener(new View.OnKeyListener() {
                 @Override
                 public boolean onKey(View v, int keyCode, KeyEvent event) {
                     if(event.getAction() ==  KeyEvent.ACTION_DOWN) {
                         String number_plate = edittext.getText().toString();
                         String x = regEx.validate(number_plate);

                         if(number_plate.length()>=9 && !(x.equals("INVALID"))) {
                             action(number_plate);

                         }

                         else if(number_plate.length()==0)
                             return false;

                         else
                             Toast.makeText(FindActivity.this, "Incorrect", Toast.LENGTH_SHORT).show();
                         return true;
                     }
                     return false;
                 }
             });
    }

    public void action(final String number_plate){
        final MaterialDialog.Builder mdialogBuilder = new MaterialDialog.Builder(FindActivity.this).content("Conntecting").cancelable(false).progress(true, 0).widgetColorRes(R.color.colorPrimary);
        final MaterialDialog mdialog = mdialogBuilder.build();
        mdialog.show();

        new GetCaptcha(new AsyncCaptchaResponse() {
            @Override
            public void processFinish(Bitmap bitmap, int statuscode) {
                mdialog.dismiss();

                if(bitmap!=null)
                {
                    final MaterialDialog mdialog = new MaterialDialog.Builder(FindActivity.this)
                            .customView(R.layout.dialogue, true)
                            .positiveText("Continue")
                            .positiveColorRes(R.color.colorPrimary)
                            .negativeText("Cancel")
                            .negativeColorRes(R.color.colorPrimary)
                            .widgetColorRes(R.color.colorPrimary)
                            .build();

                    mdialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                    mdialog.getActionButton(DialogAction.POSITIVE).setEnabled(false);

                    captchaInput = (EditText)mdialog.getCustomView().findViewById(R.id.captcha_input);
                    captchaInput.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            if(s.length()<=6)
                                mdialog.getActionButton(DialogAction.POSITIVE).setEnabled(true);
                            else
                                mdialog.getActionButton(DialogAction.POSITIVE).setEnabled(false);
                        }

                        @Override
                        public void afterTextChanged(Editable s) {

                        }
                    });

                    ImageView imageView = (ImageView) mdialog.getCustomView().findViewById(R.id.captcha);
                    imageView.setImageBitmap(bitmap);

                    mdialog.getActionButton(DialogAction.POSITIVE).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mdialog.dismiss();

                            final MaterialDialog.Builder mdiaBuilder =new MaterialDialog.Builder(FindActivity.this)
                                    .content("Requesting Details...")
                                    .cancelable(false)
                                    .progress(true, 0);
                            //.widgetColorRes(R.color.colorPrimary);

                            final MaterialDialog mdiag = mdiaBuilder.build();
                            mdiag.show();

                            new FetchFromVahan(new AsyncResponse() {
                                @Override
                                public void processFinish(Vehicle vehicle, int statuscode) {
                                    mdiag.dismiss();
                                    databasehelper db = new databasehelper(FindActivity.this);
                                    if(statuscode==OK) {
                                        if (vehicle != null) {
                                            Vehicle obj = new Vehicle();
                                            final MaterialDialog content = new MaterialDialog.Builder(FindActivity.this)
                                                    .customView(R.layout.activity_search_detail,true)
                                                    .build();

                                            owner_name = (TextView)content.getCustomView().findViewById(R.id.owner_name);
                                            plate_number = (TextView)content.getCustomView().findViewById(R.id.plate_number);
                                            V_Type = (TextView)content.getCustomView().findViewById(R.id.Type);
                                            Model = (TextView)content.getCustomView().findViewById(R.id.model);
                                            Engine = (TextView)content.getCustomView().findViewById(R.id.Engine);
                                            Insurance = (TextView)content.getCustomView().findViewById(R.id.inc_date);
                                            Chassis = (TextView)content.getCustomView().findViewById(R.id.Chassis);
                                            Fuel = (TextView)content.getCustomView().findViewById(R.id.Fuel);
                                            regDate = (TextView)content.getCustomView().findViewById(R.id.reg_date);

                                            owner_name.setText(obj.getOwner());
                                            plate_number.setText(obj.getReg_number());
                                            V_Type.setText(obj.getVehicle_class());
                                            Model.setText(obj.getMaker_model());
                                            Engine.setText(obj.getEngine());
                                            //Insurance.setText(obj.getInsurence());
                                            Chassis.setText(obj.getChassis());
                                            //Fuel.setText(obj.getFuel_type());
                                            regDate.setText(obj.getReg_date());

                                            content.show();

                                            boolean b = db.addVehicle(obj);

                                            everyone = db.getEveryone();
                                            RecyclerViewAdapter adapter = new RecyclerViewAdapter(everyone);
                                            recyclerView.setAdapter(adapter);

                                        } else {
                                            final MaterialDialog dialog = new MaterialDialog.Builder(FindActivity.this)
                                                    .title(number_plate + " " + "not found")
                                                    .title("Not Found")
                                                    .widgetColorRes(R.color.colorPrimary)
                                                    .positiveColorRes(R.color.colorPrimary)
                                                    .neutralText("Learn More")
                                                    .build();
                                            dialog.show();
                                        }
                                    }else if(statuscode==CAPTCHA_FAILED){
                                        final MaterialDialog dialog = new MaterialDialog.Builder(FindActivity.this)
                                                .title("Verification Failed")
                                                .content("The Captcha Didn't Match! Try Again")
                                                .positiveText("Retry")
                                                .onPositive(new MaterialDialog.SingleButtonCallback() {
                                                    @Override
                                                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                                        action(number_plate);
                                                    }
                                                })
                                                .negativeText("Cancel")
                                                .build();

                                        dialog.show();
                                    }else{
                                        Toast.makeText(FindActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }).execute(number_plate.substring(0,number_plate.length()-4), number_plate.substring(number_plate.length()-4), captchaInput.getText().toString());
                        }
                    });
                    mdialog.show();
                }
            }
        }).execute(number_plate.substring(0,number_plate.length()-4),number_plate.substring(number_plate.length()-4));
    }
}
