package gopackdev.arrivalpack.useraccount;

import gopackdev.arrivalpack.bluemixbean.StudentBean;

/**
 * Created by Chi-Han on 4/21/2016.
 */
public class CurrentUserHolder {
    private static CurrentUserHolder ourInstance = new CurrentUserHolder();
    private StudentBean sb = null;
    public static CurrentUserHolder getInstance() {
        return ourInstance;
    }

    private CurrentUserHolder() {
        sb = null;
    }

    /**
     * Check if the singleton has the cached student data or not.
     * @return
     */
    public boolean isHaveStudentBean() {
        return sb!=null;
    }

    /**
     * Get cached student data
     * @return
     */
    public StudentBean getStudentBean() {
        return sb;
    }

    /**
     * Set stduent data.
     * @param sb
     */
    public void setStudentBean(StudentBean sb) {
        this.sb = sb;
    }
}
