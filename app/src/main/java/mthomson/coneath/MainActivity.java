package mthomson.coneath;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        graph = (GraphView) findViewById(R.id.graph);
        graph.getViewport().setMaxX(20.0);
        graph.getViewport().setXAxisBoundsManual(true);
        update_graph();

        final Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                update_graph();
            }
        });

    }
    private GraphView graph;

    private ArrayList<DataPoint> dataPoints = new ArrayList<>();
    private double datapoint_num = 0.0;

    private void update_graph() {
        dataPoints.add(new DataPoint(datapoint_num, ping()));
        datapoint_num+=1.0;
        graph.removeAllSeries();
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(dataPoints.toArray(new DataPoint[dataPoints.size()]));
        graph.addSeries(series);
        graph.getViewport().scrollToEnd();
    }


}
