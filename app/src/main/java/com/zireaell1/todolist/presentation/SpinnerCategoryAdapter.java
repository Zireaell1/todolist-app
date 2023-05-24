package com.zireaell1.todolist.presentation;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.zireaell1.todolist.domain.entities.Category;

import java.util.List;

public class SpinnerCategoryAdapter extends ArrayAdapter<Category> {
    private final List<Category> items;

    public SpinnerCategoryAdapter(Context context, int textViewResourceId, List<Category> items) {
        super(context, textViewResourceId, items);
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Nullable
    @Override
    public Category getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        TextView label = (TextView) super.getView(position, convertView, parent);
        label.setText(items.get(position).getName());
        return label;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        TextView label = (TextView) super.getDropDownView(position, convertView, parent);
        label.setText(items.get(position).getName());
        return label;
    }
}
