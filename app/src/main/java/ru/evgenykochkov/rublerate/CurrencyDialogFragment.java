package ru.evgenykochkov.rublerate;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.LinearLayout;

/**
 * Created by Жека on 05.03.2018.
 */

public class CurrencyDialogFragment extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Bundle inputBundle = getArguments();
        String dollar = inputBundle.getString(Constants.FILE_DOLLAR);
        String euro = inputBundle.getString(Constants.FILE_EURO);
        String pound = inputBundle.getString(Constants.FILE_POUND);

        LinearLayout dialog = (LinearLayout) LayoutInflater
                .from(getActivity())
                .inflate(R.layout.dialog, null);

        final EditText editDollar = dialog.findViewById(R.id.editDollar);
        final EditText  editEuro = dialog.findViewById(R.id.editEuro);
        final EditText  editPound = dialog.findViewById(R.id.editPound);

        if (dollar!=null) {
            editDollar.setText(dollar);
        }
        if (euro!=null) {
            editEuro.setText(euro);
        }
        if (pound!=null) {
            editPound.setText(pound);
        }

        AlertDialog.Builder b = new AlertDialog.Builder(getActivity());
        b
                .setTitle("Моя валюта")
                .setView(dialog)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        String dollar="0";
                        String euro="0";
                        String pound="0";

                        //запоминание инфы с edit полей
                        if (!editDollar.getText().toString().isEmpty()) {
                            dollar=editDollar.getText().toString();
                        }

                        if (!editEuro.getText().toString().isEmpty()) {
                            euro=editEuro.getText().toString();
                        }

                        if (!editPound.getText().toString().isEmpty()) {
                            pound=editPound.getText().toString();
                        }

                        ((MainActivity)getActivity()).saveCurrency(dollar, euro, pound);
                        ((MainActivity)getActivity()).getCurrency();
                        dialogInterface.dismiss();
                    }
                });
        return b.create();
    }
}
