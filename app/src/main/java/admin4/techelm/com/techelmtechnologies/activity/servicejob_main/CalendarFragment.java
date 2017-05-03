package admin4.techelm.com.techelmtechnologies.activity.servicejob_main;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.marcohc.robotocalendar.RobotoCalendarView;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import admin4.techelm.com.techelmtechnologies.R;
import admin4.techelm.com.techelmtechnologies.adapter.SJ_CalendarListAdapter;
import admin4.techelm.com.techelmtechnologies.db.servicejob.CalendarSJDBUtil;
import admin4.techelm.com.techelmtechnologies.task.TaskCanceller;
import admin4.techelm.com.techelmtechnologies.utility.SnackBarNotificationUtil;
import admin4.techelm.com.techelmtechnologies.utility.json.ConvertJSON_SJ;
import admin4.techelm.com.techelmtechnologies.utility.json.JSONHelper;
import admin4.techelm.com.techelmtechnologies.model.servicejob.ServiceJobWrapper;
import admin4.techelm.com.techelmtechnologies.webservice.WebServiceRequest;
import admin4.techelm.com.techelmtechnologies.webservice.command.GetCommand;
import admin4.techelm.com.techelmtechnologies.webservice.command.PostCommand;
import admin4.techelm.com.techelmtechnologies.webservice.interfaces.OnServiceListener;
import admin4.techelm.com.techelmtechnologies.webservice.model.WebResponse;
import admin4.techelm.com.techelmtechnologies.webservice.model.WebServiceInfo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static admin4.techelm.com.techelmtechnologies.utility.Constants.LIST_DELIM;
import static admin4.techelm.com.techelmtechnologies.utility.Constants.SERVICE_JOB_BY_MONTH_URL;
import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class CalendarFragment extends Fragment implements
        RobotoCalendarView.RobotoCalendarListener,
        SJ_CalendarListAdapter.OnItemClickListener // not used onClick Interface
{
    private static final String SERVICE_JOB_URL =
            "http://enercon714.firstcomdemolinks.com/sampleREST/ci-rest-api-techelm/index.php/servicejob/";
    private static final String TAG = CalendarFragment.class.getSimpleName();
    private static final int REQUEST_CODE = 1234;

    private RobotoCalendarView robotoCalendarView;
    private SlidingUpPanelLayout mLayout;

    private Context mContext;
    private TextView name;
    private TextView textViewCalendarResult;
    private SJ_CalendarListAdapter mListAdapter;
    private RecyclerView mCalendarResultsList;
    private RecyclerView.LayoutManager mLayoutManager;

    // Swipe Set up
    private SwipeRefreshLayout swipeRefreshCalendarLayout;

    private List<ServiceJobWrapper> results = null;
    private List<String> SERVICE_JOB = null;


    // C. Task Killer
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private CalendarSJTask_RenderList mAuthCalendarTask = null;
    private ParseJasonToDateDotsTask mAuthDotTask = null;
    //private TaskCanceller mTaskCanceller;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = /*(View)*/ inflater.inflate(
                R.layout.calendar_activity, container, false);
        setContext(container.getContext());

        setUpCalendarView(view);

        setupSlidingPanel(view);

        setUpRecyclerView(view);

        setupResultsList(view);

        setupSwipeRefreshCalendarLayout(view);

        if (results == null) {
            // populateCardList();
        }

        return view;
    }

    // TODO : Test this with CalendarServiceJobTask.java without calling Native Class UnsignedFormSJTask_RenderList
    /*@Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            callback = (AdapterViewBindingAdapter.OnItemSelected) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnItemSelected");
        }
    }*/

    /********* SLIDING PANEL *********/
    private void setupSlidingPanel(View view) {
        /** Listeners and Instanstiation */
        mLayout = (SlidingUpPanelLayout) view.findViewById(R.id.sliding_layout_calendar);
        mLayout.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                Log.i(TAG, "onPanelSlide, offset " + slideOffset);
            }

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
                Log.i(TAG, "onPanelStateChanged " + newState);
            }
        });
        mLayout.setFadeOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                collapseCalendarPanel(SlidingUpPanelLayout.PanelState.COLLAPSED);
            }
        });

        /** Set up Sliding Panel height to ANCHORED... */
        mLayout.setAnchorPoint(0.7f);
        collapseCalendarPanel(SlidingUpPanelLayout.PanelState.COLLAPSED);
    }

    private void collapseCalendarPanel(SlidingUpPanelLayout.PanelState state) {
//        mLayout.setPanelState(state);
        mLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        System.out.println("CalendarFragment: I'm on the onCreate");
    }
    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    /**
     * These Two Lines should be included on every Fragment to maintain the state and donnot load again
     *
     * @param outState
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // setRetainInstance(true);
        System.out.println("CalendarFragment: I'm on the onSaveInstanceState");
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (results == null) { // If Data is Null then fetch the Data List again

        } else { // Restore the Data List again
            mListAdapter.swapData(results);
        }
        System.out.println("CalendarFragment: I'm on the onActivityCreated");
    }

    private void setContext(Context c) {
        mContext = c;
    }

    /***** SWIPE REFRESH LAYOUT *****/
    private void setupSwipeRefreshCalendarLayout(View view) {
        swipeRefreshCalendarLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshCalendarLayout);
        swipeRefreshCalendarLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                displayDotsPerMonth("setupSwipeRefreshCalendarLayout");
                if (robotoCalendarView != null) {
                    renderListFromCalendar(robotoCalendarView.getCurrentCalendar());
                } else {
                    renderListFromCalendar(Calendar.getInstance());
                }
            }
        });
        swipeRefreshCalendarLayout.setColorSchemeResources(
                R.color.refresh_progress_1,
                R.color.refresh_progress_2,
                R.color.refresh_progress_3);
    }
    private void hideSwipeRefreshing() {
        if (swipeRefreshCalendarLayout != null)
            swipeRefreshCalendarLayout.setRefreshing(false);
    }

    public void setUpRecyclerView(View upRecyclerView) {
        mCalendarResultsList = (RecyclerView) upRecyclerView.findViewById(R.id.calendar_service_job_list);
        textViewCalendarResult = (TextView) upRecyclerView.findViewById(R.id.textViewCalendarResult);
        textViewCalendarResult.setVisibility(View.GONE);
    }
    public void setupResultsList(View view) {
        mListAdapter = new SJ_CalendarListAdapter(view.getContext());
        mCalendarResultsList.setAdapter(mListAdapter);
        mLayoutManager = new LinearLayoutManager(view.getContext());
        mCalendarResultsList.setLayoutManager(mLayoutManager);
    }

    /**
     * Set up the Calendar View Listener on Click of the Date/Month
     *
     * @param view
     */
    private void setUpCalendarView(View view) {
        // Gets the calendar from the view
        robotoCalendarView = (RobotoCalendarView) view.findViewById(R.id.robotoCalendarPicker);

        displayDotsPerMonth("setUpCalendarView");
        name = (TextView) view.findViewById(R.id.name);
        renderListFromCalendar(Calendar.getInstance());

        // Set listener, in this case, the same activity
        robotoCalendarView.setRobotoCalendarListener(this);

        robotoCalendarView.setShortWeekDays(false);

        robotoCalendarView.showDateTitle(true);

        robotoCalendarView.updateView();
    }

    private void setUpCalendarView_OLD(View view) {
        // Gets the calendar from the view
        robotoCalendarView = (RobotoCalendarView) view.findViewById(R.id.robotoCalendarPicker);
        displayDotsPerMonth("setUpCalendarView");
        renderListFromCalendar(Calendar.getInstance());

        // Set listener, in this case, the same activity
        robotoCalendarView.setRobotoCalendarListener(this);

        robotoCalendarView.setShortWeekDays(false);

        robotoCalendarView.showDateTitle(true);

        robotoCalendarView.updateView();


    }

    public String convertLongDateToSimpleDate(Calendar daySelectedCalendar) {
        Date date = daySelectedCalendar.getTime();
        String formattedDate = new SimpleDateFormat("yyyy-MM-dd").format(date);
        System.out.println("DATE Clicked: " + formattedDate);
        return formattedDate;
    }

    private void renderListFromCalendar(Calendar daySelectedCalendar) {
        String formattedDate = convertLongDateToSimpleDate(daySelectedCalendar);

        mAuthCalendarTask = new CalendarSJTask_RenderList(formattedDate, "", mContext);
        //mTaskCanceller = new TaskCanceller(mAuthCalendarTask);
        new TaskCanceller(mAuthCalendarTask).setWait(getActivity());
        mAuthCalendarTask.execute((Void) null);
        name.setText(formattedDate);
    }

    @Override
    public void onDayClick(Calendar daySelectedCalendar) {
        System.out.println("onDayClick: " + daySelectedCalendar.getTime());
        renderListFromCalendar(daySelectedCalendar);
        collapseCalendarPanel(SlidingUpPanelLayout.PanelState.COLLAPSED); // Collapse the panel
    }

    @Override
    public void onDayLongClick(Calendar daySelectedCalendar) {
        System.out.println("onDayLongClick: " + daySelectedCalendar.getTime());
        renderListFromCalendar(daySelectedCalendar);
    }

    @Override
    public void onRightButtonClick() {
        displayDotsPerMonth("onRightButtonClick");
    }

    @Override
    public void onLeftButtonClick() {
        displayDotsPerMonth("onLeftButtonClick");
    }

    /**
     * Display Dots on the Calendar on Click
     * @param msgStr
     */
    private void displayDotsPerMonth(String msgStr) {
        Date date;
        if (robotoCalendarView != null) { // If roboto is not Set,,, this is not realy gonna be false, just to make sure
            date = robotoCalendarView.getCurrentCalendar().getTime();
        } else {
            date = Calendar.getInstance().getTime();
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        new CalendarServiceJobDatesDots_POST().post(month +1, year);
        System.out.println(msgStr + "! MONTH " + month + "YEAR " + year);
    }

    /**
     * To be implemented at Fragments later
     * or Use a another class uusing ProjectJobListener for more modularity
     */
    private class CalendarServiceJobDatesDots_POST {
        private PostCommand postCommand;
        public static final String TAG = "CALENDAR_POST";

        public void cancel(View v) {
            postCommand.cancel();
        }

        public void post(final int month, final int year) {
        /*web info*/
            WebServiceInfo webServiceInfo = new WebServiceInfo();
            // String url = SERVICE_JOB_URL + "get_date_services_by_month";
            webServiceInfo.setUrl(SERVICE_JOB_BY_MONTH_URL);

        /*add parameter*/
            webServiceInfo.addParam("month", month+"");
            webServiceInfo.addParam("year", year+"");

        /*postStartDate command*/
            postCommand = new PostCommand(webServiceInfo);

        /*request*/
            WebServiceRequest webServiceRequest = new WebServiceRequest(postCommand);
            webServiceRequest.execute();
            webServiceRequest.setOnServiceListener(new OnServiceListener() {
                @Override
                public void onServiceCallback(WebResponse response) {
                    Log.e(TAG, "WebResponse: " + response.getStringResponse());
                    // textView23.setText(response.getStringResponse());
                    // TODO: Add this inside the Asynctask
                    //getListSJ(response.getStringResponse());

                    mAuthDotTask = new ParseJasonToDateDotsTask();
                    new TaskCanceller(mAuthDotTask).setWait(getActivity());
                    mAuthDotTask.execute(response.getStringResponse());
                }
            });
        }
    }

    /**
     * Called on Change of Date CalendarView Month DOTS
     */
    private class ParseJasonToDateDotsTask extends AsyncTask<String, Void, List<ServiceJobWrapper>> {

        private boolean hasResutFlag = true; // Set to 1 if no aResponse
        /**
         * Converstion of JSON string to ServiceJob Wrapper
         * TODO: Need to store at sqlite on edit/start
         * DOING in Background...
         * @param JSONResult
         * @return
         */
        private ArrayList<ServiceJobWrapper> getListSJ(String JSONResult) {
            if (JSONResult == null || JSONResult.equals("")) { // No Connection or server is off
                hasResutFlag = false;
                return null;
            }
            try {
                ConvertJSON_SJ cJSON = new ConvertJSON_SJ();
                ArrayList<ServiceJobWrapper> resultList = cJSON.parseServiceListJSON(JSONResult);
                hasResutFlag = cJSON.hasResult();
                // return (hasResutFlag ? resultList : null);
                return resultList;
            } catch (JSONException e) {
                e.printStackTrace();
                // mCallback.onHandleShowDetails(e.toString());
            }
            return null;
        }

        protected List<ServiceJobWrapper> doInBackground(String... response) {
            if (response[0] == "")
                return null;
            return getListSJ(response[0]);
        }

        protected void onPostExecute(List<ServiceJobWrapper> list) {
            if (list != null) {
                SimpleDateFormat formatter = new SimpleDateFormat("yyy-MM-dd");
                Calendar calendar = Calendar.getInstance();

                for (ServiceJobWrapper sjw : list) {
                    try {
                        Date date = formatter.parse(sjw.getStartDate()); // Proper conversion of Date
                        calendar.setTime(date);
                        calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DATE));
                        robotoCalendarView.markCircleImage2(calendar);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                if (!hasResutFlag) // Has no aResponse, else do nothing
                    noInternetSnackBar();
                else
                    noResultSnackBar();
            }
            hideSwipeRefreshing();
        }

        @Override
        protected void onCancelled() {
            noResultTryAgain();
            hideSwipeRefreshing();
            Log.i(TAG, "onCancelled hideSwipeRefreshing() new PJTask_RenderList()");
        }
    }


    /********* SLIDING PANEL END *********/

    private void activityResultIntent() {
        Intent check = new Intent();
        startActivityForResult(check, REQUEST_CODE);
    }

    /**
     * Setting up for the Result OnClick inside the CardView on the List Adapter
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {

            case RESULT_OK:
                break;

            case RESULT_CANCELED:
                // ... Handle this situation
                break;
            default:
                break;
        }
    }

    private void populateCardList() {
        results = new CalendarSJDBUtil(mContext).getAllDetailsOfServiceJob();
        mCalendarResultsList.setHasFixedSize(true);
        mCalendarResultsList.setLayoutManager(new LinearLayoutManager(mContext));
        mCalendarResultsList.setItemAnimator(new DefaultItemAnimator());
        mListAdapter.swapData(results);
    }

    /*private class UpdateJobServiceTask extends AsyncTask<String, Void, String> {

        private View mView;

        public UpdateJobServiceTask(View view) {
            mView = view;
        }

        @Override
        protected String doInBackground(String... params) {
            populateCardList();
            return null;
        }

        @Override
        protected void onPostExecute(String aResponse) {
            //textView.setText(aResponse);
        }
    }*/

    private void noInternetSnackBar() {
        mCalendarResultsList.setVisibility(View.GONE);
        textViewCalendarResult.setText(R.string.noInternetPrompt);
        textViewCalendarResult.setVisibility(View.VISIBLE);
        SnackBarNotificationUtil
                .setSnackBar(getActivity().findViewById(android.R.id.content), getResources().getString(R.string.noInternetConnection))
                .setColor(getResources().getColor(R.color.colorPrimary1))
                .show();
    }

    private void noResultSnackBar() {
        mCalendarResultsList.setVisibility(View.GONE);
        textViewCalendarResult.setText("No service job this time.");
        textViewCalendarResult.setVisibility(View.VISIBLE);
    }

    private void noResultTryAgain() {
        mCalendarResultsList.setVisibility(View.GONE);
        textViewCalendarResult.setText("Try again later.");
        textViewCalendarResult.setVisibility(View.VISIBLE);
    }

    public void messageFromTask(String message) {
        System.out.println("CalendarFragment: I'm on the messageFromTask " + message);
    }

    @Override
    public void onClick(ServiceJobWrapper colorWrapper) {
        System.out.println(TAG + "CalendarFragment: I'm on the onClick Interface from CalendarListAddapter");
    }


    /**
     * Called on click of Date CAlendar the render a list of Services at CardView
     * Show a list of SJ retrieved from API
     */
    private class CalendarSJTask_RenderList extends AsyncTask<Void, Void, List<ServiceJobWrapper>> {

        private String mDate;
        private String mID;
        private int resultStatus = 0;

        private GetCommand getCommand;
        private ArrayList<String> serviceList = new ArrayList<String>();

        public CalendarSJTask_RenderList(String date, String id, Context context) {
            mDate = date;
            mID = id;
            mContext = context;
            // System.gc();
        }
        private String getDetailsLink() {
            StringBuilder sb = new StringBuilder();
            sb.append(SERVICE_JOB_URL);
            sb.append("get_date_services/" + mDate);
            return sb.toString();
        }

        public String getServiceJobLink() {
            StringBuilder sb = new StringBuilder();
            sb.append(SERVICE_JOB_URL);
            sb.append("detail/" + mID);
            return sb.toString();
        }

        /**
         *
         * @param JSONResult
         * @return
         *      null - no data
         *      '' - no internet connection/ server error
         *      String - successful aResponse
         *      TODO: Do this on the ConvertJSON_SJ.java
         *          Same with Unsigned and Service Job classes
         */
        private String parseServiceListJSON(String JSONResult) {
            if (JSONResult == null || JSONResult == "")
                return "";
            try {
                JSONObject json = new JSONObject(JSONResult);
                String str = "";

                JSONArray jsonArray = json.getJSONArray("servicelist");
                int jsonLen = json.getJSONArray("servicelist").length();
                if (jsonLen == 0)
                    return "null";

                str += "names: " + jsonArray.getJSONObject(0).names();
                str += "\n--------\n";
                str += "jsonA length = " + jsonLen;
                str += "\n--------\n";
                str += "ID: " + jsonArray.getJSONObject(0).getString("id");
                str += "\n--------\n";
                str += "Service No: " + jsonArray.getJSONObject(0).getString("service_no");
                str += "\n--------\n";
                str += "Customer ID: " + jsonArray.getJSONObject(0).getString("customer_id");
                str += "\n--------\n";
                str += "Service ID: " + jsonArray.getJSONObject(0).getString("service_id");
                str += "\n--------\n";
                str += "Engineer id: " + jsonArray.getJSONObject(0).getString("engineer_id");
                str += "\n--------\n";
                str += "Price ID: " + jsonArray.getJSONObject(0).getString("price_id");
                str += "\n--------\n";
                str += "Complaint: " + jsonArray.getJSONObject(0).getString("complaint");
                str += "\n--------\n";
                str += "Remarks: " + jsonArray.getJSONObject(0).getString("remarks");
                str += "\n--------\n";
                str += "Equipment Type: " + jsonArray.getJSONObject(0).getString("equipment_type");
                str += "\n--------\n";
                str += "Serial No: " + jsonArray.getJSONObject(0).getString("serial_no");
                str += "\n--------\n";
                str += "Start Date: " + jsonArray.getJSONObject(0).getString("start_date");
                str += "\n--------\n";
                str += "End Date: " + jsonArray.getJSONObject(0).getString("end_date");
                str += "\n--------\n";
                str += "Status: " + jsonArray.getJSONObject(0).getString("status");

                Log.d(TAG, "parseJSON: " + str);

                // jsonLen += 1;
                int i = 0;
                do { // 24 + 2
                    StringBuilder jsonRes = new StringBuilder();
                    jsonRes.append(jsonArray.getJSONObject(i).getString("id"))
                            .append(LIST_DELIM)
                            .append(jsonArray.getJSONObject(i).getString("service_no"))
                            .append(LIST_DELIM)
                            .append(jsonArray.getJSONObject(i).getString("customer_id"))
                            .append(LIST_DELIM)
                            .append(jsonArray.getJSONObject(i).getString("service_id"))
                            .append(LIST_DELIM)
                            .append(jsonArray.getJSONObject(i).getString("engineer_id"))
                            .append(LIST_DELIM)
                            .append(jsonArray.getJSONObject(i).getString("price_id"))
                            .append(LIST_DELIM)
                            .append(jsonArray.getJSONObject(i).getString("complaint"))
                            .append(LIST_DELIM)
                            .append(jsonArray.getJSONObject(i).getString("remarks"))
                            .append(LIST_DELIM)
                            .append(jsonArray.getJSONObject(i).getString("remarks_before"))
                            .append(LIST_DELIM)
                            .append(jsonArray.getJSONObject(i).getString("remarks_after"))
                            .append(LIST_DELIM)
                            .append(jsonArray.getJSONObject(i).getString("equipment_type"))
                            .append(LIST_DELIM)
                            .append(jsonArray.getJSONObject(i).getString("serial_no"))
                            .append(LIST_DELIM)
                            .append(jsonArray.getJSONObject(i).getString("start_date").split(" ")[0])
                            .append(LIST_DELIM)
                            .append(jsonArray.getJSONObject(i).getString("end_date").split(" ")[0])
                            .append(LIST_DELIM)
                            .append(jsonArray.getJSONObject(i).getString("status"))
                            .append(LIST_DELIM)
                            .append(jsonArray.getJSONObject(i).getString("contract_servicing"))
                            .append(LIST_DELIM)
                            .append(jsonArray.getJSONObject(i).getString("warranty_servicing"))
                            .append(LIST_DELIM)
                            .append(jsonArray.getJSONObject(i).getString("charges"))
                            .append(LIST_DELIM)
                            .append(jsonArray.getJSONObject(i).getString("contract_repair"))
                            .append(LIST_DELIM)
                            .append(jsonArray.getJSONObject(i).getString("warranty_repair"))
                            .append(LIST_DELIM)
                            .append(jsonArray.getJSONObject(i).getString("others"))
                            .append(LIST_DELIM)
                            .append(jsonArray.getJSONObject(i).getString("type_of_service"))
                            .append(LIST_DELIM)
                            .append(jsonArray.getJSONObject(i).getString("signature_name"))
                            .append(LIST_DELIM)
                            .append(jsonArray.getJSONObject(i).getString("start_date_task"))
                            .append(LIST_DELIM)
                            .append(jsonArray.getJSONObject(i).getString("end_date_task"))
                            .append(LIST_DELIM)
                            .append(jsonArray.getJSONObject(i).getString("fullname"))
                            .append(LIST_DELIM)
                            .append(jsonArray.getJSONObject(i).getString("job_site"))
                            .append(LIST_DELIM)
                            .append(jsonArray.getJSONObject(i).getString("fax"))
                            .append(LIST_DELIM)
                            .append(jsonArray.getJSONObject(i).getString("phone_no"))
                            .append(LIST_DELIM)
                            .append(jsonArray.getJSONObject(i).getString("engineer_name"))
                            .append(LIST_DELIM)
                    ;

                    serviceList.add(jsonRes.toString());
                    i++;
                } while (jsonLen > i);

                return "ok";
            } catch (JSONException e) {
                e.printStackTrace();
                // mCallback.onHandleShowDetails(e.toString());
            }
            return "";
        }

        // TO DO: Network API activity
        public void postLogin(String email, String password) {
            /*web info*/
            WebServiceInfo webServiceInfo = new WebServiceInfo();
            // String url = "http://jsonplaceholder.typicode.com/posts";
            String url = "http://enercon714.firstcomdemolinks.com/sampleREST/simple-codeigniter-rest-api-master/index.php/auth/user?user=@dev&password=password";
            //String url = "http://enercon714.firstcomdemolinks.com/sampleREST/simple-codeigniter-rest-api-master/index.php/auth/user";
            webServiceInfo.setUrl(url);

            /*add parameter*/
            //webServiceInfo.addParam("user", email);
            //webServiceInfo.addParam("password", password);
            // webServiceInfo.addParam("userId", "2");

            /*postStartDate command*/
            getCommand = new GetCommand(webServiceInfo);

            //mCallback.onHandleShowDetails("2");
            /*request*/
            WebServiceRequest webServiceRequest = new WebServiceRequest(getCommand);
            webServiceRequest.execute();
            webServiceRequest.setOnServiceListener(new OnServiceListener() {
                @Override
                public void onServiceCallback(WebResponse response) {
                    Log.e(TAG, "WebResponse: " + response.getStringResponse());
                    // textView23.setText(response.getStringResponse());
                    // SERVICE_JOB = response.getStringResponse();
                    //mCallback.onHandleShowDetails("3");
                    // parseJSON(response.getStringResponse());
                }
            });
        }

        @Override
        protected void onPreExecute() { super.onPreExecute(); }

        /**
         * resultStatus
         * 0 - Default no Internet
         * 1 - ok, with data
         * 2 - no response or no Data
         * 3 - no internet??? or blank reponse
         */
        @Override
        protected List<ServiceJobWrapper> doInBackground(Void... params) {
            String parsedServiceJob = "";
            try {
                parsedServiceJob = parseServiceListJSON(JSONHelper.GET(getDetailsLink()));
                if (parsedServiceJob.equals("ok")) {
                    ConvertJSON_SJ cJSON = new ConvertJSON_SJ();
                    ArrayList<ServiceJobWrapper> resultList =  cJSON.serviceJobList(serviceList);
                    resultStatus = (cJSON.hasResult() ? 1 : 3);
                    // return (resultStatus == 1 ? resultList : null);
                    return resultList;
                } else if (parsedServiceJob.equals("null")) {
                    resultStatus = 2;
                    return null;
                } else if (parsedServiceJob.equals("")) {
                    // NO CONNECTION
                    resultStatus = 3;
                    return null;
                } else {
                    Thread.sleep(2000); // Simulate network access.
                    return null; // Data Return is null or either no internet
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<ServiceJobWrapper> list) {
            switch (resultStatus) {
                case 1 :
                    results = list;
                    mCalendarResultsList.setItemAnimator(new DefaultItemAnimator());
                    mListAdapter.swapData(list);
                    mCalendarResultsList.setVisibility(View.VISIBLE);
                    textViewCalendarResult.setVisibility(View.GONE);
                    // populateCardList();
                    break;
                case 2 :
                    messageFromTask("There's no data on the Date " + mDate);
                    mCalendarResultsList.setVisibility(View.GONE);
                    textViewCalendarResult.setText("There's no service job on the Date \n" + mDate + ".");
                    // textViewCalendarResult.setText("No service job this time.");
                    textViewCalendarResult.setVisibility(View.VISIBLE);
                    break;
                case 3 :
                default :
                    messageFromTask("Error Check your Internet Connection " + mDate + " ID:" + mID);
                    noInternetSnackBar();
                    break;
            }
            hideSwipeRefreshing();
        }

        @Override
        protected void onCancelled() {
            noResultTryAgain();
            hideSwipeRefreshing();
            Log.i(TAG, "onCancelled hideSwipeRefreshing() new PJTask_RenderList()");
        }

    }
}
