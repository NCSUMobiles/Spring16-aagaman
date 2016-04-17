package gopackdev.arrivalpack.bluemix;

import android.content.Context;
import android.util.Log;

import com.ibm.mobilefirstplatform.clientsdk.android.core.api.Request;
import com.ibm.mobilefirstplatform.clientsdk.android.core.api.BMSClient;
import com.ibm.mobilefirstplatform.clientsdk.android.core.api.ResponseListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import gopackdev.arrivalpack.bluemixbean.flightinfobean;
/**
 * Created by shashwath on 4/16/16.
 */
public class flightConnector {
    private BMSClient client;
    private String subURL = "/api/flightinfo";
    private boolean addflightinforesult = false;
    public flightConnector(BMSClient c){this.client = c;}
    private Context context;

    public void addflightinfotoCloudant(flightinfobean bean, Context ctx, ResponseListener rl){
        Request request = new Request(client.getBluemixAppRoute()+subURL, Request.POST);
        String json = bean.JSONFormat();
        context = ctx;

    }
}
