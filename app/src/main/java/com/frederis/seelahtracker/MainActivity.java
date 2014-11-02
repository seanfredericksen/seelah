package com.frederis.seelahtracker;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.frederis.seelahtracker.card.CardType;
import com.frederis.seelahtracker.dialog.IdentifyCardActionDialogFragment;
import com.frederis.seelahtracker.widget.DeckView;
import com.frederis.seelahtracker.widget.HandView;

import javax.inject.Inject;


public class MainActivity extends Activity implements IdentifyCardActionDialogFragment.Callbacks {

    private static final String TAG_IDENTIFY_HAND_CARD = "identifyHandCard";
    private static final String TAG_IDENTIFY_ACQUIRED_CARD = "identifyAcquiredCard";
    private static final String TAG_IDENTIFY_PAULA_CARD = "identifyPaulaCard";

    @Inject CardManager mCardManager;

    private DeckView mDeckView;
    private HandView mHandView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        mDeckView = (DeckView) findViewById(R.id.deck_view);
        mHandView = (HandView) findViewById(R.id.hand_view);

        mHandView.setCallbacks(new HandView.Callbacks() {
            @Override
            public void onCardInHandClicked(CardType cardType) {
                identifyCard(cardType, TAG_IDENTIFY_HAND_CARD);
            }
        });

        ((SeelahTrackerApplication) getApplication()).inject(this);
    }

    private void identifyCard(CardType startingType, String tag) {
        IdentifyCardActionDialogFragment.newInstance(startingType)
                .show(getFragmentManager(), tag);
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
            case R.id.paula:
                identifyCard(CardType.UNKNOWN, TAG_IDENTIFY_PAULA_CARD);
                return true;
            case R.id.acquire:
                identifyCard(CardType.UNKNOWN, TAG_IDENTIFY_ACQUIRED_CARD);
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

    @Override
    public void onIdentifyTypeClicked(String tag, CardType startingType, CardType newType) {
        if (tag.equals(TAG_IDENTIFY_HAND_CARD)) {
            mCardManager.identifyCardInHand(startingType, newType);
        } else if (tag.equals(TAG_IDENTIFY_ACQUIRED_CARD)) {
            mCardManager.acquireCard(newType);
        } else if (tag.equals(TAG_IDENTIFY_PAULA_CARD)) {
            mCardManager.paulaCard(newType);
        } else {
            throw new IllegalStateException("Trying to identify card for unknown action");
        }


    }

}
