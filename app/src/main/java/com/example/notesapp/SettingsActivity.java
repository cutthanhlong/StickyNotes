package com.example.notesapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import com.example.notesapp.DataBase.RoomDB;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.color.DynamicColors;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.elevation.SurfaceColors;
import com.google.android.material.materialswitch.MaterialSwitch;

import java.io.File;
import java.util.Objects;

/**
 * @author 30415
 */
public class SettingsActivity extends AppCompatActivity {
    MaterialButton deleteAll;
    MaterialSwitch materialSwitch;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void finish() {
        setResult(RESULT_OK);
        super.finish();
        overridePendingTransition(R.anim.stayout, R.anim.out);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DynamicColors.applyToActivityIfAvailable(this);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getWindow().setStatusBarColor(SurfaceColors.SURFACE_2.getColor(this));
        setContentView(R.layout.activity_settings);
        SharedPreferences sharedPreferences = getSharedPreferences("login", MODE_PRIVATE);


        materialSwitch = findViewById(R.id.materialSwitch);
        deleteAll = findViewById(R.id.materialButton2);

        materialSwitch.setChecked(sharedPreferences.getBoolean("usePassword", false));
        materialSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("usePassword", isChecked);
            editor.apply();
        });

        deleteAll.setOnClickListener(v -> new MaterialAlertDialogBuilder(this)
                .setTitle(getString(R.string.deleteAll))
                .setPositiveButton(getString(R.string.confirm),
                        (dialog, which) -> {
                            RoomDB.getInstance(SettingsActivity.this).clearAllTables();
                            File dir = getFilesDir();
                            deleteRecursive(dir);
                        })
                .setNegativeButton(getString(R.string.cancel), null)
                .setCancelable(false)
                .show());

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                finish();
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);
    }

    void deleteRecursive(File fileOrDirectory) {
        if (fileOrDirectory.exists() && fileOrDirectory.isDirectory()) {
            for (File child : Objects.requireNonNull(fileOrDirectory.listFiles())) {
                deleteRecursive(child);
            }
        }
        fileOrDirectory.delete();
    }
}
