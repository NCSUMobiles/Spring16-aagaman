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

import gopackdev.arrivalpack.bluemix.StudentConnector;
import gopackdev.arrivalpack.bluemix.flightConnector;
import gopackdev.arrivalpack.bluemixbean.StudentMatches;

public class Airport extends AppCompatActivity {
    private BMSClient client;

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_airport);

        client = BMSClient.getInstance();
        try {
            client.initialize(this, "http://arrivalmobileapp.mybluemix.net", "6d959bbb-9863-4b78-bd85-e92a8ea57159");
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }

        Button btn = (Button)findViewById(R.id.flightInfo);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText no = (EditText) findViewById(R.id.flight_no);
                EditText dat = (EditText) findViewById(R.id.flight_date);
                String flightNo = no.getText().toString();
                String flightDate = dat.getText().toString();
                TimePicker arrTime = (TimePicker) findViewById(R.id.dest_arr_time);
                int hour = arrTime.getHour();
                int min = arrTime.getMinute();
                StudentMatches smbean = new StudentMatches(flightNo, flightDate, hour, min);

                // call the function passing parameters as flightDate and NO
                // Store the flight no, flight date, flight time, email id, student id

                flightConnector flConn = new flightConnector(client);
                flConn.getMatches(Airport.this, smbean, new ResponseListener() {
                    @Override
                    public void onSuccess(Response response) {
                        Log.i("Airport - Response Success", "success");
                    }

                    @Override
                    public void onFailure(Response response, Throwable t, JSONObject extendedInfo) {
                        Log.i("Airport - Response Failure", "failure");
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
