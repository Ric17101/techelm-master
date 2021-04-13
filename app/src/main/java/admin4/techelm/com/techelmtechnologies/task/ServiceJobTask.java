package admin4.techelm.com.techelmtechnologies.task;

import admin4.techelm.com.techelmtechnologies.model.servicejob.ServiceJobNewReplacementPartsRatesWrapper;
import admin4.techelm.com.techelmtechnologies.model.servicejob.ServiceJobWrapper;
import admin4.techelm.com.techelmtechnologies.utility.json.ConvertJSON_SJ;
import admin4.techelm.com.techelmtechnologies.utility.json.ConvertJSON_SJ_Complaints;
import android.os.AsyncTask;
import org.json.JSONException;

import java.util.ArrayList;

public class ServiceJobTask extends AsyncTask<Void, Void, Boolean> {
    // Instance Variables
    private JobTaskDelegate task;
    private ServiceJobWrapper atServicejob;
    private String atStatus;
    private String atStartTaskResponse;
    private String atComplaintsResponse;

    // Resulting Data
    private ArrayList<ServiceJobNewReplacementPartsRatesWrapper> atRateList;
    ConvertJSON_SJ_Complaints atComplaints;

    public ServiceJobTask(ServiceJobWrapper serviceJob, String status, String startTaskResponse, String complaintsResponse, JobTaskDelegate task) {
        this.task = task;
        this.atServicejob = serviceJob;
        this.atStatus = status;
        this.atStartTaskResponse = startTaskResponse;
        this.atComplaintsResponse = complaintsResponse;
    }

    @Override
    protected void onPreExecute() {
        // showOrHideProgress(true);
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        try {
            this.atRateList = new ConvertJSON_SJ().getResponseJSONPartReplacementRate(this.atStartTaskResponse);
            this.atComplaints = new ConvertJSON_SJ_Complaints(this.atComplaintsResponse);
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }

        try {
            // Simulate network access.
            Thread.sleep(2000);
            // init_DrawerNav();
        } catch (InterruptedException e) {
            return false;
        }
        // TO DO: register the new account here.
        return true;
    }

    @Override
    protected void onPostExecute(final Boolean aSuccess) {
        if (aSuccess) {
            task.proceedViewPagerActivity(atServicejob, atStatus, this.atRateList, this.atComplaints);
        } else {
            onCancelled();
        }

        task.showOrHideProgress(false);
    }

    @Override
    protected void onCancelled() {
        // Will Prpomt User that Prices for the form isnot ok
        task.showSnackBar();
        task.showOrHideProgress(false);
    }
}