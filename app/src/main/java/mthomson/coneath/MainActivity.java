package mthomson.coneath;
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
import java.util.ArrayList;
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
        graph.addSeries(mSeries);
        graph.getViewport().setXAxisBoundsManual(true);
        for (PingData ping : data_connection.getPing(new java.sql.Date(new java.util.Date().getTime()-300000))) {
            mSeries.appendData(new DataPoint((double) ping.Timestamp.getTime(), ping.PingValue), true, 10);
        }

        final Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // kick off service to ping
                Intent serviceIntent = new Intent(getActivity(), Service.class);
                serviceIntent.putExtra("messenger", new Messenger(pingHandler));
                getActivity().startService(serviceIntent);
            }
        });

    }
    private PingDataConnector data_connection = new PingDataConnector(this, null);

    private LineGraphSeries<DataPoint> mSeries = new LineGraphSeries<>();

    public Handler pingHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Bundle ping_bundle = msg.getData().getBundle("PingData");
            PingData ping = new PingData();
            ping.Timestamp = new java.sql.Date(ping_bundle.getLong("Timestamp"));
            ping.PingValue = ping_bundle.getDouble("PingValue");
            mSeries.appendData(new DataPoint((double) ping.Timestamp.getTime(), ping.PingValue), true, 10);
            System.out.print("Added a point");
        }
    };
}
