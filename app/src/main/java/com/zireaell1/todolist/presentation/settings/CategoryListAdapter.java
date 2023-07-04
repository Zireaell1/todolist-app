package com.zireaell1.todolist.presentation.settings;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.zireaell1.todolist.R;
import com.zireaell1.todolist.domain.entities.Category;

import java.util.List;

public class CategoryListAdapter extends ArrayAdapter<Category> {
    public final List<Category> items;
    private final @LayoutRes int resource;

    public CategoryListAdapter(Context context, @LayoutRes int resource, List<Category> items) {
        super(context, resource, items);
        this.resource = resource;
        this.items = items;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(resource, parent, false);
        }
        Category category = getItem(position);

        TextView categoryTextView = convertView.findViewById(R.id.category_name);
        categoryTextView.setText(category.getName());

        return convertView;
    }
}
