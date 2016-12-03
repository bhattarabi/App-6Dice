package edu.mnstate.vh8237yk.project2;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by aemuxu on 10/31/2016.
 */

public class RollsAdapter extends RecyclerView.Adapter<RollsAdapter.MyViewHolder> {

    private class GetAllFromDB extends AsyncTask<Void, Void, ArrayList<Roll> >
    {
        protected ArrayList<Roll> doInBackground(Void... params) {
            return rollDbHelper.getAllRolls();
        }

        protected void onPostExecute(ArrayList<Roll> r) {
            rolls = r;
            notifyDataSetChanged();
        }
    }

    private class InsertNewRoll extends AsyncTask<Roll, Void, Roll>
    {
        protected Roll doInBackground(Roll... params) {
            rollDbHelper.insertRoll(params[0]);
            return params[0];
        }

        protected void onPostExecute(Roll r) {
            if (showingAll)
                rolls = rollDbHelper.getAllRolls();
            else
                rolls = rollDbHelper.getGameRolls(r.date, r.gameId);
            notifyDataSetChanged();
        }
    }

    private class GetGameRolls extends AsyncTask<String, Void, ArrayList<Roll> >
    {
        @Override
        protected ArrayList<Roll> doInBackground(String... params) {
            return rollDbHelper.getGameRolls(params[0], params[1]);
        }

        protected void onPostExecute(ArrayList<Roll> r) {
            rolls = r;
            notifyDataSetChanged();
        }
    }

    Context context;
    ArrayList<Roll> rolls;
    RollDB rollDbHelper;

    private boolean showingAll = false;

    public void setShowing(boolean showing)
    {
        showingAll = showing;
    }

    public void showGame(String s, String timeId) {
        //new GetGameRolls().execute(s,timeId);
        rolls = rollDbHelper.getGameRolls(s, timeId);
        notifyDataSetChanged();
    }

    public void showAll() {
        new GetAllFromDB().execute();
    }

    public void deleteGame(String s, String selectedItem) {
        rollDbHelper.delete(s, selectedItem);
    }


    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView lblRollNum, lblPlayerNum, lblRollVals, lblSum, lblDate, lblGameId;

        public MyViewHolder(View view)
        {
            super(view);
            lblRollNum = (TextView)view.findViewById(R.id.txtRollNo);
            lblPlayerNum = (TextView)view.findViewById(R.id.txtPlayerNo);
            lblRollVals = (TextView)view.findViewById(R.id.txtDices);
            lblSum = (TextView)view.findViewById(R.id.txtTotal);
            lblDate = (TextView)view.findViewById(R.id.txtDate);
            lblGameId = (TextView)view.findViewById(R.id.txtGameId);
        }
    }

    public RollsAdapter(Context c) {
        context = c;
        rollDbHelper = new RollDB(c);
        //new GetAllFromDB().execute();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.roll_row_layout, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int pos)
    {
        Roll roll = rolls.get(pos);
        holder.lblPlayerNum.setText(String.valueOf(roll.playerNum));
        holder.lblRollVals.setText(String.valueOf(roll.getRolls()));
        holder.lblSum.setText(String.valueOf(roll.getSum()));
        
        if (showingAll) {
            holder.lblRollNum.setText(String.valueOf(roll.rollId));
            holder.lblDate.setVisibility(View.VISIBLE);
            holder.lblGameId.setVisibility(View.VISIBLE);
            holder.lblDate.setText(roll.date);
            holder.lblGameId.setText(roll.gameId);
        }
        else
        {
            holder.lblRollNum.setText(
                    String.valueOf(roll.rollId - rolls.get(0).rollId + 1));
            holder.lblDate.setVisibility(View.GONE);
            holder.lblGameId.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount()
    {
        return rolls.size();
    }

    public void newRoll(Roll r)
    {
        new InsertNewRoll().execute(r);
    }
}
