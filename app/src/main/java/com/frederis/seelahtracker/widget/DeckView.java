package com.frederis.seelahtracker.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.LinearLayout;

import com.frederis.seelahtracker.CardManager;
import com.frederis.seelahtracker.SeelahTrackerApplication;
import com.frederis.seelahtracker.card.CardType;

import java.util.List;

import javax.inject.Inject;

public class DeckView extends LinearLayout implements CardManager.Listener {

    @Inject CardManager mCardManager;

    private int mTopWidth;
    private int mSecondWidth;
    private int mThirdWidth;

    private LayoutCallback mLayoutCallback;

    public DeckView(Context context) {
        super(context);
    }

    public DeckView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DeckView(Context context, AttributeSet attrs, int defStyle) {
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

        int width = getWidth();

        mTopWidth = width / 2;
        mSecondWidth = mTopWidth / 2;
        mThirdWidth = mSecondWidth / 2;

        if (mLayoutCallback != null) {
            mLayoutCallback.onViewLaidOut();
            mLayoutCallback = null;
        }
    }

    @Override
    public void onCardsUpdated(final List<CardType> deck,
                               List<CardType> hand,
                               List<CardType> discard,
                               final int unknownPercentage) {
        if (mTopWidth != 0) {
            populateCards(deck, unknownPercentage);
        } else {
            mLayoutCallback = new LayoutCallback() {
                @Override
                public void onViewLaidOut() {
                    populateCards(deck, unknownPercentage);
                }
            };
        }
    }

    private void populateCards(List<CardType> deck, int unknownPercentage) {
        removeAllViews();

        for (CardType cardType : deck) {
            DeckCardView cardView = new DeckCardView(getContext());
            cardView.showLabel(true);
            cardView.setUnknownPercentage(unknownPercentage);
            cardView.setCardType(cardType);

            LayoutParams params = new LayoutParams(mTopWidth, LayoutParams.MATCH_PARENT);
            params.gravity = Gravity.RIGHT;
            addView(cardView, params);
        }

        adjustCardPositions();
    }

    private void adjustCardPositions() {
        if (getChildCount() > 3) {
            adjustSecondCardPosition();
            adjustThirdCardPosition();
            adjustRemainingCardWidths();
        } else if (getChildCount() > 2) {
            adjustSecondCardPosition();
            matchThirdToSecondPosition();
        } else if (getChildCount() > 1) {
            adjustSecondCardPosition();
        }

        requestLayout();
    }

    private void adjustSecondCardPosition() {
        LayoutParams params = (LayoutParams) getChildAt(getChildCount() - 2).getLayoutParams();
        params.width = mSecondWidth;
    }

    private void adjustThirdCardPosition() {
        LayoutParams params = (LayoutParams) getChildAt(getChildCount() - 3).getLayoutParams();
        params.width = mThirdWidth;
    }

    private void adjustRemainingCardWidths() {
        int individualWidth = Math.min((getWidth() - (mTopWidth + mSecondWidth + mThirdWidth))
                / (getChildCount() - 3),
                mThirdWidth);

        for (int i = (getChildCount() - 4); i >= 0; i--) {
            CardView cardView = (CardView) getChildAt(i);
            cardView.showLabel(false);
            LayoutParams params = (LayoutParams) cardView.getLayoutParams();
            params.width = individualWidth;
        }
    }

    private void matchThirdToSecondPosition() {
        LayoutParams params = (LayoutParams) getChildAt(getChildCount() - 3).getLayoutParams();
        params.width = mSecondWidth;
    }

    public static interface LayoutCallback {
        void onViewLaidOut();
    }

}
