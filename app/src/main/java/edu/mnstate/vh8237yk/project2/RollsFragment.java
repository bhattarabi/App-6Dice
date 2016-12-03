package edu.mnstate.vh8237yk.project2;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by aemuxu on 10/31/2016.
 */

public class RollsFragment extends Fragment{

    ArrayList<Roll> rolls = new ArrayList<Roll>();
    RecyclerView rollsReclView;
    RollsAdapter adapter;
    Activity a;
    LinearLayout dbToolbar;

    Button btnChangeDate, btnApplyFilter, btnDeleteGame, btnClearFilter;
    Spinner spnGameId;
    TextView txtDateDisp;
    TextView txtDateHeader, txtTimeHeader;

    String latestDate;
    String gameID;

    DateFormat dateFormatter = new SimpleDateFormat("MM-dd-yyyy");
    DateFormat timeFormatter = new SimpleDateFormat("HH:mm:ss");
    int day, month, year;

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId())
            {
                case R.id.btnChangeDate:
                case R.id.filterDate:
                    showDatePicker();
                    break;
                case R.id.btnClearFilters:
                    clearFilters();
                    break;
                case R.id.btnApplyFilters:
                    applyFilter();
                    break;
                case R.id.btnDeleteGame:
                    deleteGame();
                    break;
            }
        }
    };

    private void deleteGame() {
        String tmp = spnGameId.getSelectedItem()==null ? " " :
                (String)spnGameId.getSelectedItem();
        adapter.deleteGame(txtDateDisp.getText().toString(),
                tmp);
        setupDate(true);
        applyFilter();
    }

    private void applyFilter() {
        String tmp = spnGameId.getSelectedItem()==null ? " " :
                (String)spnGameId.getSelectedItem();

        adapter.showGame(txtDateDisp.getText().toString(),
                tmp);

        adapter.setShowing(false);
        txtTimeHeader.setVisibility(View.GONE);
        txtDateHeader.setVisibility(View.GONE);
    }

    private void clearFilters() {
        if (btnClearFilter.getText().equals(getString(R.string.showAllRolls)))
        {
            btnClearFilter.setText(R.string.showCurrent);
            adapter.setShowing(true);
            adapter.showAll();
            txtTimeHeader.setVisibility(View.VISIBLE);
            txtDateHeader.setVisibility(View.VISIBLE);
        }
        else
        {
            btnClearFilter.setText(R.string.showAllRolls);
            showCurrent();
        }
    }

    public void showCurrent()
    {
        adapter.setShowing(false);
        txtTimeHeader.setVisibility(View.GONE);
        txtDateHeader.setVisibility(View.GONE);
        adapter.showGame(latestDate, gameID);
    }

    @Override
    public void onCreate(Bundle save)
    {
        super.onCreate(save);
        setRetainInstance(true);

        long s = System.currentTimeMillis();
        latestDate = dateFormatter.format(s);
        gameID = timeFormatter.format(s);
    }

    @Override
    public View onCreateView(LayoutInflater inf,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        return inf.inflate(R.layout.rolls_fragment, container, false);
    }

    @Override
    public void onActivityCreated(Bundle b) {
        super.onActivityCreated(b);
        a = getActivity();

        btnChangeDate = (Button) getView().findViewById(R.id.btnChangeDate);
        btnApplyFilter = (Button) getView().findViewById(R.id.btnApplyFilters);
        btnClearFilter = (Button) getView().findViewById(R.id.btnClearFilters);
        btnDeleteGame = (Button) getView().findViewById(R.id.btnDeleteGame);
        spnGameId = (Spinner) getView().findViewById(R.id.spnGameID);
        txtDateDisp = (TextView) getView().findViewById(R.id.filterDate);
        dbToolbar = (LinearLayout) getView().findViewById(R.id.dbToolbar);
        txtDateHeader = (TextView) getView().findViewById(R.id.dateHeader);
        txtTimeHeader = (TextView) getView().findViewById(R.id.gameIdHeader);

        if (a.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            btnClearFilter.setVisibility(View.GONE);
           btnDeleteGame.setVisibility(View.GONE);
        }
        else{
            btnClearFilter.setVisibility(View.VISIBLE);
            btnDeleteGame.setVisibility(View.VISIBLE);
        }
            setupDate(false);

        btnChangeDate.setOnClickListener(clickListener);
        btnClearFilter.setOnClickListener(clickListener);
        btnApplyFilter.setOnClickListener(clickListener);
        btnDeleteGame.setOnClickListener(clickListener);
        txtDateDisp.setOnClickListener(clickListener);


//        adapter = new RollsAdapter(rolls);
        adapter = new RollsAdapter(getActivity());
        rollsReclView = (RecyclerView)getView().findViewById(R.id.reclViewRolls);
        rollsReclView.setHasFixedSize(true);
        rollsReclView.setAdapter(adapter);
        rollsReclView.setLayoutManager(new LinearLayoutManager(getActivity()));
        showCurrent();
    }

    public void newRoll(Roll r)
    {
        r.date = latestDate;
        r.gameId = gameID;
        adapter.newRoll(r);
        rollsReclView.post(new Runnable() {
            @Override
            public void run() {
                // Select the last row so it will scroll into view...
                rollsReclView.scrollToPosition(rolls.size()-1);
            }
        });
    }

    private void setupDate(boolean fromPicker)
    {
        if (!fromPicker)
        {
            Calendar calendar = Calendar.getInstance();
            year = calendar.get(Calendar.YEAR);

            month = calendar.get(Calendar.MONTH);
            day = calendar.get(Calendar.DAY_OF_MONTH);
        }

        String date = new StringBuilder().append(month+1).append("-")
                .append(day).append("-").append(year).toString();
        try {
            Date d = dateFormatter.parse(date);
            date = dateFormatter.format(d);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        txtDateDisp.setText(date);

//        if (fromPicker) {
            spnGameId.setAdapter(null);
            ArrayAdapter<String> adapter = new ArrayAdapter(getActivity(),
                    android.R.layout.simple_spinner_item, new RollDB(getActivity()).getGameIds(date));
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spnGameId.setAdapter(adapter);
//        }
    }

    private void showDatePicker()
    {
        DatePickerDialog.OnDateSetListener dateListener =
                new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker arg0,
                                          int arg1, int arg2, int arg3) {
                        year = arg1;
                        month = arg2;
                        day = arg3;
                        setupDate(true);
                    }
                };

        DatePickerDialog dp = new DatePickerDialog(getActivity(),
                dateListener, year, month, day);
        dp.show();
    }
}