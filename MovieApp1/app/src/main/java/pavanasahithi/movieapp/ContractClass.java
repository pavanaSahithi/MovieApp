package pavanasahithi.movieapp;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Lenovo on 16-05-2018.
 */

public class ContractClass {
    public static final String SCHEME = "content://";
    public static final String AUTHORITY = "pavanasahithi.movieapp.udacity";
    public static final Uri BASE_CONTENT = Uri.parse(SCHEME + AUTHORITY);

    public static final class TableEntry implements BaseColumns {
        public static final String TableName = "Data";
        public static final Uri CONTENT_URI =
                BASE_CONTENT.buildUpon().appendPath(TableName).build();
        public static final String DatabaseName = "MyDataBase";
        public static final String ColumnId = "ID";
        public static final String ColumnTitle = "TITLE";
        public static final String ColumnPosterPath = "POSTER_PATH";
        public static final String ColumnBackdropPath = "BACKDROP_PATH";
        public static final String ColumnSynopsis = "SYNOPSIS";
        public static final String ColumnRating = "RATING";
        public static final String ColumnDate = "RELEASE_DATE";
        public static final String ColumnOriginalTitle = "ORIGINAL_TITLE";
        public static final String ColumnLanguage = "LANGUAGE";
    }
}
