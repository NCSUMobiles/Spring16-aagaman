package gopackdev.arrivalpack;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.PowerManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import gopackdev.arrivalpack.baseactivities.DrawerBaseActivity;
import gopackdev.arrivalpack.bluemix.StudentConnector;
import gopackdev.arrivalpack.bluemixbean.StudentBean;

public class ProfileSettingActivity extends DrawerBaseActivity {
    private BMSClient client;
    StudentBean updatedStudentBean;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameLayout relativeLayout = (FrameLayout)findViewById(R.id.child_layout);
        LayoutInflater layoutInflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        relativeLayout.addView(layoutInflater.inflate(R.layout.content_profile_setting, null, false));
        client = BMSClient.getInstance();
        try {
            //initialize SDK with IBM Bluemix application ID and route
            //You can find your backendRoute and backendGUID in the Mobile Options section on top of your Bluemix application dashboard
            //TODO: Please replace <APPLICATION_ROUTE> with a valid ApplicationRoute and <APPLICATION_ID> with a valid ApplicationId
            client.initialize(this, "http://arrivalmobileapp.mybluemix.net", "6d959bbb-9863-4b78-bd85-e92a8ea57159");
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        autoFillProfileForm();
    }


    public void autoFillProfileForm(){
//        EditText emailadd = (EditText) findViewById(R.id.editText5);
//        EditText pw = (EditText) findViewById(R.id.editText6);
        EditText firstName = (EditText) findViewById(R.id.editFirstName);
        firstName.setText(super.currentUser.getFirstName());
        EditText lastName = (EditText) findViewById(R.id.editLastName);
        lastName.setText(super.currentUser.getLastName());
        EditText language = (EditText) findViewById(R.id.editFirstLanguage);
        language.setText(super.currentUser.getFirstLanguage());
        Spinner nationality = (Spinner) findViewById(R.id.spinnerNationality);
        String [] countries = getResources().getStringArray(R.array.countries_array);
        int nationality_position = 0;
        List countries_list = Arrays.asList(countries);
        if(countries_list.contains(super.currentUser.getNationality())){
            nationality_position = countries_list.indexOf(super.currentUser.getNationality());
        }
        nationality.setSelection(nationality_position);
        //EditText diet = (EditText) findViewById(R.id.dietary_restrictionDropDown);
        Spinner diet_restriction = (Spinner) findViewById(R.id.dietary_restrictionDropDown);
        String [] dietary_list = getResources().getStringArray(R.array.diet_list);
        String user_diet = super.currentUser.getDietaryRestriction();
        int diet_position = 0;
        for(int i = 0 ; i < dietary_list.length-1;i++){
            if(dietary_list[i].equals(user_diet)){
                diet_position = i;
                break;
            }
        }
        diet_restriction.setSelection(diet_position);
        Spinner school_id = (Spinner) findViewById(R.id.universityDropDown);
        //school id is increment integer
        //since school id start from 1 , so -1 first
        school_id.setSelection(super.currentUser.getSchoolID()-1);
        EditText interest = (EditText) findViewById(R.id.edit_interest_expertise);
        interest.setText(super.currentUser.getInterestExpertise());
        EditText club = (EditText) findViewById(R.id.edit_club_participated);
        club.setText(super.currentUser.getClubParticipated());
        EditText favor_class = (EditText) findViewById(R.id.edit_favorite_class);
        favor_class.setText(super.currentUser.getFavoriteClass());
        EditText concern = (EditText) findViewById(R.id.edit_recent_concern);
        concern.setText(super.currentUser.getRecentConcern());
        EditText something = (EditText) findViewById(R.id.edit_something_want_to_try_in_future);
        something.setText(super.currentUser.getSomethingWantToTryInFuture());
        RadioGroup rg = (RadioGroup) findViewById(R.id.genderRadioGroup);
        if(super.currentUser.isMale()){
            rg.check(R.id.radioButtonMale);
        }else{
            rg.check(R.id.radioButtonFemale);
        }
        //rg.check();
        TimePicker sleepPicker = (TimePicker) findViewById(R.id.sleepTime);
        int sleepHour = super.currentUser.getSleepTime()/100;
        int sleepMinute = super.currentUser.getSleepTime()%100;
        sleepPicker.setCurrentHour(sleepHour);
        sleepPicker.setCurrentMinute(sleepMinute);
        TimePicker wakePicker = (TimePicker) findViewById(R.id.wakeTime);
        int wakeHour = super.currentUser.getWakeupTime()/100;
        int wakeMinute = super.currentUser.getWakeupTime()%100;
        wakePicker.setCurrentHour(wakeHour);
        wakePicker.setCurrentMinute(wakeMinute);
    }

    public boolean validateForm( RadioButton bt, String first, String last, String diet){
        if(bt==null||first==null||last==null||diet==null){
            return false;
        }
        return true;
    }

    private void closeAcitvity() {
        this.finish();
    }

    /**
     * User click on submit button form to update their profile
     */
    public void submitForm(View v){
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
        if(!validateForm(selectedBtn,firstNameString,lastNameString,dietRestrictionStr)){
            Toast.makeText(getApplicationContext(),"Missing inputs. *- required fields.",Toast.LENGTH_SHORT).show();
            return;
        }
        String gender = selectedBtn.getText().toString();
        //created temp updated verion studnet bean
        updatedStudentBean = new StudentBean(super.currentUser.getSchoolEmail(),
                super.currentUser.getPassWord(),schoolID,
                firstNameString, lastNameString, gender,
                languateStr, nationalityStr, dietRestrictionStr,
                interestStr, clubStr, favorClassStr,
                concernStr, somethingStr,
                sleepTime, wakeTime);
        updatedStudentBean.setID(super.currentUser.getID());
        StudentConnector connector = new StudentConnector(client);
        connector.updateStudentToCloudant(updatedStudentBean, getApplicationContext(), new ResponseListener() {
            // On success, update local list with new TodoItem
            @Override
            public void onSuccess(Response response) {
                Log.i("New account", "Profile update successfully");
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "Profile Updated!", Toast.LENGTH_LONG).show();
                        userHolder.setStudentBean(updatedStudentBean);
                        closeAcitvity();
                    }
                });
            }

            // On failure, log errors
            @Override
            public void onFailure(Response response, Throwable t, JSONObject extendedInfo) {
                if (response != null) {
                    Log.e("StudentConnector", "updateStudent failed with error: " + response.getResponseText());
                }
                if (t != null) {
                    Log.e("StudentConnector", "updateStudent failed with error: " + t.getLocalizedMessage(), t);
                }
                if (extendedInfo != null) {
                    Log.e("StudentConnector", "updateStudent failed with error: " + extendedInfo.toString());
                }
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "Account Failed To Update!", Toast.LENGTH_LONG).show();
                        closeAcitvity();
                    }
                });
            }
        });
    }
}
