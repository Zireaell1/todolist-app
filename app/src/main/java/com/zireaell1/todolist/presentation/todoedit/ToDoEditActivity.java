package com.zireaell1.todolist.presentation.todoedit;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.chip.Chip;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.zireaell1.todolist.R;
import com.zireaell1.todolist.Utils;
import com.zireaell1.todolist.domain.entities.Attachment;
import com.zireaell1.todolist.domain.entities.Category;
import com.zireaell1.todolist.domain.entities.ToDo;
import com.zireaell1.todolist.domain.entities.ToDoState;
import com.zireaell1.todolist.presentation.AlarmReceiver;
import com.zireaell1.todolist.presentation.AttachmentListAdapter;
import com.zireaell1.todolist.presentation.SpinnerCategoryAdapter;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;

public class ToDoEditActivity extends AppCompatActivity {

    private ToDoEditViewModel toDoEditViewModel;
    private AttachmentListAdapter attachmentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do_edit);

        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            Log.e("ToDoEditActivity", "Extras is null");
            return;
        }

        int toDoId = extras.getInt("toDoId");
        toDoEditViewModel = new ViewModelProvider(this, new ToDoEditViewModelFactory(this, toDoId)).get(ToDoEditViewModel.class);

        toDoEditViewModel.chooseFileLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Uri contentUri = result.getData().getData();
                        toDoEditViewModel.newFiles.add(contentUri);
                        refreshAttachmentList();
                    }
                }
        );

        toDoEditViewModel.saveFileLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        try {
                            try (InputStream in = getContentResolver().openInputStream(toDoEditViewModel.fileToSave); OutputStream out = getContentResolver().openOutputStream(result.getData().getData())) {
                                byte[] buffer = new byte[1024];
                                int len;
                                while ((len = in.read(buffer)) != -1) {
                                    out.write(buffer, 0, len);
                                }
                            }
                        } catch (IOException e) {
                            Log.e("ToDoAddActivity", "Error saving file", e);
                        }
                    }
                }
        );

        TextInputLayout titleTextView = findViewById(R.id.title);
        TextInputLayout descriptionTextView = findViewById(R.id.description);
        titleTextView.getEditText().setText(toDoEditViewModel.title);
        descriptionTextView.getEditText().setText(toDoEditViewModel.description);

        Spinner categoryDropdown = findViewById(R.id.category_dropdown);
        SpinnerCategoryAdapter categoryAdapter = new SpinnerCategoryAdapter(this, android.R.layout.simple_spinner_dropdown_item, new ArrayList<>());
        categoryDropdown.setAdapter(categoryAdapter);
        categoryDropdown.setSelection(toDoEditViewModel.categoryDropdownSelectedItemId);
        categoryDropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Category selectedCategory = categoryAdapter.getItem(position);
                toDoEditViewModel.categoryId = selectedCategory.getId();
                toDoEditViewModel.categoryDropdownSelectedItemId = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        toDoEditViewModel.getCategoriesState().observe(this, categoriesState -> {
            categoryAdapter.items.clear();
            categoryAdapter.items.addAll(categoriesState);
            categoryAdapter.notifyDataSetChanged();
        });
        toDoEditViewModel.loadCategories(this);

        Chip doneButton = findViewById(R.id.done_button);
        doneButton.setChecked(toDoEditViewModel.stateChipIsChecked);
        doneButton.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                toDoEditViewModel.state = ToDoState.DONE;
                toDoEditViewModel.stateChipIsChecked = true;
            } else {
                toDoEditViewModel.state = ToDoState.IN_PROGRESS;
                toDoEditViewModel.stateChipIsChecked = false;
            }
        });

        Chip notificationsButton = findViewById(R.id.notifications_button);
        notificationsButton.setChecked(toDoEditViewModel.notificationsChipIsChecked);
        notificationsButton.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                toDoEditViewModel.notifications = true;
                toDoEditViewModel.notificationsChipIsChecked = true;
            } else {
                toDoEditViewModel.notifications = false;
                toDoEditViewModel.notificationsChipIsChecked = false;
            }
        });

        Button timeButton = findViewById(R.id.time_button);
        timeButton.setOnClickListener(v -> {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
            LocalDateTime dateTime = LocalDateTime.of(toDoEditViewModel.year, toDoEditViewModel.month, toDoEditViewModel.dayOfMonth, toDoEditViewModel.hourOfDay, toDoEditViewModel.minute);
            String formattedDateTime = dateTime.format(formatter);
            DialogFragment dateFragment = new EditDatePickerFragment(toDoEditViewModel, () -> timeButton.setText(formattedDateTime));
            dateFragment.show(getSupportFragmentManager(), "datePicker");
        });

        if (toDoEditViewModel.isTimeSet) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
            LocalDateTime dateTime = LocalDateTime.of(toDoEditViewModel.year, toDoEditViewModel.month, toDoEditViewModel.dayOfMonth, toDoEditViewModel.hourOfDay, toDoEditViewModel.minute);
            String formattedDateTime = dateTime.format(formatter);
            timeButton.setText(formattedDateTime);
        }

        ImageButton closeButton = findViewById(R.id.close_button);
        closeButton.setOnClickListener(v -> finish());

        ImageButton deleteButton = findViewById(R.id.delete_button);
        deleteButton.setOnClickListener(v -> new MaterialAlertDialogBuilder(this)
                .setTitle(R.string.delete_todo_dialog_title)
                .setMessage(R.string.delete_todo_dialog_message)
                .setPositiveButton(R.string.delete_todo_dialog_positive, (dialog, which) -> {
                    CompletableFuture<Integer> futureDeleteAttachments = toDoEditViewModel.getDeleteAttachments().execute(toDoEditViewModel.getAttachmentsState().getValue());
                    futureDeleteAttachments.thenAccept(result -> {
                        CompletableFuture<Integer> futureDeleteToDo = toDoEditViewModel.getDeleteToDo().execute(toDoEditViewModel.toDoObj);
                        futureDeleteToDo.thenAccept(result1 -> {
                            String pathDestination = getFilesDir().getAbsolutePath() + "/" + String.format(Locale.getDefault(), "%d", toDoId);
                            deleteRecursive(new File(pathDestination));
                            setResult(RESULT_OK);
                            finish();
                        });
                    });
                })
                .setNegativeButton(R.string.delete_todo_dialog_negative, (dialog, which) -> dialog.cancel())
                .show());

        Button attachmentsButton = findViewById(R.id.attachments);
        attachmentsButton.setOnClickListener(v -> {
            Intent chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
            chooseFile.addCategory(Intent.CATEGORY_OPENABLE);
            chooseFile.setType("*/*");
            chooseFile = Intent.createChooser(chooseFile, "Choose a file");
            toDoEditViewModel.chooseFileLauncher.launch(chooseFile);
        });

        attachmentAdapter = new AttachmentListAdapter(this, R.layout.attachment_list_item, toDoEditViewModel.fileUris, uri -> {
            toDoEditViewModel.originalFiles.remove(uri);
            toDoEditViewModel.newFiles.remove(uri);
            toDoEditViewModel.filesToDelete.add(uri);
            refreshAttachmentList();
        }, fileUri -> {
            Intent saveFile = new Intent(Intent.ACTION_CREATE_DOCUMENT);
            saveFile.addCategory(Intent.CATEGORY_OPENABLE);
            saveFile.setType(getContentResolver().getType(fileUri));
            saveFile.putExtra(Intent.EXTRA_TITLE, Utils.getContent(this, fileUri, MediaStore.MediaColumns.DISPLAY_NAME));
            saveFile = Intent.createChooser(saveFile, "Save a file");

            toDoEditViewModel.fileToSave = fileUri;
            toDoEditViewModel.saveFileLauncher.launch(saveFile);
        });
        toDoEditViewModel.getAttachmentsState().observe(this, attachmentsState -> {
            for (Attachment attachment : attachmentsState) {
                try {
                    File file = new File(attachment.getFilePath());
                    String authority = getApplicationContext().getPackageName() + ".fileprovider";
                    Uri contentUri = FileProvider.getUriForFile(this, authority, file);
                    toDoEditViewModel.originalFiles.add(contentUri);
                } catch (Exception e) {
                    Log.e("ToDoEditActivity", "Exception", e);
                }
            }

            refreshAttachmentList();
        });

        FloatingActionButton confirmButton = findViewById(R.id.confirm_button);
        confirmButton.setOnClickListener(v -> {
            int id = toDoEditViewModel.id;
            String title = titleTextView.getEditText().getText().toString();
            String description = descriptionTextView.getEditText().getText().toString();
            LocalDateTime createDate = LocalDateTime.now();
            LocalDateTime completionDate = LocalDateTime.of(toDoEditViewModel.year, toDoEditViewModel.month, toDoEditViewModel.dayOfMonth, toDoEditViewModel.hourOfDay, toDoEditViewModel.minute);
            ToDoState state = toDoEditViewModel.state;
            boolean notifications = toDoEditViewModel.notifications;
            int categoryId = toDoEditViewModel.categoryId;

            ToDo toDo = new ToDo(id, title, description, createDate, completionDate, state, notifications, categoryId);

            CompletableFuture<Integer> futureEditToDo = toDoEditViewModel.getEditToDo().execute(toDo);
            futureEditToDo.thenAccept(result -> {
                String folderName = String.format(Locale.getDefault(), "%d", toDoId);
                File folder = new File(getFilesDir(), folderName);
                if (!folder.exists()) {
                    folder.mkdirs();
                }

                for (Uri fileUri : toDoEditViewModel.newFiles) {
                    String pathDestination = getFilesDir().getAbsolutePath() + "/" + folderName + "/" + Utils.getContent(this, fileUri, MediaStore.MediaColumns.DISPLAY_NAME);
                    Log.d("ToDoAddActivity", String.format("Saving file %s", pathDestination));

                    CompletableFuture<Void> futureAddAttachment = toDoEditViewModel.getAddAttachment().execute(new Attachment(Math.toIntExact(toDoId), pathDestination));
                    futureAddAttachment.thenAccept(result1 -> {
                        try {
                            copy(fileUri, pathDestination);
                        } catch (IOException e) {
                            Log.e("ToDoAddActivity", String.format("Error copying file %s", fileUri.getLastPathSegment()), e);
                        }
                    });
                }

                runOnUiThread(() -> {
                    AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                    long timeDifference = Duration.between(LocalDateTime.now(), completionDate).toMillis();

                    if (!notifications) {
                        Intent intent = new Intent(this, AlarmReceiver.class);
                        int toDoIdInt = Math.toIntExact(toDoId);
                        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), toDoIdInt, intent, PendingIntent.FLAG_IMMUTABLE);
                        alarmManager.cancel(pendingIntent);
                    }

                    if (timeDifference >= (long) toDoEditViewModel.getConfig().execute().getNotificationsReminderTime() * 60 * 1000 && notifications) {
                        Intent intent = new Intent(this, AlarmReceiver.class);
                        int toDoIdInt = Math.toIntExact(toDoId);
                        intent.putExtra("toDoId", toDoIdInt);
                        intent.putExtra("title", title);
                        intent.putExtra("description", description);

                        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), toDoIdInt, intent, PendingIntent.FLAG_IMMUTABLE);
                        alarmManager.cancel(pendingIntent);

                        long alarmTime = LocalDateTime.now().atZone(ZoneOffset.systemDefault()).toInstant().toEpochMilli() + timeDifference - (long) toDoEditViewModel.getConfig().execute().getNotificationsReminderTime() * 60 * 1000;
                        alarmManager.setExact(AlarmManager.RTC_WAKEUP, alarmTime, pendingIntent);
                    }
                });

                for (Uri uri : toDoEditViewModel.filesToDelete) {
                    String path = getFilesDir().getAbsolutePath() + "/" + folderName + "/" + Utils.getContent(this, uri, MediaStore.MediaColumns.DISPLAY_NAME);
                    toDoEditViewModel.getDeleteAttachmentByFilePath().execute(path);
                    new File(path).delete();
                }

                setResult(RESULT_OK);
                finish();
            });
        });

        refreshAttachmentList();
    }

    private void refreshAttachmentList() {
        attachmentAdapter.items.clear();
        attachmentAdapter.items.addAll(toDoEditViewModel.originalFiles);
        attachmentAdapter.items.addAll(toDoEditViewModel.newFiles);

        int adapterCount = attachmentAdapter.getCount();
        LinearLayout attachmentList = findViewById(R.id.attachments_list);
        attachmentList.removeAllViews();

        for (int i = 0; i < adapterCount; i++) {
            View item = attachmentAdapter.getView(i, null, null);
            attachmentList.addView(item);
        }
    }

    private void copy(Uri source, String pathDestination) throws IOException {
        File destination = new File(pathDestination);
        destination.createNewFile();

        try (InputStream in = getContentResolver().openInputStream(source); OutputStream out = Files.newOutputStream(destination.toPath())) {
            byte[] buffer = new byte[1024];
            int len;
            while ((len = in.read(buffer)) != -1) {
                out.write(buffer, 0, len);
            }
        }
    }

    void deleteRecursive(File fileOrDirectory) {
        if (fileOrDirectory.isDirectory())
            for (File child : fileOrDirectory.listFiles())
                deleteRecursive(child);

        fileOrDirectory.delete();
    }
}
