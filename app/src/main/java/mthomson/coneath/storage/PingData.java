package mthomson.coneath.storage;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PingData {
    public long Timestamp;
    public int PingValue;

    public String toString() {
        try{
            DateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
            Date netDate = (new Date(Timestamp*1000L));
            return String.format("%s, %d", sdf.format(netDate), PingValue);
        }
        catch(Exception ex){
            return String.format("%d, %d", Timestamp, PingValue);
        }
    }
}
