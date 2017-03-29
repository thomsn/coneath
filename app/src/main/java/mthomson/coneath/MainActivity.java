package mthomson.coneath;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import java.util.ArrayList;
import mthomson.coneath.background.Service;
import mthomson.coneath.storage.PingDataConnector;

public class MainActivity extends AppCompatActivity {
    // TODO: 2017-03-28 Need to make the UI update every-time the service adds a new point. http://stackoverflow.com/questions/14695537/android-update-activity-ui-from-service
    private AppCompatActivity getActivity() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        graph = (GraphView) findViewById(R.id.graph);
        update_graph();

        final Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // kick off service to ping
                Intent serviceIntent = new Intent(getActivity(), Service.class);
                getActivity().startService(serviceIntent);
            }
        });

    }
    private GraphView graph;
    private PingDataConnector data_connection = new PingDataConnector(this, null);

    private void update_graph() {
        ArrayList<DataPoint> dataPoints = new ArrayList<>();
        // TODO: 2017-03-28 Need to deal with dates.
        for (ping : data_connection.getPing(today);) {
            dataPoints.add(new DataPoint(ping.Timestamp, ping.Pingvalue));
        }
        graph.removeAllSeries();
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(dataPoints.toArray(new DataPoint[dataPoints.size()]));
        graph.addSeries(series);
        graph.getViewport().scrollToEnd();
    }
}
