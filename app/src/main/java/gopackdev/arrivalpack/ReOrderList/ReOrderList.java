package gopackdev.arrivalpack.ReOrderList;

import java.util.ArrayList;
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

    boolean gender = false;
    boolean language = false;
    boolean nationality = false;
    boolean dietary_restriction = false;
    boolean sleepTime = false;
    boolean wakeTime = false;

    public List<StudentBean> findMatch(List<StudentBean> student_list, boolean gender,
                                       boolean language, boolean nationality, boolean
                                       dietary_restriction, boolean sleepTime,
                                       boolean wakeTime) throws Exception {
        this.gender = gender;
        this.language = language;
        this.nationality = nationality;
        this.dietary_restriction = dietary_restriction;
        this.sleepTime = sleepTime;
        this.wakeTime = wakeTime;

        List<StudentBean> reorderedList = new ArrayList<StudentBean>();

        for (int i = 0; i < student_list.size(); i++) {
            int value = getValues(student_list.get(i));

            if (reorderedList.size() == 0) {
                reorderedList.add(student_list.get(i));
                continue;
            }
            int listsize = reorderedList.size();
            for (int j = 0; j < listsize ; j++) {
                int newValue = getValues(reorderedList.get(j));
                if (value >= newValue) {
                    reorderedList.add(j, student_list.get(i));
                    break;
                }
                reorderedList.add(student_list.get(i));
            }
        }
        return reorderedList;
    }

    private int getValues(StudentBean student) {
        int genderValue = 0;
        int languageValue = 0;
        int nationalityValue = 0;
        int dietaryValue = 0;
        int sleepTimeValue = 0;
        int wakeTimeValue = 0;

        if (gender && currentUser.getGender() == student.getGender())
            genderValue = 100;

        if (language && currentUser.getLanguage() == student.getFirstLanguage())
            languageValue = 100;

        if (nationality && currentUser.getNationality() == student.getNationality())
            nationalityValue = 100;

        if (dietary_restriction && currentUser.getDietary_Restriction() == student.getDietaryRestriction())
            dietaryValue = 100;

        if (sleepTime)
            sleepTimeValue = Math.abs(currentUser.getSleepTime() - student.getSleepTime());

        if (wakeTime)
            wakeTimeValue = Math.abs(currentUser.getWakeTime() - student.getWakeupTime());

        return genderValue + languageValue + nationalityValue + dietaryValue + sleepTimeValue + wakeTimeValue;
    }
}
