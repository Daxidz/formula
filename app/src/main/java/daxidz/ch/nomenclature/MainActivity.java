package daxidz.ch.nomenclature;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    public static final String MODE = "MODE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void startCardChooser(Mode mode) {
        Intent intent = new Intent(this, CardChooser.class);

        intent.putExtra(MODE, mode);

        startActivity(intent);
    }

    public void startNameToNomeclature(View view) {
        startCardChooser(Mode.NAME_TO_FORMULA);
    }

    public void startNomenclatureToName(View view) {
        startCardChooser(Mode.FORMULA_TO_NAME);
    }

    public void startRandom(View view) {
        startCardChooser(Mode.RANDOM);
    }

    public void startComponentsList(View view) {
        Intent intent = new Intent(this, ComponentsList.class);
        startActivity(intent);
    }

}
