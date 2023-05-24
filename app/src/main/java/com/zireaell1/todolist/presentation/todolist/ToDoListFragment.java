package com.zireaell1.todolist.presentation.todolist;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.zireaell1.todolist.R;
import com.zireaell1.todolist.domain.entities.Category;
import com.zireaell1.todolist.domain.entities.Sort;
import com.zireaell1.todolist.domain.entities.ToDo;
import com.zireaell1.todolist.domain.entities.ToDoState;
import com.zireaell1.todolist.presentation.SortItem;
import com.zireaell1.todolist.presentation.SpinnerCategoryAdapter;
import com.zireaell1.todolist.presentation.SpinnerSortAdapter;
import com.zireaell1.todolist.presentation.todoadd.ToDoAddActivity;
import com.zireaell1.todolist.presentation.todoedit.ToDoEditActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

public class ToDoListFragment extends Fragment {
    private ToDoListViewModel toDoListViewModel;
    private ToDoListAdapter adapter;

    public ToDoListFragment() {
    }

    public static ToDoListFragment newInstance() {
        return new ToDoListFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        toDoListViewModel = new ViewModelProvider(requireActivity(), new ToDoListViewModelFactory(getContext())).get(ToDoListViewModel.class);
        toDoListViewModel.toDoAddActivityLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        refresh();
                    }
                }
        );

        toDoListViewModel.toDoEditActivityLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        refresh();
                    }
                }
        );
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_to_do_list, container, false);

        EditText searchBar = view.findViewById(R.id.search_edit_text);
        searchBar.setText(toDoListViewModel.searchQuery);
        searchBar.setOnEditorActionListener((textView, i, keyEvent) -> {
            if (i == EditorInfo.IME_ACTION_SEARCH) {
                if (!Objects.equals(toDoListViewModel.searchQuery, textView.getText().toString())) {
                    toDoListViewModel.searchQuery = textView.getText().toString();
                    refresh();
                    return true;
                }
            }
            return false;
        });

        Spinner sortDropdown = view.findViewById(R.id.sort_dropdown);
        List<SortItem> sortItems = Arrays.asList(new SortItem(R.string.sort_desc, Sort.DESC), new SortItem(R.string.sort_asc, Sort.ASC));
        SpinnerSortAdapter sortAdapter = new SpinnerSortAdapter(getContext(), android.R.layout.simple_spinner_dropdown_item, sortItems);
        sortDropdown.setAdapter(sortAdapter);
        sortDropdown.setSelection(toDoListViewModel.sortDropdownSelectedItemId);
        sortDropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (toDoListViewModel.sortDropdownSelectedItemId != position) {
                    SortItem sortItem = sortAdapter.getItem(position);
                    toDoListViewModel.sortMethod = sortItem.getSortMethod();
                    toDoListViewModel.sortDropdownSelectedItemId = position;
                    refresh();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Spinner categoryDropdown = view.findViewById(R.id.category_dropdown);
        CompletableFuture<List<Category>> futureCategories = toDoListViewModel.getCategories().execute();
        futureCategories.thenAccept(categories -> {
            categories.add(0, new Category(-1, getString(R.string.category_none)));
            SpinnerCategoryAdapter categoryAdapter = new SpinnerCategoryAdapter(getContext(), android.R.layout.simple_spinner_dropdown_item, categories);
            categoryDropdown.setAdapter(categoryAdapter);
            categoryDropdown.setSelection(toDoListViewModel.categoryDropdownSelectedItemId);
            categoryDropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (toDoListViewModel.categoryDropdownSelectedItemId != position) {
                        Category selectedCategory = categoryAdapter.getItem(position);
                        toDoListViewModel.categoryId = selectedCategory.getId();
                        toDoListViewModel.categoryDropdownSelectedItemId = position;
                        refresh();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        });

        Chip showDoneButton = view.findViewById(R.id.show_done_button);
        showDoneButton.setChecked(toDoListViewModel.stateChipIsChecked);
        showDoneButton.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (toDoListViewModel.stateChipIsChecked != isChecked) {
                if (isChecked) {
                    toDoListViewModel.state = ToDoState.DONE;
                    toDoListViewModel.stateChipIsChecked = true;
                } else {
                    toDoListViewModel.state = ToDoState.IN_PROGRESS;
                    toDoListViewModel.stateChipIsChecked = false;
                }
                refresh();
            }
        });

        List<ToDo> items = new ArrayList<>();
        RecyclerView recyclerView = view.findViewById(R.id.todos_list);
        View.OnClickListener onClickListener = new ToDoListOnClickListener(recyclerView, items, (position, item) -> {
            Intent intent = new Intent(getActivity(), ToDoEditActivity.class);
            intent.putExtra("toDoId", item.getId());

            toDoListViewModel.toDoEditActivityLauncher.launch(intent);
        });
        adapter = new ToDoListAdapter(items, onClickListener);
        recyclerView.setAdapter(adapter);

        CompletableFuture<List<ToDo>> futureToDos = toDoListViewModel.getToDos().execute(toDoListViewModel.sortMethod, toDoListViewModel.categoryId, toDoListViewModel.state, toDoListViewModel.searchQuery);
        futureToDos.thenAccept(toDos -> {
            adapter.list.clear();
            adapter.list.addAll(toDos);
            adapter.notifyDataSetChanged();
        });

        FloatingActionButton addButton = view.findViewById(R.id.add_button);
        addButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ToDoAddActivity.class);
            toDoListViewModel.toDoAddActivityLauncher.launch(intent);
        });

        return view;
    }

    private void refresh() {
        CompletableFuture<List<ToDo>> futureToDos = toDoListViewModel.getToDos().execute(toDoListViewModel.sortMethod, toDoListViewModel.categoryId, toDoListViewModel.state, toDoListViewModel.searchQuery);
        futureToDos.thenAccept(toDos -> requireActivity().runOnUiThread(() -> {
            adapter.list.clear();
            adapter.list.addAll(toDos);
            adapter.notifyDataSetChanged();
        }));
    }
}
