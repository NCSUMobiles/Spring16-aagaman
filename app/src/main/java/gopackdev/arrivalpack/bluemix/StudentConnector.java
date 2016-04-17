package gopackdev.arrivalpack.bluemix;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.ibm.mobilefirstplatform.clientsdk.android.core.api.Request;
import com.ibm.mobilefirstplatform.clientsdk.android.core.api.BMSClient;
import com.ibm.mobilefirstplatform.clientsdk.android.core.api.Response;
import com.ibm.mobilefirstplatform.clientsdk.android.core.api.ResponseListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import gopackdev.arrivalpack.MainActivity;
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
    private Context context;
    public void addStudentToCloudant(StudentBean bean, Context ctx, ResponseListener rl){
        Request request = new Request(client.getBluemixAppRoute() + subURL, Request.POST);
        String json = bean.JSONFormat();
        context = ctx;
        HashMap headers = new HashMap();
        List<String> cType = new ArrayList<>();
        cType.add("application/json");
        List<String> accept = new ArrayList<>();
        accept.add("Application/json");

        headers.put("Content-Type", cType);
        headers.put("Accept", accept);

        request.setHeaders(headers);
        addStudentResult = false;
        Log.i("Student Connector",json);
        request.send(ctx, json, rl);
    }

    /**
     * Login method
     * Should only get one value (studnet id)
     * @param ctx
     * @param username
     * @param password
     */
    public void authenticateStudent(Context ctx, String username, String password, ResponseListener rl){
        Request request = new Request(client.getBluemixAppRoute() + subURL +"authenticateUser", Request.POST);
        JSONObject credential = new JSONObject();
        try {
            credential.put("school_email", username);
            credential.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        context = ctx;
        HashMap headers = new HashMap();
        List<String> cType = new ArrayList<>();
        cType.add("application/json");
        List<String> accept = new ArrayList<>();
        accept.add("Application/json");

        headers.put("Content-Type", cType);
        headers.put("Accept", accept);

        request.setHeaders(headers);
        addStudentResult = false;
        Log.i("Student Connector","authenticate user");
        request.send(ctx, credential.toString(), rl);
    }
    /**
     * You will need to retrieve the student JSON datra in ResponseListerner rl from the caller.
     * @param ctx
     * @param id
     * @param rl
     * @return
     */
    public void getStudentByID(Context ctx, String id, ResponseListener rl){
        Request request = new Request(client.getBluemixAppRoute() + subURL +"/"+id, Request.GET);
        JSONObject studentJSON = new JSONObject();
        context = ctx;
        HashMap headers = new HashMap();
        List<String> cType = new ArrayList<>();
        cType.add("application/json");
        List<String> accept = new ArrayList<>();
        accept.add("Application/json");

        headers.put("Content-Type", cType);
        headers.put("Accept", accept);

        request.setHeaders(headers);
        addStudentResult = false;
        Log.i("Student Connector","get studentByID");
        request.send(ctx, rl);
    }

    /**
     * Get school by ID, also need to retreive the stduentJSON from ResponseListerner rl from caller
     * @param ctx
     * @param schoolID  (if -1, it will get all Students JSON)
     * @param rl
     */
    public void getStudentsBySchool(Context ctx, long schoolID, ResponseListener rl){
        Request request = new Request(client.getBluemixAppRoute() + subURL +
                "/getStudentBySchoolID?school_id="+schoolID, Request.GET);
        JSONObject studentJSON = new JSONObject();
        context = ctx;
        HashMap headers = new HashMap();
        List<String> cType = new ArrayList<>();
        cType.add("application/json");
        List<String> accept = new ArrayList<>();
        accept.add("Application/json");

        headers.put("Content-Type", cType);
        headers.put("Accept", accept);

        request.setHeaders(headers);
        addStudentResult = false;
        Log.i("Student Connector","get studentBySchool");
        request.send(ctx, rl);
    }



}
