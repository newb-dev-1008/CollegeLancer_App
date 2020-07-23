package com.college.freelancestartup;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

public class DatePickerFragment extends DialogFragment {

    private int pickedYear = 0, pickedDay = 0, pickedMonth = 0, year, month, day;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Calendar calendar = Calendar.getInstance();

        if (pickedYear == 0) {
            year = calendar.get(Calendar.YEAR);
            month = calendar.get(Calendar.MONTH);
            day = calendar.get(Calendar.DAY_OF_MONTH);
        } else {
            year = pickedYear;
            month = pickedMonth;
            day = pickedDay;
        }

        return new DatePickerDialog(getActivity(), (DatePickerDialog.OnDateSetListener) getActivity(), year, month, day);
    }

    public void getPickedDate(int pickedDay1, int pickedMonth1, int pickedYear1){
        pickedDay = pickedDay1;
        pickedMonth = pickedMonth1;
        pickedYear = pickedYear1;
    }
}
