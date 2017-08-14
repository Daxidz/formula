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
                + ChemicalCardDAO.FORMULA + ") " + "VALUES" +
                "('Acide fluorhydrique', 'HF') ," +
                "('Acide chlorhydrique', 'HC\u2113') ," +
                "('Acide bromhydrique', 'HBr') ," +
                "('Acide iodhydrique', 'HI') ," +
                "('Acide sulfhydrique', 'H\u2082S') ," +
                "('Acide cyanhydrique', 'HCN') ," +
                "('Acide nitrique', 'HNO\u2083') ," +
                "('Acide nitreux', 'HNO\u2082') ," +
                "('Acide sulfurique', 'H\u2082SO\u2084') ," +
                "('Acide sulfureux', 'H\u2082SO\u2083') ," +
                "('Acide carbonique', 'H\u2082CO\u2083') ," +
                "('Acide phosphorique', 'H\u2083PO\u2084') ," +
                "('Acide phosphoreux', 'H\u2083PO\u2083') ," +
                "('Acide perchlorique', 'HC\u2113O\u2084') ," +
                "('Acide chlorique', 'HC\u2113O\u2083') ," +
                "('Acide chloreux', 'HC\u2113O\u2082') ," +
                "('Acide hypochloreux', 'HC\u2113O') ," +
                "('Acide permanganique', 'HMnO\u2084') ," +
                "('Acide chromique', 'H\u2082CrO\u2084') ," +
                "('Acide acétique', 'CH\u2083COOH') ," +
                "('Acide oxalique', 'H\u2082C\u2082O\u2084') ," +
                "('Ammonium', 'NH\u2084\u207A') ," +
                "('Hydronium', 'H\u2083O\u207A') ," +
                "('Iodate', 'IO\u2083\u207B') ," +
                "('Thiosulfate', 'S\u2082O\u2083\u00B2\u207B') ," +
                "('Thiosulfite', 'S\u2082O\u2082\u00B2\u207B') ," +
                "('Oxalate', 'C\u2082O\u2084\u00B2\u207B') ," +
                "('Fluorure', 'F\u207B') ," +
                "('Chlorure', 'C\u2113\u207B') ," +
                "('Bromure', 'Br\u207B') ," +
                "('Iodure', 'I\u207B') ," +
                "('Sulfure', 'S\u00B2\u207B') ," +
                "('Cyanure', 'CN\u207B') ," +
                "('Oxyde', 'O\u00B2\u207B') ," +
                "('Hydroxyde', 'OH\u207B') ," +
                "('Nitrate', 'NO\u2083\u207B') ," +
                "('Nitrite', 'NO\u2082\u207B') ," +
                "('Sulfate', 'SO\u2084\u00B2\u207B') ," +
                "('Hydrogénosulfate', 'HSO\u2084\u207B') ," +
                "('Sulfite', 'SO\u2083\u00B2\u207B') ," +
                "('Hydrogénosulfite', 'HSO\u2083\u207B') ," +
                "('Carbonate', 'CO\u2083\u00B2\u207B') ," +
                "('Hydrogénocarbonate', 'HCO\u2083\u207B') ," +
                "('Phosphate', 'PO\u2084\u00B3\u207B') ," +
                "('Hydrogénophosphate', 'HPO\u2084\u00B2\u207B') ," +
                "('Phosphite', 'PO\u2083\u00B3\u207B') ," +
                "('Hydrogénophosphite', 'HPO\u2083\u00B2\u207B') ," +
                "('Perchlorate', 'C\u2113O\u2084\u207B') ," +
                "('Chlorate', 'C\u2113O\u2083\u207B') ," +
                "('Chlorite', 'C\u2113O\u2082\u207B') ," +
                "('Hypochlorite', 'C\u2113O\u207B') ," +
                "('Permanganate', 'MnO\u2084\u207B') ," +
                "('Chromate', 'CrO\u2084\u00B2\u207B') ," +
                "('Dichromate', 'Cr\u2082O\u2087\u00B2\u207B') ," +
                "('Acétate', 'CH\u2083COO\u207B');"
        );


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(ChemicalCardDAO.DROP_TABLE);
        onCreate(db);
    }
}
