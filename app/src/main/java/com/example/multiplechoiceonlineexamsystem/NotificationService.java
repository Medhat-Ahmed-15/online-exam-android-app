package com.example.multiplechoiceonlineexamsystem;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

public class NotificationService extends Service {

    @Nullable
    @Override
    /**
     IBinder: To create a bound service, you must define the interface that
     specifies how a client can communicate with the service.
     This interface between the service and a client must be
     an implementation of IBinder and is what your service must return from the onBind() callback method**/

    /**
     To provide binding for a service, you must implement the onBind() callback method.
     This method returns an IBinder object that defines the programming interface that
     clients can use to interact with the service.**/

    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private void startScoreService() {


        String channelId = "score_notification_channnel";
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Intent resultIntent = new Intent();

        /**
         A PendingIntent is a token that you give to a foreign application (e.g. NotificationManager, AlarmManager, Home Screen AppWidgetManager,
         or other 3rd party applications), which allows the foreign application to use your
         application's permissions to execute a predefined piece of code.
         If you give the foreign application an Intent, it will execute your
         Intent with its own permissions. But if you give the foreign application a PendingIntent,
         that application will execute your Intent using your application's permission.**/
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);


        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), channelId);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setContentTitle("Online Exam");
        builder.setDefaults(NotificationCompat.DEFAULT_ALL);//Set the default notification options that will be used
        builder.setContentText("your exam has been checked📑📑📑");
        builder.setContentIntent(pendingIntent);//Setting this flag will make it so the notification is automatically canceled when the user clicks it in the panel. The PendingIntent set with setDeleteIntent will be broadcast when the notification is canceled.
        builder.setAutoCancel(false);
        builder.setPriority(NotificationCompat.PRIORITY_MAX);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (notificationManager != null
                    && notificationManager.getNotificationChannel(channelId) == null) {
                NotificationChannel notificationChannel = new NotificationChannel(
                        channelId, "Score Service",
                        NotificationManager.IMPORTANCE_HIGH
                );
                notificationChannel.setDescription("This channel is used by Score service");
                notificationManager.createNotificationChannel(notificationChannel);
            }
        }


        startForeground(Constants.SCORE_SERVICE_ID,builder.build());
    }

    private void stopScoreService()
    {
        stopForeground(true);
        stopSelf();//Stop the service, if it was previously started. This is the same as calling


    }




    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        if(intent!=null)
        {
            String action=intent.getAction();
            if(action!=null)
            {
                if(action.equals(Constants.ACTION_START_NOTIFICATION_SERVICE))
                {
                    startScoreService();
                }

                else if(action.equals(Constants.ACTION_STOP_NOTIFICATION_SERVICE))
                {
                    stopScoreService();
                }

            }
        }



        return super.onStartCommand(intent, flags, startId);
    }
}

