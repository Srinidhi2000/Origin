package com.example.android.dictionary;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
//Helper class
public final class Connect {
    private Connect() {
    }
//To create url from a string
    private static URL createUrl(String stringurl) {
        URL url = null;
        try {
            url = new URL(stringurl);
        } catch (MalformedURLException e) {
            Log.e("Connect", "Problem in building URL", e);
        }
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";
        if (url == null) {
            return jsonResponse;
        }
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestProperty("app_id","41f590bd");
            urlConnection.setRequestProperty("app_key","b16c5918f9bbc780dcd32924fb554c0f");
            urlConnection.setRequestProperty("Accept","application/json");
            urlConnection.setReadTimeout(100000);
            urlConnection.setConnectTimeout(100000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readInputStream(inputStream);
            } else {
                Log.e("Connect", "Error response code:" + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e("Connect", "Problem retrieving Info", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }

        return jsonResponse;
    }

    private static String readInputStream(InputStream inputStream) throws IOException {
        StringBuilder finalstring = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String currentline = reader.readLine();
            while (currentline != null) {
                finalstring.append(currentline);
                currentline = reader.readLine();
            }
        }
        return finalstring.toString();
    }
private static Dic_parameters Extract_word(String jsonResponse)
{
    if(TextUtils.isEmpty(jsonResponse))
    {
        return null;
    }
    Dic_parameters current=null;
    try{
        JSONObject root=new JSONObject(jsonResponse);
        JSONArray results=root.getJSONArray("results");
        for(int i=0;i<results.length();i++)
        {   JSONObject one=results.getJSONObject(i);
            JSONArray two=one.getJSONArray("lexicalEntries");
            JSONObject three=two.getJSONObject(0);
            JSONArray entries=three.getJSONArray("entries");
            JSONObject four=entries.getJSONObject(0);
            JSONArray etimologies=four.getJSONArray("etymologies");
           String string=etimologies.getString(0);
           current=new Dic_parameters(string);
        }
    } catch (JSONException e) {
        e.printStackTrace();
    }
    return current;
}
public static Dic_parameters Fetch_word(String requestedUrl)
{
    URL url=createUrl(requestedUrl);
    String jsonResponse=null;
    try{
        jsonResponse=makeHttpRequest(url);
    }catch (IOException e){
        Log.e("Connect","Problem making HTTP request",e);
    }
    Dic_parameters current=Extract_word(jsonResponse);
    return current;
}
}

