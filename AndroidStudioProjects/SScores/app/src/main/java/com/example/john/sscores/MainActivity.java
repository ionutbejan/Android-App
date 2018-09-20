package com.example.john.sscores;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final String[] sportsArray = getResources().getStringArray(R.array.sports);

        ArrayAdapter listviewAdapter = new ArrayAdapter<String>(this, R.layout.activity_list_view, sportsArray);
        final ListView listView = (ListView) findViewById(R.id.listview);
        listView.setAdapter(listviewAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = (String) listView.getItemAtPosition(position);
                //textView.setText("Selected item is : " + selectedItem);
                switch(selectedItem){
                    case "Football": {
                        Intent footballIntent = new Intent(view.getContext(), FootballActivity.class);
                        startActivity(footballIntent);
                        //FootballActivity football = new FootballActivity();
                        //football.execute();
                        break;
                    }
                    case "Basketball": {
                        Intent basketballIntent = new Intent(view.getContext(), BasketBallActivity.class);
                        startActivity(basketballIntent);
                        break;
                    }
                    case "Rugby": {
                        Intent rugbyIntent = new Intent(view.getContext(), RugbyActivity.class);
                        startActivity(rugbyIntent);
                        break;
                    }
                    case "Tennis": {
                        Intent tennisIntent = new Intent(view.getContext(), TennisActivity.class);
                        startActivity(tennisIntent);
                        break;
                    }
                }
            }
        });
    }

}
