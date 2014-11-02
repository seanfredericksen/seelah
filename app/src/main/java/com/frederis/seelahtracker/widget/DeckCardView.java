package com.frederis.seelahtracker.widget;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;

import com.frederis.seelahtracker.R;
import com.frederis.seelahtracker.card.CardType;

public class DeckCardView extends CardView {

    private int mUnknownPercentage;

    public DeckCardView(Context context) {
        super(context);

        initialize();
    }

    public DeckCardView(Context context, AttributeSet attrs) {
        super(context, attrs);

        initialize();
    }

    public DeckCardView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        initialize();
    }

    private void initialize() {
        showLabel(true);
    }

    @Override
    protected void updateTextLabel() {
        mCardLabel.setText(mCardType.accept(new CardLabelVisitor()));
    }

    public void setUnknownPercentage(int unknownPercentage) {
        mUnknownPercentage = unknownPercentage;
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
            return mResources.getString(R.string.unknown_percent, mUnknownPercentage);
        }

    }

}
