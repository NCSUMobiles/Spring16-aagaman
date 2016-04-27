package gopackdev.arrivalpack;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Toast;


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

import gopackdev.arrivalpack.ReOrderList.ReOrderList;
import gopackdev.arrivalpack.baseactivities.DrawerBaseActivity;
import gopackdev.arrivalpack.bluemix.StudentConnector;
import gopackdev.arrivalpack.bluemixbean.StudentBean;
import gopackdev.arrivalpack.dialog.SortByDialogFragment;
import gopackdev.arrivalpack.listviewadapter.RoommateRecycleAdapter;


public class MainHomeActivity extends DrawerBaseActivity implements SortByDialogFragment.SortByDialogListener {
    private RecyclerView mRecyclerView;
    private RoommateRecycleAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private BMSClient client;
    private List<StudentBean> student_base_list = new ArrayList<StudentBean>();
    private List<StudentBean> student_recom_list = new ArrayList<StudentBean>();
    private ProgressBar progressBar;
    private Activity thisactivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Roommate Suggestions");
        thisactivity = this;
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
        mAdapter = new RoommateRecycleAdapter(student_recom_list, this);
        mRecyclerView.setAdapter(mAdapter);
        progressBar = (ProgressBar)findViewById(R.id.loading_progress);
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
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                StudentConnector sc = new StudentConnector(client);
                boolean school_pref = getSharedPreferences(getResources().getString(R.string.roommate_list_preference),
                        MODE_PRIVATE).getBoolean(getResources().getString(R.string.school_preference),false);
                sc.getStudentsBySchool(getApplicationContext(), school_pref?currentUser.getSchoolID():-1, new ResponseListener() {
                    @Override
                    public void onSuccess(Response response) {
                        try {
                            JSONArray student_list = new JSONArray(response.getResponseText());
                            student_base_list.clear();
                            for (int i=0; i<student_list.length(); i++) {
                                JSONObject tmp_json = student_list.getJSONObject(i);
                                StudentBean tmp_b = new StudentBean(tmp_json);
                                student_base_list.add(tmp_b);
                            }
                            ReOrderList reOrderList = new ReOrderList();
                            boolean gender_pref = getSharedPreferences(getResources().getString(R.string.roommate_list_preference),
                                    MODE_PRIVATE).getBoolean(getResources().getString(R.string.gender_preference),false);
                            boolean language_pref = getSharedPreferences(getResources().getString(R.string.roommate_list_preference),
                                    MODE_PRIVATE).getBoolean(getResources().getString(R.string.language_preference),false);
                            boolean country_pref = getSharedPreferences(getResources().getString(R.string.roommate_list_preference),
                                    MODE_PRIVATE).getBoolean(getResources().getString(R.string.country_preference),false);
                            boolean diet_pref = getSharedPreferences(getResources().getString(R.string.roommate_list_preference),
                                    MODE_PRIVATE).getBoolean(getResources().getString(R.string.diet_preference),false);
                            boolean sleep_pref = getSharedPreferences(getResources().getString(R.string.roommate_list_preference),
                                    MODE_PRIVATE).getBoolean(getResources().getString(R.string.sleep_preference),false);
                            boolean wakeup_pref = getSharedPreferences(getResources().getString(R.string.roommate_list_preference),
                                    MODE_PRIVATE).getBoolean(getResources().getString(R.string.wakeup_preference),false);
                            //student_recom_list = null;
                            student_recom_list = reOrderList.findMatch(currentUser, student_base_list,student_recom_list,
                                    gender_pref,
                                    language_pref,
                                    country_pref,
                                    diet_pref,
                                    sleep_pref,
                                    wakeup_pref);
                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                @Override
                                public void run() {
//                                    mAdapter.swapWholeList(student_recom_list);
                                    mAdapter = new RoommateRecycleAdapter(student_recom_list, thisactivity);
                                    mRecyclerView.invalidate();
                                    mRecyclerView.setAdapter(mAdapter);
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
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                    }
                });
            }

            @Override
            protected Void doInBackground(Void... params) {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.VISIBLE);
                    }
                });
                while(currentUser == null){
                    GregorianCalendar startTime  = new GregorianCalendar();
                    Log.i("busy wait in other thread", "waiting...");
                    GregorianCalendar anchor = new GregorianCalendar();
                    if(startTime.getTimeInMillis() - anchor.getTimeInMillis() > 5000){
                        Log.i("server not responding", "error server not responding");
                    }
                }

                return null;
            }
        }.execute();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_home, menu);
        return true;
    }
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle item selection
//        switch (item.getItemId()) {
//            case R.id.new_game:
//                newGame();
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }
    public void openSortBySetup(MenuItem item){
        //Toast.makeText(this, "Sort By clicked!",Toast.LENGTH_SHORT).show();
        FragmentManager fm =this.getSupportFragmentManager();
        SortByDialogFragment tmpFragment = new SortByDialogFragment();
        tmpFragment.show(fm, "sortByDialog");
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        Dialog dialogView = dialog.getDialog();
        //update shared preference
        CheckBox school_checkbox = (CheckBox)dialogView.findViewById(R.id.schoolPreference);
        CheckBox gender_checkbox = (CheckBox)dialogView.findViewById(R.id.genderPreference);
        CheckBox language_checkbox = (CheckBox)dialogView.findViewById(R.id.languagePreference);
        CheckBox country_checkbox = (CheckBox)dialogView.findViewById(R.id.countryPreference);
        CheckBox diet_checkbox = (CheckBox)dialogView.findViewById(R.id.dietPreference);
        CheckBox sleep_checkbox = (CheckBox)dialogView.findViewById(R.id.sleepPreference);
        CheckBox wakeup_checkbox = (CheckBox)dialogView.findViewById(R.id.wakeupPreference);
        getSharedPreferences(getResources().getString(R.string.roommate_list_preference),
                MODE_PRIVATE).edit().putBoolean(getResources().getString(R.string.school_preference),
                school_checkbox.isChecked()).apply();
        getSharedPreferences(getResources().getString(R.string.roommate_list_preference),
                MODE_PRIVATE).edit().putBoolean(getResources().getString(R.string.gender_preference),
                gender_checkbox.isChecked()).apply();
        getSharedPreferences(getResources().getString(R.string.roommate_list_preference),
                MODE_PRIVATE).edit().putBoolean(getResources().getString(R.string.language_preference),
                language_checkbox.isChecked()).apply();
        getSharedPreferences(getResources().getString(R.string.roommate_list_preference),
                MODE_PRIVATE).edit().putBoolean(getResources().getString(R.string.country_preference),
                country_checkbox.isChecked()).apply();
        getSharedPreferences(getResources().getString(R.string.roommate_list_preference),
                MODE_PRIVATE).edit().putBoolean(getResources().getString(R.string.diet_preference),
                diet_checkbox.isChecked()).apply();
        getSharedPreferences(getResources().getString(R.string.roommate_list_preference),
                MODE_PRIVATE).edit().putBoolean(getResources().getString(R.string.sleep_preference),
                sleep_checkbox.isChecked()).apply();
        getSharedPreferences(getResources().getString(R.string.roommate_list_preference),
                MODE_PRIVATE).edit().putBoolean(getResources().getString(R.string.wakeup_preference),
                wakeup_checkbox.isChecked()).apply();
        //----
        try {
            getStudentListData();
        } catch (Exception e) {
            Toast.makeText(this, "Something wrong communicating with server", Toast.LENGTH_SHORT);
        }
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {

    }
}
