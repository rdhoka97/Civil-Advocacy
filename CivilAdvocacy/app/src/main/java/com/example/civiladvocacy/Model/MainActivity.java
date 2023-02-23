package com.example.civiladvocacy.Model;


import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.core.content.ContextCompat;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;

import android.annotation.SuppressLint;

import android.content.DialogInterface;
import android.content.Intent;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import java.io.IOException;
import java.io.Serializable;

import com.example.civiladvocacy.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    TextView locations;
    RecyclerView recyclerView;
    ListDisplay governmentOfficialsAdapter;
    List<Official_Gov_Details> officialsList;
    String loc = "Chicago";
    private final static int REQUEST_CODE = 100;
    FusedLocationProviderClient fusedLocationProviderClient;
    double latitude, longitude;
    Context context = this;


    @SuppressLint("MissingPermission")
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Civic Advocacy");
        locations = findViewById(R.id.currentLocation);
        recyclerView = findViewById(R.id.recycle);
        if(!hasNetworkConnection()){
            setContentView(R.layout.network_check);
            return;
        }
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        String chek = getLastLocation();
    }

    private String getLastLocation() {
        System.out.println("before");
        final String[] locat = {loc};
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            System.out.println("After");
            LocationRequest mLocationRequest = LocationRequest.create();
            mLocationRequest.setInterval(60000);
            mLocationRequest.setFastestInterval(5000);
            mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            LocationCallback mLocationCallback = new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    if (locationResult == null) {
                        return;
                    }
                    for (Location location : locationResult.getLocations()) {
                        if (location != null) {
                            //TODO: UI updates.
                        }
                    }
                }
            };

            LocationServices.getFusedLocationProviderClient(context).requestLocationUpdates(mLocationRequest, mLocationCallback, null);


            fusedLocationProviderClient.getLastLocation()
                    .addOnSuccessListener(new OnSuccessListener<Location>() {

                        @Override
                        public void onSuccess(Location location) {
                            System.out.println("Location:Before"+location);
                            if (location != null){
                                System.out.println("Location"+location);
                                try {
                                    Geocoder geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
                                    List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                                    latitude=addresses.get(0).getLatitude();
                                    longitude=addresses.get(0).getLongitude();
                                    locat[0] = addresses.get(0).getAddressLine(0);
                                    location(addresses.get(0).getAddressLine(0));
                                    System.out.println("Address::::Final "+locat[0]);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }

                        }
                    });


        } else {
            askPermission();
        }

        return locat[0];

    }
    private void askPermission() {
        ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE) {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                getLastLocation();

            } else {
                Toast.makeText(MainActivity.this, "Please provide the required permission", Toast.LENGTH_SHORT).show();

            }
        }
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_list, menu);
        return true;
    }

    @SuppressLint("ResourceAsColor")
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search:
                if (!hasNetworkConnection()){
                    setContentView(R.layout.network_check);
                    return true;
                }
                final EditText txtUrl = new EditText(this);

                new AlertDialog.Builder(this)
                        .setTitle("Enter a Address")
                        .setView(txtUrl)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                txtUrl.setTextColor(R.color.white);
                                if(!hasNetworkConnection()){
                                    setContentView(R.layout.network_check);
                                    return;
                                }
                                location(txtUrl.getText().toString());
                            }
                        })
                        .setNegativeButton("CANCEL",null)
                        .show();
                return true;
            case R.id.about:
                Intent intent = new Intent(this, AboutActivity.class);
                startActivity(intent);
                return true;

        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onClick(View v) {
        int pos = recyclerView.getChildAdapterPosition(v);
        Official_Gov_Details officials = officialsList.get(pos);
        System.out.println("NAme  :::"+officials.getName());
        Intent intent = new Intent(this, OfficialDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("List",(Serializable) officials);
        intent.putExtra("List",bundle);
        startActivity(intent);

    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    public boolean hasNetworkConnection() {
        ConnectivityManager connectivityManager = getSystemService(ConnectivityManager.class);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnectedOrConnecting());
    }

    public void location(String s){
        DataDownloader.downloadData(this,s);
    }

    public void updateData(List<Official_Gov_Details> officials) {
        if(officials!=null) {
            System.out.println("MainActivity ::::::::::::" + officials.get(0).getParty());
            officialsList = new ArrayList<>(officials);
            locations.setText(officialsList.get(0).getAddress());
            governmentOfficialsAdapter = new ListDisplay(this, officials);
            recyclerView.setAdapter(governmentOfficialsAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        }else{
            Toast.makeText(MainActivity.this, "Please Enter valid city", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }
}