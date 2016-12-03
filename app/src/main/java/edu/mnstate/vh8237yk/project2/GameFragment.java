package edu.mnstate.vh8237yk.project2;

import android.app.Activity;
import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by aemuxu on 10/31/2016.
 */

public class GameFragment extends Fragment{

    volatile int i = 0;
    public Runnable r;

    ArrayList<Integer>ivsToFill = new ArrayList<>();
    ArrayList<Integer>facesToShow = new ArrayList<>();

    public Integer[] images = {R.drawable.face1,
            R.drawable.face2,
            R.drawable.face3,
            R.drawable.face4,
            R.drawable.face5,
            R.drawable.face6};

    public interface OnDicesRolledListener
    {
        public void OnDicesRolled(Roll r);
    }

    OnDicesRolledListener rollListener;

    int curPlayer = 1, rollNum = 1, numPlayers, numDices, numFaces, turn =1 ;
    Random rand = new Random();
    Activity a;

    ToggleButton tglReverse;
    Button btnRoll;
    Button btnSkip;
    TextView txtRollResult;
    TextView txtRollCount;
    TextView txtCurPlayer;
    GridView gridRollDisp;

    RollGridAdapter rollGridAdapter;

    @Override
    public void onCreate(Bundle save)
    {
        super.onCreate(save);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inf,
                             ViewGroup container,
                             Bundle savedInstanceState){
        a = getActivity();
        numPlayers = a.getIntent().getIntExtra("numPlayers",2);
        numDices = a.getIntent().getIntExtra("numDices",1);
        numFaces = a.getIntent().getIntExtra("numFaces",6);

        if (savedInstanceState != null)
        {
            curPlayer = savedInstanceState.getInt("curPlayer", 1);
            rollNum = savedInstanceState.getInt("curRoll", 1);
            ivsToFill = savedInstanceState.getIntegerArrayList("imgs");
            facesToShow = savedInstanceState.getIntegerArrayList("faces");
        }
        View v = inf.inflate(R.layout.game_fragment, container, false);
        return v;
    }

    @Override
    public void onAttach(Activity a)
    {
        super.onAttach(a);
        rollListener = (OnDicesRolledListener) a;

    }

    @Override
    public void onActivityCreated(Bundle b)
    {
        super.onActivityCreated(b);
        txtRollResult = (TextView)(getView().findViewById(R.id.lblPlayerRoll));
        txtCurPlayer = (TextView)(getView().findViewById(R.id.lblPlayer));
        txtRollCount = (TextView)(getView().findViewById(R.id.lblRollCount));
        btnRoll = (Button) (getView().findViewById(R.id.btnRoll));
        btnSkip = (Button) (getView().findViewById(R.id.btnSkip));
        gridRollDisp = (GridView) (getView().findViewById(R.id.gridRoll));

        rollGridAdapter = new RollGridAdapter(a);
        gridRollDisp.setAdapter(rollGridAdapter);


        ViewTreeObserver vto = gridRollDisp.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                gridRollDisp.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                if (ivsToFill != null)
                    recreateRollDisplay();
                    setupRunnable();
            }
        });

        txtCurPlayer.setText("Player "+curPlayer+": ");
        txtRollCount.setText("Roll# "+rollNum);


        btnRoll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                facesToShow.clear();
                for (int i = 0; i < numDices; i++) {
                    facesToShow.add(1 + rand.nextInt(numFaces));
                }
                updateRollDisplay();
                Roll r = new Roll(rollNum, curPlayer,
                        new ArrayList<Integer>(facesToShow));
                rollListener.OnDicesRolled(r);
                nextPlayer();
            }
        });

        btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<Integer> res = new ArrayList<Integer>();
                for (int i = 0; i < numDices; i++) {
                    res.add(0);
                }
                clearRollDisp();
                Roll r = new Roll(rollNum, curPlayer, res);
                rollListener.OnDicesRolled(r);
                nextPlayer();
            }
        });
    }

    private void setupRunnable() {
    }

    private void nextPlayer()
    {
        curPlayer += turn;
        rollNum++;
        if (curPlayer > numPlayers)
            curPlayer = 1;
        if (curPlayer < 1)
            curPlayer = numPlayers;
        txtCurPlayer.setText("Player "+curPlayer+ ":");
        txtRollCount.setText("Roll# "+ rollNum);
    }

    private void clearRollDisp()
    {
        for (int i=0; i<9; i++)
        {
            ImageView v = (ImageView) gridRollDisp.getChildAt(i -
                    gridRollDisp.getFirstVisiblePosition());
            v.setImageDrawable(null);
        }
    }

    private void recreateRollDisplay() {
        for (int i=0; i<ivsToFill.size(); i++)
        {
            ImageView v = (ImageView) gridRollDisp.getChildAt(ivsToFill.get(i));
            v.setImageResource(images[facesToShow.get(i)-1]);
        }
    }

    private void updateRollDisplay() {
        i = 0;

        ivsToFill.clear();
        for (int i=0; i<numDices; i++)
        {
            int n = rand.nextInt(9);
            while (ivsToFill.contains(n)) {
                n = rand.nextInt(9);
            }
            ivsToFill.add(n);
        }

        clearRollDisp();

        for (int i=0; i<numDices; i++)
        {
            ImageView v = (ImageView) gridRollDisp.getChildAt(ivsToFill.get(i) -
                    gridRollDisp.getFirstVisiblePosition());
            v.setImageResource(images[facesToShow.get(i)-1]);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);

        outState.putInt("curPlayer", curPlayer);
        outState.putInt("curRoll", rollNum);
        outState.putIntegerArrayList("imgs", ivsToFill);
        outState.putIntegerArrayList("faces", facesToShow);
    }
}