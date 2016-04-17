package gopackdev.arrivalpack.bluemixbean;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Date;
import java.sql.Time;


/**
 * Created by shashwath on 4/16/16.
 */
public class flightinfobean {
    String id;
    String flightNum;
    String departureDate;
    String arrivalTime;

    public flightinfobean(JSONObject flightJSON){

        try {
            this.flightNum = flightJSON.getString("flightNum");
            this.departureDate = flightJSON.getString("departureDate");
            this.arrivalTime = flightJSON.getString("arrivalTime");
        } catch (JSONException e){
            Log.e("flightinfobean","Error handling JSONObject constructor "+e.getLocalizedMessage());
        }
    }

    public String JSONFormat(){

        JSONObject json = new JSONObject();

        try{
            json.put("flightNum",this.flightNum);
            json.put("departurDate",this.departureDate);
            json.put("arrivalTime",this.arrivalTime);
        } catch (JSONException e){
            Log.e("Studentbean","Error handling JSONForamtfunction: "+e.getLocalizedMessage());
        }
        return json.toString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
