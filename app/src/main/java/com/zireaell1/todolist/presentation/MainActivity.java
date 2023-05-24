package com.zireaell1.todolist.presentation;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.zireaell1.todolist.R;
import com.zireaell1.todolist.presentation.todolist.ToDoListFragment;
import com.zireaell1.todolist.presentation.settings.SettingsFragment;

public class MainActivity extends AppCompatActivity {
    private final static String NOTIFICATION_CHANNEL_ID = "1";
    private MainActivityViewModel mainActivityViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainActivityViewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);

        createNotificationChannel();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_view);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            int itemId = item.getItemId();
            if (itemId == R.id.action_todo_list) {
                selectedFragment = ToDoListFragment.newInstance();
                mainActivityViewModel.setFragment(0);
            } else if (itemId == R.id.action_settings) {
                selectedFragment = SettingsFragment.newInstance();
                mainActivityViewModel.setFragment(1);
            }

            if (selectedFragment != null) {
                getSupportFragmentManager().beginTransaction().replace(R.id.main_container, selectedFragment).commit();
            }

            return true;
        });

        if (mainActivityViewModel.getFragment() == 0) {
            getSupportFragmentManager().beginTransaction().replace(R.id.main_container, ToDoListFragment.newInstance()).commit();
        }
        if (mainActivityViewModel.getFragment() == 1) {
            getSupportFragmentManager().beginTransaction().replace(R.id.main_container, SettingsFragment.newInstance()).commit();
        }
    }

    private void createNotificationChannel() {
        CharSequence name = getString(R.string.notification_channel_name);
        String description = getString(R.string.notification_channel_description);
        int importance = NotificationManager.IMPORTANCE_HIGH;
        NotificationChannel channel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, name, importance);
        channel.setDescription(description);
        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
    }
}
