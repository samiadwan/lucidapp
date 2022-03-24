package com.lucid.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import android.net.Uri;


public class About extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about);
    }

    public void backHome(View v) {
        finish();
    }

    public void GoToUR(View v){
        Uri uri = Uri.parse("http://projectlucid.ml");
        Intent intent= new Intent(Intent.ACTION_VIEW,uri);
        startActivity(intent);
    }
}
