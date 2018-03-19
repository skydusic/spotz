package addup.fpcompany.com.addsup.fcm;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.RemoteMessage;

import addup.fpcompany.com.addsup.MainActivity;
import addup.fpcompany.com.addsup.R;

/**
 * Created by ahn on 2017-05-01.
 */

public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {
    private static final String TAG = "FirebaseMsgService";

    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        String messageBody = "";
        String messageTitle = "";
        if (remoteMessage.getData().size() > 0) {
            Log.d("heu", "FCM Data Message : " + remoteMessage.getData());
        }

        if (remoteMessage.getNotification() != null) {
            messageBody = remoteMessage.getNotification().getBody();
            messageTitle = remoteMessage.getNotification().getTitle();
            Log.d("heu", "FCM Notification Message Body : " + messageBody);
        }

        Log.d("heu", messageBody);
        sendPushNotification(messageBody, messageTitle);
    }

    private void sendPushNotification(String message, String title) {
        Log.d("heu","received message : " + message);
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        /** 기본 노티피케이션 효과음 **/
        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        /** 노티피케이션 세팅 하나하나 없애면서 체크해보자**/
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher_round).setLargeIcon(BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher) )
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)                                    // 모두 지우기 가능
                .setSound(defaultSoundUri).setLights(000000255,500,2000) // 알림 왔을 때 소리, 빛 깜빡임 보여주는 거
                .setContentIntent(pendingIntent); // 클릭 했을 때 이동!

        Log.d("heu", "title : " + title + "\nMsg : " + message);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        /** 핸드폰이 꺼져있다면 화면을 깨움 **/
        PowerManager pm = (PowerManager) this.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wakelock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "TAG");
        wakelock.acquire(5000);
        // 알림 왔을 때 어느 시간만큼 핸드폰 화면을 밝혀줄지

        /** 노티피케이션 실행 **/
        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }

}