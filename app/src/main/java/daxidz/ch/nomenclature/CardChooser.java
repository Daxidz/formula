package daxidz.ch.nomenclature;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.ArrayList;

public class CardChooser extends AppCompatActivity {

    Mode mode;

    Intent intent;

    public static final String TAGS_CHOSEN = "TAGS_CHOSEN";
    public static final String HARD_FREQUENCY = "HARD_FREQUENCY";

    private final int[] hardFrequencyValues = {1, 2, 4, 6};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_chooser);

        ((RadioButton) findViewById(R.id.radio0)).setText("x" + hardFrequencyValues[0]);
        ((RadioButton) findViewById(R.id.radio1)).setText("x" + hardFrequencyValues[1]);
        ((RadioButton) findViewById(R.id.radio2)).setText("x" + hardFrequencyValues[2]);
        ((RadioButton) findViewById(R.id.radio3)).setText("x" + hardFrequencyValues[3]);

        intent = getIntent();

        mode = (Mode) intent.getSerializableExtra(MainActivity.MODE);

    }

    public void startMemorizer(View view) {
        ArrayList<Integer> tagsChosenInt = new ArrayList<>();

        intent = new Intent(this, CardMemorizer.class);

        if (((CheckBox) findViewById(R.id.checkboxDifficult)).isChecked()) {
            tagsChosenInt.add(ChemicalCard.Tag.HARD.ordinal());
            RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
            int indexHardFrequency = radioGroup.indexOfChild(findViewById(radioGroup.getCheckedRadioButtonId()));
            int hardFrequency = hardFrequencyValues[indexHardFrequency];
            intent.putExtra(HARD_FREQUENCY, hardFrequency);
        }
        if (((CheckBox) findViewById(R.id.checkboxKnown)).isChecked()) {
            tagsChosenInt.add(ChemicalCard.Tag.KNOWN.ordinal());
        }
        if (((CheckBox) findViewById(R.id.checkboxNormal)).isChecked()) {
            tagsChosenInt.add(ChemicalCard.Tag.NONE.ordinal());
        }

        if (tagsChosenInt.size() == 0) {
            Toast toast = Toast.makeText(getApplicationContext(), "Vous devez sélectionner des cartes!", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }

        intent.putIntegerArrayListExtra(TAGS_CHOSEN, tagsChosenInt);

        intent.putExtra(MainActivity.MODE, mode);

        startActivity(intent);

    }

}
