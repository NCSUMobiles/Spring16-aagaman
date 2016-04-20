package gopackdev.arrivalpack.bluemixbean;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by vamsivikash on 4/19/2016.
 */
public class FlightBuddies {

    String Student_ID;
    String First_Name;
    String Last_Name;

    public FlightBuddies(String id, String first_Name, String last_Name){
        this.Student_ID = id;
        this.First_Name = first_Name;
        this.Last_Name = last_Name;
    }

    public String getStudent_ID() {
        return Student_ID;
    }

    public void setStudent_ID(String student_ID) {
        Student_ID = student_ID;
    }

    public String getFirst_Name() {
        return First_Name;
    }

    public void setFirst_Name(String first_Name) {
        First_Name = first_Name;
    }

    public String getLast_Name() {
        return Last_Name;
    }

    public void setLast_Name(String last_Name) {
        Last_Name = last_Name;
    }

    public String JSONFormat(){

        JSONObject json = new JSONObject();

        try{
            json.put("first_name",this.getFirst_Name());
            json.put("last_name",this.getLast_Name());
            json.put("email",this.getStudent_ID());
        } catch (JSONException e){
            Log.e("Studentbean", "Error handling JSONForamtfunction: " + e.getLocalizedMessage());
        }
        return json.toString();
    }
}
