package gopackdev.arrivalpack;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.ibm.mobilefirstplatform.clientsdk.android.core.api.BMSClient;
import com.ibm.mobilefirstplatform.clientsdk.android.core.api.Response;
import com.ibm.mobilefirstplatform.clientsdk.android.core.api.ResponseListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import gopackdev.arrivalpack.baseactivities.DrawerBaseActivity;
import gopackdev.arrivalpack.bluemix.flightConnector;
import gopackdev.arrivalpack.bluemixbean.FlightBuddies;

public class FlightUpdates  extends DrawerBaseActivity {

    private BMSClient client;
    boolean alreadyExists = false;
    String flightID = "";
    String stud_id1 = "";
    String flight_Date = "";
    String fltNum = "";
    List<String> flightNumber = new ArrayList();
    List<String> flightDate = new ArrayList();
    List<String> StudentIds = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_flight_updates);
        FrameLayout relativeLayout = (FrameLayout) findViewById(R.id.child_layout);
        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        relativeLayout.addView(layoutInflater.inflate(R.layout.content_flight_updates, null, false));
        //---- avoid override whole base activtiy layout
        client = BMSClient.getInstance();
        try {
            client.initialize(this, "http://arrivalmobileapp.mybluemix.net", "6d959bbb-9863-4b78-bd85-e92a8ea57159");
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }

        stud_id1 = currentUser.getID();

        flightConnector flConn = new flightConnector(client);

        flConn.getFlightIDByStudentID(FlightUpdates.this, stud_id1, new ResponseListener() {
            @Override
            public void onSuccess(Response response) {
                if (response.getResponseText().equals("null")) {
                    alreadyExists = false;
                } else {
                    // Tried so long to read the response data as JSON but didn't work! So finally using substring
                    flightID = response.getResponseText().substring(7, 39).toString();
                    Log.i("FLIGHT ID ", "" + flightID);
                    alreadyExists = true;
                    checkFlightBuddies();
                }
            }

            @Override
            public void onFailure(Response response, Throwable t, JSONObject extendedInfo) {
                Log.i("Failure - Response for Flight ID: ", "");
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "Please enter the flight information first", Toast.LENGTH_LONG).show();

                    }
                });
            }
        });
    }

    public void checkFlightBuddies(){
        // Getting the Flight Date based on the flight id
        flightConnector flConn1 = new flightConnector(client);
        flConn1.getFlightDateByFlightID(FlightUpdates.this, flightID, new ResponseListener() {
            @Override
            public void onSuccess(Response response) {
                try {
                    JSONArray array = new JSONArray(response.getResponseText());
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject row = array.getJSONObject(i);
                        fltNum = row.optString("flight_number").toString();
                        flight_Date = row.optString("flight_date").toString();
                        flightNumber.add(fltNum);
                        flightDate.add(flight_Date);
                    }
                    getFlightBuddies(flightNumber, flightDate);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Response response, Throwable t, JSONObject extendedInfo) {
                Log.i("FlightUpdates", "Didn't receive Flight Date" + flight_Date);
            }
        });
    }

    public void getFlightBuddies(List<String> flightNumber, List<String> flightDate){
        // Get Flight Buddies information
        Iterator iterator1 = flightNumber.iterator();
        Iterator iterator2 = flightDate.iterator();
        while(iterator1.hasNext() && iterator2.hasNext()){
            String fNum = (String) iterator1.next();
            String fDat = (String) iterator2.next();

            FlightBuddies fbean = new FlightBuddies(fNum,fDat);
            flightConnector flConn2 = new flightConnector(client);
            flConn2.getFlightBuddies(FlightUpdates.this, fbean, new ResponseListener() {
                @Override
                public void onSuccess(Response response) {
                    System.out.println("Success");
                    JSONArray array = null;
                    try {
                        array = new JSONArray(response.getResponseText());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    for (int i = 0; i < array.length(); i++) {
                        System.out.println("INSIDE FOR LOOP");
                        JSONObject row = null;
                        try {
                            row = array.getJSONObject(i);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        String sID = row.optString("student_id").toString();
                        StudentIds.add(sID);
                        System.out.println("Student ID" +sID);
                    }
                    displayBuddyList(StudentIds);
                    System.out.println("Successfully received");
                }

                @Override
                public void onFailure(Response response, Throwable t, JSONObject extendedInfo) {
                    System.out.println("Failure");
                    Log.i("Flight Buddies not recieved", " " + t.toString());
                }
            });
        }
    }

    public void displayBuddyList(List<String> SIds){
        
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void closeActvity() {
        this.finish();
    }
}
