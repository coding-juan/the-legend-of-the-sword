package com.codingjuan.manager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class ConfigurationManager {
    private static Preferences preferences = Gdx.app.getPreferences("legend-of-the-sword");

    public static boolean isSoundEnabled() {
        return preferences.getBoolean("soundEnabled", true);
    }

    public static void setSoundEnabled(boolean enabled) {
        preferences.putBoolean("soundEnabled", enabled);
    }

    public static float getSoundVolume() {
        if (isSoundEnabled())
            return preferences.getFloat("soundVolume" , 100f);
        else
            return 0;
    }

    public static void setSoundVolume(float volume) {
        preferences.putFloat("soundVolume", volume);
    }

    public static boolean isDebugEnabled() {
        return preferences.getBoolean("debug", false);
    }

    public static void setDebugEnabled(boolean fullscreen) {
        preferences.putBoolean("debug", fullscreen);
    }

    public static void savePreferences() {
        preferences.flush();
    }
}
