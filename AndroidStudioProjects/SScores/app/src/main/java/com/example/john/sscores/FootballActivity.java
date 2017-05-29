package com.example.john.sscores;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import static android.R.attr.id;

public class FootballActivity extends AppCompatActivity {
    private TextView txtView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_football);

        Intent footballIntent = getIntent();

        txtView = (TextView) findViewById(R.id.footballTextView);

        FootballAsync football = new FootballAsync();
        football.execute();
    }


    /* Async Class executed in background */
    private class FootballAsync extends AsyncTask<Object, Object, String> {
        HttpURLConnection conn = null;
        @Override
        protected String doInBackground(Object... params) {
            StringBuilder response = new StringBuilder();
            try{
                URL url = new URL("https://api.sportradar.us/soccer-t3/eu/en/tournaments.json?api_key=rcjd99h8gbawwwx7xaw2a8au");
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");

                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                String line;
                if(conn.getResponseCode() != 200){
                    throw new RuntimeException("Failed: Error code : " + conn.getResponseCode());
                }
                while ((line = br.readLine()) != null){
                    response.append(line);
                    response.append("\n");
                }
                br.close();


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                if(conn != null){
                    conn.disconnect();
                }
            }
            return response.toString();
        }
        @Override
        public void onPostExecute(String result){
            String [] leagues = new String[16];
            try {
                JSONObject obj = new JSONObject(result);
                JSONArray names = obj.getJSONArray("tournaments");
                int n = names.length();
                for (int i = 0; i < n; i++){
                    JSONObject leagueName = names.getJSONObject(i);
                    JSONObject leagueCountryName = names.getJSONObject(i).getJSONObject("category");
                    //System.out.println(leagueCountryName.getString("name"));
                    leagues[i] = leagueName.getString("name") + " - " + leagueCountryName.getString("name");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            ArrayAdapter leaguesAdapter = new ArrayAdapter<String>(txtView.getContext(), R.layout.activity_football_list_view, leagues);
            //System.out.println(leagues);
            final ListView leagueList = (ListView) findViewById(R.id.leaguelist);
            leagueList.setAdapter(leaguesAdapter);
            leagueList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    //System.out.println("Position : " + position + " || Id : " + id);
                    Intent championship = new Intent(view.getContext(), Championship.class);
                    championship.putExtra("my_id", position);
                    startActivity(championship);
                }
            });
        }
    }
}
