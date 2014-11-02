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

import java.util.List;

public class DiscardDialogFragment extends DialogFragment implements DiscardView.Callbacks {

    private static final String KEY_CURING = "curing";

    public static DiscardDialogFragment newInstance() {
        return newInstance(false);
    }

    public static DiscardDialogFragment newInstance(boolean curing) {
        Bundle args = new Bundle();
        args.putBoolean(KEY_CURING, curing);

        DiscardDialogFragment fragment = new DiscardDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private DiscardView mDiscardView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_discard, container, false);

        boolean isCuring = getArguments().getBoolean(KEY_CURING);

        mDiscardView = (DiscardView) view.findViewById(R.id.discard_view);
        mDiscardView.setCallbacks(this);
        mDiscardView.setIsInCureMode(isCuring);

        view.findViewById(R.id.cure_button_bar).setVisibility(isCuring ? View.VISIBLE : View.GONE);

        view.findViewById(R.id.ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Callbacks) getActivity()).cure(mDiscardView.getSelectedCards());
                dismiss();
            }
        });

        view.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

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
        void cure(List<CardType> cardTypes);
    }

}
