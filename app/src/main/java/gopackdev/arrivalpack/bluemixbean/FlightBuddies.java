package gopackdev.arrivalpack.bluemixbean;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by vamsivikash on 4/19/2016.
 */
public class FlightBuddies {

    String Student_ID;
    String Flight_Number;
    String Date;

    public FlightBuddies(String flight_Number, String date){
        this.Flight_Number = flight_Number;
        this.Date = date;
    }
    public FlightBuddies(String id, String flight_Number, String date){
        this.Student_ID = id;
        this.Flight_Number = flight_Number;
        this.Date = date;
    }

    public String getFlight_ID() {
        return Flight_Number;
    }

    public void setFlight_ID(String flight_ID) {
        Flight_Number = flight_ID;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getStudent_ID() {
        return Student_ID;
    }

    public void setStudent_ID(String student_ID) {
        Student_ID = student_ID;
    }

    public String JSONFormat(){

        JSONObject json = new JSONObject();

        try{
            json.put("student_id",this.getStudent_ID());
            json.put("flight_number",this.getFlight_ID());
            json.put("flight_date",this.getDate());
        } catch (JSONException e){
            Log.e("Studentbean", "Error handling JSONFormatfunction: " + e.getLocalizedMessage());
        }
        return json.toString();
    }
}
