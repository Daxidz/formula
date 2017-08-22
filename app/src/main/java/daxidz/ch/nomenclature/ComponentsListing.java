package daxidz.ch.nomenclature;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;

import daxidz.ch.nomenclature.database.ChemicalCardDAO;

public class ComponentsListing extends AppCompatActivity {

    private ChemicalCardDAO chemicalCardDAO = new ChemicalCardDAO(this);

    private ListView componentsList;

    private ComponentListAdapter componentListAdapter;

    private ArrayList<ChemicalCard> cardsListComplete;

    private ArrayList<ChemicalCard> cardsListToShow;

    private ArrayList<ChemicalCard.Tag> tagsToShow = new ArrayList<>();

    private boolean showShouldLearn = true;

    private Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_components_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Liste");
        setSupportActionBar(toolbar);

        tagsToShow.add(ChemicalCard.Tag.NONE);
        tagsToShow.add(ChemicalCard.Tag.HARD);
        tagsToShow.add(ChemicalCard.Tag.KNOWN);

        chemicalCardDAO.open();
        cardsListComplete = chemicalCardDAO.selectAll();
        chemicalCardDAO.close();

        cardsListToShow = new ArrayList<>(cardsListComplete);

        componentsList = (ListView) findViewById(R.id.components_list);

        componentsList.setNestedScrollingEnabled(true);

        componentListAdapter = new ComponentListAdapter(cardsListToShow, getApplicationContext(), chemicalCardDAO);

        componentsList.setAdapter(componentListAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_list_component, menu);
        this.menu = menu;
        return true;
    }

    synchronized public void changeTagsShown(MenuItem item) {
        ChemicalCard.Tag tagToModify = null;

        switch (item.getItemId()) {
            case R.id.showHardCheckbox:
                tagToModify = ChemicalCard.Tag.HARD;
                break;
            case R.id.showKnownCheckbox:
                tagToModify = ChemicalCard.Tag.KNOWN;
                break;
            case R.id.showNoneCheckbox:
                tagToModify = ChemicalCard.Tag.NONE;
                break;
            default:

        }
        if (!item.isChecked()) {
            item.setChecked(true);
            tagsToShow.add(tagToModify);
        } else {
            item.setChecked(false);
            tagsToShow.remove(tagToModify);
        }

        updateListToShow();
    }

    public void resetTags(MenuItem item) {
        ChemicalCard.Tag tagToChange = null;
        switch (item.getItemId()) {
            case R.id.resetHardTags:
                tagToChange = ChemicalCard.Tag.HARD;
                break;
            case R.id.resetKnownTags:
                tagToChange = ChemicalCard.Tag.KNOWN;
                break;
        }

        chemicalCardDAO.open();
        for (int i = 0; i < cardsListComplete.size(); ++i) {
            if ((cardsListComplete.get(i).getTag()).equals(tagToChange))
                cardsListComplete.get(i).setTag(ChemicalCard.Tag.NONE);
            chemicalCardDAO.updateTag(cardsListComplete.get(i));
        }
        chemicalCardDAO.close();
        updateListToShow();
    }

    public void changeShouldLearnShown(MenuItem item) {
        if (!item.isChecked()) {
            item.setChecked(true);
            showShouldLearn = true;
        } else {
            item.setChecked(false);
            showShouldLearn = false;
        }
        updateListToShow();
    }

    public void updateListToShow() {
        cardsListToShow.clear();
        for (ChemicalCard card : cardsListComplete) {
            if (!showShouldLearn) {
                if (!card.isShouldLearn()) {
                    continue;
                }
            }
            for (int i = 0; i < tagsToShow.size(); ++i) {
                if (card.getTag() == tagsToShow.get(i)) {
                    cardsListToShow.add(card);
                    break;
                }
            }
        }
        componentListAdapter.notifyDataSetChanged();
    }

    public void sortAlphabetical(MenuItem item) {
        Collections.sort(cardsListComplete, ChemicalCard.NameComparator);
        updateListToShow();
    }

    public void sortByTag(MenuItem item) {
        Collections.sort(cardsListComplete, ChemicalCard.TagComparator);
        updateListToShow();
    }

}
