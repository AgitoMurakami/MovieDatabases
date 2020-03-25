package com.example.moviedatabase;


import com.example.moviedatabase.model.APIModel;
import com.example.moviedatabase.model.Review;
import com.example.moviedatabase.model.Trailer;

public interface IConnectInternet {

    void callback(APIModel obj);
    void callbackreview (Review obj);
    void callbacktrailer (Trailer obj);
}
