package com.example.hw5application;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.hw5application.utilities.NetworkUtilities;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.ArrayList;
import java.util.Random;

public class SearchActivity extends AppCompatActivity {

    private TextView mSearchTitle;
    private EditText mSearchBar;
    private LinearLayout mSearchResults;
    private Button mSearchButton;
    private Button mResetButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

    mSearchTitle = (TextView) findViewById(R.id.tv_search_title);
    mSearchBar = (EditText) findViewById(R.id.et_search_bar);
    mSearchResults = (LinearLayout) findViewById(R.id.ll_search_results);
    mSearchButton = (Button) findViewById(R.id.search_button);
    mResetButton = (Button) findViewById(R.id.reset_button);

    mSearchTitle.append("Search Page");
    final String defaultDisplayText = "";

    mSearchButton.setOnClickListener(
            new View.OnClickListener(){ // a unnamed object
                //inner method def
                public void onClick(View v){
                    String searchTerm = mSearchBar.getText().toString();
                    TextView searchText = new TextView(getApplicationContext());
                    searchText.setText("Searching for: \"" + searchTerm + "\" \n\n");
                    mSearchResults.addView(searchText);

                    makeNetworkSearchQuery();
                } // end of onClick method

            } // end of View.OnClickListener
    ); // end of setOnClickListener

    mResetButton.setOnClickListener(
            new View.OnClickListener(){ // a unnamed object
                //inner method def
                public void onClick(View v){
                    mSearchBar.setText(defaultDisplayText);
                    mSearchResults.removeAllViews();
                } // end of onClick method
             } // end of View.OnClickListener
    ); // end of setOnClickListener
    }

    public void makeNetworkSearchQuery(){
        new SearchActivity.FetchNetworkData().execute();
    }

    public class FetchNetworkData extends AsyncTask<String, Void, String>{
        @Override
        protected String doInBackground(String... params){
            //get search term
            String searchTerm = mSearchBar.getText().toString();
            //put in result box
            Log.d("info", "doInBackground: " + searchTerm);

            //set search format i.e. " " -> %20
            String newSearchTerm = searchTerm.replace(" ","%20");
            Log.d("info", "doInBackground: " + newSearchTerm);

            //get Url
            URL topUrl = NetworkUtilities.buildURL("https://api.jikan.moe/v3/search/anime?q=" + newSearchTerm + "&genre=12&genre_exclude=0");

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
                mSearchResults.addView(title);

                score.setText("User Score: " + (animeList.get(i)).getScore());
                score.setTextSize(15);
                score.setGravity(Gravity.CENTER_HORIZONTAL);
                mSearchResults.addView(score);

                String imageUri = ((animeList.get(i)).getImageURL());
                Picasso.get().load(imageUri).resize(700,1000).centerCrop().into(titleImage);
                mSearchResults.addView(titleImage);

                space.setText("\n");
                mSearchResults.addView(space);


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
        if(menuItemSelected == R.id.menu_random){
            //react to menu search
            Class destinationActivity = RandomActivity.class;
            Intent startRandomActivityIntent = new Intent(SearchActivity.this,destinationActivity);
            startActivity(startRandomActivityIntent);
            Log.d("info","Random Activity launched");
        }
        else if(menuItemSelected == R.id.menu_home){
            //react to menu random
            Class destinationActivity = MainActivity.class;
            Intent startMainActivityIntent = new Intent(SearchActivity.this,destinationActivity);
            startActivity(startMainActivityIntent);
            Log.d("info","Main Activity launched");

        }//end if
        return true;
    }

}
