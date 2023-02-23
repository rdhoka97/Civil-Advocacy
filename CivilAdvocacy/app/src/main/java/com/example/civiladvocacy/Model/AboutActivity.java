package com.example.civiladvocacy.Model;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.TextView;

import com.example.civiladvocacy.R;

public class AboutActivity extends AppCompatActivity {

    TextView textView;
    String s = "Google Civic \n Information API";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        textView = findViewById(R.id.google);
        SpannableString spanStr = new SpannableString(s);
        spanStr.setSpan(new UnderlineSpan(), 0, spanStr.length(), 0);
        textView.setText(spanStr);
    }


    public void GoogleClick(View v){
        String s ="https://developers.google.com/civic-information/";

        Intent in = null;
        try {
            in = new Intent(Intent.ACTION_VIEW);
            in.setData(Uri.parse(s));
            startActivity(in);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse(s)));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater;

        inflater= getMenuInflater();
        inflater.inflate(R.menu.menu_list, menu);

        return true;
    }
}