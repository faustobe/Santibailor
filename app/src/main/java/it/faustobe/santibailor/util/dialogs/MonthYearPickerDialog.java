package it.faustobe.santibailor.util.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

import it.faustobe.santibailor.R;

public class MonthYearPickerDialog extends DialogFragment {

    private static final int MAX_YEAR = 2099;

    public interface OnDateSetListener {
        void onDateSet(int month, int day);
    }

    private OnDateSetListener listener;

    public void setListener(OnDateSetListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        Calendar cal = Calendar.getInstance();

        View dialog = inflater.inflate(R.layout.dialog_month_year_picker, null);
        final NumberPicker monthPicker = dialog.findViewById(R.id.picker_month);
        final NumberPicker dayPicker = dialog.findViewById(R.id.picker_day);

        monthPicker.setMinValue(1);
        monthPicker.setMaxValue(12);
        monthPicker.setValue(cal.get(Calendar.MONTH) + 1);

        dayPicker.setMinValue(1);
        dayPicker.setMaxValue(31);
        dayPicker.setValue(cal.get(Calendar.DAY_OF_MONTH));

        builder.setView(dialog)
                .setPositiveButton("OK", (dialog1, id) -> {
                    listener.onDateSet(monthPicker.getValue() , dayPicker.getValue());
                })
                .setNegativeButton("Annulla", (dialog12, id) -> MonthYearPickerDialog.this.getDialog().cancel());

        return builder.create();
    }
}




