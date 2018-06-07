package pavanasahithi.movieapp;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;


public class MovieActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String> {
    public static final int VIDEOS_ID = 35;
    public static final int REVIEWS_ID = 40;
    public static RecyclerView recyclerViewVideo, recyclerViewReviews;
    ImageView back_drop, poster;
    TextView title, synopsis, rating, release_date, language, reviews;
    JSONConnection js;
    RatingBar ratingBar;
    Button buttonFavourite;
    String id_movie = "";
    ReviewAdapter recyclerAdapterReview;
    VideoAdapter recyclerAdapterVideo;
    ArrayList<ReviewJSONPOJO> reviewJSONPOJOs;
    ArrayList<VideoJSONPOJO> videoJSONPOJOs;
    FavoritesDB favoritesDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);
        back_drop = (ImageView) findViewById(R.id.id_backdrop_poster);
        poster = (ImageView) findViewById(R.id.id_poster);
        title = (TextView) findViewById(R.id.id_title);
        rating = (TextView) findViewById(R.id.id_user_rating);
        synopsis = (TextView) findViewById(R.id.id_synopsis);
        release_date = (TextView) findViewById(R.id.id_release_date);
        language = (TextView) findViewById(R.id.id_language);
        ratingBar = (RatingBar) findViewById(R.id.id_rating_bar);
        buttonFavourite = (Button) findViewById(R.id.id_button_favourite);
        reviews = (TextView) findViewById(R.id.id_tv_reviews);
        js = new JSONConnection();
        final Bundle bundle = getIntent().getExtras();
        final String backdrop_path = bundle.getString(getString(R.string.back_drop_path));
        final String Normal_Title = bundle.getString(getString(R.string.title));
        final String tv_synopsis = bundle.getString(getString(R.string.synopsis));
        final String tv_title = bundle.getString(getString(R.string.title_original));
        final String poster_path = bundle.getString(getString(R.string.poster_path));
        final double tv_rating = bundle.getDouble(getString(R.string.rating));
        final String tv_releaseDate = bundle.getString(getString(R.string.release_date));
        final String tv_language = bundle.getString(getString(R.string.language));
        id_movie = bundle.getString(getString(R.string.id));
        favoritesDB = new FavoritesDB(this);
        Picasso.with(this).load(String.valueOf(js.buildImageUrl(backdrop_path))).placeholder(R.drawable.error).
                error(R.drawable.error).into(back_drop);
        Picasso.with(this).load(String.valueOf(js.buildImageUrl(poster_path))).placeholder(R.drawable.error).
                error(R.drawable.error).into(poster);
        setTitle(Normal_Title);
        title.setText(tv_title);
        synopsis.setText(tv_synopsis);
        release_date.setText(tv_releaseDate);
        rating.setText(Double.toString(tv_rating));
        language.setText(tv_language);
        ratingBar.setRating((float) tv_rating);
        recyclerViewVideo = (RecyclerView) findViewById(R.id.id_videos_recycler_view);
        recyclerViewReviews = (RecyclerView) findViewById(R.id.id_reviews_recycler_view);
        reviewJSONPOJOs = new ArrayList<ReviewJSONPOJO>();
        if (isNetworkAvailable()) {
            getSupportLoaderManager().restartLoader(REVIEWS_ID, null, this);
            getSupportLoaderManager().restartLoader(VIDEOS_ID, null, this);
            reviews.setVisibility(View.GONE);
        } else {
            Toast.makeText(this, R.string.network, Toast.LENGTH_SHORT).show();
        }

        if (isPresent(id_movie)) {
            buttonFavourite.setText(R.string.added_fav);
        }
        buttonFavourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isPresent(id_movie)) {
                    Uri uri = ContractClass.TableEntry.CONTENT_URI.buildUpon().appendPath(id_movie).build();
                    int i = getContentResolver().delete(uri, null, null);
                    Toast.makeText(MovieActivity.this, getString(R.string.deleted) + i, Toast.LENGTH_SHORT).show();
                    buttonFavourite.setText(R.string.add_to_favourite);
                } else {
                    buttonFavourite.setText(R.string.added_fav);
                    ContentValues cv = new ContentValues();
                    cv.put(ContractClass.TableEntry.ColumnId, id_movie);
                    cv.put(ContractClass.TableEntry.ColumnTitle, Normal_Title);
                    cv.put(ContractClass.TableEntry.ColumnBackdropPath, backdrop_path);
                    cv.put(ContractClass.TableEntry.ColumnPosterPath, poster_path);
                    cv.put(ContractClass.TableEntry.ColumnDate, tv_releaseDate);
                    cv.put(ContractClass.TableEntry.ColumnLanguage, tv_language);
                    cv.put(ContractClass.TableEntry.ColumnOriginalTitle, tv_title);
                    cv.put(ContractClass.TableEntry.ColumnRating, tv_rating);
                    cv.put(ContractClass.TableEntry.ColumnSynopsis, tv_synopsis);
                    Uri uri = getContentResolver().insert(ContractClass.TableEntry.CONTENT_URI, cv);
                    if (uri != null) {
                        Toast.makeText(MovieActivity.this, uri.toString(), Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }

    public boolean isPresent(String id) {
        Uri uri = ContractClass.TableEntry.CONTENT_URI.buildUpon().appendPath(id).build();
        Cursor cursor = getContentResolver().query(uri, null, null,
                null, null);
        int count = cursor.getCount();
        if (count == 0) {
            return false;
        } else {
            return true;
        }
    }
    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }

    public String loadVideos() {
        videoJSONPOJOs = new ArrayList<VideoJSONPOJO>();
        JSONConnection connection = new JSONConnection();
        URL url = connection.buildVideoUrl(id_movie);
        String response = null;
        try {
            response = connection.getResponseFromHttpUrl(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            if (response != null) {
                JSONObject jsonObject = new JSONObject(response);
                JSONArray jsonArray = jsonObject.getJSONArray(getString(R.string.json_results));
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject videos = jsonArray.getJSONObject(i);
                    String id = videos.optString(getString(R.string.json_id), null);
                    String iso_639_1 = videos.optString(getString(R.string.json_iso6391), null);
                    String iso_3166_1 = videos.optString(getString(R.string.json_iso31661), null);
                    String key = videos.optString(getString(R.string.json_key), null);
                    String name = videos.optString(getString(R.string.json_name), null);
                    String site = videos.optString(getString(R.string.json_site), null);
                    String size = videos.optString(getString(R.string.json_size), null);
                    String type = videos.optString(getString(R.string.json_type), null);
                    VideoJSONPOJO videoJSONPOJO = new VideoJSONPOJO(id, iso_639_1, iso_3166_1, key, name, site,
                            size, type);
                    videoJSONPOJOs.add(videoJSONPOJO);
                }
            }

        } catch (JSONException e1) {
            e1.printStackTrace();
        }
        return response;
    }

    public String loadReviews() {
        reviewJSONPOJOs = new ArrayList<ReviewJSONPOJO>();
        JSONConnection connection = new JSONConnection();
        URL url = connection.buildReviewUrl(id_movie);
        String response = null;
        try {
            response = connection.getResponseFromHttpUrl(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            if (response != null) {
                JSONObject jsonObject = new JSONObject(response);
                JSONArray jsonArray = jsonObject.getJSONArray(getString(R.string.json_results));
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject reviews = jsonArray.getJSONObject(i);
                    String author = reviews.optString(getString(R.string.json_author), null);
                    String content = reviews.optString(getString(R.string.json_content), null);
                    String id = reviews.optString(getString(R.string.json_id), null);
                    String review_url = reviews.optString(getString(R.string.json_url), null);
                    ReviewJSONPOJO reviewJSONPOJO = new ReviewJSONPOJO(author, content, id, review_url);
                    reviewJSONPOJOs.add(reviewJSONPOJO);
                }
            }
        } catch (JSONException e1) {
            Log.i("MyException", "" + e1);
            e1.printStackTrace();
        }
        return response;
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public Loader<String> onCreateLoader(int id_loader, Bundle args) {
        switch (id_loader) {
            case REVIEWS_ID:
                return new AsyncTaskLoader<String>(this) {
                    String response = null;

                    @Override
                    public String loadInBackground() {
                        response = loadReviews();
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
            case VIDEOS_ID:
                return new AsyncTaskLoader<String>(this) {
                    String response = null;

                    @Override
                    public String loadInBackground() {
                        response = loadVideos();
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

    @Override
    public void onLoadFinished(Loader<String> loader, String data) {
        if(loader.getId()==REVIEWS_ID){
            recyclerAdapterReview = new ReviewAdapter(MovieActivity.this, reviewJSONPOJOs);
            recyclerViewReviews.setLayoutManager(new LinearLayoutManager(MovieActivity.this));
            recyclerViewReviews.setAdapter(recyclerAdapterReview);
        }
        else{
            recyclerAdapterVideo = new VideoAdapter(MovieActivity.this, videoJSONPOJOs);
            recyclerViewVideo.setLayoutManager(new LinearLayoutManager(MovieActivity.this));
            recyclerViewVideo.setAdapter(recyclerAdapterVideo);
        }
    }

    @Override
    public void onLoaderReset(Loader<String> loader) {
    }

}
