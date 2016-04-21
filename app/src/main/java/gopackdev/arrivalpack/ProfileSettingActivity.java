package gopackdev.arrivalpack;

import android.content.Context;
import android.os.Bundle;
import android.os.PowerManager;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TimePicker;

import gopackdev.arrivalpack.baseactivities.DrawerBaseActivity;

public class ProfileSettingActivity extends DrawerBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameLayout relativeLayout = (FrameLayout)findViewById(R.id.child_layout);
        LayoutInflater layoutInflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        relativeLayout.addView(layoutInflater.inflate(R.layout.content_profile_setting, null, false));
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
        EditText nationality = (EditText) findViewById(R.id.editNationality);
        nationality.setText(super.currentUser.getNationality());
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

    public boolean validateForm(String un, String pw, RadioButton bt, String first, String last, String diet){
        if(un==null||pw==null||bt==null||first==null||last==null||diet==null){
            return false;
        }
        return true;
    }
}
