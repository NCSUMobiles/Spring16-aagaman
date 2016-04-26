package gopackdev.arrivalpack;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import gopackdev.arrivalpack.baseactivities.DrawerBaseActivity;
import gopackdev.arrivalpack.bluemixbean.StudentBean;

public class StudentDetailsActivity extends DrawerBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameLayout relativeLayout = (FrameLayout) findViewById(R.id.child_layout);
        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        relativeLayout.addView(layoutInflater.inflate(R.layout.content_student_details, null, false));
        TableLayout tl = (TableLayout)findViewById(R.id.detailTable);
        for(int i = 0 ; i < tl.getChildCount(); i++){
            TableRow row = (TableRow) tl.getChildAt(i);
            if(i%2 == 0){
                row.setBackgroundDrawable(getResources().getDrawable(R.drawable.list_view_item_background));
            }else{
                row.setBackgroundDrawable(getResources().getDrawable(R.color.default_inlineaction));
            }
        }
        JSONObject obj = null;
        try {
            obj = new JSONObject(getIntent().getStringExtra("student"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        StudentBean bean = null;
        if(obj != null){
            bean = new StudentBean(obj);
        }
        if(bean != null){
            ImageView portrait = (ImageView)findViewById(R.id.portraitView);
            this.setTitle(bean.getFirstName()+" "+bean.getLastName());
            if(bean.isMale()){
                portrait.setImageResource(R.mipmap.ic_male_default_portrait);
            }else{
                portrait.setImageResource(R.mipmap.ic_female_default_portrait);
            }
            ImageView schoolLogo = (ImageView)findViewById(R.id.schoolLogoView);
            schoolLogo.setImageResource(MainHomeActivity.getSchoolLogoID(bean.getSchoolID()));
            TextView schoolName = (TextView)findViewById(R.id.schoolText);
            String [] schools = getResources().getStringArray(R.array.college_names);
            schoolName.setText(schools[bean.getSchoolID()-1]);
            TextView gender = (TextView) findViewById(R.id.genderText);
            gender.setText(bean.getGender());
            TextView firstName = (TextView)findViewById(R.id.firstNameText);
            firstName.setText(bean.getFirstName());
            TextView lastName = (TextView)findViewById(R.id.lastNameText);
            lastName.setText(bean.getLastName());
            TextView firstLanguage = (TextView)findViewById(R.id.firstLanguageText);
            firstLanguage.setText(bean.getFirstLanguage());
            TextView nation = (TextView)findViewById(R.id.nationalityText);
            nation.setText(bean.getNationality());
            TextView diet = (TextView)findViewById(R.id.dietaryRestrictionText);
            diet.setText(bean.getDietaryRestriction());
            TextView interest = (TextView)findViewById(R.id.interestExpertiseText);
            interest.setText(bean.getInterestExpertise());
            TextView sleepTime = (TextView)findViewById(R.id.sleepTimeText);
            sleepTime.setText(TimeStringConverter.convertFromTime(bean.getSleepTime()));
            TextView wakeTime = (TextView)findViewById(R.id.wakeTimeText);
            wakeTime.setText(TimeStringConverter.convertFromTime(bean.getWakeupTime()));
            TextView club = (TextView)findViewById(R.id.clubParticipatedText);
            club.setText(bean.getClubParticipated());
            TextView favClass = (TextView)findViewById(R.id.favoriteClassText);
            favClass.setText(bean.getFavoriteClass());
            TextView concern = (TextView)findViewById(R.id.recentConcernText);
            concern.setText(bean.getRecentConcern());
            TextView tryInFuture = (TextView)findViewById(R.id.something_want_to_try_in_future_Text);
            tryInFuture.setText(bean.getSomethingWantToTryInFuture());

        }else{
            Toast.makeText(this, "Something wrong on retrieving selected student...", Toast.LENGTH_SHORT);
        }
    }


}
