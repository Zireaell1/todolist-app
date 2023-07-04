package com.zireaell1.todolist.presentation;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.zireaell1.todolist.R;
import com.zireaell1.todolist.Utils;

import java.util.List;

public class AttachmentListAdapter extends ArrayAdapter<Uri> {
    public final List<Uri> items;
    private final @LayoutRes int resource;
    private final RefreshAttachmentListCallback refreshAttachmentListCallback;
    private final SaveAttachmentCallback saveAttachmentCallback;

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

        ImageView file_icon = convertView.findViewById(R.id.file_icon);

        switch (getContext().getContentResolver().getType(uri)) {
            case "image/jpeg":
            case "image/png":
                file_icon.setImageResource(R.drawable.image_24px);
                break;
            case "audio/mpeg":
            case "audio/ogg":
                file_icon.setImageResource(R.drawable.audio_file_24px);
                break;
            case "application/pdf":
            case "text/plain":
                file_icon.setImageResource(R.drawable.description_24px);
                break;
            case "application/zip":
            case "application/rar":
                file_icon.setImageResource(R.drawable.folder_zip_24px);
                break;
            case "video/mp4":
                file_icon.setImageResource(R.drawable.movie_24px);
                break;
            default:
                file_icon.setImageResource(R.drawable.draft_24px);
                break;
        }

        TextView fileNameTextView = convertView.findViewById(R.id.file_name);
        fileNameTextView.setText(Utils.getContent(getContext(), uri, MediaStore.MediaColumns.DISPLAY_NAME));

        TextView fileSizeTextView = convertView.findViewById(R.id.file_size);
        fileSizeTextView.setText(getContext().getString(R.string.attachment_size, Utils.getContent(getContext(), uri, MediaStore.MediaColumns.SIZE)));

        ImageButton openFileButton = convertView.findViewById(R.id.open_button);
        openFileButton.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.setDataAndType(uri, getContext().getContentResolver().getType(uri));
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            getContext().startActivity(intent);
        });

        ImageButton saveFileButton = convertView.findViewById(R.id.save_file_button);
        saveFileButton.setOnClickListener(v -> {
            saveAttachmentCallback.onSave(uri);
        });

        ImageButton removeFileButton = convertView.findViewById(R.id.remove_file_button);
        removeFileButton.setOnClickListener(v -> {
            items.remove(position);
            refreshAttachmentListCallback.onAttachmentsChanged(uri);
        });

        return convertView;
    }
}
