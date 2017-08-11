package daxidz.ch.nomenclature;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import daxidz.ch.nomenclature.database.ChemicalCardDAO;

public class CardMemorizer extends AppCompatActivity {


    ChemicalCard card1 = new ChemicalCard("Nom1", "Nomenclature1");
    ChemicalCard card2 = new ChemicalCard("Nom2", "Nomenclature2");
    ChemicalCard card3 = new ChemicalCard("Nom3", "Nomenclature3");

    ArrayList<ChemicalCard> cardsList = new ArrayList<>();

    //! ArrayList representing the order of the indexes of the cardList to be processed
    ArrayList<Integer> indexes;

    ChemicalCard actualCard;

    private TextView questionText;
    private TextView answerText;

    CheckBox hardCheckbox;
    CheckBox knownCheckbox;

    private View decorView;

    private ChemicalCardDAO chemicalCardDAO;

    int listIndex = 0;

    Mode mode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_card_memorizer);

        Intent intent = getIntent();

        // Convert the Integer ArrayList to Tags ArrayList
        ArrayList<ChemicalCard.Tag> tagsChosen = new ArrayList<>();
        ArrayList<Integer> tagsChosenInt = intent.getIntegerArrayListExtra(CardChooser.TAGS_CHOSEN);
        for (int tagInt : tagsChosenInt) {
            tagsChosen.add(ChemicalCard.Tag.values()[tagInt]);
        }

        // Open the DB and load the cards corresponding to the tags chosen before
        chemicalCardDAO = new ChemicalCardDAO(this);
        chemicalCardDAO.open();
        cardsList = chemicalCardDAO.selectTagged(tagsChosen);
        chemicalCardDAO.close();

        if (cardsList.size() == 0) {
            Toast toast = Toast.makeText(getApplicationContext(), "Aucune carte ne correspond aux tags choisis", Toast.LENGTH_SHORT);
            toast.show();
            finish();
            return;
        }

        indexes = new ArrayList<>();

        for (int i = 0; i < cardsList.size(); ++i) {
            indexes.add(i);
        }

        mode = (Mode) intent.getSerializableExtra(MainActivity.MODE);

        if (tagsChosen.contains(ChemicalCard.Tag.HARD)) {
            int hardFrequency = intent.getIntExtra(CardChooser.HARD_FREQUENCY, 1);
            if (hardFrequency != 1) {
                for (int i = 0; i < cardsList.size(); ++i) {
                    if (cardsList.get(i).getTag().equals(ChemicalCard.Tag.HARD)) {
                        for (int j = 0; j < hardFrequency; ++j) {
                            indexes.add(i);
                        }
                    }
                }
            }
        }

        Collections.shuffle(indexes);

        questionText = (TextView) findViewById(R.id.question);
        answerText = (TextView) findViewById(R.id.answer);
        hardCheckbox = (CheckBox) findViewById(R.id.hardCheckbox);
        knownCheckbox = (CheckBox) findViewById(R.id.knownCheckbox);

        setActualCard(cardsList.get(indexes.get(0)));
    }

   /* @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            //| View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            //| View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            //| View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
            );}
    }*/

    /**
     * Reveal the answer of the card.
     *
     * @param view
     */
    public void flipCard(View view) {
        if (!(answerText.getText()).equals("")) {
            answerText.setText("");
        } else {
            if (actualCard.getName().equals(questionText.getText().toString())) {
                answerText.setText(actualCard.getNomenclature());
            } else {
                answerText.setText(actualCard.getName());
            }
        }
    }

    /**
     * Set the card passed in parameter as the actual card.
     *
     * @param card
     */
    public void setActualCard(ChemicalCard card) {
        actualCard = card;

        if (card.getTag().equals(ChemicalCard.Tag.HARD)) {
            hardCheckbox.setChecked(true);
        } else {
            hardCheckbox.setChecked(false);
        }

        if (card.getTag().equals(ChemicalCard.Tag.KNOWN)) {
            knownCheckbox.setChecked(true);
        } else {
            knownCheckbox.setChecked(false);
        }

        String textToDisplay = "";
        switch (mode) {
            case NAME_TO_NOMENCLATURE:
                textToDisplay = card.getName();
                break;
            case NOMECLATURE_TO_NAME:
                textToDisplay = card.getNomenclature();
                break;
            case RANDOM:
                Random random = new Random();
                if (random.nextBoolean()) {
                    textToDisplay = card.getName();
                } else {
                    textToDisplay = card.getNomenclature();
                }
                break;
            default:
                break;
        }
        answerText.setText("");
        questionText.setText(textToDisplay);
    }

    public void changeToPreviousCard(View view) {
        if (listIndex > 0) {
            listIndex--;

        } else {
            listIndex = indexes.size() - 1;
        }

        setActualCard(cardsList.get(indexes.get(listIndex)));
    }

    public void changeToNextCard(View view) {
        if (listIndex < indexes.size() - 1) {
            listIndex++;
        } else {
            listIndex = 0;
        }
        setActualCard(cardsList.get(indexes.get(listIndex)));
    }

    public void checkboxChecked(View view) {
        if (!((CheckBox) view).isChecked()) {
            actualCard.setTag(ChemicalCard.Tag.NONE);
        } else {
            switch (view.getId()) {
                case R.id.hardCheckbox:
                    knownCheckbox.setChecked(false);
                    actualCard.setTag(ChemicalCard.Tag.HARD);
                    break;
                case R.id.knownCheckbox:
                    hardCheckbox.setChecked(false);
                    actualCard.setTag(ChemicalCard.Tag.KNOWN);
                    break;
            }
        }
        chemicalCardDAO.open();
        chemicalCardDAO.updateTag(actualCard);
        chemicalCardDAO.close();
    }


    @Override
    public void onStop() {
        super.onStop();
        chemicalCardDAO.open();
        for (ChemicalCard card : cardsList) {
            chemicalCardDAO.updateTag(card);
        }
        chemicalCardDAO.close();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        chemicalCardDAO.open();
        for (ChemicalCard card : cardsList) {
            chemicalCardDAO.updateTag(card);
        }
        chemicalCardDAO.close();
    }
}
