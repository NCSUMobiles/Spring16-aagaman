package gopackdev.arrivalpack.baseactivities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ibm.mobilefirstplatform.clientsdk.android.core.api.Response;
import com.ibm.mobilefirstplatform.clientsdk.android.core.api.ResponseListener;

import org.json.JSONException;
import org.json.JSONObject;

import gopackdev.arrivalpack.Airport;
import gopackdev.arrivalpack.FlightUpdates;
import gopackdev.arrivalpack.LoginActivity;
import gopackdev.arrivalpack.MainHomeActivity;
import gopackdev.arrivalpack.ProfileSettingActivity;
import gopackdev.arrivalpack.R;
import gopackdev.arrivalpack.Roommate;
import gopackdev.arrivalpack.bluemixbean.StudentBean;
import gopackdev.arrivalpack.useraccount.CurrentUserHolder;
import gopackdev.arrivalpack.useraccount.UserAccountManager;

public abstract class DrawerBaseActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    protected StudentBean currentUser;
    protected CurrentUserHolder userHolder;
    private Activity mActivity;
    private boolean paused = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = this;
        setContentView(R.layout.activity_main_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        loadCurrentUser();

        // where to start rendering customized child activity layout
//        FrameLayout relativeLayout = (FrameLayout)findViewById(R.id.child_layout);
//        LayoutInflater layoutInflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        relativeLayout.addView(layoutInflater.inflate(R.layout.content_roommate, null, false));
    }

    @Override
    protected void onPause() {
        super.onPause();
        //Tag if the activity has bee paused.
        paused = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (paused)
            loadCurrentUser();
        paused = false;

    }

    private void updateHeader() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View header = navigationView.getHeaderView(0);
        TextView greet = (TextView) header.findViewById(R.id.navHeaderUserGreetText);
        TextView email = (TextView) header.findViewById(R.id.navHeaderUserEmail);
        greet.setText("Hello! " + currentUser.getFirstName());
        email.setText(currentUser.getSchoolEmail());
        ImageView schoolLogo = (ImageView) header.findViewById(R.id.navHeaderSchoolLogo);
        schoolLogo.setImageResource(getSchoolLogoID(currentUser.getSchoolID()));
    }


    public static int getSchoolLogoID(int school_code) {
        /**
         *     <string-array name="college_names">
         <item>North Carolina State University</item>
         <item>University of South California</item>
         <item>Carnegie Mellon</item>
         <item>University of California, Berkeley</item>
         <item>Duke University</item>
         <item>Yale University</item>
         <item>University of Notre Dame</item>
         <item>University of Washington</item>
         <item>University of North Carolina Chapel Hill</item>
         <item>University of North Carolina Charlotte</item>
         <item>University of North Carolina Greensboro</item>
         </string-array>

         <integer-array name="college_code">
         <item>1</item>
         <item>2</item>
         <item>3</item>
         <item>4</item>
         <item>5</item>
         <item>6</item>
         <item>7</item>
         <item>8</item>
         <item>9</item>
         <item>10</item>
         <item>11</item>
         </integer-array>
         */
        switch (school_code) {
            case 1:
                return R.mipmap.ncsu_logo_ic;
            case 2:
                return R.mipmap.usc_logo_ic;
            case 3:
                return R.mipmap.cmu_logo_ic;
            case 4:
                return R.mipmap.ucb_logo_ic;
            case 5:
                return R.mipmap.duke_logo_ic;
            case 6:
                return R.mipmap.yale_logo_ic;
            case 7:
                return R.mipmap.und_logo_ic;
            case 8:
                return R.mipmap.uwc_logo_ic;
            case 9:
                return R.mipmap.uncch_logo_ic;
            case 10:
                return R.mipmap.uncc_logo_ic;
            case 11:
                return R.mipmap.uncg_logo_ic;
            default:
                return R.mipmap.ncsu_logo_ic;
        }
    }

    /**
     * Load current cahced log in user.
     */
    private void loadCurrentUser() {
        userHolder = CurrentUserHolder.getInstance();
        if (userHolder.isHaveStudentBean()) {
            currentUser = userHolder.getStudentBean();
            updateHeader();
        } else {
            String student_code = getSharedPreferences(getResources().getString(R.string.login_cache), MODE_PRIVATE)
                    .getString(getResources().getString(R.string.login_token), null);
            UserAccountManager.getChachedUser(this, student_code, new ResponseListener() {
                @Override
                public void onSuccess(Response response) {
                    try {
                        JSONObject obTmp = new JSONObject(response.getResponseText());
                        StudentBean sb = new StudentBean(obTmp);
                        sb.setID(obTmp.getString("id"));
                        currentUser = sb;
                        //Set student data to holder
                        userHolder.setStudentBean(currentUser);
                        //update drawer header if needed, show correct info to the screen.
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                updateHeader();

                            }
                        });
                        Log.i("MainHome", obTmp.getString("id") + "get!");
//                    correctCredential();
                    } catch (JSONException e) {
                        Log.i("MainHome", "Error JSON.");
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(), "Please login again...", Toast.LENGTH_SHORT).show();
                                LogOut();
                            }
                        });
                    }
//                new Handler(Looper.getMainLooper()).post(new Runnable() {
//                    @Override
//                    public void run() {
//                        showProgress(false);
//                    }
//                });
                }

                @Override
                public void onFailure(Response response, Throwable t, JSONObject extendedInfo) {
                    Log.i("MainHomeActivity", "Something went wrong on retrieving user data from server: " + response.toString());
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "Something went wrong, please try again later.", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
      /*  DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.roommate_suggestions) {
            // Handle the camera action
            Roommate();
        } else if (id == R.id.add_flight_info) {
            Airport();
        } else if (id == R.id.nav_flight_updates) {
            flightUpdates();
        } else if (id == R.id.nav_profile_setting) {
            //Toast.makeText(this, "Not yet implemented",Toast.LENGTH_SHORT).show();
            ProfileSetting();
        } else if (id == R.id.nav_log_out) {
            LogOut();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void ProfileSetting() {
        if (!getClass().equals(ProfileSettingActivity.class)) {
            Intent myIntent = new Intent(DrawerBaseActivity.this, ProfileSettingActivity.class);
            //myIntent.putExtra("key", value); //Optional parameters
            startActivity(myIntent);
        }
    }

    // temp actions
    public void Airport() {
        if (!getClass().equals(Airport.class)) {
            Intent myIntent = new Intent(DrawerBaseActivity.this, Airport.class);
            //myIntent.putExtra("key", value); //Optional parameters
            startActivity(myIntent);
        }
    }

    public void flightUpdates() {
        if (!getClass().equals(FlightUpdates.class)) {
            Intent intent = new Intent(DrawerBaseActivity.this, FlightUpdates.class);
            startActivity(intent);
        }
    }

    public void Roommate() {
        if (!getClass().equals(MainHomeActivity.class)) {
            Intent intent1 = new Intent(DrawerBaseActivity.this, MainHomeActivity.class);
            startActivity(intent1);
        }
    }

    public void LogOut() {
        UserAccountManager.logout(this);
        userHolder.setStudentBean(null);
        currentUser = null;
        Intent intent1 = new Intent(DrawerBaseActivity.this, LoginActivity.class);
        startActivity(intent1);
        //go back to login page and finish the home page.
        finish();
    }
}
