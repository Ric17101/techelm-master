package admin4.techelm.com.techelmtechnologies.model.servicejob;

import android.os.Parcel;
import android.os.Parcelable;

public class ServiceJobComplaint_CFWrapper implements Parcelable {
    private int mId;                //id in database
    private int mSJCategoryID;      // servicejob_category_id in database
    private String mComplaint;      // Complaint
    private String mDateCreated;    // date created

    public ServiceJobComplaint_CFWrapper() { }

    public ServiceJobComplaint_CFWrapper(Parcel in) {
        mId = in.readInt();
        mSJCategoryID = in.readInt();
        mComplaint = in.readString();
        mDateCreated = in.readString();
    }

    public static final Creator<ServiceJobComplaint_CFWrapper> CREATOR = new Creator<ServiceJobComplaint_CFWrapper>() {
        public ServiceJobComplaint_CFWrapper createFromParcel(Parcel in) {
            return new ServiceJobComplaint_CFWrapper(in);
        }

        public ServiceJobComplaint_CFWrapper[] newArray(int size) {
            return new ServiceJobComplaint_CFWrapper[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mId);
        dest.writeInt(mSJCategoryID);
        dest.writeString(mComplaint);
        dest.writeString(mDateCreated);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public int getID() {
        return mId;
    }
    public void setId(int id) {
        mId = id;
    }

    public int getSJCategoryId() {
        return mSJCategoryID;
    }
    public void setSJCategoryId(int val) { mSJCategoryID = val; }

    public String getDateCreated() {
        return mDateCreated;
    }
    public void setDateCreated(String val) {
        mDateCreated = val;
    }

    public String getComplaint() { return mComplaint; }
    public void setComplaint(String val) {
        mComplaint = val;
    }

    public String toString() {
        return "ID : " + this.mId +
                "\nmSJCategoryID: " + this.mSJCategoryID +
                "\nmComplaint: " + this.mComplaint +
                "\nmDateCreated : " + this.mDateCreated;
    }
}