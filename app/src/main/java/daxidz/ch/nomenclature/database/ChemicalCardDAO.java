package daxidz.ch.nomenclature.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;

import daxidz.ch.nomenclature.ChemicalCard;

/**
 * Created by David on 22.06.2017.
 */

public class ChemicalCardDAO extends BaseDAO {

    public static final String NAME = "name";
    public static final String FORMULA = "formula";
    public static final String TAG = "tag";
    public static final String SHOULD_LEARN = "should_learn";

    public static final int TRUE = 1;
    public static final int FALSE = 0;

    public static final String TABLE_NAME = "Chemical_card";

    private String[] allCollumns = {NAME, FORMULA, TAG, SHOULD_LEARN};

    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    NAME + " TEXT NOT NULL PRIMARY KEY, " +
                    FORMULA + " TEXT NOT NULL, " +
                    TAG + " INTEGER DEFAULT " + ChemicalCard.Tag.NONE.ordinal() + ", " +
                    SHOULD_LEARN + " INTEGER DEFAULT " + TRUE +
                    " );";

    public static final String DROP_TABLE =
            "DROP TABLE IF EXISTS " + TABLE_NAME + ";";


    public ChemicalCardDAO(Context context) {
        super(context);
    }

    public void delete(int id) {

    }

    public void insert(ChemicalCard card) {
        ContentValues value = new ContentValues();

        value.put(NAME, card.getName());
        value.put(FORMULA, card.getNomenclature());

        value.put(TAG, card.getTag().ordinal());

        database.insert(TABLE_NAME, null, value);
    }

    public void updateTag(ChemicalCard card) {
        ContentValues value = new ContentValues();

        value.put(TAG, card.getTag().ordinal());
        database.update(TABLE_NAME, value, NAME + " = ?", new String[]{String.valueOf(card.getName())});
    }

    public void updateShouldLearn(ChemicalCard card) {
        ContentValues value = new ContentValues();

        int shouldLearnInt = card.isShouldLearn() ? TRUE : FALSE;
        value.put(SHOULD_LEARN, shouldLearnInt);
        database.update(TABLE_NAME, value, NAME + " = ?", new String[]{String.valueOf(card.getName())});
    }

    public ArrayList<ChemicalCard> selectAll() {
        ArrayList<ChemicalCard> chemicalCards = new ArrayList<>();

        Cursor cursor = database.query(TABLE_NAME, allCollumns, null, null, null, null, null);

        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            ChemicalCard card = cursorToChemicalCard(cursor);
            chemicalCards.add(card);
            cursor.moveToNext();
        }
        cursor.close();
        return chemicalCards;
    }

    public ArrayList<ChemicalCard> selectShouldLearn() {
        ArrayList<ChemicalCard> chemicalCards = new ArrayList<>();

        Cursor cursor = database.query(TABLE_NAME, allCollumns, null, null, null, null, null);

        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            ChemicalCard card = cursorToChemicalCard(cursor);
            if (card.isShouldLearn()) {
                chemicalCards.add(card);
            }
            cursor.moveToNext();
        }
        cursor.close();
        return chemicalCards;

    }

    public ArrayList<ChemicalCard> selectTagged(ArrayList<ChemicalCard.Tag> tags) {
        ArrayList<ChemicalCard> chemicalCards = new ArrayList<>();

        Cursor cursor = database.query(TABLE_NAME, allCollumns, null, null, null, null, null);

        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            ChemicalCard card = cursorToChemicalCard(cursor);
            ChemicalCard.Tag tag = card.getTag();
            // Iterate over the tags wanted and if one matches, add the card to the deck
            for (int i = 0; i < tags.size(); ++i) {
                if (tag == tags.get(i)) {
                    chemicalCards.add(card);
                    break;
                }
            }
            cursor.moveToNext();
        }
        cursor.close();
        return chemicalCards;
    }

    public ArrayList<ChemicalCard> selectShouldLearnTagged(ArrayList<ChemicalCard.Tag> tags) {
        ArrayList<ChemicalCard> chemicalCards = new ArrayList<>();

        Cursor cursor = database.query(TABLE_NAME, allCollumns, null, null, null, null, null);

        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            ChemicalCard card = cursorToChemicalCard(cursor);
            if (card.isShouldLearn()) {
                ChemicalCard.Tag tag = card.getTag();
                // Iterate over the tags wanted and if one matches, add the card to the deck
                for (int i = 0; i < tags.size(); ++i) {
                    if (tag == tags.get(i)) {
                        chemicalCards.add(card);
                        break;
                    }
                }
            }
            cursor.moveToNext();
        }
        cursor.close();
        return chemicalCards;
    }

    private ChemicalCard cursorToChemicalCard(Cursor cursor) {
        ChemicalCard card = new ChemicalCard();
        card.setName(cursor.getString(0));
        card.setNomenclature(cursor.getString(1));

        card.setTag(ChemicalCard.Tag.values()[cursor.getInt(2)]);

        card.setShouldLearn(cursor.getInt(3) == 1);

        return card;
    }
}
