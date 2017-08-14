package daxidz.ch.nomenclature;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import daxidz.ch.nomenclature.database.ChemicalCardDAO;

public class ComponentsList extends Activity {

    private ChemicalCardDAO chemicalCardDAO = new ChemicalCardDAO(this);

    private ListView componentsList;

    private ComponentListAdapter componentListAdapter;

    private ArrayList<ChemicalCard> cardsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_components_list);

        chemicalCardDAO.open();
        cardsList = chemicalCardDAO.selectAll();
        chemicalCardDAO.close();

        componentsList = (ListView) findViewById(R.id.components_list);

        componentListAdapter = new ComponentListAdapter(cardsList, getApplicationContext());

        componentsList.setAdapter(componentListAdapter);

        componentsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                ChemicalCard cardToChange = (ChemicalCard) adapterView.getAdapter().getItem(position);

                cardToChange.setTag(ChemicalCard.Tag.values()[(cardToChange.getTag().ordinal() + 1) % ChemicalCard.Tag.values().length]);
                //componentListAdapter.notifyDataSetChanged();
                chemicalCardDAO.open();
                chemicalCardDAO.updateTag(cardToChange);
                chemicalCardDAO.close();
                changeTagsShown(null);
            }
        });
    }

    synchronized public void changeTagsShown(View view) {

        ArrayList<ChemicalCard.Tag> tagsToShow = new ArrayList<>();

        if (((CheckBox) findViewById(R.id.showHardCheckbox)).isChecked()) {
            tagsToShow.add(ChemicalCard.Tag.HARD);
        }
        if (((CheckBox) findViewById(R.id.showKnownCheckbox)).isChecked()) {
            tagsToShow.add(ChemicalCard.Tag.KNOWN);
        }
        if (((CheckBox) findViewById(R.id.showNoneCheckbox)).isChecked()) {
            tagsToShow.add(ChemicalCard.Tag.NONE);
        }

        chemicalCardDAO.open();
        ArrayList<ChemicalCard> cardsListTagsChosen = chemicalCardDAO.selectTagged(tagsToShow);
        chemicalCardDAO.close();
        componentListAdapter.clear();
        componentListAdapter.addAll(cardsListTagsChosen);
        componentListAdapter.notifyDataSetChanged();
    }

    public void resetTags(View view) {
        /*new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voidable) {
                chemicalCardDAO.open();
                cardsList = chemicalCardDAO.selectAll();
                for (int i = 0; i < cardsList.size(); ++i) {
                    cardsList.get(i).setTag(ChemicalCard.Tag.NONE);
                    chemicalCardDAO.updateTag(cardsList.get(i));
                }
                chemicalCardDAO.close();
                return null;
            }

            @Override
            protected void onPostExecute(Void voidable) {
                changeTagsShown(null);
            }
        }.execute();*/
        chemicalCardDAO.open();
        cardsList = chemicalCardDAO.selectAll();
        for (int i = 0; i < cardsList.size(); ++i) {
            cardsList.get(i).setTag(ChemicalCard.Tag.NONE);
            chemicalCardDAO.updateTag(cardsList.get(i));
        }
        chemicalCardDAO.close();
        changeTagsShown(null);
    }
}
