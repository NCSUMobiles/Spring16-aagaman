package gopackdev.arrivalpack;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.ibm.mobilefirstplatform.clientsdk.android.core.api.BMSClient;
import com.ibm.mobilefirstplatform.clientsdk.android.core.api.Response;
import com.ibm.mobilefirstplatform.clientsdk.android.core.api.ResponseListener;

import org.json.JSONObject;

import java.net.MalformedURLException;

import gopackdev.arrivalpack.bluemix.StudentConnector;
import gopackdev.arrivalpack.bluemixbean.StudentBean;

public class NewAcctScrollingActivity extends AppCompatActivity {
    private BMSClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_acct_scrolling);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        client = BMSClient.getInstance();
        try {
            //initialize SDK with IBM Bluemix application ID and route
            //You can find your backendRoute and backendGUID in the Mobile Options section on top of your Bluemix application dashboard
            //TODO: Please replace <APPLICATION_ROUTE> with a valid ApplicationRoute and <APPLICATION_ID> with a valid ApplicationId
            client.initialize(this, "http://arrivalmobileapp.mybluemix.net", "6d959bbb-9863-4b78-bd85-e92a8ea57159");
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Button submitBtn = (Button) findViewById(R.id.submitSignUp);
        assert submitBtn != null;
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText emailadd = (EditText) findViewById(R.id.editText5);
                EditText pw = (EditText) findViewById(R.id.editText6);
                EditText firstName = (EditText) findViewById(R.id.editFirstName);
                EditText lastName = (EditText) findViewById(R.id.editLastName);
                EditText language = (EditText) findViewById(R.id.editFirstLanguage);
                Spinner nationality = (Spinner) findViewById(R.id.spinnerNationality);
                //EditText diet = (EditText) findViewById(R.id.dietary_restrictionDropDown);
                Spinner diet_restriction = (Spinner) findViewById(R.id.dietary_restrictionDropDown);
                Spinner school_id = (Spinner) findViewById(R.id.universityDropDown);
                EditText interest = (EditText) findViewById(R.id.edit_interest_expertise);
                EditText club = (EditText) findViewById(R.id.edit_club_participated);
                EditText favor_class = (EditText) findViewById(R.id.edit_favorite_class);
                EditText concern = (EditText) findViewById(R.id.edit_recent_concern);
                EditText something = (EditText) findViewById(R.id.edit_something_want_to_try_in_future);
                RadioGroup rg = (RadioGroup) findViewById(R.id.genderRadioGroup);
                String username = emailadd.getText().toString();
                String password = pw.getText().toString();
                RadioButton selectedBtn = (RadioButton) findViewById(rg.getCheckedRadioButtonId());
                String firstNameString = firstName.getText().toString();
                String lastNameString = lastName.getText().toString();
                String languateStr = language.getText().toString();
                String nationalityStr = nationality.getSelectedItem().toString();
                String interestStr = interest.getText().toString();
                String clubStr = club.getText().toString();
                String favorClassStr = favor_class.getText().toString();
                String concernStr = concern.getText().toString();
                String somethingStr = something.getText().toString();

                TimePicker sleepPicker = (TimePicker) findViewById(R.id.sleepTime);
                TimePicker wakePicker = (TimePicker) findViewById(R.id.wakeTime);
                int sleepHour = sleepPicker.getCurrentHour();
                int sleepMin = sleepPicker.getCurrentMinute();
                int sleepTime = sleepHour*100 + sleepMin;
                int wakeHour = wakePicker.getCurrentHour();
                int wakeMin = wakePicker.getCurrentMinute();
                int wakeTime = wakeHour*100 + wakeMin;
                int shoolID_position = school_id.getSelectedItemPosition();
                int[] schoolID_list = getResources().getIntArray(R.array.college_code);
                int schoolID = schoolID_list[shoolID_position];
                String dietRestrictionStr = diet_restriction.getSelectedItem().toString();
                if(!validateForm(username,password,selectedBtn,firstNameString,lastNameString,dietRestrictionStr)){
                    Toast.makeText(getApplicationContext(),"Missing inputs. *- required fields.",Toast.LENGTH_SHORT).show();
                    return;
                }
                String gender = selectedBtn.getText().toString();
                StudentBean bean = new StudentBean(username, password,
                        schoolID, firstNameString, lastNameString, gender,
                        languateStr, nationalityStr, dietRestrictionStr,
                        interestStr, clubStr, favorClassStr,
                        concernStr, somethingStr,
                        sleepTime, wakeTime);

                StudentConnector connector = new StudentConnector(client);
                connector.addStudentToCloudant(bean, getApplicationContext(), new ResponseListener() {
                    // On success, update local list with new TodoItem
                    @Override
                    public void onSuccess(Response response) {
                        Log.i("New account", "Student created successfully");
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(), "Account Created!", Toast.LENGTH_LONG).show();
                                closeAcitvity();
                            }
                        });
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
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(), "Account Failed To Create!", Toast.LENGTH_LONG).show();
                                closeAcitvity();
                            }
                        });
                    }
                });
            }
        });
    }

    private void closeAcitvity() {
        this.finish();
    }

    public boolean validateForm(String un, String pw, RadioButton bt, String first, String last, String diet){
        if(un==null||pw==null||bt==null||first==null||last==null||diet==null){
            return false;
        }
        return true;
    }
}
