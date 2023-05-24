package com.zireaell1.todolist.presentation.todoedit;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.zireaell1.todolist.presentation.SetTime;

import java.util.Calendar;

public class EditDatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
    private final ToDoEditViewModel toDoEditViewModel;
    private final SetTime setTime;

    public EditDatePickerFragment(ToDoEditViewModel toDoEditViewModel, SetTime setTime) {
        this.toDoEditViewModel = toDoEditViewModel;
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
        toDoEditViewModel.year = year;
        toDoEditViewModel.month = month + 1;
        toDoEditViewModel.dayOfMonth = dayOfMonth;

        DialogFragment timeFragment = new EditTimePickerFragment(toDoEditViewModel, setTime);
        timeFragment.show(requireActivity().getSupportFragmentManager(), "timePicker");
    }
}
