package mthomson.coneath;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.db.chart.model.BarSet;
import com.db.chart.model.LineSet;
import com.db.chart.view.BarChartView;
import com.db.chart.view.ChartView;
import com.db.chart.view.LineChartView;

import java.util.Arrays;

import mthomson.coneath.background.Alarm;
import mthomson.coneath.storage.PingData;
import mthomson.coneath.storage.PingDataConnector;

public class MainActivity extends AppCompatActivity {
    private AppCompatActivity getActivity() {
        return this;
    }
    AlarmManager alarmManager;
    private PendingIntent pendingIntent;

    private BarChartView mChart;
    private BarSet mLine;

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
        mChart = (BarChartView) findViewById(R.id.barchart);
        mLine = new BarSet(new String[]{"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"}, new float[]{0,1,0,2,0,5,0});
        int ONE_DAY = 86400;

        mDataConnection.getPings(ONE_DAY);




        mChart.addData(mLine);
        mChart.show();
    }


    private void setAlarm(){
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, SystemClock.elapsedRealtime()+AlarmManager.INTERVAL_FIFTEEN_MINUTES, AlarmManager.INTERVAL_FIFTEEN_MINUTES, pendingIntent);
    }
    private void cancelAlarm() {
        if (alarmManager!= null) {
            alarmManager.cancel(pendingIntent);
        }
    }

    private PingDataConnector mDataConnection = new PingDataConnector(this);

}
