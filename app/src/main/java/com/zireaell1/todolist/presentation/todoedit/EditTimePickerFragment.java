package com.zireaell1.todolist.presentation.todoedit;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.zireaell1.todolist.presentation.SetTime;

import java.util.Calendar;

public class EditTimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {
    private final ToDoEditViewModel toDoEditViewModel;
    private final SetTime setTime;

    public EditTimePickerFragment(ToDoEditViewModel toDoEditViewModel, SetTime setTime) {
        this.toDoEditViewModel = toDoEditViewModel;
        this.setTime = setTime;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        return new TimePickerDialog(getActivity(), this, hour, minute, DateFormat.is24HourFormat(getActivity()));
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        toDoEditViewModel.hourOfDay = hourOfDay;
        toDoEditViewModel.minute = minute;
        toDoEditViewModel.isTimeSet = true;
        setTime.setTime();
    }
}
