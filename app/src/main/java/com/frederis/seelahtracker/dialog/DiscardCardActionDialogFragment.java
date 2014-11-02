package com.frederis.seelahtracker.dialog;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.frederis.seelahtracker.R;
import com.frederis.seelahtracker.card.CardType;

public class DiscardCardActionDialogFragment extends DialogFragment {

    private static final String KEY_CARD_TYPE = "cardType";

    public static DiscardCardActionDialogFragment newInstance(CardType startingType) {
        Bundle args = new Bundle();
        args.putSerializable(KEY_CARD_TYPE, startingType);

        DiscardCardActionDialogFragment fragment =  new DiscardCardActionDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_discard_card_action, container, false);

        view.findViewById(R.id.add_to_hand).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Callbacks) getActivity()).addToHandFromDiscard(getCardType());
                dismiss();
            }
        });

        view.findViewById(R.id.recharge).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Callbacks) getActivity()).rechargeFromDiscard(getCardType());
                dismiss();
            }
        });

        view.findViewById(R.id.remove).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Callbacks) getActivity()).removeFromDiscard(getCardType());
                dismiss();
            }
        });

        return view;
    }

    private CardType getCardType() {
        return (CardType) getArguments().getSerializable(KEY_CARD_TYPE);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.setTitle(R.string.action);

        return dialog;
    }

    public static interface Callbacks {
        void addToHandFromDiscard(CardType cardType);
        void rechargeFromDiscard(CardType cardType);
        void removeFromDiscard(CardType cardType);
    }

}