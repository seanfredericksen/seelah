package com.frederis.seelahtracker.dialog;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.frederis.seelahtracker.R;
import com.frederis.seelahtracker.card.CardType;

public class HandCardActionDialogFragment extends DialogFragment {

    private static final String KEY_CARD_TYPE = "cardType";

    public static HandCardActionDialogFragment newInstance(CardType startingType) {
        Bundle args = new Bundle();
        args.putSerializable(KEY_CARD_TYPE, startingType);

        HandCardActionDialogFragment fragment =  new HandCardActionDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_hand_card_action, container, false);

        View activate = view.findViewById(R.id.activate);
        activate.setVisibility(getCardType().equals(CardType.CURE) ? View.VISIBLE : View.GONE);
        activate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Callbacks) getActivity()).activateCure();
                dismiss();
            }
        });

        view.findViewById(R.id.discard).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Callbacks) getActivity()).discardFromHand(getCardType());
                dismiss();
            }
        });

        view.findViewById(R.id.recharge).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Callbacks) getActivity()).rechargeFromHand(getCardType());
                dismiss();
            }
        });

        view.findViewById(R.id.remove).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Callbacks) getActivity()).removeFromHand(getCardType());
                dismiss();
            }
        });

        view.findViewById(R.id.change_type).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Callbacks) getActivity()).changeType(getCardType());
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
        void activateCure();
        void discardFromHand(CardType cardType);
        void rechargeFromHand(CardType cardType);
        void removeFromHand(CardType cardType);
        void changeType(CardType cardType);
    }

}
