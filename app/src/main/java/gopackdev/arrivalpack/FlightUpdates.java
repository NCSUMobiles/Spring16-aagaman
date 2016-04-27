package gopackdev.arrivalpack;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
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
import gopackdev.arrivalpack.bluemix.StudentConnector;
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
        //setContentView(R.layout.content_flight_updates);
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
                        if(!sID.equals(stud_id1)){
                            StudentIds.add(sID);
                        }
                        System.out.println("Student ID " + sID);
                    }
                    if(StudentIds.size() > 0){
                        Handler refresh = new Handler(Looper.getMainLooper());
                        refresh.post(new Runnable() {
                            public void run() {
                                displayBuddyList(StudentIds);
                            }
                        });
                    }
                    else{
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(FlightUpdates.this, "Sorry! No flight buddies till date", Toast.LENGTH_LONG).show();
                            }
                        });
                    }


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
        Iterator iter = SIds.iterator();
       // setContentView(R.layout.content_flight_updates);

       // final TableLayout table = new TableLayout(FlightUpdates.this);
         final TableLayout table = (TableLayout)findViewById(R.id.TL);
        //table.invalidate();
        table.setStretchAllColumns(true);
        table.setShrinkAllColumns(true);

        TableRow rowTitle = new TableRow(FlightUpdates.this);
        TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
        rowTitle.setLayoutParams(lp);

        rowTitle.setGravity(Gravity.CENTER_HORIZONTAL);

        TableRow rowDayLabels = new TableRow(FlightUpdates.this);
        TableRow.LayoutParams lp1 = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
        rowDayLabels.setLayoutParams(lp1);

        TableRow rowTop = new TableRow(FlightUpdates.this);
        TableRow.LayoutParams lp2 = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
        rowTop.setLayoutParams(lp2);

        TextView empty = new TextView(FlightUpdates.this);

        TextView title = new TextView(FlightUpdates.this);
        title.setText("FLIGHT BUDDIES INFORMATION");

        title.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
        title.setGravity(Gravity.CENTER);
        title.setTypeface(Typeface.SERIF, Typeface.BOLD);

        TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
        params.span = 6;

        rowTitle.addView(title, params);

        TextView firstCol = new TextView(FlightUpdates.this);
        firstCol.setText("EMAIL");
        firstCol.setTypeface(Typeface.DEFAULT_BOLD);

        TextView secondCol = new TextView(FlightUpdates.this);
        secondCol.setText("FIRST NAME");
        secondCol.setTypeface(Typeface.DEFAULT_BOLD);

        TextView thirdCol = new TextView(FlightUpdates.this);
        thirdCol.setText("LAST NAME");
        thirdCol.setTypeface(Typeface.DEFAULT_BOLD);

        TextView fourthCol = new TextView(FlightUpdates.this);
        fourthCol.setText("GENDER");
        fourthCol.setTypeface(Typeface.DEFAULT_BOLD);

        rowTop.addView(empty);
        rowDayLabels.addView(firstCol);
        rowDayLabels.addView(secondCol);
        rowDayLabels.addView(thirdCol);
        rowDayLabels.addView(fourthCol);

        table.addView(rowTitle);
        table.addView(rowTop);
        table.addView(rowDayLabels);
        while(iter.hasNext()){
            String id = (String) iter.next();

            StudentConnector studentConnector = new StudentConnector(client);
            studentConnector.getStudentDetails(FlightUpdates.this, id, new ResponseListener() {
                @Override
                public void onSuccess(Response response) {
                    JSONObject jsonObject = null;
                    String email = null;
                    String firstName = null;
                    String lastName = null;
                    String gender = null;

                    try {
                        jsonObject = new JSONObject(response.getResponseText());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    try {
                        email = jsonObject.getString("school_email").toString();
                        firstName = jsonObject.getString("first_name").toString();
                        lastName = jsonObject.getString("last_name").toString();
                        gender = jsonObject.getString("gender").toString();
                        System.out.println("\n EMAIL: " +email +"\nFIRST NAME: "+firstName + "\n LAST NAME: "+lastName + "\n GENDER: "+gender);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    final TableRow rowData = new TableRow(FlightUpdates.this);
                    TableRow.LayoutParams lp4 = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
                    rowData.setLayoutParams(lp4);

                    // labels column
                    TextView emailLabel = new TextView(FlightUpdates.this);
                    emailLabel.setText(email);
                    emailLabel.setTypeface(Typeface.DEFAULT_BOLD);

                    TextView firstNameLabel = new TextView(FlightUpdates.this);
                    firstNameLabel.setText(firstName);
                    firstNameLabel.setGravity(Gravity.CENTER_HORIZONTAL);

                    TextView lastNameLabel = new TextView(FlightUpdates.this);
                    lastNameLabel.setText(lastName);
                    lastNameLabel.setGravity(Gravity.CENTER_HORIZONTAL);

                    TextView GenderLabel = new TextView(FlightUpdates.this);
                    GenderLabel.setText(gender);
                    GenderLabel.setGravity(Gravity.CENTER_HORIZONTAL);

                    rowData.addView(emailLabel);
                    rowData.addView(firstNameLabel);
                    rowData.addView(lastNameLabel);
                    rowData.addView(GenderLabel);

                    Handler refresh = new Handler(Looper.getMainLooper());
                    refresh.post(new Runnable() {
                        public void run() {
                            table.addView(rowData);
                        }
                    });
                }

                @Override
                public void onFailure(Response response, Throwable t, JSONObject extendedInfo) {
                    System.out.println("FAILURE DISPLAY");
                }
            });
        }

        System.out.println("AFTER FOR LOOP - SUCCESS DISPLAY");
       // setContentView(table);
        System.out.println("SUCCESS DISPLAY");
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}
