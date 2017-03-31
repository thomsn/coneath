package mthomson.coneath.background;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import mthomson.coneath.storage.PingData;
import mthomson.coneath.storage.PingDataConnector;

public class Service extends IntentService {
    public Service() {super("Service");}
    public Service(String name) {super(name);}


    private PingDataConnector storage_connection = new PingDataConnector(this, null);
    @Override
    protected void onHandleIntent(Intent intent) {
        PingData pingData = PingDataConnector.makePing(ping());
        storage_connection.savePing(pingData);

        if (intent.hasExtra("messenger")) { // if its from the main thread.
            Bundle bundle = intent.getExtras();
            Messenger return_messenger = (Messenger) bundle.get("messenger");
            update_ui(pingData, return_messenger);
        }
    }

    private void update_ui(PingData ping_data, Messenger return_messenger) {
        Message message = new Message();
        Bundle bundle = new Bundle();
        bundle.putLong("Timestamp",ping_data.Timestamp.getTime());
        bundle.putDouble("PingValue", ping_data.PingValue);
        message.getData().putBundle("PingData",bundle);
        try {
            return_messenger.send(message);
        } catch (RemoteException e){
            e.printStackTrace();
        }
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
