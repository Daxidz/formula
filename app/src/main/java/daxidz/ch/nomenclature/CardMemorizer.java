package daxidz.ch.nomenclature;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

import daxidz.ch.nomenclature.database.ChemicalCardDAO;

public class CardMemorizer extends AppCompatActivity {


    ChemicalCard card1 = new ChemicalCard("Nom1", "Nomenclature1");
    ChemicalCard card2 = new ChemicalCard("Nom2", "Nomenclature2");
    ChemicalCard card3 = new ChemicalCard("Nom3", "Nomenclature3");

    ArrayList<ChemicalCard> cardsList = new ArrayList<>();

    ChemicalCard actualCard;

    private TextView questionText;
    private TextView answerText;

    private View decorView;

    private ChemicalCardDAO chemicalCardDAO;

    int listIndex = 0;

    Mode mode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();

        ArrayList<ChemicalCard.Tag> selectedTags = intent.getSerializableExtra(CardChooser.TAGS_CHOSEN);

        chemicalCardDAO = new ChemicalCardDAO(this);

        chemicalCardDAO.open();

        cardsList = chemicalCardDAO.selectAll();

        chemicalCardDAO.close();

        mode = (Mode) intent.getSerializableExtra(MainActivity.MODE);

        /*decorView = getWindow().getDecorView();
        // Set immersive mode
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        setContentView(R.layout.activity_card_memorizer);*/

        questionText = (TextView) findViewById(R.id.question);
        answerText = (TextView) findViewById(R.id.answer);

        setActualCard(mode, cardsList.get(0));
    }

    @Override
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
    }

    public void flipCard(View view) {
        if (actualCard.getName().equals(questionText.getText().toString())) {
            answerText.setText(actualCard.getNomenclature());
        } else {
            answerText.setText(actualCard.getName());
        }
    }

    public void setActualCard(Mode mode, ChemicalCard card) {
        actualCard = card;

        CheckBox hard = (CheckBox) findViewById(R.id.hardCheckbox);

        CheckBox known = (CheckBox) findViewById(R.id.knownCheckbox);

        if (card.getTag().equals(ChemicalCard.Tag.HARD)) {
            hard.setChecked(true);
        } else {
            hard.setChecked(false);
        }

        if (card.getTag().equals(ChemicalCard.Tag.KNOWN)) {
            known.setChecked(true);
        } else {
            known.setChecked(false);
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
        questionText.setText(textToDisplay);
    }

    public void changeToPreviousCard(View view) {
        if (listIndex > 0) {
            listIndex--;

        } else {
            listIndex = cardsList.size() - 1;
        }

        setActualCard(mode, cardsList.get(listIndex));
    }

    public void changeToNextCard(View view) {
        if (listIndex < cardsList.size() - 1) {
            listIndex++;
        } else {
            listIndex = 0;
        }
        setActualCard(mode, cardsList.get(listIndex));
    }

    public void checkboxChecked(View view) {

    }

    @Override
    public void onStop() {
        chemicalCardDAO.open();
        for (ChemicalCard card : cardsList) {
            chemicalCardDAO.updateTag(card);
        }
        chemicalCardDAO.close();
    }

    @Override
    public void onDestroy() {
        chemicalCardDAO.open();
        for (ChemicalCard card : cardsList) {
            chemicalCardDAO.updateTag(card);
        }
        chemicalCardDAO.close();
    }
}
