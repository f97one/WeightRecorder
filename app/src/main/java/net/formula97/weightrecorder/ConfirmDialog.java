package net.formula97.weightrecorder;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import java.util.EventListener;

/**
 * Created by f97one on 14/11/06.
 */
public class ConfirmDialog extends DialogFragment {

    public static final String DIALOG_TAG = ConfirmDialog.class.getName() + ".DIALOG_TAG";

    private OnButtonSelectedListener mListener;

    public interface OnButtonSelectedListener extends EventListener {
        public void onButtonSelected(int whichButton);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if (activity instanceof OnButtonSelectedListener) {
            mListener = (OnButtonSelectedListener) activity;
        } else {
            mListener = null;
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setTitle(getString(R.string.confirm))
                .setMessage(R.string.wish_to_erase)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (mListener != null) {
                            mListener.onButtonSelected(DialogInterface.BUTTON_POSITIVE);
                        }
                        getDialog().dismiss();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (mListener != null) {
                            mListener.onButtonSelected(DialogInterface.BUTTON_NEGATIVE);
                        }
                        getDialog().dismiss();
                    }
                })
                .create();

        return dialog;
    }
}
