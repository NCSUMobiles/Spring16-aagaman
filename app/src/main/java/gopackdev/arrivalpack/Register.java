package gopackdev.arrivalpack;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.ibm.mobilefirstplatform.clientsdk.android.core.api.BMSClient;

import java.net.MalformedURLException;

import gopackdev.arrivalpack.bluemix.StudentConnector;
import gopackdev.arrivalpack.bluemixbean.StudentBean;

public class Register extends AppCompatActivity {
    private BMSClient client;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        client = BMSClient.getInstance();
        try {
            //initialize SDK with IBM Bluemix application ID and route
            //You can find your backendRoute and backendGUID in the Mobile Options section on top of your Bluemix application dashboard
            //TODO: Please replace <APPLICATION_ROUTE> with a valid ApplicationRoute and <APPLICATION_ID> with a valid ApplicationId
            client.initialize(this, "http://arrivalmobileapp.mybluemix.net", "6d959bbb-9863-4b78-bd85-e92a8ea57159");
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        //Toast.makeText(this, "Account Created!", Toast.LENGTH_SHORT);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        Button submitBtn = (Button) findViewById(R.id.submitSignUp);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText emailadd = (EditText)findViewById(R.id.editText5);
                EditText pw = (EditText) findViewById(R.id.editText6);
                RadioGroup rg = (RadioGroup) findViewById(R.id.radioGroup);
                String username = emailadd.getText().toString();
                String password = pw.getText().toString();
                RadioButton selectedBtn =(RadioButton)findViewById(rg.getCheckedRadioButtonId());
                String gender = selectedBtn.getText().toString();
                StudentBean bean = new StudentBean(username, password,
                        "Test_first_name", "Test_last_name", gender,
                        "Test_language", "Test_nationality", "Test_diet",
                        "Test_interest", "Test_club", "Test_favor",
                        "Test_concern", "Test_something",
                2300, 800);

                StudentConnector connector = new StudentConnector(client);
                connector.addStudentToCloudant(bean, getApplicationContext());

                closeAcitvity();
            }
        });
    }

    private void closeAcitvity(){
        this.closeAcitvity();
    }
}