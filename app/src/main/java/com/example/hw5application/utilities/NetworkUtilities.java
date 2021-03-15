package com.example.hw5application.utilities;

import android.util.Log;

import com.example.hw5application.AnimeObj;

import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;


public class NetworkUtilities {

        public static URL buildURL(String urlString) {
            URL animeUrl = null;
            try {
                animeUrl = new URL(urlString);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            return animeUrl;
        }

        public static String getResponseFromUrl(URL url) throws IOException {
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            try {
                InputStream in = urlConnection.getInputStream();
                Scanner scanner = new Scanner(in);
                scanner.useDelimiter("\\A");
                boolean hasInput = scanner.hasNext();
                if (hasInput) return scanner.next();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                urlConnection.disconnect();
            }
            return null;
        }


        public static ArrayList<AnimeObj> parseAnimeJson(String responseString) {
            ArrayList<AnimeObj> animeList = new ArrayList<AnimeObj>();
            try {
                Log.d("debugging", "ResponseText from url: " + responseString);
                JSONObject queryResults = new JSONObject(responseString);
                JSONArray allAnimeObjs;
                if (queryResults.has("top")) {
                    allAnimeObjs = queryResults.getJSONArray("top");
                    for (int i = 0; i < allAnimeObjs.length(); i++) {
                        JSONObject currObj = allAnimeObjs.getJSONObject(i);
                        String title = currObj.getString("title");
                        String imageURL = currObj.getString("image_url");
                        double score = currObj.getDouble("score");
                        String rating = "";
                        boolean airing = true;
                        String descrip = "";
                        String type = currObj.getString("type");
                        AnimeObj newAnimeObj = new AnimeObj(title, imageURL, score, rating, airing, descrip, type);
                        animeList.add(newAnimeObj);
                    }
                }
                else {
                    allAnimeObjs = queryResults.getJSONArray("results");
                for (int i = 0; i < allAnimeObjs.length(); i++) {
                    JSONObject currObj = allAnimeObjs.getJSONObject(i);
                    String title = currObj.getString("title");
                    String imageURL = currObj.getString("image_url");
                    double score = currObj.getDouble("score");
                    String rating = currObj.getString("rated");
                    boolean airing = currObj.getBoolean("airing");
                    String descrip = currObj.getString("synopsis");
                    String type = currObj.getString("type");
                    AnimeObj newAnimeObj = new AnimeObj(title,imageURL,score,rating,airing,descrip,type);
                    animeList.add(newAnimeObj);
                }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return animeList;
        }
}