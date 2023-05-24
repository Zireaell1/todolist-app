package com.zireaell1.todolist.presentation.todoedit;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.chip.Chip;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.zireaell1.todolist.R;
import com.zireaell1.todolist.domain.entities.Attachment;
import com.zireaell1.todolist.domain.entities.Category;
import com.zireaell1.todolist.domain.entities.ToDo;
import com.zireaell1.todolist.domain.entities.ToDoState;
import com.zireaell1.todolist.presentation.AlarmReceiver;
import com.zireaell1.todolist.presentation.AttachmentListAdapter;
import com.zireaell1.todolist.presentation.SaveAttachmentCallback;
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
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ToDoEditActivity extends AppCompatActivity {

    private ToDoEditViewModel toDoEditViewModel;
    private AttachmentListAdapter attachmentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do_edit);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            int toDoId = extras.getInt("toDoId");
            toDoEditViewModel = new ViewModelProvider(this, new ToDoEditViewModelFactory(this, toDoId)).get(ToDoEditViewModel.class);

            toDoEditViewModel.chooseFileLauncher = registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    result -> {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            Uri contentUri = data.getData();
                            toDoEditViewModel.fileUris.add(contentUri);
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
                                Log.d("ToDoEditActivity", "Error saving file with content provider.");

                                try {
                                    try (InputStream in = Files.newInputStream(new File(toDoEditViewModel.fileToSave.getPath()).toPath()); OutputStream out = getContentResolver().openOutputStream(result.getData().getData())) {
                                        byte[] buffer = new byte[1024];
                                        int len;
                                        while ((len = in.read(buffer)) != -1) {
                                            out.write(buffer, 0, len);
                                        }
                                    }
                                } catch (IOException ex) {
                                    Log.e("ToDoEditActivity", "Error saving file", ex);
                                }
                            }
                        }
                    }
            );

            TextView titleTextView = findViewById(R.id.title);
            TextView descriptionTextView = findViewById(R.id.description);
            titleTextView.setText(toDoEditViewModel.title);
            descriptionTextView.setText(toDoEditViewModel.description);

            Spinner categoryDropdown = findViewById(R.id.category_dropdown);
            CompletableFuture<List<Category>> futureCategories = toDoEditViewModel.getCategories().execute();
            futureCategories.thenAccept(categories -> {
                categories.add(0, new Category(-1, getString(R.string.category_none)));
                SpinnerCategoryAdapter categoryAdapter = new SpinnerCategoryAdapter(this, android.R.layout.simple_spinner_dropdown_item, categories);
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
            });

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
            deleteButton.setOnClickListener(v -> {
                CompletableFuture<Integer> futureDeleteToDo = toDoEditViewModel.getDeleteToDo().execute(toDoEditViewModel.toDoObj);
                futureDeleteToDo.thenAccept(result -> {
                    setResult(RESULT_OK);
                    finish();
                });
            });

            Button attachmentsButton = findViewById(R.id.attachments);
            attachmentsButton.setOnClickListener(v -> {
                Intent chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
                chooseFile.addCategory(Intent.CATEGORY_OPENABLE);
                chooseFile.setType("*/*");
                chooseFile = Intent.createChooser(chooseFile, "Choose a file");
                toDoEditViewModel.chooseFileLauncher.launch(chooseFile);
            });

            CompletableFuture<List<Attachment>> futureAttachments = toDoEditViewModel.getAttachments().execute(toDoId);
            futureAttachments.thenAccept(attachments -> {
                for (Attachment attachment : attachments) {
                    toDoEditViewModel.fileUris.add(Uri.parse(attachment.getFilePath()));
                }

                attachmentAdapter = new AttachmentListAdapter(this, R.layout.attachment_list_item, toDoEditViewModel.fileUris, this::refreshAttachmentList, new SaveAttachmentCallback() {
                    @Override
                    public void onSave(Uri fileUri) {
                        Intent saveFile = new Intent(Intent.ACTION_CREATE_DOCUMENT);
                        saveFile.addCategory(Intent.CATEGORY_OPENABLE);
                        saveFile.setType("*/*");
                        saveFile = Intent.createChooser(saveFile, "Save a file");

                        toDoEditViewModel.fileToSave = fileUri;
                        toDoEditViewModel.saveFileLauncher.launch(saveFile);
                    }
                });
                refreshAttachmentList();
            });

            FloatingActionButton confirmButton = findViewById(R.id.confirm_button);
            confirmButton.setOnClickListener(v -> {
                int id = toDoEditViewModel.id;
                String title = titleTextView.getText().toString();
                String description = descriptionTextView.getText().toString();
                LocalDateTime createDate = LocalDateTime.now();
                LocalDateTime completionDate = LocalDateTime.of(toDoEditViewModel.year, toDoEditViewModel.month, toDoEditViewModel.dayOfMonth, toDoEditViewModel.hourOfDay, toDoEditViewModel.minute);
                ToDoState state = toDoEditViewModel.state;
                boolean notifications = toDoEditViewModel.notifications;
                int categoryId = toDoEditViewModel.categoryId;

                CompletableFuture<List<Attachment>> futureAttachments1 = toDoEditViewModel.getAttachments().execute(toDoId);
                futureAttachments.thenAccept(attachments -> {
                    CompletableFuture<Integer> futureDeleteAttachments = toDoEditViewModel.getDeleteAttachments().execute(attachments);
                    futureDeleteAttachments.thenAccept(deletedAttachments -> {
                        Log.d("ToDoEditActivity", String.format("Deleted %d attachments", deletedAttachments));
                    });
                    futureDeleteAttachments.join();
                });
                futureAttachments1.join();

                ToDo toDo = new ToDo(id, title, description, createDate, completionDate, state, notifications, categoryId);

                CompletableFuture<Integer> futureEditToDo = toDoEditViewModel.getEditToDo().execute(toDo);
                futureEditToDo.thenAccept(result -> {
                    String folderName = String.format("%d", toDoId);
                    File folder = new File(getFilesDir(), folderName);
                    if (!folder.exists()) {
                        folder.mkdirs();
                    }

                    for (Uri fileUri : toDoEditViewModel.fileUris) {
                        String pathDestination = getFilesDir().getAbsolutePath() + "/" + folderName + "/" + fileUri.getLastPathSegment();
                        String pattern = "primary:[^/]+/";
                        pathDestination = pathDestination.replaceAll(pattern, "");

                        CompletableFuture<Void> futureAddAttachment = toDoEditViewModel.getAddAttachment().execute(new Attachment(Math.toIntExact(toDoId), pathDestination));
                        String finalPathDestination = pathDestination;
                        futureAddAttachment.thenAccept(result1 -> {
                            try {
                                copy(fileUri, finalPathDestination);
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

                    setResult(RESULT_OK);
                    finish();
                });
            });
        }
    }

    private void refreshAttachmentList() {
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
        if (destination.exists()) {
            Log.d("ToToEditActivity", String.format("File %s already exists", destination.toPath()));
            return;
        }
        destination.createNewFile();

        try (InputStream in = getContentResolver().openInputStream(source); OutputStream out = Files.newOutputStream(destination.toPath())) {
            byte[] buffer = new byte[1024];
            int len;
            while ((len = in.read(buffer)) != -1) {
                out.write(buffer, 0, len);
            }
        }
    }
}
