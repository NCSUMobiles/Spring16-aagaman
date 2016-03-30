package gopackdev.arrivalpack.bluemix;

import android.content.Context;
import android.util.Log;

import com.ibm.mobilefirstplatform.clientsdk.android.core.api.Request;
import com.ibm.mobilefirstplatform.clientsdk.android.core.api.BMSClient;
import com.ibm.mobilefirstplatform.clientsdk.android.core.api.Response;
import com.ibm.mobilefirstplatform.clientsdk.android.core.api.ResponseListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import gopackdev.arrivalpack.bluemixbean.StudentBean;

/**
 * Created by Chi-Han on 3/30/2016.
 */
public class StudentConnector {

    private BMSClient client;
    private String subURL = "/api/Students/";
    private boolean addStudentResult = false;
    public StudentConnector(BMSClient c){
        this.client = c;
    }

    public boolean addStudentToCloudant(StudentBean bean, Context ctx){
        Request request = new Request(client.getBluemixAppRoute() + subURL, Request.POST);
        String json = bean.JSONFormat();

        HashMap headers = new HashMap();
        List<String> cType = new ArrayList<>();
        cType.add("application/json");
        List<String> accept = new ArrayList<>();
        accept.add("Application/json");

        headers.put("Content-Type", cType);
        headers.put("Accept", accept);

        request.setHeaders(headers);
        addStudentResult = false;
        request.send(ctx, json, new ResponseListener() {
            // On success, update local list with new TodoItem
            @Override
            public void onSuccess(Response response) {
                Log.i("StudentConnector", "Student created successfully");
                addStudentResult = true;
            }

            // On failure, log errors
            @Override
            public void onFailure(Response response, Throwable t, JSONObject extendedInfo) {
                if (response != null) {
                    Log.e("StudentConnector", "createStudent failed with error: " + response.getResponseText());
                }
                if (t != null) {
                    Log.e("StudentConnector", "createStudent failed with error: " + t.getLocalizedMessage(), t);
                }
                if (extendedInfo != null) {
                    Log.e("StudentConnector", "createStudent failed with error: " + extendedInfo.toString());
                }
                addStudentResult = false;
            }
        });
        return addStudentResult;
    }
}
