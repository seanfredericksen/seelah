package com.frederis.seelahtracker.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.frederis.seelahtracker.R;
import com.frederis.seelahtracker.card.CardType;

public abstract class CardView extends FrameLayout {

    private static final int [] STATE_SELECTED_FOR_CURE = {
            R.attr.state_selected_for_cure
    };

    private static final int [] STATE_BLESSING = {
            R.attr.state_blessing
    };

    private static final int [] STATE_SPELL = {
            R.attr.state_spell
    };

    private static final int [] STATE_OTHER = {
            R.attr.state_other
    };

    private static final int [] STATE_UNKNOWN = {
            R.attr.state_unknown
    };


    protected TextView mCardLabel;

    protected CardType mCardType;
    protected boolean mSelected;

    public CardView(Context context) {
        super(context);

        initialize(context);
    }

    public CardView(Context context, AttributeSet attrs) {
        super(context, attrs);

        initialize(context);
    }

    public CardView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        initialize(context);
    }

    private void initialize(Context context) {
        LayoutInflater.from(context).inflate(R.layout.card_view_root, this, true);

        mCardLabel = (TextView) findViewById(R.id.card_label);
        mCardType = CardType.UNKNOWN;

        setClickable(true);
        setBackgroundResource(R.drawable.card_view_background);

        updateTextLabel();
    }

    public void setCardType(CardType cardType) {
        mCardType = cardType;

        refreshDrawableState();
        updateTextLabel();
    }

    public void setSelected(boolean selected) {
        mSelected = selected;

        refreshDrawableState();
    }

    public CardType getCardType() {
        return mCardType;
    }

    public boolean isSelectedForCure() {
        return mSelected;
    }

    public void toggleSelected() {
        setSelected(!mSelected);
    }

    public void showLabel(boolean visible) {
        mCardLabel.setVisibility(visible ? VISIBLE : GONE);
    }

    protected abstract void updateTextLabel();

    @Override
    protected int[] onCreateDrawableState(int extraSpace) {
        final int[] drawableState = super.onCreateDrawableState(extraSpace + 2);

        if (mSelected) {
            Log.d("SEELAH", "Card is selected!");
            mergeDrawableStates(drawableState, STATE_SELECTED_FOR_CURE);
        }

        mergeDrawableStates(drawableState, mCardType.accept(new DrawableStateVisitor()));

        return drawableState;
    }

    private static class DrawableStateVisitor implements CardType.Visitor<int[]> {

        @Override
        public int[] visitBlessing() {
            return STATE_BLESSING;
        }

        @Override
        public int[] visitCure() {
            return STATE_SPELL;
        }

        @Override
        public int[] visitSpell() {
            return STATE_SPELL;
        }

        @Override
        public int[] visitOther() {
            return STATE_OTHER;
        }

        @Override
        public int[] visitUnknown() {
            return STATE_UNKNOWN;
        }

    }

}
