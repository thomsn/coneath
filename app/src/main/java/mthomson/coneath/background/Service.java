package mthomson.coneath.background;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

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
        storage_connection.savePing((int)Ping.get());
    }
}
