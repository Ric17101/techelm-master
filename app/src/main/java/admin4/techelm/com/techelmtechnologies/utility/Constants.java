package admin4.techelm.com.techelmtechnologies.utility;

import admin4.techelm.com.techelmtechnologies.BuildConfig;

/**
 * Created by admin 4 on 03/04/2017.
 * FINAL CLASS
 * USE AS:
 * import static MyValues.*
     //...

     if(variable.equals(VALUE1)){
     //...
     }
 *
 */

    /*
        0 - New - Begin Task
        1 - unsigned - Can be Edited
        2 - Pending
        3 - Completed

        Calendar and Service Jobs TAB :
            EXAMPLE : user click 14/5/2017
            -all service jobs at 14/5/2017

        Unsigned Services Form TAB:
            - all service jobs where status = ‘unsigned’

        Service JOB Form TAB:
            - SHOW ALL SERVICE JOBS
      */
public final class Constants {

    public Constants() {} // Prevents instanciation of myself and my subclasses

    public static final String VERSION = BuildConfig.VERSION_NAME;
    public static String SERVICE_JOB_DETAILS_URL =
            "http://enercon714.firstcomdemolinks.com/sampleREST/ci-rest-api-techelm/index.php/";
    public static String SERVICE_JOB_UPLOAD_URL = SERVICE_JOB_DETAILS_URL + "servicejob/";

    // ACTION, action is being called in the MainActivity
    public static final int ACTION_BEGIN_JOB_SERVICE = 0;
    public static final int ACTION_EDIT_JOB_SERVICE = 1;
    public static final int ACTION_VIEW_DETAILS = 2;
    public static final int ACTION_ALREADY_COMPLETED = 3;
    public static final int ACTION_ALREADY_ON_PROCESS = 4; // This already being called as Continue whenever service

    // STATUS of Service Jobs
    public static final String SERVICE_JOB_NEW = "0";
    public static final String SERVICE_JOB_UNSIGNED = "1";
    public static final String SERVICE_JOB_PENDING = "2";
    public static final String SERVICE_JOB_COMPLETED = "3";
    public static final String SERVICE_JOB_ON_PROCESS = "4";

    // Intent putExtra KEY, used to decode data passed to ServiceJobViewPagerActivity
    /** Type is int. */
    public static final String SERVICE_JOB_ID_KEY = "SERVICE_ID";
    /** Type is Object ServiceJobWrapper. */
    public static final String SERVICE_JOB_SERVICE_KEY = "SERVICE_JOB";
    /** Type is String. */
    public static final String SERVICE_JOB_PREVIOUS_STATUS_KEY = "SERVICE_JOB_PREVIOUS_STATUS";
    /** Type is String. */
    public static final String SERVICE_JOB_TAKEN_KEY = "TAKEN";
    /** Type is Object ServiceJobNewReplacementPartsRatesWrapper */
    public static final String SERVICE_JOB_PARTS_REPLACEMENT_LIST = "REPLACEMENT_LIST";
    /** Type is String. */
    public static final String PROJECT_JOB_FORM_TYPE_KEY = "TYPE_OF_FORM";

    /**** SECTION B ****/
    // ACTION PROJECT JOB
    public static final int ACTION_CHOOSE_FORM = 10; // PROJECT JOB, Choosing the form
    public static final int ACTION_START_TASK = 11;
    public static final int ACTION_CONTINUE_TASK = 12;
    public static final int ACTION_VIEW_TASK = 13;

    // STATUS of Project Jobs, the Task Button
    public static final String PROJECT_JOB_CHOOSE_FORM = "10";
    public static final String PROJECT_JOB_START_TASK = "11";
    public static final String PROJECT_JOB_CONTINUE_TASK = "12";

    // TYPE OF FORM
    public static final int PROJECT_JOB_FORM_B1 = 1;
    public static final int PROJECT_JOB_FORM_B2 = 2;
    public static final int PROJECT_JOB_FORM_B3 = 3;

    // FRAGMENT POSITION
    public static final int PROJECT_JOB_FRAGMENT_POSITION_1 = 1;
    public static final int PROJECT_JOB_FRAGMENT_POSITION_2 = 2;
    public static final int PROJECT_JOB_FRAGMENT_POSITION_3 = 3;


}
