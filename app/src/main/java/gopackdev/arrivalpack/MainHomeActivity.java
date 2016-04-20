package gopackdev.arrivalpack;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.ibm.mobilefirstplatform.clientsdk.android.core.api.Response;
import com.ibm.mobilefirstplatform.clientsdk.android.core.api.ResponseListener;

import org.json.JSONException;
import org.json.JSONObject;

import gopackdev.arrivalpack.bluemixbean.StudentBean;
import gopackdev.arrivalpack.useraccount.UserAccountManager;

public class MainHomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    StudentBean currentUser;
    private Activity mActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = this;
        setContentView(R.layout.activity_main_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        loadCurrentUser();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void updateHeader(){
        TextView greet = (TextView) findViewById(R.id.navHeaderUserGreetText);
        TextView email = (TextView) findViewById(R.id.navHeaderUserEmail);
        greet.setText("Hello! "+currentUser.getFirstName());
        email.setText(currentUser.getSchoolEmail());
    }
    /**
     * Load current cahced log in user.
     */
    private void loadCurrentUser(){
        String student_code = getSharedPreferences(getResources().getString(R.string.login_cache),MODE_PRIVATE)
                .getString(getResources().getString(R.string.login_token),null);
        UserAccountManager.getChachedUser(this, student_code, new ResponseListener() {
            @Override
            public void onSuccess(Response response) {
                try {
                    JSONObject obTmp = new JSONObject(response.getResponseText());
                    StudentBean sb = new StudentBean(obTmp);
                    sb.setID(obTmp.getString("id"));
                    currentUser = sb;
                    //update drawer header if needed, show correct info to the screen.
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            updateHeader();
                        }
                    });
                    Log.i("MainHome",obTmp.getString("id")+"get!");
//                    correctCredential();
                } catch (JSONException e) {
                    Log.i("MainHome","Error JSON.");
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),"Please login again...",Toast.LENGTH_SHORT).show();
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
                Log.i("MainHomeActivity","Something went wrong on retrieving user data from server.");
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "Something went wrong, please try again later.",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
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

        if (id == R.id.match_roommate) {
            // Handle the camera action
            Roommate();
        } else if (id == R.id.add_flight_info) {
            Airport();
        } else if (id == R.id.nav_flight_updates) {
            flightUpdates();
        } else if (id == R.id.nav_profile_setting) {
            Toast.makeText(this, "Not yet implemented",Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_log_out) {
            LogOut();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    // temp actions
    public void Airport()
    {
        Intent myIntent = new Intent(MainHomeActivity.this, Airport.class);
        //myIntent.putExtra("key", value); //Optional parameters
        startActivity(myIntent);
    }

    public void flightUpdates(){
        Intent intent = new Intent(MainHomeActivity.this,FlightUpdates.class);
        startActivity(intent);
    }

    public void Roommate(){
        Intent intent1 = new Intent(MainHomeActivity.this,Roommate.class);
        startActivity(intent1);
    }

    public void LogOut(){
        UserAccountManager.logout(this);
        Intent intent1 = new Intent(MainHomeActivity.this,LoginActivity.class);
        startActivity(intent1);
        //go back to login page and finish the home page.
        finish();
    }
}
