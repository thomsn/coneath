package mthomson.coneath.background;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

public class Alarm extends WakefulBroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(this.getClass().getName(), "Received Alarm");
        Intent service = new Intent(context, Service.class);
        startWakefulService(context, service);
    }
}
