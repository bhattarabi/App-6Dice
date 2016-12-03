package edu.mnstate.vh8237yk.project2;

import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

public class MainActivity extends BaseActivity {

    Spinner numPlayers;
    Spinner numDices;
    Spinner numFaces;
    Button btnStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        numPlayers = (Spinner)findViewById(R.id.spnNumPlayers);
        numDices = (Spinner)findViewById(R.id.spnNumDice);
        numFaces = (Spinner)findViewById(R.id.spnNumFaces);
        btnStart = (Button)findViewById(R.id.btnStart);

        setupSpinners();
    }

    private void setupSpinners() {
        String[] n = new String[]{"\t1\t\t","\t2\t\t","\t3\t\t",
                "\t4\t\t","\t5\t\t","\t6\t\t"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, n);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        numPlayers.setAdapter(adapter);
        numPlayers.setSelection(1);
        numDices.setAdapter(adapter);
        numDices.setSelection(0);
        numFaces.setAdapter(adapter);
        numFaces.setSelection(5);
    }

    public void startGame(View view) {
        Intent i = new Intent(this, GameActivity.class);
        i.putExtra("numPlayers", numPlayers.getSelectedItemPosition()+1);
        i.putExtra("numDices", numDices.getSelectedItemPosition()+1);
        i.putExtra("numFaces", numFaces.getSelectedItemPosition()+1);
        startActivity(i);
    }
}
