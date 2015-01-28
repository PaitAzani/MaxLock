package de.Maxr1998.xposed.maxlock.ui;

import android.annotation.SuppressLint;
import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.util.Log;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import de.Maxr1998.xposed.maxlock.Common;
import de.Maxr1998.xposed.maxlock.Util;

public class ThemeService extends IntentService {

    public final String themeOrigFile = "theme.xml";
    public final String backgroundOrigFile = "background.png";
    File themeFile;
    SharedPreferences prefs;


    public ThemeService() {
        super("ThemeService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d("MaxLock/ThemeService", "Intent received");
        themeFile = new File(Util.dataDir(this) + File.separator + "shared_prefs" + File.separator + themeOrigFile);

        int extra = intent.getIntExtra("extra", -1);
        switch (extra) {
            case 1:
                importTheme(intent.getStringExtra("package"));
                break;
            case 2:
                clearUp();
                break;
        }
        stopSelf();
    }

    @SuppressLint("InlinedApi")
    public void importTheme(String packageName) {
        prefs = getSharedPreferences(Common.PREFS, MODE_PRIVATE);
        AssetManager assets;
        try {
            assets = getPackageManager().getResourcesForApplication(packageName).getAssets();
            // theme.xml file
            InputStream themeStream = assets.open(themeOrigFile);
            FileUtils.copyInputStreamToFile(themeStream, themeFile);
            if (themeFile.length() < 10) {
                Toast.makeText(this, "No theme.xml found, exiting...", Toast.LENGTH_SHORT).show();
                //noinspection ResultOfMethodCallIgnored
                themeFile.delete();
                return;
            }
            if (Util.noGingerbread())
                getSharedPreferences("theme", MODE_MULTI_PROCESS);
            // background.png
            InputStream backgroundStream = assets.open(backgroundOrigFile);
            File backgroundFile = new File(Util.dataDir(this) + File.separator + "theme" + File.separator + backgroundOrigFile);
            FileUtils.copyInputStreamToFile(backgroundStream, backgroundFile);
            if (themeFile.length() < 10)
                //noinspection ResultOfMethodCallIgnored
                backgroundFile.delete();
            else {
                prefs.edit().putString(Common.BACKGROUND, "theme").apply();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("InlinedApi")
    public void clearUp() {
        //noinspection ResultOfMethodCallIgnored
        themeFile.delete();
        if (Util.noGingerbread())
            getSharedPreferences("theme", MODE_MULTI_PROCESS);
        File theme = new File(Util.dataDir(this) + File.separator + "theme");
        try {
            FileUtils.deleteDirectory(theme);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}