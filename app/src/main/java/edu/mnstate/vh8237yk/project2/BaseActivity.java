package edu.mnstate.vh8237yk.project2;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

/**
 * Created by aemuxu on 11/27/2016.
 */

public class BaseActivity extends AppCompatActivity {

    public boolean onCreateOptionsMenu(Menu m)
    {
        getMenuInflater().inflate(R.menu.app_menu, m);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem menuItem)
    {
        switch(menuItem.getItemId())
        {
            case R.id.about:
                displayAbout();
                return true;
            default:
                return super.onOptionsItemSelected(menuItem);
        }
    }

    private void displayAbout() {
        LayoutInflater li = LayoutInflater.from(this);
        View aboutView = li.inflate(R.layout.about_layout, null);
        AlertDialog.Builder buil = new AlertDialog.Builder(this);
        buil.setView(aboutView);
        buil.setCancelable(false);
        buil.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        buil.create().show();
    }
}
