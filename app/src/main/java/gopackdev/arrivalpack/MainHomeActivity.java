package gopackdev.arrivalpack;

import android.content.Context;
import android.os.Bundle;

import android.os.Handler;
import android.os.Looper;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;

import android.widget.FrameLayout;


import com.ibm.mobilefirstplatform.clientsdk.android.core.api.BMSClient;
import com.ibm.mobilefirstplatform.clientsdk.android.core.api.Response;
import com.ibm.mobilefirstplatform.clientsdk.android.core.api.ResponseListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import gopackdev.arrivalpack.baseactivities.DrawerBaseActivity;
import gopackdev.arrivalpack.bluemix.StudentConnector;
import gopackdev.arrivalpack.bluemixbean.StudentBean;
import gopackdev.arrivalpack.listviewadapter.RoommateRecycleAdapter;


public class MainHomeActivity extends DrawerBaseActivity {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private BMSClient client;
    private List<StudentBean> student_recom_list = new ArrayList<StudentBean>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FrameLayout relativeLayout = (FrameLayout) findViewById(R.id.child_layout);
        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        relativeLayout.addView(layoutInflater.inflate(R.layout.content_home_page, null, false));
        // initialize BMSClient for bluemix
        client = BMSClient.getInstance();
        try {
            //initialize SDK with IBM Bluemix application ID and route
            //You can find your backendRoute and backendGUID in the Mobile Options section on top of your Bluemix application dashboard
            //TODO: Please replace <APPLICATION_ROUTE> with a valid ApplicationRoute and <APPLICATION_ID> with a valid ApplicationId
            client.initialize(this, "http://arrivalmobileapp.mybluemix.net", "6d959bbb-9863-4b78-bd85-e92a8ea57159");
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        mRecyclerView = (RecyclerView) findViewById(R.id.roommate_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mAdapter = new RoommateRecycleAdapter(student_recom_list);
        mRecyclerView.setAdapter(mAdapter);
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                try {
                    getStudentListData();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void getStudentListData() throws Exception {
        StudentConnector sc = new StudentConnector(client);
        GregorianCalendar startTime  = new GregorianCalendar();
        while(super.currentUser == null){
            Log.i("busy wait in other thread", "waiting...");
            GregorianCalendar anchor = new GregorianCalendar();
            if(startTime.getTimeInMillis() - anchor.getTimeInMillis() > 5000){
                throw new Exception("Seomthing weird happen to server");
            }
        }
        sc.getStudentsBySchool(this, super.currentUser.getSchoolID(), new ResponseListener() {
            @Override
            public void onSuccess(Response response) {
                try {
                    JSONArray student_list = new JSONArray(response.getResponseText());
                    for (int i=0; i<student_list.length(); i++) {
                        JSONObject tmp_json = student_list.getJSONObject(i);
                        StudentBean tmp_b = new StudentBean(tmp_json);
                        student_recom_list.add(tmp_b);
                    }
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            mAdapter.notifyDataSetChanged();
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Response response, Throwable t, JSONObject extendedInfo) {
                Log.i("get student list by school","Error: "+response);
            }
        });
    }
}
