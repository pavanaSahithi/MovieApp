package pavanasahithi.movieapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String> {

    public static final String API_KEY=BuildConfig.API;
    final static String popular = "https://api.themoviedb.org/3/movie/popular?api_key="+API_KEY;
    final static String top_rated = "https://api.themoviedb.org/3/movie/top_rated?api_key="+API_KEY;
    final static int POPULAR_ID = 22;
    final static int TOP_RATED_ID = 24;
    final static int Fav = 25;
    final static String KEY = "ID";
    final static String TYPEID = "TypeId";
    final static String POSITION = "Position";
    ArrayList<JsonPOJO> jsonPOJOs;
    RecyclerView recyclerView;
    RecyclerAdapter recyclerAdapter;
    int restore_id = POPULAR_ID;
    Bundle bundle1=new Bundle();
    int position;
    GridLayoutManager gridLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = (RecyclerView) findViewById(R.id.id_recycler_view);
        jsonPOJOs = new ArrayList<JsonPOJO>();
        if (isNetworkAvailable()) {
            if (savedInstanceState != null) {
                if (savedInstanceState.containsKey(KEY)) {
                    switch (savedInstanceState.getInt(KEY)) {
                        case POPULAR_ID:
                            restore_id = POPULAR_ID;
                            bundle1.putInt(TYPEID, restore_id);
                            getSupportLoaderManager().restartLoader(POPULAR_ID, bundle1, this);
                            break;
                        case TOP_RATED_ID:
                            restore_id = TOP_RATED_ID;
                            bundle1.putInt(TYPEID, restore_id);
                            getSupportLoaderManager().restartLoader(TOP_RATED_ID, bundle1, this);
                            break;
                        case Fav:
                            restore_id = Fav;
                            bundle1.putInt(TYPEID, restore_id);
                            favDisplay();
                            break;
                    }
                } else {
                    restore_id = POPULAR_ID;
                    bundle1.putInt(TYPEID, restore_id);
                    getSupportLoaderManager().restartLoader(POPULAR_ID, bundle1, this);
                }
            } else {
                restore_id = POPULAR_ID;
                bundle1.putInt(TYPEID,restore_id);
                bundle1.putInt(KEY, restore_id);
                getSupportLoaderManager().restartLoader(POPULAR_ID, bundle1, this);
            }
        } else {
            displayDialog();
            favDisplay();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY, restore_id);
        position=gridLayoutManager.findFirstVisibleItemPosition();
        outState.putInt(POSITION,position);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        position=savedInstanceState.getInt(POSITION);
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }

    public void displayDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.create();
        builder.setMessage(R.string.no_internet_connection);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Toast.makeText(MainActivity.this, R.string.redirecting_fav, Toast.LENGTH_SHORT).show();
                favDisplay();
            }
        });
        builder.show();
    }

    public String loader(String s) {
        ArrayList<JsonPOJO> jsonPOJO_in = new ArrayList<JsonPOJO>();
        JSONConnection connection = new JSONConnection();
        URL url = connection.buildUrl(s);
        String response = null;
        try {
            response = connection.getResponseFromHttpUrl(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray = jsonObject.getJSONArray(getString(R.string.json_results));
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject movie = jsonArray.getJSONObject(i);
                int vote_count = movie.optInt(getString(R.string.json_vote_count), 0);
                String id = movie.optString(getString(R.string.json_id), null);
                boolean video = movie.optBoolean(getString(R.string.json_video), false);
                long vote_average = movie.optLong(getString(R.string.json_vote_average), 0);
                String title = movie.optString(getString(R.string.json_title), null);
                long popularity = movie.optLong(getString(R.string.json_popularity), 0);
                String poster_path = movie.optString(getString(R.string.json_poster_path), null);
                String original_language = movie.optString(getString(R.string.json_original_language), null);
                String original_title = movie.optString(getString(R.string.json_original_title), null);
                String backdrop_path = movie.optString(getString(R.string.json_backdrop_path), null);
                boolean adult = movie.optBoolean(getString(R.string.json_adult), false);
                String overview = movie.optString(getString(R.string.json_overview), null);
                String release_date = movie.optString(getString(R.string.json_release_date), null);
                JsonPOJO jsonPOJO = new JsonPOJO(id, vote_average, title, poster_path,
                        original_language, original_title, backdrop_path, overview, release_date);
                response = jsonPOJO.getPoster_path();
                jsonPOJO_in.add(jsonPOJO);
                jsonPOJOs = jsonPOJO_in;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return response;
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public Loader<String> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case POPULAR_ID:
                return new AsyncTaskLoader<String>(this) {
                    String response = null;
                    @Override
                    public String loadInBackground() {
                        try {
                            response = loader(popular);
                        } catch (Exception e) {

                        }
                        return response;
                    }

                    @Override
                    protected void onStartLoading() {
                        if (response == null) {
                            forceLoad();
                        } else {
                            deliverResult(response);
                        }
                    }

                    @Override
                    public void deliverResult(String data) {
                        response = data;
                        super.deliverResult(data);
                    }
                };
            case TOP_RATED_ID:
                return new AsyncTaskLoader<String>(this) {
                    String response = null;

                    @Override
                    public String loadInBackground() {
                        response = loader(top_rated);
                        return response;
                    }

                    @Override
                    protected void onStartLoading() {
                        if (response == null) {
                            forceLoad();
                        } else {

                            deliverResult(response);
                        }
                    }

                    @Override
                    public void deliverResult(String data) {
                        response = data;
                        super.deliverResult(data);
                    }
                };

        }
        return null;
    }
    int posterWidth = 500;
    private int calculateBestSpanCount(int posterWidth) {
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);
        float screenWidth = outMetrics.widthPixels;
        return Math.round(screenWidth / posterWidth);
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String data) {
        if(loader.getId()==POPULAR_ID){
            gridLayoutManager=new GridLayoutManager(MainActivity.this,calculateBestSpanCount(posterWidth));
            recyclerAdapter = new RecyclerAdapter(MainActivity.this, jsonPOJOs);
            recyclerView.setLayoutManager(gridLayoutManager);
            recyclerView.setAdapter(recyclerAdapter);
            recyclerView.scrollToPosition(position);
            position=0;
        }
        else{
            gridLayoutManager=new GridLayoutManager(MainActivity.this,calculateBestSpanCount(posterWidth));
            recyclerAdapter = new RecyclerAdapter(MainActivity.this, jsonPOJOs);
            recyclerView.setLayoutManager(gridLayoutManager);
            recyclerView.setAdapter(recyclerAdapter);
            recyclerView.scrollToPosition(position);
            position=0;
        }

    }

    @Override
    public void onLoaderReset(Loader<String> loader) {
    }

    public void favDisplay() {
        gridLayoutManager=new GridLayoutManager(MainActivity.this,calculateBestSpanCount(posterWidth));
        Cursor c = getContentResolver().query(ContractClass.TableEntry.CONTENT_URI,
                null, null, null, null);
        if (c.getCount() == 0) {
            Toast.makeText(this, R.string.noFav, Toast.LENGTH_SHORT).show();
            recyclerAdapter = new RecyclerAdapter(MainActivity.this, null);
            recyclerView.setLayoutManager(gridLayoutManager);
            recyclerView.setAdapter(recyclerAdapter);
            recyclerView.scrollToPosition(position);
            position=0;

        } else {
            jsonPOJOs = null;
            jsonPOJOs = new ArrayList<JsonPOJO>();
            while (c.moveToNext()) {
                JsonPOJO jsonPOJO = new JsonPOJO(c.getString(0), c.getLong(7), c.getString(1), c.getString(3),
                        c.getString(5), c.getString(6), c.getString(2), c.getString(8), c.getString(4));
                jsonPOJOs.add(jsonPOJO);
                recyclerAdapter = new RecyclerAdapter(MainActivity.this, jsonPOJOs);
                recyclerView.setLayoutManager(gridLayoutManager);
                recyclerView.setAdapter(recyclerAdapter);
                recyclerView.scrollToPosition(position);
                position=0;
            }
            c.close();

        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.id_popular:
                jsonPOJOs = new ArrayList<JsonPOJO>();
                if (isNetworkAvailable()) {
                    restore_id = POPULAR_ID;
                    bundle1.putInt(TYPEID, restore_id);
                    getSupportLoaderManager().restartLoader(POPULAR_ID, bundle1, this);
                } else {
                    displayDialog();
                }
                break;
            case R.id.id_top_rated:
                jsonPOJOs = new ArrayList<JsonPOJO>();
                if (isNetworkAvailable()) {
                    restore_id = TOP_RATED_ID;
                    bundle1.putInt(TYPEID, restore_id);
                    getSupportLoaderManager().restartLoader(TOP_RATED_ID, bundle1, this);
                } else {
                    displayDialog();
                }
                break;
            case R.id.id_favourites:
                restore_id = Fav;
                bundle1.putInt(TYPEID, restore_id);
                favDisplay();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onResume() {
        super.onResume();
        restore_id = bundle1.getInt(TYPEID);
        switch (restore_id) {
                case POPULAR_ID:
                    jsonPOJOs = new ArrayList<JsonPOJO>();
                    if (isNetworkAvailable()) {
                        restore_id = POPULAR_ID;
                        bundle1.putInt(TYPEID, restore_id);
                        getSupportLoaderManager().restartLoader(POPULAR_ID, bundle1, this);
                    } else {
                        displayDialog();
                    }
                    break;
                case TOP_RATED_ID:
                    jsonPOJOs = new ArrayList<JsonPOJO>();
                    if (isNetworkAvailable()) {
                        restore_id = TOP_RATED_ID;
                        bundle1.putInt(TYPEID, restore_id);
                        getSupportLoaderManager().restartLoader(TOP_RATED_ID, bundle1, this);
                    } else {
                        displayDialog();
                    }
                    break;
                case Fav:
                    restore_id = Fav;
                    bundle1.putInt(TYPEID, restore_id);
                    favDisplay();
            }
        }
    }
