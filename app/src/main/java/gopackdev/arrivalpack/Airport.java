package gopackdev.arrivalpack;

import android.annotation.TargetApi;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TimePicker;
import android.widget.Toast;

import com.ibm.mobilefirstplatform.clientsdk.android.core.api.BMSClient;
import com.ibm.mobilefirstplatform.clientsdk.android.core.api.Response;
import com.ibm.mobilefirstplatform.clientsdk.android.core.api.ResponseListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;

import gopackdev.arrivalpack.baseactivities.DrawerBaseActivity;
import gopackdev.arrivalpack.bluemix.StudentConnector;
import gopackdev.arrivalpack.bluemix.flightConnector;
import gopackdev.arrivalpack.bluemixbean.StudentMatches;

public class Airport  extends DrawerBaseActivity {
    private BMSClient client;
    boolean alreadyExists = false;
    boolean result = false;
    boolean setResult = false;
    String flightID = "";
    String test = "Hello";

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_airport);
        FrameLayout relativeLayout = (FrameLayout) findViewById(R.id.child_layout);
        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        relativeLayout.addView(layoutInflater.inflate(R.layout.content_airport, null, false));
        //---- avoid override whole base activtiy layout
        client = BMSClient.getInstance();
        try {
            client.initialize(this, "http://arrivalmobileapp.mybluemix.net", "6d959bbb-9863-4b78-bd85-e92a8ea57159");
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }

        String stud_id1 = currentUser.getID();
        Log.i("AIRPORT","STUDENT ID" +stud_id1);
        flightConnector flConn = new flightConnector(client);

        flConn.getFlightIDByStudentID(Airport.this, stud_id1, new ResponseListener() {
            @Override
            public void onSuccess(Response response) {
                Log.i("Success - Response for Flight ID ", "" + response.toString());

                JSONArray responseArray = null;

                if (response.getResponseText().equals("null")) {
                    alreadyExists = false;
                } else {
                    // Tried so long to read the response data as JSON but didn't work! So finally using substring
                    flightID = response.getResponseText().substring(7, 39).toString();
                    Log.i("FLIGHT ID ", "" + flightID);
                    alreadyExists = true;
                }
            }

            @Override
            public void onFailure(Response response, Throwable t, JSONObject extendedInfo) {
                Log.i("Failure - Response for Flight ID: ", response.toString());
            }
        });

        Button btn = (Button)findViewById(R.id.flightInfo);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("ALREADY EXISTS FLAG VALUE INSIDE CLICK", "" + alreadyExists);
                Log.i("FLIGHT ID IN AIRPORT PAGE: ","" +flightID);
                Log.i("TEST MESSAGE: ","" +test);
                Log.i("0.0","0");
                if (!alreadyExists) {
                    String stud_id = currentUser.getID();
                    EditText no = (EditText) findViewById(R.id.flight_no);
                    EditText dat = (EditText) findViewById(R.id.flight_date);
                    String flightNo = no.getText().toString();
                    String flightDate = dat.getText().toString();
                    TimePicker arrTime = (TimePicker) findViewById(R.id.dest_arr_time);
                    int hour = arrTime.getHour();
                    int min = arrTime.getMinute();
                    StudentMatches smbean1 = new StudentMatches(stud_id, flightNo, flightDate, hour, min);

                    flightConnector flConn = new flightConnector(client);
                    flConn.addflightinfotoCloudant(Airport.this, smbean1, new ResponseListener() {
                        @Override
                        public void onSuccess(Response response) {
                            Log.i("Airport Data added to DB", "success");
                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getApplicationContext(), "Information successfully added to database", Toast.LENGTH_LONG).show();
                                    closeAcitvity();
                                }
                            });
                        }

                        @Override
                        public void onFailure(Response response, Throwable t, JSONObject extendedInfo) {
                            Log.i("Failed to add airport data", "failure " + response);
                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(Airport.this, "Oops! There was some error, try again!", Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    });
                } else {
                    // Update the flight Timings
                    Log.i("I'm here ","1");
                    EditText no = (EditText) findViewById(R.id.flight_no);
                    EditText dat = (EditText) findViewById(R.id.flight_date);
                    String flightNo = no.getText().toString();
                    String flightDate = dat.getText().toString();
                    TimePicker arrTime = (TimePicker) findViewById(R.id.dest_arr_time);
                    int hour = arrTime.getHour();
                    int min = arrTime.getMinute();
                    String stud_id2 = currentUser.getID();
                    StudentMatches smbean = new StudentMatches(stud_id2, flightNo, flightDate, hour, min);
                    Log.i("I'm here ","2");
                    Log.i("FLIGHT ID IN AIRPORT PAGE: BEFORE ","" +flightID);
                    Log.i("I'm here ","3");
                    flightConnector flConn = new flightConnector(client);
                    Log.i("FLIGHT ID IN AIRPORT PAGE: ","" +flightID);
                    flConn.updateFlightSchedule(getApplicationContext(), smbean, flightID, new ResponseListener() {
                        @Override
                        public void onSuccess(Response response) {
                            Log.i("Success - Flight Schedule Updated: ", "" + response.toString());
                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                @Override
                                public void run() {
                                Toast.makeText(Airport.this, "You have updated your flight schedule!", Toast.LENGTH_LONG).show();
                                closeAcitvity();
                                }
                            });
                        }

                        @Override
                        public void onFailure(Response response, Throwable t, JSONObject extendedInfo) {
                            Log.i("Failure - Flight Schedule not updated: ", "" + response.toString());
                        }
                    });
                }
            }
        });
    }

    private void closeAcitvity() {
        this.finish();
    }

    public void onStart(){
        super.onStart();
        EditText txtDate = (EditText)findViewById(R.id.flight_date);
        txtDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    DateDialog dialog = new DateDialog(v);
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    dialog.show(ft, "DatePicker");
                }
            }
        });
    }

}
