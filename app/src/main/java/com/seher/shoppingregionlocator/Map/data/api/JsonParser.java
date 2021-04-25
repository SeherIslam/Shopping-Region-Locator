package com.seher.shoppingregionlocator.Map.data.api;


import com.seher.shoppingregionlocator.Map.data.entity.Places;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

public class JsonParser
{

    public static ArrayList<Places> parseJson(String result)
    {
        ArrayList<Places> placesList = new ArrayList<>();

        try
        {
            JSONObject jsonObject = new JSONObject(result);
            JSONArray jsonArray = (JSONArray) jsonObject.get("results");

            for(int i=0;i<jsonArray.length();i++)
            {
                JSONObject jsonOBJ = jsonArray.getJSONObject(i);

                Places places = new Places();

                places.setPlace_id(jsonOBJ.get("place_id").toString());

                if(jsonOBJ.has("name"))
                {
                    places.setName(jsonOBJ.get("name").toString());
                }

                if(jsonOBJ.has("rating"))
                {
                    places.setRating(Float.parseFloat(jsonOBJ.get("rating").toString()));
                }

                else
                {
                    places.setRating(Float.parseFloat("0"));
                }

                if(jsonOBJ.has("price_level"))
                {
                    places.setPrice_level(Integer.parseInt((jsonOBJ.get("price_level").toString())));
                }

                if(jsonOBJ.has("vicinity"))
                {
                    places.setAddress(jsonOBJ.get("vicinity").toString());
                }

                if(jsonOBJ.has("geometry"))
                {
                    places.setLat(Double.parseDouble(jsonOBJ.getJSONObject("geometry").getJSONObject("location").get("lat").toString()));
                    places.setLng(Double.parseDouble(jsonOBJ.getJSONObject("geometry").getJSONObject("location").get("lng").toString()));
                }

                if(jsonOBJ.has("photos"))
                {
                    places.setPhoto_reference(jsonOBJ.getJSONArray("photos").getJSONObject(0).getString("photo_reference").toString());
                }

                if(jsonOBJ.has("opening_hours"))
                {
                    places.setOpen_now(jsonOBJ.getJSONObject("opening_hours").getBoolean("open_now"));
                }

                if(jsonOBJ.has("types"))
                {
                    places.setType(jsonOBJ.getJSONArray("types").get(0).toString().toUpperCase());
                }

                placesList.add(places);
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

        Collections.sort(placesList);

        return placesList;
    }
}