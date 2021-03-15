package com.example.hw5application;

import com.example.hw5application.utilities.NetworkUtilities;
import com.squareup.picasso.Picasso;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;


import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private TextView mMainTitle;
    private TextView mMainDescrip;
    private LinearLayout mTopAiring;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMainTitle = (TextView) findViewById(R.id.tv_app_title);
        mMainDescrip = (TextView) findViewById(R.id.tv_app_descrip);
        mTopAiring = (LinearLayout) findViewById(R.id.ll_top_airing);


        //Display Things to Text View
        mMainTitle.append("Top Airing Anime");
        mMainDescrip.append("Listed below are this season's top anime, including with title, art, and user ratings");

        makeNetworkSearchQuery();

    }

        public void makeNetworkSearchQuery(){
            new FetchNetworkData().execute();
        }

        public class FetchNetworkData extends AsyncTask<String, Void, String>{
            @Override
            protected String doInBackground(String... params){
                //get Url
                URL topUrl = NetworkUtilities.buildURL("https://api.jikan.moe/v3/top/anime/1/airing");

                //get response
                String responseString = null;
                try{
                    responseString = NetworkUtilities.getResponseFromUrl(topUrl);
                }
                catch(Exception e){
                    e.printStackTrace();
                }
                return responseString;
            } //end doInBackground

            @Override
            protected void onPostExecute(String responseData){
                ArrayList<AnimeObj> animeList = NetworkUtilities.parseAnimeJson(responseData);
                //display in GUI here
                //populate ScrollView with Title Info
                for (int i = 0; i < animeList.size()-1; i++){
                    ImageView titleImage = new ImageView(getApplicationContext());
                    TextView title = new TextView(getApplicationContext());
                    TextView score = new TextView(getApplicationContext());
                    TextView space = new TextView(getApplicationContext());

                    title.setText((animeList.get(i)).getTitle());
                    title.setTextSize(20);
                    title.setTypeface(Typeface.DEFAULT_BOLD);
                    title.setGravity(Gravity.CENTER_HORIZONTAL);
                    mTopAiring.addView(title);
                    
                    score.setText("User Score: " + (animeList.get(i)).getScore());
                    score.setTextSize(15);
                    score.setGravity(Gravity.CENTER_HORIZONTAL);
                    mTopAiring.addView(score);

                    String imageUri = ((animeList.get(i)).getImageURL());
                    Picasso.get().load(imageUri).resize(700,1000).centerCrop().into(titleImage);
                    mTopAiring.addView(titleImage);

                    space.setText("\n");
                    mTopAiring.addView(space);


                }
            }
        } // end of inner class


    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    } //end of create menu

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int menuItemSelected = item.getItemId();
        if(menuItemSelected == R.id.menu_search){
            //react to menu search
            Class destinationActivity = SearchActivity.class;
            Intent startSearchActivityIntent = new Intent(MainActivity.this,destinationActivity);
            startActivity(startSearchActivityIntent);
            Log.d("info","Search Activity launched");
        }
        else if(menuItemSelected == R.id.menu_random){
            //react to menu random
            Class destinationActivity = RandomActivity.class;
            Intent startRandomActivityIntent = new Intent(MainActivity.this,destinationActivity);
            startActivity(startRandomActivityIntent);
            Log.d("info","Random Activity launched");

        }//end if
        return true;
    }





}