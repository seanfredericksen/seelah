package com.frederis.seelahtracker.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import com.frederis.seelahtracker.R;

public class CureRechargeDialogFragment extends DialogFragment {

    public static CureRechargeDialogFragment newInstance() {
        return new CureRechargeDialogFragment();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(getActivity())
                .setTitle(R.string.cure_recharged_question)
                .setPositiveButton(R.string.yes,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                ((Callbacks) getActivity()).onCureWasRecharged();
                            }
                        }
                )
                .setNegativeButton(R.string.no,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                ((Callbacks) getActivity()).onCureWasDiscarded();
                            }
                        }
                )
                .setCancelable(false)
                .create();
    }

    public static interface Callbacks {
        void onCureWasRecharged();
        void onCureWasDiscarded();
    }
}