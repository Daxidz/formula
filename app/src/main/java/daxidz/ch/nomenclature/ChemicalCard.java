package daxidz.ch.nomenclature;

import java.util.Comparator;

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

    private boolean shouldLearn;


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

    public boolean isShouldLearn() {
        return shouldLearn;
    }

    public void setShouldLearn(boolean shouldLearn) {
        this.shouldLearn = shouldLearn;
    }

    public enum Tag implements Comparable<Tag> {
        KNOWN("Apprise"), NONE("Pas apprise"), HARD("Difficile");

        Tag(String name) {
            this.name = name;
        }

        private String name;

        public String getName() {
            return name;
        }

        public static Comparator<Tag> DifficultyComparator = new Comparator<Tag>() {
            @Override
            public int compare(Tag o1, Tag o2) {
                return o2.compareTo(o1);
            }
        };

    }


    public static Comparator NameComparator = new Comparator() {

        @Override
        public int compare(Object card1, Object card2) {
            return ((ChemicalCard) card1).getName().compareTo(((ChemicalCard) card2).getName());
        }
    };


    public static Comparator TagComparator = new Comparator() {

        @Override
        public int compare(Object card1, Object card2) {
            return Tag.DifficultyComparator.compare(((ChemicalCard) card1).getTag(), (((ChemicalCard) card2).getTag()));
        }
    };
}
