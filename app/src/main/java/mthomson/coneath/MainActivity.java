package mthomson.coneath;
import android.app.AlarmManager;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import mthomson.coneath.background.Alarm;
import mthomson.coneath.background.Service;
import mthomson.coneath.storage.PingData;
import mthomson.coneath.storage.PingDataConnector;

public class MainActivity extends AppCompatActivity {
    private AppCompatActivity getActivity() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        GraphView graph = (GraphView) findViewById(R.id.graph);
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinX(0);
        graph.getViewport().setMaxX(0.5);
        mSeries.setDrawDataPoints(true);
        mSeries.setDrawBackground(true);
        graph.addSeries(mSeries);
        for (PingData ping : data_connection.getPing(new java.sql.Date(new java.util.Date().getTime()-300000))) {
            mSeries.appendData(new DataPoint((double) ping.Timestamp.getTime()/60000.0, ping.PingValue), true, 50);
        }
        Alarm alarm = new Alarm();
        alarm.setAlarm(this);
    }

    private PingDataConnector data_connection = new PingDataConnector(this, null);

    private LineGraphSeries<DataPoint> mSeries = new LineGraphSeries<>();
}
