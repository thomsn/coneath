package mthomson.coneath;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    private double ping() {
        Runtime runtime = Runtime.getRuntime();
        try
        {
            Process  process = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int mExitValue = process.waitFor();
            BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));

            if(mExitValue==0){
                Pattern pattern = Pattern.compile("time=(\\d.+)*\\sms");
                Matcher m;
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    m = pattern.matcher(inputLine);
                    if (m.find()) {
                        return Double.parseDouble(m.group(1));
                    }
                }
            }else{
                BufferedReader error = new BufferedReader(new InputStreamReader(process.getErrorStream()));
                String error_str = error.readLine();
                System.out.println(" mExitValue "+ error_str);
                return -1.0;
            }
        }
        catch (InterruptedException | IOException ignore)
        {
            ignore.printStackTrace();
            System.out.println(" Exception:"+ignore);
        }
        return -1.0;
    }
}
