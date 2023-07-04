package com.zireaell1.todolist.presentation.todoadd;

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
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.chip.Chip;
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

public class ToDoAddActivity extends AppCompatActivity {
    private ToDoAddViewModel toDoAddViewModel;
    private AttachmentListAdapter attachmentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do_add);

        toDoAddViewModel = new ViewModelProvider(this, new ToDoAddViewModelFactory(this)).get(ToDoAddViewModel.class);

        toDoAddViewModel.chooseFileLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        Uri contentUri = data.getData();
                        toDoAddViewModel.fileUris.add(contentUri);
                        refreshAttachmentList();
                    }
                }
        );

        toDoAddViewModel.saveFileLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        try {
                            try (InputStream in = getContentResolver().openInputStream(toDoAddViewModel.fileToSave); OutputStream out = getContentResolver().openOutputStream(result.getData().getData())) {
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

        Spinner categoryDropdown = findViewById(R.id.category_dropdown);
        SpinnerCategoryAdapter categoryAdapter = new SpinnerCategoryAdapter(this, android.R.layout.simple_spinner_dropdown_item, new ArrayList<>());
        categoryDropdown.setAdapter(categoryAdapter);
        categoryDropdown.setSelection(toDoAddViewModel.categoryDropdownSelectedItemId);
        categoryDropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Category selectedCategory = categoryAdapter.getItem(position);
                toDoAddViewModel.categoryId = selectedCategory.getId();
                toDoAddViewModel.categoryDropdownSelectedItemId = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        toDoAddViewModel.getCategoriesState().observe(this, categoriesState -> {
            categoryAdapter.items.clear();
            categoryAdapter.items.addAll(categoriesState);
            categoryAdapter.notifyDataSetChanged();
        });

        toDoAddViewModel.loadCategories(this);

        Chip doneButton = findViewById(R.id.done_button);
        doneButton.setChecked(toDoAddViewModel.stateChipIsChecked);
        doneButton.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                toDoAddViewModel.state = ToDoState.DONE;
                toDoAddViewModel.stateChipIsChecked = true;
            } else {
                toDoAddViewModel.state = ToDoState.IN_PROGRESS;
                toDoAddViewModel.stateChipIsChecked = false;
            }
        });

        Chip notificationsButton = findViewById(R.id.notifications_button);
        notificationsButton.setChecked(toDoAddViewModel.notificationsChipIsChecked);
        notificationsButton.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                toDoAddViewModel.notifications = true;
                toDoAddViewModel.notificationsChipIsChecked = true;
            } else {
                toDoAddViewModel.notifications = false;
                toDoAddViewModel.notificationsChipIsChecked = false;
            }
        });

        Button timeButton = findViewById(R.id.time_button);
        timeButton.setOnClickListener(v -> {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
            LocalDateTime dateTime = LocalDateTime.of(toDoAddViewModel.year, toDoAddViewModel.month, toDoAddViewModel.dayOfMonth, toDoAddViewModel.hourOfDay, toDoAddViewModel.minute);
            String formattedDateTime = dateTime.format(formatter);
            DialogFragment dateFragment = new AddDatePickerFragment(toDoAddViewModel, () -> timeButton.setText(formattedDateTime));
            dateFragment.show(getSupportFragmentManager(), "datePicker");
        });

        if (toDoAddViewModel.isTimeSet) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
            LocalDateTime dateTime = LocalDateTime.of(toDoAddViewModel.year, toDoAddViewModel.month, toDoAddViewModel.dayOfMonth, toDoAddViewModel.hourOfDay, toDoAddViewModel.minute);
            String formattedDateTime = dateTime.format(formatter);
            timeButton.setText(formattedDateTime);
        }

        ImageButton closeButton = findViewById(R.id.close_button);
        closeButton.setOnClickListener(v -> finish());

        Button attachmentsButton = findViewById(R.id.attachments);
        attachmentsButton.setOnClickListener(v -> {
            Intent chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
            chooseFile.addCategory(Intent.CATEGORY_OPENABLE);
            chooseFile.setType("*/*");
            chooseFile = Intent.createChooser(chooseFile, "Choose a file");
            toDoAddViewModel.chooseFileLauncher.launch(chooseFile);
        });

        attachmentAdapter = new AttachmentListAdapter(this, R.layout.attachment_list_item, toDoAddViewModel.fileUris, uri -> refreshAttachmentList(), fileUri -> {
            Intent saveFile = new Intent(Intent.ACTION_CREATE_DOCUMENT);
            saveFile.addCategory(Intent.CATEGORY_OPENABLE);
            saveFile.setType(Utils.getContent(this, fileUri, MediaStore.MediaColumns.MIME_TYPE));
            saveFile.putExtra(Intent.EXTRA_TITLE, Utils.getContent(this, fileUri, MediaStore.MediaColumns.DISPLAY_NAME));
            saveFile = Intent.createChooser(saveFile, "Save a file");

            toDoAddViewModel.fileToSave = fileUri;
            toDoAddViewModel.saveFileLauncher.launch(saveFile);
        });
        refreshAttachmentList();

        FloatingActionButton confirmButton = findViewById(R.id.confirm_button);
        confirmButton.setOnClickListener(v -> {
            String title = titleTextView.getEditText().getText().toString();
            String description = descriptionTextView.getEditText().getText().toString();
            LocalDateTime createDate = LocalDateTime.now();
            LocalDateTime completionDate = LocalDateTime.of(toDoAddViewModel.year, toDoAddViewModel.month, toDoAddViewModel.dayOfMonth, toDoAddViewModel.hourOfDay, toDoAddViewModel.minute);
            ToDoState state = toDoAddViewModel.state;
            boolean notifications = toDoAddViewModel.notifications;
            int categoryId = toDoAddViewModel.categoryId;

            ToDo toDo = new ToDo(title, description, createDate, completionDate, state, notifications, categoryId);

            CompletableFuture<Long> futureAddToDo = toDoAddViewModel.getAddToDo().execute(toDo);
            futureAddToDo.thenAccept(toDoId -> {
                String folderName = String.format(Locale.getDefault(), "%d", toDoId);
                File folder = new File(getFilesDir(), folderName);
                if (!folder.exists()) {
                    folder.mkdirs();
                }

                for (Uri fileUri : toDoAddViewModel.fileUris) {
                    String pathDestination = getFilesDir().getAbsolutePath() + "/" + folderName + "/" + Utils.getContent(this, fileUri, MediaStore.MediaColumns.DISPLAY_NAME);
                    File fileCheck = new File(pathDestination);
                    int count = 1;
                    String fileName = fileCheck.getName();
                    String extension = "";
                    int dotIndex = fileName.lastIndexOf(".");
                    if (dotIndex != -1) {
                        extension = fileName.substring(dotIndex);
                        fileName = fileName.substring(0, dotIndex);
                    }

                    String tmpPathDestination = pathDestination;
                    while (fileCheck.exists()) {
                        String suffix = String.format(Locale.getDefault(), " (%d)", count);
                        tmpPathDestination = pathDestination.replace(fileName + extension, fileName + suffix + extension);
                        fileCheck = new File(tmpPathDestination);
                        count = count + 1;
                    }
                    pathDestination = tmpPathDestination;
                    Log.d("ToDoAddActivity", String.format("Saving file %s", pathDestination));

                    CompletableFuture<Void> futureAddAttachment = toDoAddViewModel.getAddAttachment().execute(new Attachment(Math.toIntExact(toDoId), pathDestination));
                    String finalPathDestination = pathDestination;
                    futureAddAttachment.thenAccept(result -> {
                        try {
                            copy(fileUri, finalPathDestination);
                        } catch (IOException e) {
                            Log.e("ToDoAddActivity", String.format("Error copying file %s", Utils.getContent(this, fileUri, MediaStore.MediaColumns.DISPLAY_NAME)), e);
                        }
                    });
                    futureAddAttachment.join();
                }

                runOnUiThread(() -> {
                    AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                    long timeDifference = Duration.between(LocalDateTime.now(), completionDate).toMillis();

                    if (timeDifference >= (long) toDoAddViewModel.getConfig().execute().getNotificationsReminderTime() * 60 * 1000 && notifications) {
                        Intent intent = new Intent(this, AlarmReceiver.class);
                        int toDoIdInt = Math.toIntExact(toDoId);
                        intent.putExtra("toDoId", toDoIdInt);
                        intent.putExtra("title", title);
                        intent.putExtra("description", description);

                        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), toDoIdInt, intent, PendingIntent.FLAG_IMMUTABLE);

                        long alarmTime = LocalDateTime.now().atZone(ZoneOffset.systemDefault()).toInstant().toEpochMilli() + timeDifference - (long) toDoAddViewModel.getConfig().execute().getNotificationsReminderTime() * 60 * 1000;
                        alarmManager.setExact(AlarmManager.RTC_WAKEUP, alarmTime, pendingIntent);
                    }
                });

                setResult(RESULT_OK);
                finish();
            });
        });
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
