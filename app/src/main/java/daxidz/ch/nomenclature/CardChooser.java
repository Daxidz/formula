package daxidz.ch.nomenclature;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import java.util.ArrayList;

public class CardChooser extends AppCompatActivity {

    Mode mode;

    Intent intent;

    public static final String TAGS_CHOSEN = "TAGS_CHOSEN";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_chooser);

        intent = getIntent();

        mode = (Mode) intent.getSerializableExtra(MainActivity.MODE);


    }

    public void startMemorizer(View view) {
        ArrayList<Integer> tagsChosenInt = new ArrayList<>();

        if (((CheckBox) findViewById(R.id.checkboxDifficult)).isChecked()) {
            tagsChosenInt.add(ChemicalCard.Tag.HARD.ordinal());
        }
        if (((CheckBox) findViewById(R.id.checkboxKnown)).isChecked()) {
            tagsChosenInt.add(ChemicalCard.Tag.KNOWN.ordinal());
        }
        if (((CheckBox) findViewById(R.id.checkboxNormal)).isChecked()) {
            tagsChosenInt.add(ChemicalCard.Tag.NONE.ordinal());
        }

        if (tagsChosenInt.size() == 0) {
            Toast toast = Toast.makeText(getApplicationContext(), "Vous devez selectionner des cartes!", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }

        intent = new Intent(this, CardMemorizer.class);

        intent.putIntegerArrayListExtra(TAGS_CHOSEN, tagsChosenInt);

        intent.putExtra(MainActivity.MODE, mode);

        startActivity(intent);

    }

}
