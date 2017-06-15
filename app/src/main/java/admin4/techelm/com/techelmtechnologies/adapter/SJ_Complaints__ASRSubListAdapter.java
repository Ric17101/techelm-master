package admin4.techelm.com.techelmtechnologies.adapter;

/**
 * Created by admin 4 on 16/02/2017.
 *
 */

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import admin4.techelm.com.techelmtechnologies.R;
import admin4.techelm.com.techelmtechnologies.model.servicejob.ServiceJobComplaintWrapper;

public class SJ_Complaints__ASRSubListAdapter extends RecyclerView.Adapter<SJ_Complaints__ASRSubListAdapter.ViewHolder> {

    private static final String LOG_TAG = SJ_Complaints__ASRSubListAdapter.class.getSimpleName();
    private final int CHECK_CODE = 0x1;
    private final int SHORT_DURATION = 1000;
    private List<ServiceJobComplaintWrapper> mDataSet = new ArrayList<>();
    private ServiceJobComplaintWrapper serviceJobDataSet;
    private CallbackInterface mCallback;
    private int mLastAnimatedItemPosition = -1;
    private int mLasItemPosition = 0;
    private Context mContext;

    private OnItemClickListener mItemsOnClickListener;
    private int counterOnBindViewHolder = 0;

    public SJ_Complaints__ASRSubListAdapter(Context context) {
        mContext = context;

        // .. Attach the interface
        try {
            mCallback = (CallbackInterface) context;
        } catch (ClassCastException ex) {
            //.. should log the error or throw and exception
            Log.e(LOG_TAG, "Must implement the ProjectJobListener in the Activity", ex);
        }
        // System.gc();
    }

    public SJ_Complaints__ASRSubListAdapter(List<ServiceJobComplaintWrapper> serviceJobList) {
        this.mDataSet = serviceJobList;
        notifyDataSetChanged();
    }

    public void swapData(List<ServiceJobComplaintWrapper> mNewDataSet) {
        mDataSet = mNewDataSet;
        notifyDataSetChanged();
    }

    public void setItemsOnClickListener(OnItemClickListener onClickListener) {
        this.mItemsOnClickListener = onClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.service_report_voice_record_list_item, parent, false);
        mContext = view.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        serviceJobDataSet = mDataSet.get(holder.getAdapterPosition());
        holder.textViewNameRecord.setText(serviceJobDataSet.getAction() + " recorded.");

        Log.d(LOG_TAG, "onBindViewHolder (" + ++counterOnBindViewHolder + ") = " +
                serviceJobDataSet.getAction() + " " + serviceJobDataSet.getCategory());
        if (mLastAnimatedItemPosition < position) {
            animateItem(holder.itemView);
            mLastAnimatedItemPosition = holder.getAdapterPosition(); // or mLastAnimatedItemPosition = position;
        }
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    private static int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }

    private void animateItem(View view) {
        view.setTranslationY(getScreenHeight());
        view.animate()
                .translationY(0)
                .setInterpolator(new DecelerateInterpolator(3.f))
                .setDuration(700)
                .start();
    }

    public interface CallbackInterface {

        /**
         * Callback invoked when clicked
         *
         * @param position - the position
         * @param ServiceJobComplaintWrapper - the text to pass back
         */
        void onHandleActionsSelection(int position, ServiceJobComplaintWrapper ServiceJobComplaintWrapper, int mode);
        void onHandleDeleteActionsFromListSelection(int id);
    }

    public interface OnItemClickListener {
        void onClick(ServiceJobComplaintWrapper colorWrapper);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView textViewNameRecord;
        TextView textViewDeleteRecord;

        ImageButton imageButtonDelete;
        ImageButton imageButtonUploaded;

        public ViewHolder(View view) {
            super(view);

            // Date Information
            textViewNameRecord = (TextView) view.findViewById(R.id.textViewNameRecord);
            textViewNameRecord.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mCallback != null) {
                        mCallback.onHandleActionsSelection(getAdapterPosition(), mDataSet.get(getAdapterPosition()), 1);
                    }
                }
            });
            textViewDeleteRecord = (TextView) view.findViewById(R.id.textViewDeleteRecord);
            textViewDeleteRecord.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mCallback != null) {
                        mCallback.onHandleDeleteActionsFromListSelection(mDataSet.get(getAdapterPosition()).getID());
                    }
                }
            });

            // ImageButtons
            imageButtonDelete = (ImageButton) view.findViewById(R.id.imageButtonDelete);
            imageButtonDelete.setOnClickListener(this);

            imageButtonUploaded = (ImageButton) view.findViewById(R.id.imageButtonUploaded);
            imageButtonUploaded.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            if (v.getId() == imageButtonDelete.getId()) {
                if (mCallback != null) {
                    // mCallback.onHandleRecordingsSelection(getAdapterPosition(), mDataSet.get(getAdapterPosition()), 2);
                    mCallback.onHandleDeleteActionsFromListSelection(mDataSet.get(getAdapterPosition()).getID());
                }
            } else if (v.getId() == imageButtonUploaded.getId()) {
                if (mCallback != null) {
                    // mCallback.onHandleRecordingsSelection(getAdapterPosition(), mDataSet.get(getAdapterPosition()), 2);
                    //mCallback.onHandlePlayFromListSelection(mDataSet.get(getAdapterPosition()));
                }

            }

        }
    }
}
