package gopackdev.arrivalpack.ReOrderList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
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

public class ReOrderList {

    boolean gender = false;
    boolean language = false;
    boolean nationality = false;
    boolean dietary_restriction = false;
    boolean sleepTime = false;
    boolean wakeTime = false;
    StudentBean currentUser;

    /**
     *
     * @param currentUser
     * @param student_base_list
     * @param student_list
     * @param gender
     * @param language
     * @param nationality
     * @param dietary_restriction
     * @param sleepTime
     * @param wakeTime
     * @return
     */
    public List<StudentBean> findMatch(StudentBean currentUser,List<StudentBean> student_base_list,List<StudentBean> student_list, boolean gender,
                                       boolean language, boolean nationality, boolean
                                       dietary_restriction, boolean sleepTime,
                                       boolean wakeTime) {
        this.gender = gender;
        this.language = language;
        this.nationality = nationality;
        this.dietary_restriction = dietary_restriction;
        this.sleepTime = sleepTime;
        this.wakeTime = wakeTime;
        this.currentUser = currentUser;
        StudentBean currStudent;
        Integer value;
        List<StudentBean> reorderedList;
        ArrayList<IndexPair> order_list = new ArrayList<>();
        for (int i = 0; i < student_base_list.size(); i++) {
            currStudent = student_base_list.get(i);
            value = getValues(currStudent); //Here, getValue will return the matching score for the specific student
            order_list.add(new IndexPair(i, value));
        }
        Collections.sort(order_list, new IndexPairComparator());
        reorderedList = new ArrayList<>();
        for(int i = 0 ; i < order_list.size() ; i++){
            int index = order_list.get(i).base_list_index;
            reorderedList.add(student_base_list.get(index));
        }
        //clean up the old list
        student_list.clear();
        //add up new elemtns
        for(int i = 0 ; i < reorderedList.size() ; i++){
            student_list.add(reorderedList.get(i));
        }
        return student_list;
    }

    private class IndexPair{
        int base_list_index;
        int value;
        IndexPair(int bindex, int v){
            base_list_index = bindex;
            value = v;
        }
    }
    private class IndexPairComparator implements Comparator<IndexPair> {
        @Override
        public int compare(IndexPair o1, IndexPair o2) {
            return o2.value - o1.value;
        }
    }

    private int getValues(StudentBean student) {
        int genderValue = 0;
        int languageValue = 0;
        int nationalityValue = 0;
        int dietaryValue = 0;
        int sleepTimeValue = 0;
        int wakeTimeValue = 0;

        if (gender              && (currentUser.isMale() && student.isMale()))             genderValue      = 100;
        if (nationality         && (currentUser.getNationality()        == student.getNationality()))        nationalityValue = 100;
        if (dietary_restriction && (currentUser.getDietaryRestriction() == student.getDietaryRestriction())) dietaryValue     = 100;
        if (language            && (currentUser.getFirstLanguage()      == student.getFirstLanguage()))      languageValue    = 100;

        if (sleepTime) sleepTimeValue = 100 - Math.abs((currentUser.getSleepTime()  - student.getSleepTime() )*100/2400);
        if (wakeTime)  wakeTimeValue  = 100 - Math.abs((currentUser.getWakeupTime() - student.getWakeupTime())*100/2400);

        return genderValue + languageValue + nationalityValue + dietaryValue + sleepTimeValue + wakeTimeValue;
    }



}
