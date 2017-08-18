package daxidz.ch.nomenclature;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.ArrayList;

import daxidz.ch.nomenclature.database.ChemicalCardDAO;

public class ComponentsList extends AppCompatActivity {

    private ChemicalCardDAO chemicalCardDAO = new ChemicalCardDAO(this);

    private ListView componentsList;

    private ComponentListAdapter componentListAdapter;

    private ArrayList<ChemicalCard> cardsList;

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
        cardsList = chemicalCardDAO.selectAll();
        chemicalCardDAO.close();

        componentsList = (ListView) findViewById(R.id.components_list);

        componentsList.setNestedScrollingEnabled(true);

        componentListAdapter = new ComponentListAdapter(cardsList, getApplicationContext(), chemicalCardDAO);

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
        ArrayList<ChemicalCard.Tag> tagToChange = new ArrayList<>();
        switch (item.getItemId()) {
            case R.id.resetHardTags:
                tagToChange.add(ChemicalCard.Tag.HARD);
                break;
            case R.id.resetKnownTags:
                tagToChange.add(ChemicalCard.Tag.KNOWN);
                break;
        }
        chemicalCardDAO.open();
        cardsList = chemicalCardDAO.selectTagged(tagToChange);
        for (int i = 0; i < cardsList.size(); ++i) {
            cardsList.get(i).setTag(ChemicalCard.Tag.NONE);
            chemicalCardDAO.updateTag(cardsList.get(i));
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
        chemicalCardDAO.open();
        if (showShouldLearn) {
            cardsList = chemicalCardDAO.selectTagged(tagsToShow);
        } else {
            cardsList = chemicalCardDAO.selectShouldLearnTagged(tagsToShow);
        }
        chemicalCardDAO.close();

        componentListAdapter.clear();
        componentListAdapter.addAll(cardsList);
        componentListAdapter.notifyDataSetChanged();
    }

}
