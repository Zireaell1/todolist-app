package com.zireaell1.todolist.presentation.settings;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.zireaell1.todolist.R;
import com.zireaell1.todolist.domain.entities.Category;
import com.zireaell1.todolist.domain.entities.Config;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class SettingsFragment extends Fragment {
    private SettingsViewModel settingsViewModel;
    private CategoryListAdapter categoryListAdapter;

    public SettingsFragment() {
    }

    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        settingsViewModel = new ViewModelProvider(requireActivity(), new SettingsViewModelFactory(getContext())).get(SettingsViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        Spinner notificationsReminderTimeDropdown = view.findViewById(R.id.notifications_reminder_time_dropdown);
        List<Integer> notificationsReminderTimeValues = Arrays.asList(1, 5, 10, 30, 60);
        ArrayAdapter<Integer> notificationsReminderTimeAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, notificationsReminderTimeValues);
        notificationsReminderTimeDropdown.setAdapter(notificationsReminderTimeAdapter);
        notificationsReminderTimeDropdown.setSelection(notificationsReminderTimeValues.indexOf(settingsViewModel.getConfig().execute().getNotificationsReminderTime()));
        notificationsReminderTimeDropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int selected = (int) parent.getItemAtPosition(position);
                Config newConfig = new Config(selected);
                if (!settingsViewModel.getConfig().execute().equals(newConfig)) {
                    settingsViewModel.getSaveConfig().execute(newConfig);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Button addCategoryButton = view.findViewById(R.id.add_category_button);
        addCategoryButton.setOnClickListener(v -> {
            final EditText input = new EditText(getContext());
            input.setInputType(InputType.TYPE_CLASS_TEXT);
            input.setHint(R.string.add_category_dialog_input_hint);
            new AlertDialog.Builder(getContext())
                    .setTitle(R.string.add_category_dialog_title)
                    .setView(input)
                    .setPositiveButton(R.string.add_category_dialog_positive, (dialog, which) -> {
                        Category newCategory = new Category(input.getText().toString());
                        CompletableFuture<Void> futureAddCategory = settingsViewModel.getAddCategory().execute(newCategory);
                        futureAddCategory.thenAccept(result -> {
                            categoryListAdapter.items.add(newCategory);
                            categoryListAdapter.notifyDataSetChanged();
                        });
                    })
                    .setNegativeButton(R.string.add_category_dialog_negative, (dialog, which) -> dialog.cancel())
                    .show();
        });

        CompletableFuture<List<Category>> futureCategories = settingsViewModel.getCategories().execute();
        futureCategories.thenAccept(categories -> {
            categoryListAdapter = new CategoryListAdapter(getContext(), R.layout.category_list_item, categories);
            ListView categoryList = view.findViewById(R.id.category_list);
            categoryList.setAdapter(categoryListAdapter);

            categoryList.setOnItemClickListener((parent, view1, position, id) -> {
                settingsViewModel.getDeleteCategory().execute(categoryListAdapter.getItem(position));
                categoryListAdapter.items.remove(position);
                categoryListAdapter.notifyDataSetChanged();
            });
        });

        return view;
    }
}
