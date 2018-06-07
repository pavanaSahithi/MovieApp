package pavanasahithi.movieapp;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class MyContentProvider extends ContentProvider {
    public static final int FAV = 100;
    public static final int FAV_DET = 105;
    private static final UriMatcher uri_matcher = buildUriMatcher();
    private FavoritesDB favoritesDB;

    public MyContentProvider() {
    }

    public static UriMatcher buildUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(ContractClass.AUTHORITY, ContractClass.TableEntry.TableName, FAV);
        uriMatcher.addURI(ContractClass.AUTHORITY, ContractClass.TableEntry.TableName + "/*", FAV_DET);
        return uriMatcher;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = favoritesDB.getWritableDatabase();
        int match = uri_matcher.match(uri);
        int deleted_rows;
        switch (match) {
            case FAV_DET:
                String id = uri.getPathSegments().get(1);
                deleted_rows = db.delete(ContractClass.TableEntry.TableName,
                        ContractClass.TableEntry.ColumnId + "=?", new String[]{id});
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        db.close();
        return deleted_rows;
    }

    @Override
    public String getType(Uri uri) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = favoritesDB.getWritableDatabase();
        int match = uri_matcher.match(uri);
        Uri returnUri;
        switch (match) {
            case FAV:
                long id = db.insert(ContractClass.TableEntry.TableName, null, values);
                if (id > 0) {
                    returnUri = ContentUris.withAppendedId(ContractClass.TableEntry.CONTENT_URI, id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        db.close();
        return returnUri;
    }

    @Override
    public boolean onCreate() {
        Context context = getContext();
        favoritesDB = new FavoritesDB(context);
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        final SQLiteDatabase db = favoritesDB.getReadableDatabase();
        int match = uri_matcher.match(uri);
        Cursor cursor;
        switch (match) {
            case FAV:
                cursor = db.query(ContractClass.TableEntry.TableName, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case FAV_DET:
                String id = uri.getPathSegments().get(1);
                cursor = db.query(ContractClass.TableEntry.TableName,
                        new String[]{ContractClass.TableEntry.ColumnId},
                        ContractClass.TableEntry.ColumnId + "=?", new String[]{id}, null, null, sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return cursor;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
