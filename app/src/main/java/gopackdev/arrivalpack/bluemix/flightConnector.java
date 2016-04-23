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
        context = ctx;
        try {
            detail.put("flightNum", bean.getFlightNum());
            detail.put("flightDate", bean.getDepartureDate());
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

    public void getMatches(Context ctx,StudentMatches bean, ResponseListener rl) {
        Request request = new Request(client.getBluemixAppRoute()+ subURL + "getMatches", Request.POST);
        String json = bean.JSONFormat();
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
        Log.i("Get Similar Info", json);
        request.send(ctx, json, rl);
    }
}
