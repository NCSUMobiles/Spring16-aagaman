package gopackdev.arrivalpack;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import gopackdev.arrivalpack.baseactivities.DrawerBaseActivity;
import gopackdev.arrivalpack.bluemix.StudentConnector;
import gopackdev.arrivalpack.bluemixbean.StudentBean;
import gopackdev.arrivalpack.listviewadapter.RoommateRecycleAdapter;

/*
 * Reorder class that contains function findMatch which expects a list of student beans and also
 * boolean values representing the different categories to be filtered. Function then sorts the array
 * and returns the sorted array.
 */

public class ReOrderList extends DrawerBaseActivity {

    private List<StudentBean> student_recom_list = new ArrayList<StudentBean>();

    public ArrayList findMatch(List<StudentBean> student_list, boolean gender, boolean langauge,
                          boolean nationality, boolean dietary_restriction,
                          boolean sleepTime, boolean wakeTime) throws Exception {
        List<StudentBean> reorderedList = new ArrayList<StudentBean>();


        for (int i = 0; i < student_list.size(); i++) {
            int value = getValues(student_list.get(i), gender, langauge,
            nationality, dietary_restriction,
            sleepTime, wakeTime);
            if (reorderedList.size() == 0) {
                reorderedList.add(student_list.get(i));
            }
            for (int j = 0; j < reorderedList.size(); j++) {
                int newValue = getValues(reorderedList.get(j), boolean gender, boolean langauge,
                boolean nationality, boolean dietary_restriction,
                boolean sleepTime, boolean wakeTime);
                if (value >= newValue) {
                    reorderedList.add(j, student_list.get(i));
                }

            }
        }
        return reorderedList;
    }

    private int getValues(StudentBean student) {
        int genderValue = 0;
        int langaugeValue = 0;
        int nationalityValue = 0;
        int dietaryValue = 0;
        int sleepTimeValue = 0;
        int wakeTimeValue = 0;

        if (gender && super.currentUser.getGender() == student.getGender())
            genderValue = 100;

        if (language && super.currentUser.getLanguage() == student.getLanguage())
            languageValue = 100;

        if (nationality && super.currentUser.getNationality() == student.getNationality())
            nationality = 100;

        if (dietary_restriction && super.currentUser.getDietary_Restriction() == student.getDietary_Restriction())
            dietaryValue = 100;

        if (sleepTime && super.currentUser.getSleepTime() == student.getSleepTime())
            sleepTimeValue = 100;

        if (wakeTime && super.currentUser.getWakeTime() == student.getWakeTime())
            wakeTimeValue = 100;

        return genderValue + langaugeValue + nationality + dietaryValue + sleepTimeValue + wakeTimeValue;
    }
}
