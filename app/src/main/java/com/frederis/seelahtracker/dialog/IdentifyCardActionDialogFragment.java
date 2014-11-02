package com.frederis.seelahtracker.dialog;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.frederis.seelahtracker.R;
import com.frederis.seelahtracker.card.CardType;


public class IdentifyCardActionDialogFragment extends DialogFragment {

    private static final String KEY_STARTING_TYPE = "startingType";

    public static IdentifyCardActionDialogFragment newInstance(CardType startingType) {
        Bundle args = new Bundle();
        args.putSerializable(KEY_STARTING_TYPE, startingType);

        IdentifyCardActionDialogFragment fragment =  new IdentifyCardActionDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_unknown_card_action, container, false);

        view.findViewById(R.id.blessing).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectCard(CardType.BLESSING);
            }
        });

        view.findViewById(R.id.cure).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectCard(CardType.CURE);
            }
        });

        view.findViewById(R.id.spell).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectCard(CardType.SPELL);
            }
        });

        view.findViewById(R.id.other).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectCard(CardType.OTHER);
            }
        });

        return view;
    }

    private void selectCard(CardType cardType) {
        ((Callbacks) getActivity()).onIdentifyTypeClicked(getTag(),
                (CardType) getArguments().getSerializable(KEY_STARTING_TYPE),
                cardType);

        dismiss();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.setTitle(R.string.identify);

        return dialog;
    }

    public static interface Callbacks {
        void onIdentifyTypeClicked(String tag, CardType startingType, CardType newType);
    }

}
