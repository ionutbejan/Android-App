package com.example.john.sscores;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.style.TextAppearanceSpan;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static android.R.attr.color;
import static android.R.attr.colorPrimaryDark;
import static android.R.attr.id;
import static android.R.attr.textAppearanceMedium;

public class Championship extends AppCompatActivity {
    int league_id;

    public boolean onCreateOptionMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_championship);
        Intent championship = getIntent();
        int rleague_id = championship.getIntExtra("my_id", id);

        System.out.println("League id is : " + league_id);
        league_id = rleague_id;

        Button clasament = (Button) findViewById(R.id.clasament);
        clasament.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final TextView textView = (TextView) findViewById(R.id.simpleTextView);
                textView.setVisibility(View.GONE);
                TableLayout tableLayout = (TableLayout) findViewById(R.id.table);
                tableLayout.removeAllViews();

                ChampAsync tournament = new ChampAsync();
                tournament.execute();
                int j = tableLayout.getChildCount();
                for (int i = 0; i < j; i++){
                    View view = tableLayout.getChildAt(i);
                    TableRow row = (TableRow) view;
                    row.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            textView.setVisibility(View.VISIBLE);
                        }
                    });
                }
            }
        });

        Button dailyresults = (Button) findViewById(R.id.dailyresult);
        dailyresults.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final TextView textView = (TextView) findViewById(R.id.simpleTextView);
                textView.setVisibility(View.GONE);
                TableLayout tableLayout = (TableLayout) findViewById(R.id.table);
                tableLayout.removeAllViews();
                GamesAsync game = new GamesAsync();
                game.execute();

                int j = tableLayout.getChildCount();
                for (int i = 0; i < j; i++){
                    View view = tableLayout.getChildAt(i);
                    TableRow row = (TableRow) view;
                    row.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            textView.setVisibility(View.VISIBLE);
                            /* API call and display data received in this text view */
                        }
                    });
                }
            }
        });

        Button dailygames = (Button) findViewById(R.id.dailygames);
        dailygames.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final TextView textView = (TextView) findViewById(R.id.simpleTextView);
                textView.setVisibility(View.GONE);
                TableLayout tableLayout = (TableLayout) findViewById(R.id.table);
                tableLayout.removeAllViews();
                ScheduleAsync scheduleAsync = new ScheduleAsync();
                scheduleAsync.execute();

            }
        });

    }

    private class ChampAsync extends AsyncTask<Object, Object, String> implements ChampAsyncc{

        @Override
        protected String doInBackground(Object... params) {
            HttpURLConnection conn = null;
            HttpURLConnection newConn = null;
            StringBuilder response = new StringBuilder();
            StringBuilder tournaments = new StringBuilder();
            String[] leagueIds = new String[16];
            System.out.println(league_id);
            /* request tournament list so I can get leagues ID's */
            try {
                URL url = new URL("https://api.sportradar.us/soccer-t3/eu/en/tournaments.json?api_key=rcjd99h8gbawwwx7xaw2a8au");
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");

                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                String line;
                if (conn.getResponseCode() != 200) {
                    throw new RuntimeException("Failed: Error code : " + conn.getResponseCode());
                }
                while ((line = br.readLine()) != null) {
                    tournaments.append(line);
                    tournaments.append("\n");
                }
                br.close();
                System.out.println(tournaments);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (conn != null) {
                    conn.disconnect();
                }
            }
            /* try/catch parsing JSONObject to make an array with tournament ID's*/
                try {
                    JSONObject obj = new JSONObject(tournaments.toString());
                    JSONArray names = obj.getJSONArray("tournaments");
                    int n = names.length();
                    for (int i = 0; i < n; i++) {
                        JSONObject leagueName = names.getJSONObject(i);
                        leagueIds[i] = leagueName.getString("id");
                    }
                    //System.out.println(leagueIds[1]);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            /* request data for certain tournament */
                try {
                    System.out.println("League_id : " + league_id);
                    URL url = new URL("https://api.sportradar.us/soccer-t3/eu/en/tournaments/" + leagueIds[league_id] + "/standings.json?api_key=rcjd99h8gbawwwx7xaw2a8au");
                    newConn = (HttpURLConnection) url.openConnection();
                    do {
                        newConn.setRequestMethod("GET");
                    } while (newConn.getResponseCode() != 200);

                    System.out.println("newConn initialized . Code : " + newConn.getResponseCode());

                    BufferedReader newBr = new BufferedReader(new InputStreamReader(newConn.getInputStream()));

                    String newLine;
                    if (newConn.getResponseCode() != 200) {
                        System.out.println("Failed: error code : " + newConn.getResponseCode());
                        throw new RuntimeException("Failed: error code : " + newConn.getResponseCode());
                    } else {
                        System.out.println("Success : code - " + newConn.getResponseCode());
                    }

                    while ((newLine = newBr.readLine()) != null) {
                        response.append(newLine);
                        response.append("\n");
                        System.out.println("In while ...");
                    }
                    //System.out.println(response);
                    newBr.close();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (newConn != null) {
                        newConn.disconnect();
                    }
                }
                //System.out.println(response);
                return response.toString();
            }
        @Override
        public void onPostExecute(String result) {
            if (league_id != 0 && league_id != 14 && league_id != 15 && league_id != 16)  {
                TableLayout table = (TableLayout) findViewById(R.id.table);
                String[] total_points = {};
                table.setStretchAllColumns(true);
                table.bringToFront();
                try {
                    JSONObject obj = new JSONObject(result);
                    JSONArray type_array = obj.getJSONArray("standings").getJSONObject(0).getJSONArray("groups").getJSONObject(0).getJSONArray("team_standings");
                    System.out.println(type_array + "\n" + type_array.length());
                    int n = type_array.length();
                    int c = 0;
                    TableRow tablerow = new TableRow(getBaseContext());
                    TextView textview1 = new TextView(getBaseContext());textview1.setText("Rank");textview1.setTextColor(Color.BLACK);textview1.setTypeface(null, Typeface.BOLD_ITALIC);
                    TextView textview2 = new TextView(getBaseContext());textview2.setText("Team Name");textview2.setTextColor(Color.BLACK);textview2.setTypeface(null, Typeface.BOLD_ITALIC);
                    TextView textview4 = new TextView(getBaseContext());textview4.setText("Games");textview4.setTextColor(Color.BLACK);textview4.setTypeface(null, Typeface.BOLD_ITALIC);
                    TextView textview5 = new TextView(getBaseContext());textview5.setText("Win");textview5.setTextColor(Color.BLACK);textview5.setTypeface(null, Typeface.BOLD_ITALIC);
                    TextView textview6 = new TextView(getBaseContext());textview6.setText("Draw");textview6.setTextColor(Color.BLACK);textview6.setTypeface(null, Typeface.BOLD_ITALIC);
                    TextView textview7 = new TextView(getBaseContext());textview7.setText("Loss");textview7.setTextColor(Color.BLACK);textview7.setTypeface(null, Typeface.BOLD_ITALIC);
                    TextView textview3 = new TextView(getBaseContext());textview3.setText("Pts");textview3.setTextColor(Color.BLACK);textview3.setTypeface(null, Typeface.BOLD_ITALIC);
                    tablerow.addView(textview1);tablerow.addView(textview2);tablerow.addView(textview4);tablerow.addView(textview5);tablerow.addView(textview6);tablerow.addView(textview7);tablerow.addView(textview3);
                    table.addView(tablerow);
                    for (int i = 0; i < n; i++) {
                        JSONObject teams_array = type_array.getJSONObject(i);
                        JSONObject team_name_array = type_array.getJSONObject(i).getJSONObject("team");
                        //total_points[i] = teams_array.getString("rank") + team_name_array.getString("name") + teams_array.getString("points");
                        //System.out.println(teams_array.getString("rank") + " " + team_name_array.getString("name") + " " + teams_array.getString("points"));
                        TableRow tr = new TableRow(getBaseContext());
                        TextView tv1 = new TextView(getBaseContext());tv1.setText("#" + teams_array.getString("rank"));tv1.setTextColor(Color.DKGRAY);tv1.setTypeface(null, Typeface.BOLD);
                        TextView tv2 = new TextView(getBaseContext());tv2.setText(team_name_array.getString("name"));tv2.setTextColor(Color.DKGRAY);tv2.setTypeface(null, Typeface.BOLD);
                        TextView tv4 = new TextView(getBaseContext());tv4.setText(teams_array.getString("played"));tv4.setTextColor(Color.DKGRAY);
                        TextView tv5 = new TextView(getBaseContext());tv5.setText(teams_array.getString("win"));tv5.setTextColor(Color.GREEN);
                        TextView tv6 = new TextView(getBaseContext());tv6.setText(teams_array.getString("draw"));tv6.setTextColor(Color.BLUE);
                        TextView tv7 = new TextView(getBaseContext());tv7.setText(teams_array.getString("loss"));tv7.setTextColor(Color.CYAN);
                        TextView tv3 = new TextView(getBaseContext());tv3.setText(teams_array.getString("points"));tv3.setTextColor(Color.RED);tv3.setTypeface(null, Typeface.BOLD);
                        tr.addView(tv1);tr.addView(tv2);tr.addView(tv4);tr.addView(tv5);tr.addView(tv6);tr.addView(tv7);tr.addView(tv3);
                        table.addView(tr);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                System.out.println(result);
            }else{
                TableLayout table = (TableLayout) findViewById(R.id.table);
                String[] total_points = {};
                table.setStretchAllColumns(true);
                table.bringToFront();
                try {
                    JSONObject obj = new JSONObject(result);
                    JSONArray type_array = obj.getJSONArray("standings").getJSONObject(0).getJSONArray("groups");/*.getJSONObject(0).getJSONArray("team_standings");*/
                    //JSONArray groups_array = obj.getJSONArray("standings").getJSONObject(0).getJSONArray("groups");
                    System.out.println(type_array + "\n" + type_array.length());
                    int m = type_array.length();
                    //int n = type_array.length();
                    int c = 0;

                    /* First table line */
                    TableRow tablerow = new TableRow(getBaseContext());
                    TextView textview1 = new TextView(getBaseContext());textview1.setText("Rank");textview1.setTextColor(Color.BLACK);textview1.setTypeface(null, Typeface.BOLD_ITALIC);
                    TextView textview2 = new TextView(getBaseContext());textview2.setText("Team Name");textview2.setTextColor(Color.BLACK);textview2.setTypeface(null, Typeface.BOLD_ITALIC);
                    TextView textview4 = new TextView(getBaseContext());textview4.setText("Games");textview4.setTextColor(Color.BLACK);textview4.setTypeface(null, Typeface.BOLD_ITALIC);
                    TextView textview5 = new TextView(getBaseContext());textview5.setText("Win");textview5.setTextColor(Color.BLACK);textview5.setTypeface(null, Typeface.BOLD_ITALIC);
                    TextView textview6 = new TextView(getBaseContext());textview6.setText("Draw");textview6.setTextColor(Color.BLACK);textview6.setTypeface(null, Typeface.BOLD_ITALIC);
                    TextView textview7 = new TextView(getBaseContext());textview7.setText("Loss");textview7.setTextColor(Color.BLACK);textview7.setTypeface(null, Typeface.BOLD_ITALIC);
                    TextView textview3 = new TextView(getBaseContext());textview3.setText("Pts");textview3.setTextColor(Color.BLACK);textview3.setTypeface(null, Typeface.BOLD_ITALIC);
                    tablerow.addView(textview1);tablerow.addView(textview2);tablerow.addView(textview4);tablerow.addView(textview5);tablerow.addView(textview6);tablerow.addView(textview7);tablerow.addView(textview3);
                    table.addView(tablerow);
                    //System.out.println("MADAFAKA M = " + m);
                    for (int j = 0; j < m; j++) {
                        /* Group name ! */
                        JSONObject group_name = type_array.getJSONObject(j);
                        TableRow grouprow = new TableRow(getBaseContext());
                        TextView group = new TextView(getBaseContext());group.setText("Group " + group_name.getString("name"));
                        grouprow.addView(group);
                        table.addView(grouprow);
                        int n = type_array.length();
                        System.out.println(" MADAFAKA N = " + n);
                        for (int i = 0; i < 4; i++) {
                            JSONObject teams_array = type_array.getJSONObject(j);
                            JSONArray team_name_array = teams_array.getJSONArray("team_standings");
                            JSONObject team_name = team_name_array.getJSONObject(i);
                            JSONObject team = team_name.getJSONObject("team");
                            //total_points[i] = teams_array.getString("rank") + team_name_array.getString("name") + teams_array.getString("points");
                            //System.out.println(teams_array.getString("rank") + " " + team_name_array.getString("name") + " " + teams_array.getString("points"));
                            TableRow tr = new TableRow(getBaseContext());
                            TextView tv1 = new TextView(getBaseContext());tv1.setText("#" + team_name.getString("rank"));tv1.setTextColor(Color.DKGRAY);tv1.setTypeface(null, Typeface.BOLD);
                            TextView tv2 = new TextView(getBaseContext());tv2.setText(team.getString("name"));tv2.setTextColor(Color.DKGRAY);tv2.setTypeface(null, Typeface.BOLD);
                            TextView tv4 = new TextView(getBaseContext());tv4.setText(team_name.getString("played"));tv4.setTextColor(Color.DKGRAY);
                            TextView tv5 = new TextView(getBaseContext());tv5.setText(team_name.getString("win"));tv5.setTextColor(Color.GREEN);
                            TextView tv6 = new TextView(getBaseContext());tv6.setText(team_name.getString("draw"));tv6.setTextColor(Color.BLUE);
                            TextView tv7 = new TextView(getBaseContext());tv7.setText(team_name.getString("loss"));tv7.setTextColor(Color.CYAN);
                            TextView tv3 = new TextView(getBaseContext());tv3.setText(team_name.getString("points"));tv3.setTextColor(Color.RED);tv3.setTypeface(null, Typeface.BOLD);
                            tr.addView(tv1);tr.addView(tv2);tr.addView(tv4);tr.addView(tv5);tr.addView(tv6);tr.addView(tv7);tr.addView(tv3);
                            table.addView(tr);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                System.out.println(result);
            }
        }
    }

    private class GamesAsync extends AsyncTask<Object, Object, String> implements ChampAsyncc{

        @Override
        protected String doInBackground(Object... params) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DATE, -1);
            String date = sdf.format(cal.getTime());
            //System.out.println("Current date : " + date);
            HttpURLConnection conn = null;
            StringBuilder response = new StringBuilder();
            try{
                URL url = new URL("https://api.sportradar.us/soccer-t3/eu/en/schedules/2017-01-16/results.json?api_key=rcjd99h8gbawwwx7xaw2a8au");
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                //System.out.println("inainte de bufferedreader");
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                //System.out.println("dupa bufferedreader");

                String line;
                if(conn.getResponseCode() != 200){
                    //throw new RuntimeException("Failed: error code : " + conn.getResponseCode());
                    //System.out.println("Nu s-au gasit rezultate .");
                    response.append("No results found.\n");
                }
                while ((line = br.readLine()) != null){
                    response.append(line);
                    response.append("\n");
                }
                br.close();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                if(conn != null){
                    conn.disconnect();
                }
            }
            //System.out.println(response);
            return response.toString();
        }
        @Override
        public void onPostExecute(String result){
            TableLayout table = (TableLayout) findViewById(R.id.table);
            table.setStretchAllColumns(true);
            table.bringToFront();
            try{
                JSONObject obj = new JSONObject(result);
                JSONArray results = obj.getJSONArray("results");
                int n = results.length();
                String team1; String score;
                String team2;

                /* First table row */
                TableRow tableRow = new TableRow(getBaseContext());
                TextView textview1 = new TextView(getBaseContext());textview1.setText("Yesterday's Results");textview1.setTextColor(Color.DKGRAY);textview1.setTypeface(null,Typeface.BOLD_ITALIC);textview1.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                tableRow.addView(textview1);
                table.addView(tableRow);
                for (int i = 0; i < n; i++){
                    JSONObject sport_event = results.getJSONObject(i).getJSONObject("sport_event");
                    JSONArray competitors = sport_event.getJSONArray("competitors");
                    team1 = competitors.getJSONObject(0).getString("name") + "(" + competitors.getJSONObject(0).getString("abbreviation") + ")";
                    team2 = competitors.getJSONObject(1).getString("name") + "(" + competitors.getJSONObject(1).getString("abbreviation") + ")";
                    JSONObject sport_event_status = results.getJSONObject(i).getJSONObject("sport_event_status");
                    JSONArray period_score = sport_event_status.getJSONArray("period_scores");
                    score = sport_event_status.getString("home_score") + " - " + sport_event_status.getString("away_score") + " (" + period_score.getJSONObject(1).getString("home_score") + "-" + period_score.getJSONObject(1).getString("away_score") + ")";
                    TableRow tr = new TableRow(getBaseContext());
                    TextView textview2 = new TextView(getBaseContext()); textview2.setText("Home"); textview2.setTypeface(null,Typeface.ITALIC);
                    TextView textview3 = new TextView(getBaseContext()); textview3.setText("Score"); textview3.setTypeface(null,Typeface.ITALIC);
                    TextView textview4 = new TextView(getBaseContext()); textview4.setText("Away"); textview4.setTypeface(null,Typeface.ITALIC);
                    textview4.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_END);
                    tr.addView(textview2);tr.addView(textview3);tr.addView(textview4);
                    table.addView(tr);
                    TableRow game_tr = new TableRow(getBaseContext());
                    TextView textview_home = new TextView(getBaseContext()); textview_home.setText(team1); textview_home.setTextColor(Color.BLUE); textview_home.setTypeface(null,Typeface.BOLD);
                    TextView textview_score = new TextView(getBaseContext()); textview_score.setText(score); textview_score.setTextColor(Color.BLUE); textview_score.setTypeface(null,Typeface.BOLD);
                    TextView textview_away = new TextView(getBaseContext()); textview_away.setText(team2); textview_away.setTextColor(Color.BLUE); textview_away.setTypeface(null,Typeface.BOLD);
                    game_tr.addView(textview_home);game_tr.addView(textview_score);game_tr.addView(textview_away);
                    table.addView(game_tr);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            final TextView textView = (TextView) findViewById(R.id.simpleTextView);
            int j = table.getChildCount();
            System.out.println("Table rows : " + j);
            for (int i = 2; i < j; i+=2){
                View view = table.getChildAt(i);
                if (view instanceof TableRow) {
                    TableRow row = (TableRow) view;
                    row.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            textView.setVisibility(View.VISIBLE);

                            /* API call... */
                        }
                    });
                }
            }
            System.out.println(result);
        }
    }

    private class ScheduleAsync extends AsyncTask<Object, Object, String> implements ChampAsyncc{

        @Override
        protected String doInBackground(Object... params) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DATE, 0);
            String date = sdf.format(cal.getTime());
            //System.out.println("Current date : " + date);
            HttpURLConnection conn = null;
            StringBuilder response = new StringBuilder();
            try{
                URL url = new URL("https://api.sportradar.us/soccer-t3/eu/en/schedules/"+date+"/schedule.json?api_key=rcjd99h8gbawwwx7xaw2a8au");
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                //System.out.println("inainte de bufferedreader");
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                //System.out.println("dupa bufferedreader");

                String line;
                if(conn.getResponseCode() != 200){
                    //throw new RuntimeException("Failed: error code : " + conn.getResponseCode());
                    //System.out.println("Nu s-au gasit rezultate .");
                    response.append("No results found.\n");
                }
                while ((line = br.readLine()) != null){
                    response.append(line);
                    response.append("\n");
                }
                br.close();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                if(conn != null){
                    conn.disconnect();
                }
            }
            //System.out.println(response);
            return response.toString();
        }

        @Override
        public void onPostExecute(String result){
            TableLayout table = (TableLayout) findViewById(R.id.table);
            table.setStretchAllColumns(true);
            table.bringToFront();
            String scheduled; String team1; String team2;
            try{
                JSONObject obj = new JSONObject(result);
                JSONArray sport_events = obj.getJSONArray("sport_events");
                int n = sport_events.length();

                /* First table row */
                TableRow tableRow = new TableRow(getBaseContext());
                TextView textview1 = new TextView(getBaseContext());textview1.setText("Today's Games");textview1.setTextColor(Color.DKGRAY);textview1.setTypeface(null,Typeface.BOLD_ITALIC);textview1.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                tableRow.addView(textview1);
                table.addView(tableRow);
                for (int i = 0; i < n; i++){
                    scheduled = sport_events.getJSONObject(i).getString("scheduled");
                    String split[] = scheduled.split("T");
                    String match_schedule[] = split[1].split(":");
                    System.out.println("Ora meciului : " + match_schedule[0] + ":" + match_schedule[1]);
                    JSONArray competitors = sport_events.getJSONObject(i).getJSONArray("competitors");
                    team1 = competitors.getJSONObject(0).getString("name") + "(" + competitors.getJSONObject(0).getString("abbreviation") + ")";
                    team2 = competitors.getJSONObject(1).getString("name") + "(" + competitors.getJSONObject(1).getString("abbreviation") + ")";
                    TableRow tr = new TableRow(getBaseContext());
                    TextView textview2 = new TextView(getBaseContext()); textview2.setText("Home"); textview2.setTypeface(null,Typeface.ITALIC);
                    TextView textview3 = new TextView(getBaseContext()); textview3.setText("Start"); textview3.setTypeface(null,Typeface.ITALIC);
                    TextView textview4 = new TextView(getBaseContext()); textview4.setText("Away"); textview4.setTypeface(null,Typeface.ITALIC);
                    textview4.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_END);
                    tr.addView(textview2);tr.addView(textview3);tr.addView(textview4);
                    table.addView(tr);

                    /*dinamyc part*/
                    TableRow game_tr = new TableRow(getBaseContext());
                    TextView textview_home = new TextView(getBaseContext()); textview_home.setText(team1); textview_home.setTextColor(Color.BLUE); textview_home.setTypeface(null,Typeface.BOLD);
                    TextView textview_schedule = new TextView(getBaseContext()); textview_schedule.setText(match_schedule[0]+":"+match_schedule[1]); textview_schedule.setTextColor(Color.BLUE); textview_schedule.setTypeface(null,Typeface.BOLD);
                    TextView textview_away = new TextView(getBaseContext()); textview_away.setText(team2); textview_away.setTextColor(Color.BLUE); textview_away.setTypeface(null,Typeface.BOLD);
                    game_tr.addView(textview_home);game_tr.addView(textview_schedule);game_tr.addView(textview_away);
                    table.addView(game_tr);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            final TextView textView = (TextView) findViewById(R.id.simpleTextView);
            int j = table.getChildCount();
            System.out.println("Table rows : " + j);
            for (int i = 2; i < j; i+=2){
                View view = table.getChildAt(i);
                if (view instanceof TableRow) {
                    TableRow row = (TableRow) view;
                    row.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            textView.setVisibility(View.VISIBLE);

                            /* API call... */
                        }
                    });
                }
            }
            System.out.println("Table successfully created ! ");
        }
    }
}
