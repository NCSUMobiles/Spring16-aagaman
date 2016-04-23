package gopackdev.arrivalpack.bluemixbean;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by vamsivikash on 4/19/2016.
 */
public class StudentMatches {

    private String flightNum;
    private String departureDate;
    private int arrivalHour;
    private int arrivalMin;
    private String StudentId;

    public StudentMatches(String Id, String flightNum, String departureDate, int arrivalHour, int arrivalMin){
        this.flightNum = flightNum;
        this.departureDate = departureDate;
        this.arrivalHour = arrivalHour;
        this.arrivalMin = arrivalMin;
        this.StudentId = Id;
        Log.i("Flight Details Entered","Airport");
    }

    public void setStudentId(String studentId){
        this.StudentId = studentId;
    }

    public String getStudentId() {
        return StudentId;
    }

    public String getFlightNum(){
        return this.flightNum;
    }

    public void setFlightNum(String flightnum){
        this.flightNum = flightnum;
    }

    public String getDepartureDate(){
        return this.departureDate;
    }

    public void setDepartureDate(String dat){
        this.departureDate = dat;
    }

    public int getArrivalHour(){
        return this.arrivalHour;
    }

    public void setArrivalHour(int hour){
        this.arrivalHour = hour;
    }

    public int getArrivalMin(){
        return this.arrivalHour;
    }

    public void setArrivalMin(int min){
        this.arrivalMin = min;
    }

    public String JSONFormat(){

        JSONObject json = new JSONObject();

        try{
            json.put("flightNum",this.flightNum);
            json.put("departurDate",this.departureDate);
            json.put("arrHour",this.arrivalHour);
            json.put("arrMin",this.arrivalMin);
        } catch (JSONException e){
            Log.e("Studentbean","Error handling JSONForamtfunction: "+e.getLocalizedMessage());
        }
        return json.toString();
    }
}
