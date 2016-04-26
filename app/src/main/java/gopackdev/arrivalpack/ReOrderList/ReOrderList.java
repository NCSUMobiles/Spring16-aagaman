package gopackdev.arrivalpack.ReOrderList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.TreeMap;

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
        StudentBean currStudent;
        Integer value;
        TreeMap<Integer, StudentBean> matchScore = new TreeMap<Integer, StudentBean>();
        Collection<StudentBean> matchedStudents;
        List<StudentBean> reorderedList;

        for (int i = 0; i < student_list.size(); i++) {
            currStudent = student_list.get(i);
            value = getValues(currStudent); //Here, getValue will return the matching score for the specific student
            matchScore.put(value, currStudent);
        }

        matchedStudents = matchScore.descendingMap().values();
        reorderedList = new ArrayList<>(matchedStudents);
        return reorderedList;
    }

    private int getValues(StudentBean student) {
        int genderValue = 0;
        int languageValue = 0;
        int nationalityValue = 0;
        int dietaryValue = 0;
        int sleepTimeValue = 0;
        int wakeTimeValue = 0;

        if (gender              && (super.currentUser.getGender()             == student.getGender()))             genderValue      = 100;
        if (nationality         && (super.currentUser.getNationality()        == student.getNationality()))        nationalityValue = 100;
        if (dietary_restriction && (super.currentUser.getDietaryRestriction() == student.getDietaryRestriction())) dietaryValue     = 100;
        if (language            && (super.currentUser.getFirstLanguage()      == student.getFirstLanguage()))      languageValue    = 100;

        if (sleepTime) sleepTimeValue = Math.abs(super.currentUser.getSleepTime()  - student.getSleepTime());
        if (wakeTime)  wakeTimeValue  = Math.abs(super.currentUser.getWakeupTime() - student.getWakeupTime());

        return genderValue + languageValue + nationalityValue + dietaryValue + sleepTimeValue + wakeTimeValue;
    }
}
