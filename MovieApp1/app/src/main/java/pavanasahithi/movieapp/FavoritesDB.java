package pavanasahithi.movieapp;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

/**
 * Created by Lenovo on 16-05-2018.
 */

public class FavoritesDB extends SQLiteOpenHelper {

    public static final int version = 1;

    public static final String Create_query = "create table " + ContractClass.TableEntry.TableName + "(" +
            ContractClass.TableEntry.ColumnId + " text primary key," +
            ContractClass.TableEntry.ColumnTitle + " text NOT NULL," +
            ContractClass.TableEntry.ColumnBackdropPath + " text NOT NULL," +
            ContractClass.TableEntry.ColumnPosterPath + " text NOT NULL," +
            ContractClass.TableEntry.ColumnDate + " text NOT NULL," +
            ContractClass.TableEntry.ColumnLanguage + " text NOT NULL," +
            ContractClass.TableEntry.ColumnOriginalTitle + " text NOT NULL," +
            ContractClass.TableEntry.ColumnRating + " REAL NOT NULL," +
            ContractClass.TableEntry.ColumnSynopsis + " text NOT NULL)";

    public Context context;

    public FavoritesDB(Context context) {
        super(context, ContractClass.TableEntry.DatabaseName, null, version);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(Create_query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("drop table if exists " + ContractClass.TableEntry.TableName);
        onCreate(sqLiteDatabase);
    }

}

