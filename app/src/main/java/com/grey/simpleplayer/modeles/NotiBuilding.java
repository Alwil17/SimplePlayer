package com.grey.simpleplayer.modeles;

import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v4.media.MediaDescriptionCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.MediaSessionCompat;

import androidx.core.app.NotificationCompat;

import com.grey.simpleplayer.services.MusicService;

public class NotiBuilding {

    public static final String NOTIFICATION_ID = "simpleplayer";

    public static PendingIntent getActionIntent(Context context, String action){
        ComponentName component = new ComponentName(context, MusicService.class);
        Intent intent = new Intent(action);
        intent.setComponent(component);
        return PendingIntent.getService(context, 0, intent, 0);
    }

    public static NotificationCompat.Builder from(Context context, MediaSessionCompat sessionCompat){
        MediaControllerCompat controllerCompat = sessionCompat.getController();
        MediaMetadataCompat metadataCompat = controllerCompat.getMetadata();
        MediaDescriptionCompat descriptionCompat = metadataCompat.getDescription();

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, NOTIFICATION_ID);
        builder.setContentTitle(descriptionCompat.getTitle())
                .setContentText(descriptionCompat.getSubtitle())
                .setSubText(descriptionCompat.getDescription())
                .setLargeIcon(descriptionCompat.getIconBitmap())
                .setContentIntent(controllerCompat.getSessionActivity())
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC);
        return builder;
    }
}
