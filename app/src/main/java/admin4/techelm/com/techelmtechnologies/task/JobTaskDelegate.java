package admin4.techelm.com.techelmtechnologies.task;

import admin4.techelm.com.techelmtechnologies.model.servicejob.ServiceJobNewReplacementPartsRatesWrapper;
import admin4.techelm.com.techelmtechnologies.model.servicejob.ServiceJobWrapper;
import admin4.techelm.com.techelmtechnologies.utility.json.ConvertJSON_SJ_Complaints;

import java.util.ArrayList;

public interface JobTaskDelegate {
    void showOrHideProgress(Boolean value);

    void proceedViewPagerActivity(
        ServiceJobWrapper atServicejob,
        String atStatus,
        ArrayList<ServiceJobNewReplacementPartsRatesWrapper> atRateList,
        ConvertJSON_SJ_Complaints atComplaints
    );

    void showSnackBar();
}
