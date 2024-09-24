package it.faustobe.santibailor;

import android.app.Application;

import dagger.hilt.android.HiltAndroidApp;
import it.faustobe.santibailor.util.ImageManager;

@HiltAndroidApp
public class MyApplication extends Application {
    private ImageManager imageManager;

    @Override
    public void onCreate() {
        super.onCreate();
        imageManager = new ImageManager(this);
    }

    public ImageManager getImageManager() {
        return imageManager;
    }
}
