/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.moviedatabase.utilities;

import android.nfc.Tag;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * These utilities will be used to communicate with the network.
 */
public class NetworkUtils {

    String  id;

    final static String MOVIE_POPULAR_URL =
            "https://api.themoviedb.org/3/movie/popular?api_key=f2b1ee93b23ec622ee804999aff4567e";

    final static String MOVIE_TOP_RATED_URL =
            "https://api.themoviedb.org/3/movie/top_rated?api_key=f2b1ee93b23ec622ee804999aff4567e";

    final static String MOVIE_REVIEW =
            "https://api.themoviedb.org/3/movie/";

    final static String MOVIE_TRAILER =
            "https://api.themoviedb.org/3/movie/";

    final static String PARAM_QUERY = "q";
    final static String REVIEW_REST = "/reviews?api_key=f2b1ee93b23ec622ee804999aff4567e&language=en-US&page=1";
    final static String TRAILER_REST = "/videos?api_key=f2b1ee93b23ec622ee804999aff4567e";


    /*
     * The sort field. One of stars, forks, or updated.
     * Default: results are sorted by best match if no field is specified.
     */
    final static String PARAM_SORT = "sort";
    final static String sortBy = "stars";



    public static URL buildPopularUrl() throws MalformedURLException {
        String word= MOVIE_POPULAR_URL;

        Log.d("top rated", "bisa");

        URL url = new URL(word);

        return url;
    }

    public static URL buildTopRatedUrl() throws MalformedURLException {
        String word= MOVIE_TOP_RATED_URL;

        Log.d("top rated","bisa");

        URL url = new URL(word);

        return url;
    }

    public static URL buildReview(String id) throws MalformedURLException {
        String idnow = id;
        Log.d("Review", "bisa");
        URL url = new URL(MOVIE_REVIEW+idnow+REVIEW_REST);

        return url;
    }

    public static URL buildTrailer(String id) throws MalformedURLException {
        String idnow = id;
        Log.d("Trailer", "bisa");
        URL url = new URL(MOVIE_TRAILER+idnow+TRAILER_REST);

        return url;
    }
    /**
     * This method returns the entire result from the HTTP response.
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response.
     * @throws IOException Related to network and stream reading
     */
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}