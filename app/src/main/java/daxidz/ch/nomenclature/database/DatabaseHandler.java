package daxidz.ch.nomenclature.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by David on 22.06.2017.
 */

public class DatabaseHandler extends SQLiteOpenHelper {



    public DatabaseHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(ChemicalCardDAO.CREATE_TABLE);
        db.execSQL("INSERT INTO " + ChemicalCardDAO.TABLE_NAME +
                "( " + ChemicalCardDAO.NAME + ","
                + ChemicalCardDAO.NOMENCLATURE + ") " + "VALUES" +
                "('Nom1', 'Nomenclature1') ," +
                "('Nom2', 'Nomenclature2') ," +
                "('Nom3', 'Nomenclature3') ," +
                "('Nom4', 'Nomenclature4');");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(ChemicalCardDAO.DROP_TABLE);
        onCreate(db);
    }
}
