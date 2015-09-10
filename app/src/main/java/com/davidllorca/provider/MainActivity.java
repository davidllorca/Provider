package com.davidllorca.provider;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClickContentProvider(View view) {
        startActivity(new Intent(this, ContentProviderActivity.class));
    }

    public void onClickOwnContentProvider(View view) {
        startActivity(new Intent(this, OwnContentProviderActivity.class));
    }
}