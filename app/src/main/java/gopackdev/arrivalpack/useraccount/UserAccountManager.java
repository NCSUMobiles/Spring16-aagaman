package gopackdev.arrivalpack.useraccount;

import android.app.Activity;
import android.content.Context;

import com.ibm.mobilefirstplatform.clientsdk.android.core.api.BMSClient;
import com.ibm.mobilefirstplatform.clientsdk.android.core.api.ResponseListener;

import java.net.MalformedURLException;

import gopackdev.arrivalpack.R;
import gopackdev.arrivalpack.bluemix.StudentConnector;
import gopackdev.arrivalpack.bluemixbean.StudentBean;

/**
 * Account manager to handle account log off log in...
 * Created by Chi-Han on 4/18/2016.
 */
public class UserAccountManager {
    public static String login(Context ctx){
        return "";
    }
    public static void logout(Activity act){
        act.getSharedPreferences(act.getString(R.string.login_cache),act.MODE_PRIVATE)
                .edit().clear().commit();
    }

    /**
     * Method to retrieve student object for already-log-in user account.
     * @return
     */
    public static void getChachedUser(Activity act,String cachedID, ResponseListener listener){
        //Assumer client has been initialize already in launcher activity.
        BMSClient client = BMSClient.getInstance();

        try {
            //initialize SDK with IBM Bluemix application ID and route
            //You can find your backendRoute and backendGUID in the Mobile Options section on top of your Bluemix application dashboard
            //TODO: Please replace <APPLICATION_ROUTE> with a valid ApplicationRoute and <APPLICATION_ID> with a valid ApplicationId
            client.initialize(act, "http://arrivalmobileapp.mybluemix.net", "6d959bbb-9863-4b78-bd85-e92a8ea57159");
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        StudentConnector connector = new StudentConnector(client);
        connector.getStudentByID(act, cachedID, listener);
    }
}
