package com.example.john.sscores;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class BasketBallActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basket_ball);
        Intent basketBallIntent = getIntent();

        TextView txtView = (TextView) findViewById(R.id.basketballTxtView);
        txtView.setText("Coming soon :)");
        txtView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
    }
}
