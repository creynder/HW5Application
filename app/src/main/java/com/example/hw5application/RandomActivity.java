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
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hw5application.utilities.NetworkUtilities;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.ArrayList;
import java.util.Random;

public class RandomActivity extends AppCompatActivity {

    private TextView mRandomPageTitle;
    private TextView mRandomPageDescription;
    private TextView mRandomTitle;
    private TextView mRandomDescription;
    private TextView mRandomScore;
    private TextView mRandomRating;
    private TextView mRandomAiring;
    private TextView mRandomType;
    private ImageView mRandomImage;
    private Button mRandomize;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_random2);

        mRandomPageTitle = (TextView) findViewById(R.id.tv_random_title);
        mRandomPageDescription = (TextView) findViewById(R.id.tv_random_descrip);
        mRandomTitle = (TextView) findViewById(R.id.tv_title_text);
        mRandomDescription = (TextView) findViewById(R.id.tv_descrip_text);
        mRandomScore = (TextView) findViewById(R.id.tv_score_text);
        mRandomRating = (TextView) findViewById(R.id.tv_rating_text);
        mRandomAiring = (TextView) findViewById(R.id.tv_airing_text);
        mRandomType = (TextView) findViewById(R.id.tv_type_text);
        mRandomImage = (ImageView) findViewById(R.id.iv_title_image);
        mRandomize = (Button) findViewById(R.id.b_randomize);

        mRandomPageTitle.append("Anime Randomizer");
        mRandomPageDescription.append("Hit the randomize button to get some information about a random anime.");

        final String defaultDisplayText = "";

        mRandomize.setOnClickListener(
                new View.OnClickListener(){ // a unnamed object
                    //inner method def
                    public void onClick(View v){
                        mRandomTitle.setText(defaultDisplayText);
                        mRandomDescription.setText(defaultDisplayText);
                        mRandomScore.setText(defaultDisplayText);
                        mRandomRating.setText(defaultDisplayText);
                        mRandomAiring.setText(defaultDisplayText);
                        mRandomType.setText(defaultDisplayText);
                        makeNetworkSearchQuery();
                    } // end of onClick method

                } // end of View.OnClickListener
        ); // end of setOnClickListener
    }


    public void makeNetworkSearchQuery(){
        new RandomActivity.FetchNetworkData().execute();
    }

    public class FetchNetworkData extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params){

            //get random genre
            Random random = new Random();
            int randomGenre = random.nextInt(43) + 1;
            if (randomGenre == 12){
                while(randomGenre == 12){
                    randomGenre = random.nextInt(43);
                    randomGenre += 1;
                }
            }

            String genreNum = String.valueOf(randomGenre);
            String urlString = "https://api.jikan.moe/v3/search/anime?q&order_by=score&genre=" + genreNum + "&genre_exclude=1";

            //get Url
            URL randomUrl = NetworkUtilities.buildURL(urlString);

            //get response
            String responseString = null;
            try{
                responseString = NetworkUtilities.getResponseFromUrl(randomUrl);
            }
            catch(Exception e){
                e.printStackTrace();
            }
            return responseString;
        } //end doInBackground

        @Override
        protected void onPostExecute(String responseData){
            ArrayList<AnimeObj> animeList = NetworkUtilities.parseAnimeJson(responseData);

            //get random entry
            Random random = new Random();
            int randomEntry = random.nextInt(animeList.size());

            AnimeObj randomAnime = animeList.get(randomEntry);
            //int randomMalID = randomAnime.getMalID();

            //display in GUI here
            mRandomTitle.append(randomAnime.getTitle() + "\n");
            mRandomDescription.append(randomAnime.getDescrip() + "\n");
            String score = String.valueOf(randomAnime.getScore());
            mRandomScore.append("User Score: " + score + "\n");
            String rating = String.valueOf(randomAnime.getRating());
            mRandomRating.append("TV Rating: " + rating  + "\n");
            if(randomAnime.isAiring() == true){
                mRandomAiring.append("Status: Currently Airing" + "\n");
            }
            else{
                mRandomAiring.append("Status: Finished Airing" + "\n");
            }
            mRandomType.append("Type: " + randomAnime.getType());

            //populate ScrollView with Title Info
            String imageUri = randomAnime.getImageURL();
            Picasso.get().load(imageUri).resize(700,1000).centerCrop().into(mRandomImage);
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
            Intent startSearchActivityIntent = new Intent(RandomActivity.this,destinationActivity);
            startActivity(startSearchActivityIntent);
            Log.d("info","Search Activity launched");
        }
        else if(menuItemSelected == R.id.menu_home){
            //react to menu random
            Class destinationActivity = MainActivity.class;
            Intent startMainActivityIntent = new Intent(RandomActivity.this,destinationActivity);
            startActivity(startMainActivityIntent);
            Log.d("info","Main Activity launched");

        }//end if
        return true;
    }

}