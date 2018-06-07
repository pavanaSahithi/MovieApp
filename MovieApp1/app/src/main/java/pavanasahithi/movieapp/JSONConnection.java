package pavanasahithi.movieapp;

import android.net.Uri;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;


/**
 * Created by Lenovo on 05-05-2018.
 */

public class JSONConnection {
    final static String Image_URL = "https://image.tmdb.org/t/p/w500";
    final static String MOVIE = "https://api.themoviedb.org/3/movie";

    JSONConnection() {
    }

    public static URL buildImageUrl(final String path) {
        String image = Image_URL + "" + path;
        Uri ImageUri = Uri.parse(image);
        URL url = null;
        try {
            url = new URL(ImageUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    public static URL buildUrl(String string) {
        Uri builtUri = Uri.parse(string).buildUpon().build();
        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    public static URL buildVideoUrl(String id) {
        Uri builtUri = Uri.parse(MOVIE).buildUpon().appendPath(id).appendPath("videos").appendQueryParameter("api_key", "568419cf1624f771f76d027f914272f3").build();
        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("GET");
        String TAG = "-->";
        String response = null;
        try {
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            response = convertStreamToString(in);
        } catch (MalformedURLException e) {
            Log.e(TAG, "MalformedURLException: " + e.getMessage());
        } catch (ProtocolException e) {
            Log.e(TAG, "ProtocolException: " + e.getMessage());
        } catch (IOException e) {
            Log.e(TAG, "IOException: " + e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
        return response;
    }

    private static String convertStreamToString(InputStream in) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        StringBuilder sb = new StringBuilder();

        String line;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    public URL buildReviewUrl(String id) {
        Uri builtUri = Uri.parse(MOVIE).buildUpon().appendPath(id).appendPath("reviews").appendQueryParameter("api_key", "568419cf1624f771f76d027f914272f3").build();
        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }
}


