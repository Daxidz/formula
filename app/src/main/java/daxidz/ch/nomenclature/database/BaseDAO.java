package daxidz.ch.nomenclature.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import daxidz.ch.nomenclature.database.DatabaseHandler;

/**
 * Created by David on 22.06.2017.
 */

public abstract class BaseDAO {

    protected final static int VERSION = 7;

    protected final static String NAME = "database.db";

    protected SQLiteDatabase database;

    protected DatabaseHandler handler;


    public BaseDAO(Context pContext) {
        this.handler = new DatabaseHandler(pContext, NAME, null, VERSION);
    }

    public SQLiteDatabase open() {
        database = handler.getWritableDatabase();
        return database;
    }

    public void close() {
        database.close();
    }

    public SQLiteDatabase getDatabase() {
        return database;
    }
}
