package gopackdev.arrivalpack.bluemix;

import android.content.Context;
import android.util.Log;

import com.ibm.mobilefirstplatform.clientsdk.android.core.api.Request;
import com.ibm.mobilefirstplatform.clientsdk.android.core.api.BMSClient;
import com.ibm.mobilefirstplatform.clientsdk.android.core.api.ResponseListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import gopackdev.arrivalpack.bluemixbean.StudentMatches;
import gopackdev.arrivalpack.bluemixbean.flightinfobean;
public class flightConnector {
    private BMSClient client;
    private String subURL = "/api/FlightInfos/";
    private boolean addFlightinfoResult = false;
    public flightConnector(BMSClient c){this.client = c;}
    private Context context;

    public void addflightinfotoCloudant(Context ctx,StudentMatches bean, ResponseListener rl){
        Request request = new Request(client.getBluemixAppRoute()+subURL, Request.POST);
        JSONObject detail = new JSONObject();
        try {
            detail.put("student_id",bean.getStudentId());
            detail.put("flight_number", bean.getFlightNum());
            detail.put("flight_date", bean.getDepartureDate());
            detail.put("arrival_hour",bean.getArrivalHour());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        context = ctx;

        HashMap headers = new HashMap();
        List<String> ctype = new ArrayList<>();
        ctype.add("application/json");
        List<String> accept = new ArrayList<>();
        accept.add("Application/json");

        headers.put("Content-type", ctype);
        headers.put("Accept", accept);

        request.setHeaders(headers);
        addFlightinfoResult = false;
        Log.i("flightInfo Connector",detail.toString());
        request.send(ctx, detail.toString(), rl);

    }

    public void getFlightIDByStudentID(Context ctx, String id, ResponseListener rl1){
        Request request = new Request(client.getBluemixAppRoute()+subURL+"getFlightIDByStudentID??stud_id="+id, Request.GET);
        JSONObject detail = new JSONObject();
        context = ctx;

        HashMap headers = new HashMap();
        List<String> ctype = new ArrayList<>();
        ctype.add("application/json");
        List<String> accept = new ArrayList<>();
        accept.add("Application/json");

        headers.put("Content-type", ctype);
        headers.put("Accept", accept);

        request.setHeaders(headers);
        addFlightinfoResult = false;

        request.send(ctx, detail.toString(), rl1);
    }
    /*

        function updateItem(id, text){
            var item = {
                    "text": text
            }

            return $.ajax( {
                    url:API_URL + "/" + id,
                    method: "PUT",
                    contentType: "application/json",
                    data: JSON.stringify(item)
            });
        }
    */
    public void updateFlightSchedule(Context ctx, StudentMatches smbean2, String flightID, ResponseListener rl1){
        Log.i("PUT","PUT 1");
        Request request = new Request(client.getBluemixAppRoute()+ subURL+flightID, Request.POST);
        JSONObject detail1 = new JSONObject();
        Log.i("PUT","PUT 2");
        try {
            Log.i("PUT","PUT 3");
            detail1.put("student_id", smbean2.getStudentId());
            detail1.put("flight_number", smbean2.getFlightNum());
            detail1.put("flight_date", smbean2.getDepartureDate());
            detail1.put("arrival_hour", smbean2.getArrivalHour());
            detail1.put("id",flightID);
            Log.i("PUT", "PUT 4");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("PUT","PUT 5");
        context = ctx;
        Log.i("PUT","PUT 6");
        HashMap headers = new HashMap();
        List<String> ctype = new ArrayList<>();
        ctype.add("application/json");
        List<String> accept = new ArrayList<>();
        accept.add("Application/json");
        Log.i("PUT", "PUT 7");
        headers.put("Content-type", ctype);
        headers.put("Accept", accept);
        Log.i("PUT", "PUT 8");
        request.setHeaders(headers);
        request.send(ctx, detail1.toString(), rl1);
        Log.i("PUT", "PUT 9");
    }
}
