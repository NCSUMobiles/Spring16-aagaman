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

import gopackdev.arrivalpack.bluemixbean.FlightBuddies;
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

    public void updateFlightSchedule(Context ctx, StudentMatches smbean2,String flightID, ResponseListener rl1){
        Log.i("FLIGHT ID IN CONNECTOR: "," "+flightID);
        Request request = new Request(client.getBluemixAppRoute()+ subURL +flightID, Request.PUT);
        String json = smbean2.JSONFormat();
        context = ctx;
        HashMap headers = new HashMap();
        List<String> ctype = new ArrayList<>();
        ctype.add("application/json");
        List<String> accept = new ArrayList<>();
        accept.add("Application/json");
        headers.put("Content-type", ctype);
        headers.put("Accept", accept);
        request.setHeaders(headers);
        request.send(ctx, json, rl1);
    }

    public void getFlightDateByFlightID(Context ctx, String id, ResponseListener rl1){
        System.out.println("FLIGHT ID 0 CONNECTOR: " +id);
        Request request = new Request(client.getBluemixAppRoute()+subURL+"getFlightDateNumberByFlightID?flight_id="+id, Request.GET);
        context = ctx;

        HashMap headers = new HashMap();
        List<String> ctype = new ArrayList<>();
        ctype.add("application/json");
        List<String> accept = new ArrayList<>();
        accept.add("Application/json");

        headers.put("Content-type", ctype);
        headers.put("Accept", accept);

        request.setHeaders(headers);

        request.send(ctx, rl1);
    }

    public void getFlightBuddies(Context ctx, FlightBuddies fbean, ResponseListener rl2){
        String flight_date = fbean.getDate() ;
        String flight_number = fbean.getFlight_ID(); // Returns flight number
        System.out.println("FLIGHT CONNECTOR - FLIGHT DATE " +flight_date);
        System.out.println("FLIGHT CONNECTOR - FLIGHT NUMBER " + flight_number);
        Request request = new Request(client.getBluemixAppRoute()+subURL+"getFlightBuddies?flight_info=%5B%7B%22flight_number%22%3A%22"+flight_number+"%22%2C%22flight_date%22%3A%22"+ flight_date+"%22%7D%5D", Request.GET);
        context = ctx;

        HashMap headers = new HashMap();
        List<String> ctype = new ArrayList<>();
        ctype.add("application/json");
        List<String> accept = new ArrayList<>();
        accept.add("Application/json");

        headers.put("Content-type", ctype);
        headers.put("Accept", accept);

        request.setHeaders(headers);
        request.send(ctx, rl2);
    }
}
