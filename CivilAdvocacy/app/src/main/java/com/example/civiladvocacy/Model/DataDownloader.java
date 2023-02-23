

package com.example.civiladvocacy.Model;

import android.net.Uri;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DataDownloader {

    private static MainActivity mainActivity;
    private static RequestQueue queue;

    private static List<Official_Gov_Details> officialsList = new ArrayList<>();

    private static final String dataUrl = "https://www.googleapis.com/civicinfo/v2/representatives";
    private static final String key = "AIzaSyADW7fYTCiStvXcPAOcXiu61g7Dk_FKw44";

    public static void downloadData(MainActivity activity, String address) {

        mainActivity = activity;
        queue = Volley.newRequestQueue(mainActivity);

        Uri.Builder buildURL = Uri.parse(dataUrl).buildUpon();
        buildURL.appendQueryParameter("key", key);
        buildURL.appendQueryParameter("address", address);

        String urlToUse = buildURL.build().toString();
        System.out.println("DataDownlaod UrlToUSe::::::::::::" + urlToUse);

        Response.Listener<JSONObject> listener = response -> parseJSON(response.toString());

        Response.ErrorListener error = error1 -> {
            mainActivity.updateData(null);
        };

        JsonObjectRequest jsonObjectRequest;
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, urlToUse, null, listener, error);

        queue.add(jsonObjectRequest);
    }

    private static void parseJSON(String s) {
        try {

            JSONObject jObjMain,normalizedInputObj;
            String address;
            JSONArray officesJSONArr,officialsJSONArr;
            List<Official_Gov_Details> governmentOfficials = new ArrayList<>();



            jObjMain  = new JSONObject(s);
            normalizedInputObj = jObjMain.getJSONObject("normalizedInput");


            address = normalizedInputObj.getString("line1")+" "+ normalizedInputObj.getString("city")+" "+ normalizedInputObj.getString("state")+" "+ normalizedInputObj.getString("zip") ;
            officesJSONArr = (JSONArray) jObjMain.getJSONArray("offices");

            System.out.println("DataDownlaod Address::::::::::::" + address);
            officialsJSONArr = (JSONArray) jObjMain.getJSONArray("officials");



            for (int i = 0; i < officialsJSONArr.length(); i++) {

                JSONObject officialIndicesObj;
                officialIndicesObj= (JSONObject) officialsJSONArr.get(i);
                String name,party,photoUrl = "",phoneNo = "",urls = "",emails = "",officialAddress = "";
                List<String[]> list = new ArrayList<>();

                name = officialIndicesObj.getString("name");
                party = officialIndicesObj.getString("party");

                if(officialIndicesObj.has("photoUrl"))
                    photoUrl = officialIndicesObj.getString("photoUrl");
                System.out.println("PhotoURl :::::::::"+photoUrl);




                if(officialIndicesObj.has("phones"))
                    phoneNo = ((JSONArray) officialIndicesObj.getJSONArray("phones")).get(0).toString();


                if(officialIndicesObj.has("urls"))
                    urls = ((JSONArray) officialIndicesObj.getJSONArray("urls")).get(0).toString();


                if(officialIndicesObj.has("emails"))
                    emails = ((JSONArray) officialIndicesObj.getJSONArray("emails")).get(0).toString();


                if(officialIndicesObj.has("address")) {
                    JSONObject officialAdd = (JSONObject) (((JSONArray) officialIndicesObj.getJSONArray("address")).get(0));

                    officialAddress = officialAdd.getString("line1") + " " + officialAdd.getString("city") + " " + officialAdd.getString("state") + " " + officialAdd.getString("zip");
                }

                if(officialIndicesObj.has("channels")) {
                    JSONArray officialChannel = (JSONArray) officialIndicesObj.getJSONArray("channels");

                    for (int j = 0; j < officialChannel.length(); j++) {
                        JSONObject channelObj = (JSONObject) officialChannel.get(j);
                        String[] channel = {channelObj.getString("type"), channelObj.getString("id")};
                        System.out.println("Channel String0::::::::"+channel[0]);
                        list.add(channel);
                    }
                }

                Official_Gov_Details officialGovDetails1 = new Official_Gov_Details(name, party, photoUrl, officialAddress, phoneNo,emails, urls, list);
                governmentOfficials.add(officialGovDetails1);
            }

            officialsList = new ArrayList<>();
            for (int i = 0; i < officesJSONArr.length(); i++) {

                JSONObject officialIndicesObj = (JSONObject) officesJSONArr.get(i);
                String post = officialIndicesObj.getString("name");
                JSONArray officialIndices = (JSONArray) officialIndicesObj.getJSONArray("officialIndices");
                for (int j = 0; j < officialIndices.length(); j++) {

                    int index = Integer.parseInt(officialIndices.get(j).toString());
                    System.out.println("Index::::::::::::"+index);
                    Official_Gov_Details go = governmentOfficials.get(index);

                    Official_Gov_Details finalGovObj = new Official_Gov_Details(address, post, go.getName(), go.getParty(), go.getOfficialAddress(), go.getPhoneNumber(),go.getUrls(),go.getEmail(),go.getPhotoUrl(),go.getChannelList());
                    System.out.println("FinalObject::::::::::::"+finalGovObj.toString());
                    officialsList.add(finalGovObj);
                }

            }
            System.out.println("DataDownlaod ::::::::::::" + officialsList.get(0).getParty());
            mainActivity.updateData(officialsList);

        }catch (Exception e) {
            e.printStackTrace();
        }
    }

}
