package ru.devtron.dagturism.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import ru.devtron.dagturism.R;
import ru.devtron.dagturism.model.ModelPlace;

/**
 * Created by Ruslan Aliev on 17.11.2015.
 */
public class SearchPlaceDialogFragment extends DialogFragment {

    private SearchPlaceListener searchPlaceListener;

    public interface SearchPlaceListener {
        void onSearchStarted(String cityOrTown, String selectedRest);
        void onSearchCanceled();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            searchPlaceListener = (SearchPlaceListener) activity;
        }
        catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + "must Implement SearchPlaceListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View container = inflater.inflate(R.layout.dialog_select_place, null);

        builder.setTitle(R.string.sort_by);
        builder.setView(container);

        final Spinner spPlace = (Spinner) container.findViewById(R.id.spDialogPlace);
        final Spinner spRest = (Spinner) container.findViewById(R.id.spDialogRest);

        ArrayAdapter<String> spPlaceAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_dropdown_item, ModelPlace.PLACES);

        spPlace.setAdapter(spPlaceAdapter);

        spPlace.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        ArrayAdapter<String> spRestAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_dropdown_item, ModelPlace.REST);

        spRest.setAdapter(spRestAdapter);

        spRest.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });




        builder.setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                searchPlaceListener.onSearchStarted(spPlace.getSelectedItem().toString(), spRest.getSelectedItem().toString());
                dialog.dismiss();
            }
        });

        builder.setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                searchPlaceListener.onSearchCanceled();
                dialog.cancel();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Button positiveButton = ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_POSITIVE);
                positiveButton.setEnabled(true);
            }
        });

        return alertDialog;
    }
}
