package gopackdev.arrivalpack;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class HomePage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_home_page);
    }

    public void Airport(View view)
    {
        Intent myIntent = new Intent(HomePage.this, Airport.class);
        //myIntent.putExtra("key", value); //Optional parameters
        startActivity(myIntent);
    }

    public void flightUpdates(View v){
        Intent intent = new Intent(HomePage.this,FlightUpdates.class);
        startActivity(intent);
    }

    public void Roommate(View v){
        Intent intent1 = new Intent(HomePage.this,Roommate.class);
        startActivity(intent1);
    }

}
