package mthomson.coneath.background;

import android.app.IntentService;
import android.content.Intent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import mthomson.coneath.storage.PingDataConnector;

public class Service extends IntentService {

    PingDataConnector storage_connection;

    public Service(String name) {
        super(name);
        storage_connection = new PingDataConnector(this, null);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        storage_connection.savePing(ping());
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
