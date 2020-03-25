package com.example.moviedatabase.activity;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import android.content.Context;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.moviedatabase.AppExecutor;
import com.example.moviedatabase.ConnectInternet;
import com.example.moviedatabase.ConnectInternetReview;
import com.example.moviedatabase.ConnectInternetTrailer;
import com.example.moviedatabase.IConnectInternet;
import com.example.moviedatabase.R;
import com.example.moviedatabase.database.AppDatabase;
import com.example.moviedatabase.database.TaskEntry;
import com.example.moviedatabase.model.APIModel;
import com.example.moviedatabase.model.Result;
import com.example.moviedatabase.model.Review;
import com.example.moviedatabase.model.ReviewResult;
import com.example.moviedatabase.model.Trailer;
import com.example.moviedatabase.model.TrailerLink;
import com.example.moviedatabase.utilities.NetworkUtils;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static com.example.moviedatabase.utilities.NetworkUtils.buildPopularUrl;
import static com.example.moviedatabase.utilities.NetworkUtils.buildReview;
import static com.example.moviedatabase.utilities.NetworkUtils.buildTopRatedUrl;
import static com.example.moviedatabase.utilities.NetworkUtils.buildTrailer;

public class DetailActivity extends AppCompatActivity implements IConnectInternet {


    private static final String TAG = "";
    TextView titleTextView;
    TextView yearTextView ;
    TextView descriptionTextView ;
    TextView ratingTextView;
    TextView reviewTextView;
    TextView trailerTextView;
    ImageView imageView;
    int idnow = 0;


    Context context = this;
    private List<ReviewResult> reviews = new ArrayList<>();
    private List<TrailerLink> trailers = new ArrayList<>();

    //database
    private AppDatabase mDb;

    public String title, year, description, photo, rating;
    public int id;
    //ICON BOOLEAN
    public Boolean icon=false;

    //urls
    public URL urlReview;
    public URL urlTrailer;

    //menu item
    public Menu menu;
    public MenuItem settingsItem;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail2);

        //set database
        mDb = AppDatabase.getInstance(getApplicationContext());

        titleTextView = findViewById(R.id.titledetail);
        yearTextView = findViewById(R.id.yeardetail);
        ratingTextView =findViewById(R.id.rating);
        descriptionTextView = findViewById(R.id.descriptiondetail);
        reviewTextView = findViewById(R.id.reviewdetail);
        trailerTextView = findViewById(R.id.trailerdetail);
        imageView = findViewById(R.id.lalala);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            title = extras.getString("TITLE");
            year = extras.getString("YEAR");
            description = extras.getString("DESCRIPTION");
            photo = extras.getString("PHOTO");
            rating = extras.getString("RATING");


            String id =  extras.getString("ID");
            try {
                idnow = Integer.parseInt(id);
            } catch(NumberFormatException nfe) {

            }

            //The key argument here must match that used in the other activity

            titleTextView.setText(title);
            yearTextView.setText(year);
            ratingTextView.setText(rating);
            descriptionTextView.setText(description);

            Glide.with(this).load("https://image.tmdb.org/t/p/w185/"+photo).into(imageView);

            //check if id is there
            AppExecutor.getInstance().diskIO().execute(new Runnable() {
                @Override
                public void run() {
                    TaskEntry favouriteEntry = mDb.taskDao().loadFavById(idnow);
                    if (favouriteEntry != null){
                        icon = true;

                    }
                }
            });


            //get review NOT DONE
            //
            //
            try {
                urlReview = NetworkUtils.buildReview(id);
            }catch (MalformedURLException e) {
                String textToShow = "URL gagal build";
                Toast.makeText(context, textToShow, Toast.LENGTH_SHORT).show();
            }

            if(urlReview != null){
                new ConnectInternetReview(this).execute(urlReview);

            }try {
                buildReview(id);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            //setTrailer
            try {
                urlTrailer = NetworkUtils.buildTrailer(id);
            }catch (MalformedURLException e) {
                String textToShow = "URL gagal build";
                Toast.makeText(context, textToShow, Toast.LENGTH_SHORT).show();
            }

            if(urlTrailer != null){
                new ConnectInternetTrailer(this).execute(urlTrailer);

            }try {
                buildTrailer(id);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        this.menu = menu;
        getMenuInflater().inflate(R.menu.menu_like,menu);
        setIconFav(icon);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_favorite:
                //do something with favorite button
                //////////////////////////////////
                invalidateOptionsMenu();
                AppExecutor.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            TaskEntry taskEntry = new TaskEntry(idnow, title, year, description, rating, photo);
                            mDb.taskDao().insertTask(taskEntry);
                            Log.d("SAVE", "SAVED THE NEW ADDED FILE : " + title);
                            icon = true;
                        }catch (Exception e) {
                            e.printStackTrace();
                            TaskEntry taskEntry = new TaskEntry(idnow, title, year, description, rating, photo);
                            mDb.taskDao().deleteTask(taskEntry);
                            Log.d("DELETE","DELETED THE NEW ADDED FILE : "+title);
                            icon = false;
                        }
                    }
                });
                setIconFav(!icon);
                ///////////////////////////////////////////////////
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private  void setIconFav(Boolean isfavourite){
        MenuItem item = menu.getItem(0);

        if (isfavourite){
            Log.d("Paporit","garis");
            item.setIcon(R.drawable.facebook_like);
            this.icon=true;
        }else {
            Log.d("Paporit","hitam");
            item.setIcon(R.drawable.like_png_icon_1);
            this.icon=false;
        }

    }

    @Override
    public void callback(APIModel obj) {

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void callbackreview(Review obj) {
        reviews = obj.getResults();
        String fin = "";

        Set<String> reviewess = new LinkedHashSet<>();

        for (ReviewResult review : reviews) {
            String reviewes = "";
            String title = "=*=*=*=*=*=*=";
            String authorr = review.getAuthor().toString();
            String contentt = review.getContent().toString();
            Set<String> join = new LinkedHashSet<>();
            join.add(title);
            join.add(authorr);
            join.add(contentt);
            reviewes = reviewes.join("\n",join);

            reviewess.add(reviewes);
        }
        fin = fin.join("\n\n",reviewess);
        reviewTextView.setText(fin);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void callbacktrailer(Trailer obj) {
        trailers = obj.getResults();//NULL

        Set<String> join = new LinkedHashSet<>();
        String all = "";

        for (TrailerLink link : trailers) {
            String key = link.getKey();
            String youtube = "https://www.youtube.com/watch?v="+key;

            join.add(youtube);

        }
        all = all.join("\n\n",join);
        trailerTextView.setText(all);

    }
}
