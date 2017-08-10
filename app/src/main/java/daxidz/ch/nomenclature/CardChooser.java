package daxidz.ch.nomenclature;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CheckBox;

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

        ArrayList<ChemicalCard.Tag> tagsChosen = new ArrayList<>();

        if (((CheckBox) findViewById(R.id.checkboxDifficult)).isChecked()) {
            tagsChosen.add(ChemicalCard.Tag.HARD);
        }
        if (((CheckBox) findViewById(R.id.checkboxKnown)).isChecked()) {
            tagsChosen.add(ChemicalCard.Tag.KNOWN);
        }
        if (((CheckBox) findViewById(R.id.checkboxNormal)).isChecked()) {
            tagsChosen.add(ChemicalCard.Tag.NONE);
        }

        intent = new Intent(this, CardMemorizer.class);

        intent.putExtra(TAGS_CHOSEN, tagsChosen);

        startActivity(intent);

    }

}
