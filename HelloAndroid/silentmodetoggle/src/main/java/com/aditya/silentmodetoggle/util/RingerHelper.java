package com.aditya.silentmodetoggle.util;

import android.media.AudioManager;

public class RingerHelper {
    private RingerHelper() {}

    public static void performToggle(AudioManager audioManager){
        audioManager.setRingerMode(
                isPhoneSilent(audioManager)?AudioManager.RINGER_MODE_NORMAL:AudioManager.RINGER_MODE_SILENT);
    }

    public static boolean isPhoneSilent(AudioManager audioManager){
        return audioManager.getRingerMode() == AudioManager.RINGER_MODE_SILENT;
    }
}
