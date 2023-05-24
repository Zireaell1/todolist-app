package com.zireaell1.todolist.presentation.todoadd;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.zireaell1.todolist.presentation.SetTime;

import java.util.Calendar;

public class AddDatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
    private final ToDoAddViewModel toDoAddViewModel;
    private final SetTime setTime;

    public AddDatePickerFragment(ToDoAddViewModel toDoAddViewModel, SetTime setTime) {
        this.toDoAddViewModel = toDoAddViewModel;
        this.setTime = setTime;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        return new DatePickerDialog(requireContext(), this, year, month, day);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        toDoAddViewModel.year = year;
        toDoAddViewModel.month = month + 1;
        toDoAddViewModel.dayOfMonth = dayOfMonth;

        DialogFragment timeFragment = new AddTimePickerFragment(toDoAddViewModel, setTime);
        timeFragment.show(requireActivity().getSupportFragmentManager(), "timePicker");
    }
}
