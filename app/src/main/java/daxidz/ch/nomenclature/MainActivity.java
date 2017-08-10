package daxidz.ch.nomenclature;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    public static final String MODE = "MODE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void startMemorizer(Mode mode) {
        Intent intent = new Intent(this, CardChooser.class);

        intent.putExtra(MODE, mode);

        startActivity(intent);
    }

    public void startNameToNomeclature(View view) {
        startMemorizer(Mode.NAME_TO_NOMENCLATURE);
    }

    public void startNomenclatureToName(View view) {
        startMemorizer(Mode.NOMECLATURE_TO_NAME);
    }

    public void startRandom(View view) {
        startMemorizer(Mode.RANDOM);
    }

}
