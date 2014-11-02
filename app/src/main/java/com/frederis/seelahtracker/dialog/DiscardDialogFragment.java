package com.frederis.seelahtracker.dialog;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.frederis.seelahtracker.R;
import com.frederis.seelahtracker.card.CardType;
import com.frederis.seelahtracker.widget.DiscardView;

public class DiscardDialogFragment extends DialogFragment implements DiscardView.Callbacks {

    public static DiscardDialogFragment newInstance() {
        return new DiscardDialogFragment();
    }

    private DiscardView mDiscardView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_discard, container, false);

        mDiscardView = (DiscardView) view.findViewById(R.id.discard_view);
        mDiscardView.setCallbacks(this);

        return view;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.setTitle(R.string.discard_pile);

        return dialog;
    }

    @Override
    public void onCardInDiscardClicked(CardType cardType) {
        ((Callbacks) getActivity()).onCardInDiscardClicked(cardType);
    }

    public static interface Callbacks {
        void onCardInDiscardClicked(CardType cardType);
    }

}
