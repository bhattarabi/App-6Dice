package edu.mnstate.vh8237yk.project2;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by aemuxu on 10/31/2016.
 */

public class Roll {

    public String date = null;
    public String gameId = null;
    public int rollId;
    public int playerNum;
    public ArrayList<Integer> faceValues;

    public Roll(int rollNum, int playerNum, ArrayList<Integer> vals)
    {
        this.rollId = rollNum;
        this.playerNum = playerNum;
        this.faceValues = vals;
    }

    public Roll(int rollNum, String date, String gameId,
                int playerNum, ArrayList<Integer> vals)
    {
        this(rollNum, playerNum, vals);
        this.date = date;
        this.gameId = gameId;
    }

    public String getRolls()
    {
        String s = "";
        for (int i = 0; i<faceValues.size(); i++)
        {
            if (faceValues.get(i)==0)
            {
                s += "- ";
            }
            else
            {
                s += faceValues.get(i);
                if (faceValues.size() - i > 1)
                    s += ", ";
            }
        }
        return s;
    }

    public int rollValue()
    {
        int ret = 0;
        int mult = 10;

        for (int i=0; i<faceValues.size(); i++)
        {
            ret = (ret*mult) + faceValues.get(i);
        }
        return ret;
    }

    public int getSum()
    {
        int s =0;
        for (Integer i: faceValues)
            s += i;

        return s;
    }
}
