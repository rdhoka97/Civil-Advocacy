package com.example.civiladvocacy.Model;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.civiladvocacy.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.List;

public class OfficialDetailActivity extends AppCompatActivity {
    Official_Gov_Details officials;
    TextView address, officialPost, officialName, officialParty, officialAddress, officialPhone, officialWebsite, officialEmail;
    ConstraintLayout constraintLayout;
    ImageView imageView, facebook, twitter, youtube, partyImg;
    TextView labelEmail, labelPhone, labelWebsite, labelAddress;
    String fb="";
    String tw = "";
    String yt = "";
    boolean flag = false;
    ImageView imgView2;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_official_detail);
        setTitle("Know Your Government");

        Bundle bundle = getIntent().getBundleExtra("List");
        officials = (Official_Gov_Details) bundle.getSerializable("List");
        System.out.println("Official::::"+officials.getPhotoUrl());
        address = findViewById(R.id.location);
        officialPost = findViewById(R.id.position);
        officialName = findViewById(R.id.PersonName);
        officialParty = findViewById(R.id.PartyName);
        officialAddress = findViewById(R.id.personAddress);
        officialPhone = findViewById(R.id.PersonalPhoneNo);
        officialWebsite = findViewById(R.id.personalWebsite);
        officialEmail = findViewById(R.id.PersonalEmail);
        facebook = findViewById(R.id.facebook);
        twitter = findViewById(R.id.twitter);
        youtube = findViewById(R.id.youtube);
        partyImg = findViewById(R.id.partyImg);
        constraintLayout = (ConstraintLayout) findViewById(R.id.constraint);
        labelAddress = findViewById(R.id.labelAddress);
        labelEmail = findViewById(R.id.labelEmail);
        labelPhone = findViewById(R.id.labelPhone);
        labelWebsite = findViewById(R.id.labelWebsite);
        imageView = (ImageView) findViewById(R.id.imageView);

        address.setText(officials.getAddress());
        officialPost.setText(officials.getPost());
        officialName.setText(officials.getName());

        if(!officials.getParty().equals("")){
            officialParty.setText("("+officials.getParty()+")");
            if(officials.getParty().contains("Democratic")){
                partyImg.setImageResource(R.drawable.dem_logo);
                constraintLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.blue));
            }else if(officials.getParty().contains("Republican")){
                partyImg.setImageResource(R.drawable.rep_logo);
                constraintLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.red));
            }else {
                partyImg.setVisibility(ImageView.GONE);
            }
        }
        if(!officials.getEmail().equals(""))
            officialEmail.setText(officials.getEmail());
        else{
            labelEmail.setVisibility(TextView.GONE);
            officialEmail.setVisibility(TextView.GONE);
        }

        if(!officials.getOfficialAddress().equals("")) {
            SpannableString spanStr = new SpannableString(officials.getOfficialAddress());
            spanStr.setSpan(new UnderlineSpan(), 0, spanStr.length(), 0);
            officialAddress.setText(spanStr);

        }else{
            labelAddress.setVisibility(TextView.GONE);
            officialAddress.setVisibility(TextView.GONE);
        }
        if(!officials.getPhoneNumber().equals(""))
            officialPhone.setText(officials.getPhoneNumber());
        else{
            labelPhone.setVisibility(TextView.GONE);
            officialPhone.setVisibility(TextView.GONE);
        }
        if(!officials.getUrls().equals(""))
            officialWebsite.setText(officials.getUrls());
        else{
            labelWebsite.setVisibility(TextView.GONE);
            officialWebsite.setVisibility(TextView.GONE);
        }

        if(!officials.getPhotoUrl().equals("")){
            Picasso.get()
                    .load(officials.getPhotoUrl())
                    .error(R.drawable.brokenimage)
                    .into(imageView, new Callback() {
                        @Override
                        public void onSuccess() {
                        }
                        @Override
                        public void onError(Exception e) {
                            imageView.setTag("fail");
                        }
                    });
        }else{
            imageView.setImageResource(R.drawable.missing);
        }
        if(!hasNetworkConnection()){
            imageView.setImageResource(R.drawable.brokenimage);
        }

        List<String[]> list = officials.getChannelList();

        for (String[] s : list) {
            if(s[0].equals("Facebook")) {
                facebook.setImageResource(R.drawable.facebook);
                fb = s[1];
            }else if(s[0].equals("Twitter")) {
                twitter.setImageResource(R.drawable.twitter);
                tw = s[1];
            }else if(s[0].equals("YouTube")) {
                youtube.setImageResource(R.drawable.youtube);
                yt = s[1];
            }
        }
    }
    public void LocationClick(View v) {
        String s ="geo:0,0?q="+officials.getOfficialAddress();
        Intent intent = null;
        try {
            intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(s));
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse(s)));
        }
    }
    public void twitterClick(View v) {
        Intent intent = null;
        String name = tw;
        try {
            getPackageManager().getPackageInfo("com.twitter.android", 0);
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?screen_name=" + name));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        } catch (Exception e) {
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/" + name));
        }
        startActivity(intent);
    }
    public void facebookClick(View v) {
        String FACEBOOK_URL = "https://www.facebook.com/" + fb;
        String urlToUse;
        PackageManager packageManager = getPackageManager();
        try {
            int versionCode = packageManager.getPackageInfo("com.facebook.katana", 0).versionCode;
            if (versionCode >= 3002850) { //newer versions of fb app
                urlToUse = "fb://facewebmodal/f?href=" + FACEBOOK_URL;
            } else { //older versions of fb app
                urlToUse = "fb://page/" + fb;
            }
        } catch (PackageManager.NameNotFoundException e) {
            urlToUse = FACEBOOK_URL; //normal web url
        }
        Intent facebookIntent = new Intent(Intent.ACTION_VIEW);
        facebookIntent.setData(Uri.parse(urlToUse));
        startActivity(facebookIntent);
    }
    public void youtubeClick(View v) {
        String name = yt;
        Intent intent = null;
        try {
            intent = new Intent(Intent.ACTION_VIEW);
            intent.setPackage("com.google.android.youtube");
            intent.setData(Uri.parse("https://www.youtube.com/" + name));
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://www.youtube.com/" + name)));
        }
    }
    public void Logo(View v) {
        String s = "";
        if(officials.getParty().contains("Democratic")) {
            s = "https://democrats.org/";
        }else{
            s = "https://www.gop.com/";
        }
        Intent intent = null;
        try {
            intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(s));
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse(s)));
        }
    }
    @SuppressLint("ResourceType")
    public void photoDetail(View v) {

        Intent intent = new Intent(this, PhotoDetailActivity.class);
        Bundle bundle = new Bundle();

        String t = (String) imageView.getTag();
        if (!officials.getPhotoUrl().equals("") && (t == null || !t.equals("fail"))) {
            bundle.putSerializable("List", (Serializable) officials);
            intent.putExtra("List", bundle);
            startActivity(intent);
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    private boolean hasNetworkConnection() {
        ConnectivityManager connectivityManager = getSystemService(ConnectivityManager.class);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnectedOrConnecting());
    }

}