package com.moon.meojium.ui.museumdetail;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.moon.meojium.R;

/**
 * Created by moon on 2017. 8. 7..
 */

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();

        if (intent != null) {
            Log.d("Meojium", intent.getStringExtra("id"));
        }
    }
}
