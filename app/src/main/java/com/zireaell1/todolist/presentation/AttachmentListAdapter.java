package com.zireaell1.todolist.presentation;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.zireaell1.todolist.R;

import java.util.List;

public class AttachmentListAdapter extends ArrayAdapter<Uri> {
    private final @LayoutRes int resource;
    private final RefreshAttachmentListCallback refreshAttachmentListCallback;
    private final SaveAttachmentCallback saveAttachmentCallback;
    public List<Uri> items;

    public AttachmentListAdapter(Context context, @LayoutRes int resource, List<Uri> items, RefreshAttachmentListCallback refreshAttachmentListCallback, SaveAttachmentCallback saveAttachmentCallback) {
        super(context, resource, items);
        this.resource = resource;
        this.items = items;
        this.refreshAttachmentListCallback = refreshAttachmentListCallback;
        this.saveAttachmentCallback = saveAttachmentCallback;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(resource, parent, false);
        }
        Uri uri = getItem(position);

        TextView fileNameTextView = convertView.findViewById(R.id.file_name);
        String pattern = "primary:[^/]+/";
        String fileName = uri.getLastPathSegment().replaceAll(pattern, "");
        fileNameTextView.setText(fileName);

        ImageButton saveFileButton = convertView.findViewById(R.id.save_file_button);
        saveFileButton.setOnClickListener(v -> {
            saveAttachmentCallback.onSave(uri);
        });

        ImageButton removeFileButton = convertView.findViewById(R.id.remove_file_button);
        removeFileButton.setOnClickListener(v -> {
            items.remove(position);
            refreshAttachmentListCallback.onAttachmentsChanged();
        });

        return convertView;
    }
}
