package com.example.civiladvocacy.Model;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.civiladvocacy.Model.Official_Gov_Details;
import com.example.civiladvocacy.R;
import com.squareup.picasso.Picasso;

public class PhotoDetailActivity extends AppCompatActivity {

    Official_Gov_Details officials;
    TextView address, post, name;
    ImageView imageView, imgLogo;
    ConstraintLayout constraintLayout;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_detail);
        setTitle("Know Your Government");

        Bundle bundle = getIntent().getBundleExtra("List");
        officials = (Official_Gov_Details) bundle.getSerializable("List");
        System.out.println("Official::::"+officials.getPhotoUrl());

        address = findViewById(R.id.P_Address);
        post = findViewById(R.id.P_Post);
        name = findViewById(R.id.P_Name);

        imageView = findViewById(R.id.P_Image);
        imgLogo = findViewById(R.id.P_Logo);
        constraintLayout = findViewById(R.id.P_Constraint);

        address.setText(officials.getAddress());
        post.setText(officials.getPost());
        name.setText(officials.getName());

        if(officials.getParty().contains("Democratic")){
            imgLogo.setImageResource(R.drawable.dem_logo);
            constraintLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.blue));

        }else if(officials.getParty().contains("Republican")){
            imgLogo.setImageResource(R.drawable.rep_logo);
            constraintLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.red));

        }else {
            imgLogo.setVisibility(ImageView.GONE);
            constraintLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.black));
        }

        if(officials.getPhotoUrl()!=null){
            Picasso.get()
                    .load(officials.getPhotoUrl())
                    .error(R.drawable.brokenimage)
                    .into(imageView);
        }

        if(!hasNetworkConnection()){
            imageView.setImageResource(R.drawable.brokenimage);
        }

    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    private boolean hasNetworkConnection() {
        ConnectivityManager connectivityManager = getSystemService(ConnectivityManager.class);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnectedOrConnecting());
    }
}