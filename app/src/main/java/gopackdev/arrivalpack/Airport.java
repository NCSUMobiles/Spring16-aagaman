package gopackdev.arrivalpack;

import android.annotation.TargetApi;
import android.app.FragmentTransaction;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.ibm.mobilefirstplatform.clientsdk.android.core.api.BMSClient;
import com.ibm.mobilefirstplatform.clientsdk.android.core.api.Response;
import com.ibm.mobilefirstplatform.clientsdk.android.core.api.ResponseListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;

import gopackdev.arrivalpack.baseactivities.DrawerBaseActivity;
import gopackdev.arrivalpack.bluemix.StudentConnector;
import gopackdev.arrivalpack.bluemix.flightConnector;
import gopackdev.arrivalpack.bluemixbean.StudentMatches;

public class Airport  extends DrawerBaseActivity {
    private BMSClient client;

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_airport);
        Log.i("1", "1");
        client = BMSClient.getInstance();
        Log.i("1","2");
        try {
            Log.i("1","2.1");
            client.initialize(this, "http://arrivalmobileapp.mybluemix.net", "6d959bbb-9863-4b78-bd85-e92a8ea57159");
            Log.i("1", "2.2");
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        Log.i("1", "3");

        Button btn = (Button)findViewById(R.id.flightInfo);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String stud_id = currentUser.getID();
                Log.i("Student ID in Airport Acitivity: ", stud_id);
                EditText no = (EditText) findViewById(R.id.flight_no);
                EditText dat = (EditText) findViewById(R.id.flight_date);
                String flightNo = no.getText().toString();
                String flightDate = dat.getText().toString();
                TimePicker arrTime = (TimePicker) findViewById(R.id.dest_arr_time);
                int hour = arrTime.getHour();
                int min = arrTime.getMinute();
                StudentMatches smbean = new StudentMatches(stud_id, flightNo, flightDate, hour, min);

                flightConnector flConn = new flightConnector(client);
                flConn.addflightinfotoCloudant(Airport.this, smbean, new ResponseListener() {
                    @Override
                    public void onSuccess(Response response) {
                        Log.i("Airport Data added to DB", "success");
                    }

                    @Override
                    public void onFailure(Response response, Throwable t, JSONObject extendedInfo) {
                        Log.i("Failed to add airport data", "failure " + response);
                    }
                });
            }
        });
    }

    public void onStart(){
        super.onStart();
        /*EditText txtDate = (EditText)findViewById(R.id.flight_date);
        txtDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    DateDialog dialog = new DateDialog(v);
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    dialog.show(ft, "DatePicker");
                }
            }
        });*/
    }

}
