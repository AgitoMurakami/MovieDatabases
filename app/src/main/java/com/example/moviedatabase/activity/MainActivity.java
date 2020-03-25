package com.example.moviedatabase.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moviedatabase.AppExecutor;
import com.example.moviedatabase.ConnectInternet;
import com.example.moviedatabase.Films;
import com.example.moviedatabase.IConnectInternet;
import com.example.moviedatabase.R;
import com.example.moviedatabase.adapters.FilmsAdapter;
import com.example.moviedatabase.database.AppDatabase;
import com.example.moviedatabase.database.TaskEntry;
import com.example.moviedatabase.model.APIModel;
import com.example.moviedatabase.model.Result;
import com.example.moviedatabase.model.Review;
import com.example.moviedatabase.model.Trailer;
import com.example.moviedatabase.utilities.NetworkUtils;

import java.lang.reflect.Array;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import static com.example.moviedatabase.utilities.NetworkUtils.buildPopularUrl;
import static com.example.moviedatabase.utilities.NetworkUtils.buildTopRatedUrl;

public class MainActivity extends AppCompatActivity implements IConnectInternet, FilmsAdapter.ListItemClickListerner {

    private static final String TAG = "MAIN";
    Context context = this;
    TextView mUrlDisplayTextView;
    CardView card;
    FrameLayout frame;
    RecyclerView recyclerView;
    FilmsAdapter mAdapter;
    ArrayList<Result> results = new ArrayList<Result>();
    URL urlPopular = null;
    URL urlTopRated = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        card = findViewById(R.id.cardView);
        mUrlDisplayTextView = findViewById(R.id.testtext);
        frame = findViewById(R.id.frame);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        mAdapter = new FilmsAdapter(this, (FilmsAdapter.ListItemClickListerner) this);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        //url

        // build url for popular and toprated
        try {
            urlPopular = NetworkUtils.buildPopularUrl();
            urlTopRated = NetworkUtils.buildTopRatedUrl();
        }catch (MalformedURLException e) {
            String textToShow = "URL gagal build";
            Toast.makeText(context, textToShow, Toast.LENGTH_SHORT).show();
        }

        if(urlPopular != null){
            mUrlDisplayTextView.setText(urlPopular.toString());
            new ConnectInternet(this).execute(urlPopular);
            frame.setVisibility(View.VISIBLE);
        }try {
            buildPopularUrl();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }



    //for menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.popular:

                try {
                    urlPopular = NetworkUtils.buildPopularUrl();
                    urlTopRated = NetworkUtils.buildTopRatedUrl();
                }catch (MalformedURLException e) {
                    String textToShow = "URL gagal build";
                    Toast.makeText(context, textToShow, Toast.LENGTH_SHORT).show();
                }

                if(urlPopular != null){
                    mUrlDisplayTextView.setText(urlPopular.toString());
                    new ConnectInternet(this).execute(urlPopular);
                    frame.setVisibility(View.VISIBLE);
                }try {
                buildPopularUrl();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }


                break;
            case R.id.top_rated:

                try {
                    urlPopular = NetworkUtils.buildPopularUrl();
                    urlTopRated = NetworkUtils.buildTopRatedUrl();
                }catch (MalformedURLException e) {
                    String textToShow = "URL gagal build";
                    Toast.makeText(context, textToShow, Toast.LENGTH_SHORT).show();
                }

                if(urlTopRated != null){
                    mUrlDisplayTextView.setText(urlTopRated.toString());
                    new ConnectInternet(this).execute(urlTopRated);
                    frame.setVisibility(View.VISIBLE);
                }try {
                    buildTopRatedUrl();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }


                break;
            case R.id.favorites:
                final ArrayList<Result> favoritResult = new ArrayList<Result>();
                AppExecutor.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        List<TaskEntry> favorit = AppDatabase
                                .getInstance(getApplicationContext()).taskDao().loadAllTasks();

                        for (TaskEntry taskEntry : favorit) {
                            Result a = new Result();
                            a.setId(taskEntry.getId());
                            a.setTitle(taskEntry.getTitle());
                            a.setReleaseDate(taskEntry.getYear());
                            a.setVoteAverage(Double.parseDouble(taskEntry.getRating()) );
                            a.setOverview(taskEntry.getDescription());
                            a.setPosterPath(taskEntry.getPosterpath());

                            favoritResult.add(a);
                        }
                    }
                });

                AppExecutor.getInstance().mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.setMovieVMList(favoritResult);
                    }
                });

                frame.setVisibility(View.VISIBLE);
                refreshList();
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void refreshList() {
        mAdapter = new FilmsAdapter(this, (FilmsAdapter.ListItemClickListerner) this);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    public void callback(APIModel obj) {
        results = obj.getResults();
        mAdapter.setMovieVMList(results);
    }

    @Override
    public void callbackreview(Review obj) {

    }

    @Override
    public void callbacktrailer(Trailer obj) {

    }

    @Override
    public void onListItemClicked(Result o) {
        Toast.makeText(this,o.getTitle(),Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getBaseContext(), DetailActivity.class);

        intent.putExtra("ID", o.getId().toString());
        intent.putExtra("TITLE", o.getTitle());
        intent.putExtra("YEAR", o.getReleaseDate());
        intent.putExtra("DESCRIPTION", o.getOverview());
        intent.putExtra("RATING", o.getVoteAverage().toString());
        intent.putExtra("PHOTO",o.getPosterPath());

        startActivity(intent);


    }
}
