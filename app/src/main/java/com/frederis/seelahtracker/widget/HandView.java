package com.frederis.seelahtracker.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.frederis.seelahtracker.CardManager;
import com.frederis.seelahtracker.R;
import com.frederis.seelahtracker.SeelahTrackerApplication;
import com.frederis.seelahtracker.card.CardType;

import java.util.List;

import javax.inject.Inject;

public class HandView extends LinearLayout implements CardManager.Listener {

    @Inject CardManager mCardManager;

    private LayoutCallback mLayoutCallback;
    private Callbacks mCallbacks;

    private int mWidth;

    public HandView(Context context) {
        super(context);
    }

    public HandView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HandView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        ((SeelahTrackerApplication) getContext().getApplicationContext()).inject(this);

        mCardManager.addListener(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();

        mCardManager.removeListener(this);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        mWidth = getWidth();

        if (mLayoutCallback != null) {
            mLayoutCallback.onViewLaidOut();
            mLayoutCallback = null;
        }

    }

    @Override
    public void onCardsUpdated(List<CardType> deck, final List<CardType> hand, List<CardType> discard, int unknownPercentage) {
        if (mWidth != 0) {
            populateCards(hand);
        } else {
            mLayoutCallback = new LayoutCallback() {
                @Override
                public void onViewLaidOut() {
                    populateCards(hand);
                }
            };
        }
    }

    public void setCallbacks(Callbacks callbacks) {
        mCallbacks = callbacks;
    }

    private void populateCards(List<CardType> hand) {
        removeAllViews();

        if (hand.size() > 0) {
            int columns = (int) Math.max(4, Math.ceil(hand.size() / 2.0));
            List<CardType> firstRowCards = hand.subList(0, Math.min(columns, hand.size()));
            List<CardType> secondRowCards = hand.subList(Math.min(columns, hand.size()), hand.size());

            LayoutInflater inflater = LayoutInflater.from(getContext());

            if (firstRowCards.size() > 0) {
                LinearLayout firstRow = (LinearLayout) inflater.inflate(R.layout.view_hand_row, this, false);

                int total =  mWidth - (firstRowCards.size() * 10);
                int firstRowCardWidth = Math.min(total / 4, total / firstRowCards.size());

                for (CardType cardType : firstRowCards) {
                    addCardToRow(firstRow, cardType, firstRowCardWidth);
                }

                addView(firstRow);
            }

            if (secondRowCards.size() > 0) {
                LinearLayout secondRow = (LinearLayout) inflater.inflate(R.layout.view_hand_row, this, false);

                int total =  mWidth - (secondRowCards.size() * 10);
                int secondRowCardWidth = Math.min(total / 4, total / secondRowCards.size());

                for (CardType cardType : secondRowCards) {
                    addCardToRow(secondRow, cardType, secondRowCardWidth);
                }

                addView(secondRow);
            }
        }
    }

    private void addCardToRow(LinearLayout row, final CardType cardType, int width) {
        CardView cardView = new HandCardView(getContext());
        cardView.setCardType(cardType);
        cardView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallbacks.onCardInHandClicked(cardType);
            }
        });

        LayoutParams params = new LayoutParams(width, ViewGroup.LayoutParams.MATCH_PARENT);
        params.setMargins(5, 5, 5, 5);
        row.addView(cardView, params);
    }

    public static interface LayoutCallback {
        void onViewLaidOut();
    }

    public static interface Callbacks {
        void onCardInHandClicked(CardType cardType);
    }

}
