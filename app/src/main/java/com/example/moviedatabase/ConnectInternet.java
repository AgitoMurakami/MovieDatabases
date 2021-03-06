package com.example.moviedatabase;

import android.os.AsyncTask;
import android.util.Log;


import com.example.moviedatabase.IConnectInternet;
import com.example.moviedatabase.model.APIModel;
import com.example.moviedatabase.utilities.NetworkUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.net.URL;

public class ConnectInternet extends AsyncTask<URL, Integer, APIModel> {

    public static final String TAG = "ConnectInternetTask";

    private IConnectInternet callbackHandler = null;

    public ConnectInternet (IConnectInternet callback) {
        this.callbackHandler = callback;
    }

    @Override
    protected APIModel doInBackground(URL... urls) {
        String tmpResult = "";
        try {
            tmpResult = NetworkUtils.getResponseFromHttpUrl(urls[0]);

        }catch (IOException e ){
            Log.e(TAG,e.getMessage());
        }
        Log.d("TEST",tmpResult);

        Gson tmpGSON =new GsonBuilder().create();
        APIModel tmpModel = tmpGSON.fromJson(tmpResult, APIModel.class);

        return tmpModel;
    }

    @Override
    protected void onPostExecute(APIModel s) {
        super.onPostExecute(s);

        if(callbackHandler != null) {
            callbackHandler.callback(s);
        }
    }
}
