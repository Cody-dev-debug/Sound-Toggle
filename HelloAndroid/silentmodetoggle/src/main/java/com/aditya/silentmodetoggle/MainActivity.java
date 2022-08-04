package com.aditya.silentmodetoggle;

import static androidx.constraintlayout.motion.utils.Oscillator.TAG;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.aditya.silentmodetoggle.util.RingerHelper;

public class MainActivity extends AppCompatActivity {
    String TAG="Testing";
    AudioManager mAudioManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAudioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        FrameLayout contentView = (FrameLayout) findViewById(R.id.content);
        contentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RingerHelper.performToggle(mAudioManager);
                updateUi();
            }
        });
    }

    private void updateUi() {
        ImageView imageView = (ImageView) findViewById(R.id.phone_icon);
        requestMutePermissions();
        int phoneImage=RingerHelper.isPhoneSilent(mAudioManager)?R.drawable.ringer_off:R.drawable.ringer_on;
        Log.d(TAG, String.valueOf(RingerHelper.isPhoneSilent(mAudioManager)));
        imageView.setImageResource(phoneImage);
    }
    private void requestMutePermissions() {
        try {
            if (Build.VERSION.SDK_INT < 23) {
                return;
            } else if( Build.VERSION.SDK_INT >= 23 ) {
                this.requestForDoNotDisturbPermissionOrSetDoNotDisturbForApi23AndUp();
            }
        } catch ( SecurityException e ) {

        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void requestForDoNotDisturbPermissionOrSetDoNotDisturbForApi23AndUp() {

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        // if user granted access else ask for permission
        if (notificationManager.isNotificationPolicyAccessGranted()) {
            return;
        } else {
            // Open Setting screen to ask for permisssion
            Intent intent = new Intent(android.provider.Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);
            startActivity(intent);
//
//            ActivityResultLauncher<String> sActivity = registerForActivityResult(new ActivityResultContract() {
//                @NonNull
//                @Override
//                public Intent createIntent(@NonNull Context context, Object input) {
//                    Intent intent = new Intent(android.provider.Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);
//                    return intent;
//                }
//
//                @Override
//                public Object parseResult(int resultCode, @Nullable Intent intent) {
//                    return null;
//                }
//            }, new ActivityResultCallback<Uri>() {
//                @Override
//                public void onActivityResult(Uri result) {
//                    requestForDoNotDisturbPermissionOrSetDoNotDisturbForApi23AndUp();
//                }
//            });
        }
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        // Check which request we're responding to
//        if (requestCode == ON_DO_NOT_DISTURB_CALLBACK_CODE ) {
//            this.requestForDoNotDisturbPermissionOrSetDoNotDisturbForApi23AndUp();
//        }
//    }
    @Override
    protected void onResume() {
        super.onResume();
        updateUi();
    }
}