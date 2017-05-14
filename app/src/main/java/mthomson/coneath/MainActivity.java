package mthomson.coneath;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import mthomson.coneath.background.Alarm;
import mthomson.coneath.storage.PingData;
import mthomson.coneath.storage.PingDataConnector;

public class MainActivity extends AppCompatActivity {
    private AppCompatActivity getActivity() {
        return this;
    }
    AlarmManager alarmManager;
    private PendingIntent pendingIntent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupGraph();
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent myIntent = new Intent(MainActivity.this, Alarm.class);
        pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, myIntent, 0);
        setAlarm();


//        Intent dbmanager = new Intent(getActivity(), AndroidDatabaseManager.class);
//        startActivity(dbmanager);
    }

    private void setupGraph(){
        GraphView graph = (GraphView) findViewById(R.id.graph);
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinX(0);
        graph.getViewport().setMaxX(0.5);
        mSeries.setDrawDataPoints(true);
        mSeries.setDrawBackground(true);
        graph.addSeries(mSeries);
        Log.d(this.getClass().getName(), "Initial Points:");
        long day_ago = (System.currentTimeMillis() / 1000L)-3600*24;


        for (PingData ping : data_connection.getPings(3600*24)) {
            double time = (double)(ping.Timestamp - day_ago)/3600d;
            mSeries.appendData(new DataPoint(time, ping.PingValue), true, 50);
            Log.d(this.getClass().getName(), ping.toString());
        }
    }

    private void setAlarm(){
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, SystemClock.elapsedRealtime()+AlarmManager.INTERVAL_FIFTEEN_MINUTES, AlarmManager.INTERVAL_FIFTEEN_MINUTES, pendingIntent);
    }
    private void cancelAlarm() {
        if (alarmManager!= null) {
            alarmManager.cancel(pendingIntent);
        }
    }

    private PingDataConnector data_connection = new PingDataConnector(this, null);

    private LineGraphSeries<DataPoint> mSeries = new LineGraphSeries<>();
}
