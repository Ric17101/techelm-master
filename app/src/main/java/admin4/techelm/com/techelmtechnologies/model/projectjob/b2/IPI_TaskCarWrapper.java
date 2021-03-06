/*
 * Copyright (c) 2016 Richard C.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package admin4.techelm.com.techelmtechnologies.model.projectjob.b2;

import android.os.Parcel;
import android.os.Parcelable;


public class IPI_TaskCarWrapper implements Parcelable {

    public static final Creator<IPI_TaskCarWrapper> CREATOR = new Creator<IPI_TaskCarWrapper>() {
        @Override
        public IPI_TaskCarWrapper createFromParcel(Parcel in) {
            return new IPI_TaskCarWrapper(in);
        }

        @Override
        public IPI_TaskCarWrapper[] newArray(int size) {
            return new IPI_TaskCarWrapper[size];
        }
    };

    //private variables 12
    /*
    GET : http://enercon714.firstcomdemolinks.com/sampleREST/ci-rest-api-techelm/index.php/projectjob/get_ipi_correctiveAction?projectjob_id=1&form_type=PW
        {
           "projectlist_ipi_correctiveactions":[
              {
                 "id":"1",
                 "projectjob_id":"1",
                 "serial_no":"1",
                 "car_no":"test",
                 "description":"test desc1.1",
                 "target_remedy_date":"2017-05-20",
                 "completion_date":"0000-00-00",
                 "remarks":"test",
                 "disposition":"test",
                 "status_flag":"0",
                 "date_updated":"0000-00-00 00:00:00",
                 "form_type":"PW"
              }
           ]
        }
    */
    private int _id;
    private int _projectjob_id;
    private String _serial_no;
    private String _car_no;
    private String _description;
    private String _target_remedy_date;
    private String _completion_date;
    private String _remarks;
    private String _disposition;
    private String _status_flag;
    private String _date_updated;
    private String _form_type;

    // Empty constructor
    public IPI_TaskCarWrapper() { }

    protected IPI_TaskCarWrapper(Parcel in) {
        _id = in.readInt();
        _projectjob_id = in.readInt();
        _serial_no = in.readString();
        _car_no = in.readString();
        _description = in.readString();
        _target_remedy_date = in.readString();
        _completion_date = in.readString();
        _remarks = in.readString();
        _disposition = in.readString();
        _status_flag = in.readString();
        _date_updated = in.readString();
        _form_type = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(_id);
        dest.writeInt(_projectjob_id);
        dest.writeString(_serial_no);
        dest.writeString(_car_no);
        dest.writeString(_description);
        dest.writeString(_target_remedy_date);
        dest.writeString(_completion_date);
        dest.writeString(_remarks);
        dest.writeString(_disposition);
        dest.writeString(_status_flag);
        dest.writeString(_date_updated);
        dest.writeString(_form_type);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String toString() {
        return "\nID : " + this._id +
            "\n_projectjob_id : " + this._projectjob_id +
            "\n_serial_no : " + this._serial_no +
            "\n_car_no : " + this._car_no +
            "\n_description : " + this._description +
            "\n_target_remedy_date : " + this._target_remedy_date +
            "\n_completion_date : " + this._completion_date +
            "\n_remarks : " + this._remarks +
            "\n_disposition : " + this._disposition +
            "\n_status_flag : " + this._status_flag +
            "\n_date_updated : " + this._date_updated +
            "\n_form_type : " + this._form_type
            ;
    }

    /**
     * GET AND SET METHOD
     */

    public int getID() {
        return this._id;
    }
    public void setID(int id) {
        this._id = id;
    }

    public int getProjectJob_ID() {
        return this._projectjob_id;
    }
    public void setProjectJob_ID(int id) { this._projectjob_id = id; }

    public String getSerialNo() {
        return this._serial_no;
    }
    public void setSerialNo(String val) {
        this._serial_no = val;
    }

    public String getCarNo() {
        return this._car_no;
    }
    public void setCarNo(String data) {
        this._car_no = data;
    }

    public String getDescription() {
        return this._description;
    }
    public void setDescription(String num) { this._description = num; }

    public String getTargetRemedyDate() {
        return this._target_remedy_date;
    }
    public void setTargetRemedyDate(String val) {
        this._target_remedy_date = val;
    }

    public String getCompletionDate() {
        return this._completion_date;
    }
    public void setCompletionDate(String val) {
        this._completion_date = val;
    }

    public String getRemarks() {
        return this._remarks;
    }
    public void setRemarks(String data) {
        this._remarks = data;
    }

    public String getDisposition() {
        return this._disposition;
    }
    public void setDisposition(String val) { this._disposition = val; }

    public String getStatusFlag() {
        return this._status_flag;
    }
    public void setStatusFlag(String val) { this._status_flag = val; }

    public String getDateUpdated() {
        return this._date_updated;
    }
    public void setDateUpdated(String val) { this._date_updated = val; }

    public String getFormType() {
        return this._form_type;
    }
    public void setFormType(String val) { this._form_type = val; }

}