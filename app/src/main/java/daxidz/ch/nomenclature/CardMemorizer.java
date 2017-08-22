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

    private ArrayList<ChemicalCard> cardsList = new ArrayList<>();
    private ArrayList<ChemicalCard.Tag> tagsChosen = new ArrayList<>();
    private ArrayList<Integer> tagsChosenInt;

    //! ArrayList representing the order of the indexes of the cardList to be processed
    private ArrayList<Integer> indexes;

    private ChemicalCard actualCard;

    private TextView questionText;
    private TextView answerText;

    private CheckBox hardCheckbox;
    private CheckBox knownCheckbox;

    private int hardFrequency;

    private ChemicalCardDAO chemicalCardDAO;

    int listIndex = 0;

    Mode mode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_card_memorizer);

        Intent intent = getIntent();

        // Convert the Integer ArrayList to Tags ArrayList0
        tagsChosenInt = intent.getIntegerArrayListExtra(CardChooser.TAGS_CHOSEN);
        for (int tagInt : tagsChosenInt) {
            tagsChosen.add(ChemicalCard.Tag.values()[tagInt]);
        }

        // Open the DB and load the cards corresponding to the tags chosen before
        chemicalCardDAO = new ChemicalCardDAO(this);
        chemicalCardDAO.open();
        cardsList = chemicalCardDAO.selectShouldLearnTagged(tagsChosen);
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
            hardFrequency = intent.getIntExtra(CardChooser.HARD_FREQUENCY, 1);
            if (hardFrequency != 1) {
                for (int i = 0; i < cardsList.size(); ++i) {
                    if (cardsList.get(i).getTag().equals(ChemicalCard.Tag.HARD)) {
                        // hardFrequency - 1 is because the list already contains one time the index
                        for (int j = 0; j < hardFrequency - 1; ++j) {
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
            case NAME_TO_FORMULA:
                textToDisplay = card.getName();
                break;
            case FORMULA_TO_NAME:
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

    /**
     * @param view
     */
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
        updateDeck();
        chemicalCardDAO.open();
        chemicalCardDAO.updateTag(actualCard);
        chemicalCardDAO.close();
    }

    private void updateDeck() {
        int indexOfCurrentCard = cardsList.indexOf(actualCard);
        if (!tagsChosen.contains(actualCard.getTag())) {
            indexes.removeAll(Collections.singleton(indexOfCurrentCard));
        } else {
            if (actualCard.getTag().equals(ChemicalCard.Tag.HARD)) {
                for (int j = 0; j < hardFrequency - 1; ++j) {
                    indexes.add(indexOfCurrentCard);
                }
            } else {
                indexes.removeAll(Collections.singleton(indexOfCurrentCard));
                insertIntRandom(indexes, indexOfCurrentCard);
            }
        }
        if (indexes.isEmpty()) {
            Toast toast = Toast.makeText(getApplicationContext(), "Le deck est vide!", Toast.LENGTH_SHORT);
            toast.show();
            finish();
        }
    }

    private void insertIntRandom(ArrayList<Integer> list, int elementToAdd) {
        int size = list.size();
        Random rand = new Random();
        list.add(rand.nextInt(size + 1), elementToAdd);
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
