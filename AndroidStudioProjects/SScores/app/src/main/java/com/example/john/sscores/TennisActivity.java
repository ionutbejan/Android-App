package com.example.john.sscores;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

public class TennisActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tennis);
        Intent tennisIntent = getIntent();
        TextView textView = (TextView) findViewById(R.id.tennisTextView);
        textView.setText("Coming soon :)");
        textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

    }

    private class TennisAsync extends AsyncTask<Object, Object, String> {

        @Override
        protected String doInBackground(Object... params) {

            return null;
        }

        @Override
        public void onPostExecute(String response){

        }
    }
}
