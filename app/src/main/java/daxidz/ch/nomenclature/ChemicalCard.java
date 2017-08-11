package daxidz.ch.nomenclature;

import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by David on 20.06.2017.
 */

public class ChemicalCard {

    // Name of the chemical formula
    private String name;

    // Nomenclature of the formula
    private String nomenclature;

    // Used to know if it is tagged as difficult
    private Tag tag;

    private int id;

    public ChemicalCard() {

    }

    public ChemicalCard(String name, String nomenclature) {
        this.name = name;
        this.nomenclature = nomenclature;
        this.tag = Tag.NONE;
    }

    public Tag getTag() {
        return tag;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setNomenclature(String nomenclature) {
        this.nomenclature = nomenclature;
    }

    public String getNomenclature() {
        return nomenclature;
    }

    public void setTag(Tag tag) {
        this.tag = tag;
    }

    public enum Tag {
        NONE, HARD, KNOWN;


    };
}
