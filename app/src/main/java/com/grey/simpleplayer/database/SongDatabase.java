package com.grey.simpleplayer.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class SongDatabase extends SQLiteOpenHelper {
    private static final String DATABASENAME = "song.db";
    private static final int VERSION = 4;
    public static SongDatabase instance = null;
    private Context context;

    public static SongDatabase getInstance(Context context){
        if(instance == null){
            instance = new SongDatabase(context);
        }
        return instance;
    }

    public SongDatabase(@Nullable Context context) {
        super(context, DATABASENAME, null, VERSION);

        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        PlayerStat.getInstance(context).onCreate(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int  oldVersion, int newVersion) {
        PlayerStat.getInstance(context).onUpgrade(db, oldVersion, newVersion);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        PlayerStat.getInstance(context).onDowngrade(db, oldVersion, newVersion);
    }
}
