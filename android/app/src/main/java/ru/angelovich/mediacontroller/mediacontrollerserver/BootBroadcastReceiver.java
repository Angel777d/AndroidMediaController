package ru.angelovich.mediacontroller.mediacontrollerserver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootBroadcastReceiver extends BroadcastReceiver {
    static final String ACTION = "android.intent.action.BOOT_COMPLETED";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (ACTION.equals(intent.getAction())) {
            Intent activity = new Intent(context, MainActivity.class);
            context.startActivity(activity);
        }
    }
}
