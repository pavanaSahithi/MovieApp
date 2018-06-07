package pavanasahithi.movieapp;

/**
 * Created by Lenovo on 05-05-2018.
 */

public class JsonPOJO {
    long vote_average;
    String title, poster_path, original_language, original_title,
            backdrop_path, overview, release_date, id;

    JsonPOJO(String id, long vote_average, String title, String poster_path,
             String original_language, String original_title, String backdrop_path, String overview,
             String release_date) {
        this.vote_average = vote_average;
        this.id = id;
        this.title = title;
        this.poster_path = poster_path;
        this.original_language = original_language;
        this.original_title = original_title;
        this.backdrop_path = backdrop_path;
        this.overview = overview;
        this.release_date = release_date;
    }

    public long getVote_average() {
        return vote_average;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public String getOriginal_language() {
        return original_language;
    }

    public String getOriginal_title() {
        return original_title;
    }

    public String getBackdrop_path() {
        return backdrop_path;
    }

    public String getOverview() {
        return overview;
    }

    public String getRelease_date() {
        return release_date;
    }
}
