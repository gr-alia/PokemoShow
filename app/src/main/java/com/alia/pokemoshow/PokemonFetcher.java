package com.alia.pokemoshow;


import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class PokemonFetcher {
    private static final String TAG = "FlickrFetchr";
    private static final String API_URL = "http://pokeapi.co/api/v2/pokemon/";

    public byte[] getUrlBytes(String urlSpec) throws IOException {
        URL url = new URL(urlSpec);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            InputStream in = connection.getInputStream();
            //getResponceCode() Gets the status code from an HTTP response message. OK=200
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new IOException(connection.getResponseMessage() +
                        ": with " +
                        urlSpec);
            }
            int bytesRead = 0;
            byte[] buffer = new byte[1024];
            //read(buffer) Reads 1024 of bytes from the input stream and stores them into the buffer aaray
            while ((bytesRead = in.read(buffer)) > 0) {
                out.write(buffer, 0, bytesRead);
            }
            out.close();
            return out.toByteArray();
        } finally {
            connection.disconnect();
        }
    }

    public String getUrlString(String urlSpec) throws IOException {
        return new String(getUrlBytes(urlSpec));
    }

    public List<GalleryItem> fetchItems() {
        List<GalleryItem> items = new ArrayList<>();
        try {
            for (int i = 1; i < 21; i++) {
                //URI Takes a line, splits it into parts and stores this information in itself
                String url = Uri.parse(API_URL + i)
                        .buildUpon()        // to obtain a builder representing an existing URI
                        .build().toString();
                String jsonString = getUrlString(url);
                Log.i(TAG, "Received JSON: " + jsonString);
                JSONObject jsonBody = new JSONObject(jsonString);
                parseItems(items, jsonBody);
            }


        } catch (JSONException je) {
            Log.e(TAG, "Failed to parse JSON", je);
        } catch (IOException ioe) {
            Log.e(TAG, "Failed to fetch items", ioe);
        }
        return items;
    }

    private void parseItems(List<GalleryItem> items, JSONObject jsonBody)
            throws IOException, JSONException {


        String name = jsonBody.getString("name");
        JSONObject spritesJsonObject = jsonBody.getJSONObject("sprites");
        String spritesUrl = spritesJsonObject.getString("front_default");

        Log.i(TAG, name);
        if (spritesUrl != null) {
            Log.i(TAG, spritesUrl);
        }
        GalleryItem item = new GalleryItem();
        item.setName(name);
        item.setUrl(spritesUrl);
        items.add(item);




        /*
        for (int i = 0; i < photoJsonArray.length(); i++) {
            JSONObject photoJsonObject = photoJsonArray.getJSONObject(i);
            GalleryItem item = new GalleryItem();
            item.setId(photoJsonObject.getString("id"));
            item.setCaption(photoJsonObject.getString("title"));
            if (!photoJsonObject.has("url_s")) {
                continue;
            }
            item.setUrl(photoJsonObject.getString("url_s"));
            items.add(item);
        }
        */

    }
}
