package com.example.ipark.Activities;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;


import com.example.ipark.Location.APIClient;
import com.example.ipark.Location.ApiInterface;
import com.example.ipark.Location.PlacesPOJO;
import com.example.ipark.Misc.Permission;
import com.example.ipark.R;
import com.example.ipark.Models.RecyclerViewAdapter;
import com.example.ipark.Models.StoreModel;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.SmartLocation;
import me.zhanghai.android.materialprogressbar.MaterialProgressBar;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends FragmentActivity implements OnMapReadyCallback {

    Button searchbar;
    GoogleMap map;

    CircleImageView imageView;

    ImageView camera;

    MaterialProgressBar progressBar;
    LocationManager locationManager;
    String locationProvider;
    android.location.Location lastKnownLocation;

    FirebaseUser firebaseUser;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    FirebaseStorage storage;
    StorageReference storageReference;
    String currentUserID;

    List<StoreModel> storeModels;
    ApiInterface apiService;

    String latLngString;
    LatLng latLng;
    ImageView findloc;

    Uri resultUri;

    RecyclerView recyclerView;
    final String txt = "nearby parking";
    List<PlacesPOJO.CustomA> results;

    ImageView empty;

//    private ArrayList<String> permissionsToRequest;
//    private ArrayList<String> permissionsRejected = new ArrayList<>();
//    private ArrayList<String> permissions = new ArrayList<>();
//    private final static int ALL_PERMISSIONS_RESULT = 101;

    // boolean isGPS,isCoarse;

    boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //permissions.add(ACCESS_FINE_LOCATION);
        //permissions.add(ACCESS_COARSE_LOCATION);
        empty = findViewById(R.id.box);
        progressBar = findViewById(R.id.location_loading);
        camera = findViewById(R.id.camera_button);
        imageView = findViewById(R.id.profile_pic_display);

        recyclerView = findViewById(R.id.md_contentRecyclerView);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
        recyclerView.setLayoutManager(layoutManager);

        findloc = findViewById(R.id.find_loc);

        firebaseAuth = FirebaseAuth.getInstance();

        firebaseUser = firebaseAuth.getCurrentUser();

        currentUserID = firebaseUser.getUid();

        //fetchStores("nearby", "parking");

        findloc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                fetchStores("Nearby","Parking");
            }
        });
        progressBar.setVisibility(View.VISIBLE);

        databaseReference = FirebaseDatabase.getInstance().getReference("Members").child(currentUserID);
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String email = dataSnapshot.child("email").getValue(String.class);

                storage = FirebaseStorage.getInstance();
                storageReference = storage.getReference().child(email);

                try {
                    final File file = File.createTempFile("image","jpg");
                    storageReference.getFile(file).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                            imageView.setImageBitmap(bitmap);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });
                }catch (IOException e)
                {
                    e.printStackTrace();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!Permission.check_Camera(MainActivity.this)){
                    Permission.Request_Camera(MainActivity.this,1);
                }

                if(Permission.check_Camera(MainActivity.this)) {
                    CropImage.activity()
                            .setAllowRotation(true)
                            .setGuidelines(CropImageView.Guidelines.ON)
                            .start(MainActivity.this);
                }
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,SettingActivity.class));
            }
        });

        searchbar = findViewById(R.id.search_barr);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapwhere);

        mapFragment.getMapAsync(this);
        searchbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, FindActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK){
                resultUri = result.getUri();
                detectText();
            }
            else if(resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE){
                Toast.makeText(this, "Slow internet problem. Try Again Later!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        if (map == null)
            map.setMyLocationEnabled(true);

        if (map != null) {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission( this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Go to Settings to set Location Permission", Toast.LENGTH_SHORT).show();
                return;
            }

            if (Permission.check_Coarse(MainActivity.this) && Permission.check_Coarse(MainActivity.this)) {
                if (!googleMap.isMyLocationEnabled())
                    googleMap.setMyLocationEnabled(true);

                fetchLocation();
                apiService = APIClient.getClient().create(ApiInterface.class);

                locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
                locationProvider = LocationManager.NETWORK_PROVIDER;
                locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
                locationProvider = LocationManager.NETWORK_PROVIDER;

                lastKnownLocation = locationManager.getLastKnownLocation(locationProvider);
                double userLat = lastKnownLocation.getLatitude();
                double userLong = lastKnownLocation.getLongitude();

                LatLng userLocation = new LatLng(userLat, userLong);
                googleMap.getUiSettings().setAllGesturesEnabled(true);

                CameraPosition cameraPosition = new CameraPosition.Builder().target(userLocation).zoom(17.0f).build();
                CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
                googleMap.moveCamera(cameraUpdate);

//                fetchStores("Nearby","Parking");
            }
        }
    }


    private void fetchStores(String PlaceType, String BussinessName){
        //sleep(1000);
        Call<PlacesPOJO.Root> call = apiService.doPlaces(PlaceType, latLngString, BussinessName, true, "distance", APIClient.GOOGLE_PLACE_API_KEY);

        call.enqueue(new Callback<PlacesPOJO.Root>() {
            @Override
            public void onResponse(Call<PlacesPOJO.Root> call, Response<PlacesPOJO.Root> response) {
                PlacesPOJO.Root root = response.body();
                if (response.isSuccessful()) {
                    if (root.status.equals("OK")) {

                        results = root.customA;
                        storeModels = new ArrayList<>();
                        for (int i = 0; i < results.size(); i++) {
                            if (i == 10)
                                break;

                            PlacesPOJO.CustomA info = results.get(i);

                            storeModels.add(new StoreModel(info.name, info.vicinity,info.geometry.locationA.lat,info.geometry.locationA.lng));
                            //fetchDistance(info);
                        }
//                        if (storeModels.size() <= 10 || storeModels.size() <= results.size())
                            empty.setVisibility(View.INVISIBLE);
                            progressBar.setVisibility(View.INVISIBLE);
                            recyclerView.setVisibility(View.VISIBLE);
                            RecyclerViewAdapter adapterStores = new RecyclerViewAdapter(results, storeModels);
                            recyclerView.setAdapter(adapterStores);
//                        }
                    } else {
                        progressBar.setVisibility(View.INVISIBLE);
                        empty.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.INVISIBLE);
                        Toast.makeText(MainActivity.this, "No matches found near you", Toast.LENGTH_SHORT).show();
                    }
                } else if (response.code() != 200) {
                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(MainActivity.this, "Error " + response.code() + " found.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PlacesPOJO.Root> call, Throwable t) {
                call.cancel();
            }
        });
    }

    private void fetchLocation() {

        SmartLocation.with(MainActivity.this).location()
                .oneFix()
                .start(new OnLocationUpdatedListener() {
                    @Override
                    public void onLocationUpdated(Location location) {
                        latLngString = location.getLatitude() + "," + location.getLongitude();
                        latLng = new LatLng(location.getLatitude(), location.getLongitude());
                        fetchStores("Nearby","Parking");
                    }
                });
    }

    @Override
    public void onBackPressed() {
        if(doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }
        doubleBackToExitPressedOnce=true;
        Toast.makeText(this, "Press BACK again to exit", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        doubleBackToExitPressedOnce = false;
    }

    private void detectText(){
        try {
            FirebaseVisionImage image = FirebaseVisionImage.fromFilePath(this,resultUri);
            FirebaseVisionTextRecognizer detector = FirebaseVision.getInstance().getOnDeviceTextRecognizer();

            Task<FirebaseVisionText>res = detector.processImage(image)
                    .addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>() {
                        @Override
                        public void onSuccess(FirebaseVisionText firebaseVisionText) {
                            String resultText = firebaseVisionText.getText();

                            if(resultText.length()!=0){
                                Intent intent = new Intent(MainActivity.this,FindActivity.class);
                                intent.putExtra("car_number",resultText);
                                startActivity(intent);
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(MainActivity.this, "Something went wrong! Please try again.", Toast.LENGTH_SHORT).show();
                        }
                    });
        }catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }


    }
}