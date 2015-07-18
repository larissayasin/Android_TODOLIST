package com.app.larissag.epiclist.Application;


import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class EpicListApplication extends Application {

    private static int USER_LEVEL_INITIAL = 1;
    private static String LEVEL = "Nivel";
    private static int USER_PROGRESS_INITIAL = 0;
    private static String PROGRESS = "Progresso";

    @Override
    public void onCreate()
    {
        super.onCreate();

        // Initialize the singletons so their instances
        // are bound to the application process.
        changeUserLevel(USER_LEVEL_INITIAL);
        changeUserProgress(USER_PROGRESS_INITIAL);
    }


    public void changeUserLevel(int level){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(LEVEL, level);
        editor.apply();
    }

    public int getUserLevel(){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        int level = preferences.getInt(LEVEL, 0);
       return  level;
    }
    public void changeUserProgress(int progress){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(PROGRESS, progress);
        editor.apply();
    }

    public int getUserProgress(){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        int progress = preferences.getInt(PROGRESS, 0);
        return  progress;
    }

}
