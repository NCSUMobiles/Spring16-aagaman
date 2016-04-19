package gopackdev.arrivalpack.useraccount;

import android.app.Activity;
import android.content.Context;

import gopackdev.arrivalpack.R;

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
}
