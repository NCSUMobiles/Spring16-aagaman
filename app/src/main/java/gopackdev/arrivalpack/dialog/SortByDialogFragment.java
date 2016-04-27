package gopackdev.arrivalpack.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import gopackdev.arrivalpack.R;

public class SortByDialogFragment extends DialogFragment implements CompoundButton.OnCheckedChangeListener {
    public interface SortByDialogListener {
        public void onDialogPositiveClick(DialogFragment dialog);
        public void onDialogNegativeClick(DialogFragment dialog);
    }
    SortByDialogListener mListener;

    @Override
    public void onAttach(Activity activity) {
        // TODO Auto-generated method stub
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (SortByDialogListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement NoticeDialogListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View customView = getActivity().getLayoutInflater().inflate(R.layout.fragment_sort_by_dialog, null);
        builder.setTitle(R.string.sort_by_dialog_title)
                .setView(customView)
                .setPositiveButton("Set",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // User cancelled the dialog
                                mListener.onDialogPositiveClick(SortByDialogFragment.this);
                            }
                        });
        //set check box to previous setting
        boolean gender_pref = getActivity().getSharedPreferences(getResources().getString(R.string.roommate_list_preference),
                getActivity().MODE_PRIVATE).getBoolean(getResources().getString(R.string.gender_preference),false);
        boolean language_pref = getActivity().getSharedPreferences(getResources().getString(R.string.roommate_list_preference),
                getActivity().MODE_PRIVATE).getBoolean(getResources().getString(R.string.language_preference),false);
        boolean country_pref = getActivity().getSharedPreferences(getResources().getString(R.string.roommate_list_preference),
                getActivity().MODE_PRIVATE).getBoolean(getResources().getString(R.string.country_preference),false);
        boolean diet_pref = getActivity().getSharedPreferences(getResources().getString(R.string.roommate_list_preference),
                getActivity().MODE_PRIVATE).getBoolean(getResources().getString(R.string.diet_preference),false);
        boolean sleep_pref = getActivity().getSharedPreferences(getResources().getString(R.string.roommate_list_preference),
                getActivity().MODE_PRIVATE).getBoolean(getResources().getString(R.string.sleep_preference),false);
        boolean wakeup_pref = getActivity().getSharedPreferences(getResources().getString(R.string.roommate_list_preference),
                getActivity().MODE_PRIVATE).getBoolean(getResources().getString(R.string.wakeup_preference),false);
        CheckBox gender_checkbox = (CheckBox)customView.findViewById(R.id.genderPreference);
        gender_checkbox.setChecked(gender_pref);
        CheckBox language_checkbox = (CheckBox)customView.findViewById(R.id.languagePreference);
        language_checkbox.setChecked(language_pref);
        CheckBox country_checkbox = (CheckBox)customView.findViewById(R.id.countryPreference);
        country_checkbox.setChecked(country_pref);
        CheckBox diet_checkbox = (CheckBox)customView.findViewById(R.id.dietPreference);
        diet_checkbox.setChecked(diet_pref);
        CheckBox sleep_checkbox = (CheckBox)customView.findViewById(R.id.sleepPreference);
        sleep_checkbox.setChecked(sleep_pref);
        CheckBox wakeup_checkbox = (CheckBox)customView.findViewById(R.id.wakeupPreference);
        wakeup_checkbox.setChecked(wakeup_pref);
        //--
        Dialog tmpD = builder.create();
//        enableCheckBox.setOnCheckedChangeListener(this);
//        mEditText.requestFocus();
        tmpD.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        return tmpD;
    }

    public void printMessage(Context ctx, String msg){
        Toast.makeText(ctx, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView , boolean isChecked) {
        // TODO Auto-generated method stub

    }

}