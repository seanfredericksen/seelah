package com.frederis.seelahtracker.widget;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;

import com.frederis.seelahtracker.R;
import com.frederis.seelahtracker.card.CardType;

public class HandCardView extends CardView {

    public HandCardView(Context context) {
        super(context);
    }

    public HandCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HandCardView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void updateTextLabel() {
        mCardLabel.setText(mCardType.accept(new CardLabelVisitor()));
    }

    private class CardLabelVisitor implements CardType.Visitor<String> {

        private Resources mResources;

        public CardLabelVisitor() {
            mResources = getResources();
        }

        @Override
        public String visitBlessing() {
            return mResources.getString(R.string.blessing_label);
        }

        @Override
        public String visitCure() {
            return mResources.getString(R.string.cure_label);
        }

        @Override
        public String visitSpell() {
            return mResources.getString(R.string.spell_label);
        }

        @Override
        public String visitOther() {
            return mResources.getString(R.string.other_label);
        }

        @Override
        public String visitUnknown() {
            return mResources.getString(R.string.unknown_label);
        }

    }

}

