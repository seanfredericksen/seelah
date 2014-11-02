package com.frederis.seelahtracker;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.frederis.seelahtracker.card.CardType;
import com.frederis.seelahtracker.widget.CardView;

import javax.inject.Inject;


public class MainActivity extends Activity {

    @Inject CardManager mCardManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        ((SeelahTrackerApplication) getApplication()).inject(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.acquire:
                return true;

            case R.id.draw:
                mCardManager.drawCard();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();

        mCardManager.initialize(6, 2, 1, 3, 3, 2);
    }

}
