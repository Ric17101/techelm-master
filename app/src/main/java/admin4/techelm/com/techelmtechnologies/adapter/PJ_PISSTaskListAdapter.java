package admin4.techelm.com.techelmtechnologies.adapter;

/**
 * Created by admin 4 on 16/02/2017.
 * PROJECT Rendered at the Recycler View after clicking Calendar Date
 */

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import admin4.techelm.com.techelmtechnologies.R;
import admin4.techelm.com.techelmtechnologies.activity.projectjob_main.helper.FragmentSetListHelper_ProjectJob;
import admin4.techelm.com.techelmtechnologies.adapter.listener.PISSTaskListener;
import admin4.techelm.com.techelmtechnologies.model.projectjob.b1.PISSTaskWrapper;

import static admin4.techelm.com.techelmtechnologies.utility.Constants.ACTION_VIEW_TASK;
import static admin4.techelm.com.techelmtechnologies.utility.Constants.PROJECT_JOB_START_TASK;
import static admin4.techelm.com.techelmtechnologies.utility.Constants.PROJECT_JOB_TASK_START_DRAWING;

public class PJ_PISSTaskListAdapter extends RecyclerView.Adapter<PJ_PISSTaskListAdapter.ViewHolder> {

    private static final String TAG = PJ_PISSTaskListAdapter.class.getSimpleName();

    private List<PISSTaskWrapper> mDataSet = new ArrayList<>();
    private PISSTaskWrapper dataSet;
    private PISSTaskListener mCallback;
    private int mLastAnimatedItemPosition = -1;
    private int mLasItemPosition = 0;
    private Context mContext;

    private OnItemClickListener mItemsOnClickListener;
    private int counterOnBindViewHolder = 0;

    private FragmentSetListHelper_ProjectJob mSetHelper;

    public PJ_PISSTaskListAdapter(Context context) {
        mContext = context;

        // .. Attach the interface
        try {
            mCallback = (PISSTaskListener) context; // TODO: Troubleshooting the OnClickListener of the CardView Buttons inside the RecyclerView
        } catch (ClassCastException ex) {
            //.. should log the error or throw and exception
            Log.e(TAG, "Must implement the ProjectJobListener in the Activity", ex);
        }
        System.gc();
    }

    public PJ_PISSTaskListAdapter(List<PISSTaskWrapper> serviceJobList) {
        this.mDataSet = serviceJobList;
        notifyDataSetChanged();
    }

    public void swapData(List<PISSTaskWrapper> mNewDataSet) {
        mDataSet = mNewDataSet;
        notifyDataSetChanged();
    }

    public void setItemsOnClickListener(OnItemClickListener onClickListener) {
        this.mItemsOnClickListener = onClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.search_results_servicejob_list_item, parent, false);
        mContext = view.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        this.mSetHelper = new FragmentSetListHelper_ProjectJob();

        dataSet = mDataSet.get(holder.getAdapterPosition());
        holder.textViewDay.setText(dataSet.getSerialNo()); // GREYED Below BIG Number
        holder.textViewDateNumber.setText(dataSet.getID() + ""); // BIG Number
        holder.textViewDate.setText(dataSet.getStatus()); // BLACK Date Below
        holder.textViewServiceNum.setText(dataSet.getComments() + "");
        holder.textViewCustomer.setText(dataSet.getDescription());
        holder.textViewEngineer.setText(dataSet.getConformance());
        // holder.textViewStatus.setText(this.mSetHelper.setStatus(dataSet.getStatus()+""));
        holder.textViewStatus.setText(dataSet.getStatus()+ "");
        holder.textViewStatus.setTextColor(this.mSetHelper.setColor(dataSet.getStatus()+""));
        holder.textViewTask.setText(Html.fromHtml(this.mSetHelper.setTaskText(PROJECT_JOB_START_TASK)));
        holder.buttonTask.setImageResource(this.mSetHelper.setIconTask(dataSet.getStatus()+""));

        Log.d(TAG, "onBindViewHolder (" + ++counterOnBindViewHolder + ") = " + dataSet.getSerialNo());

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

    public interface OnItemClickListener {
        void onClick(PISSTaskWrapper colorWrapper);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final TextView textViewDateNumber;
        public final TextView textViewDay;
        public final TextView textViewDate;

        public final TextView textViewServiceNum;
        public final TextView textViewCustomer;
        public final TextView textViewEngineer;
        public final TextView textViewStatus;

        public final ImageButton buttonTask;
        public final TextView textViewTask;

        private final FrameLayout frameLayoutButtonSJ;

        public ViewHolder(View view) {
            super(view);

            // Date Information
            textViewDateNumber = (TextView) view.findViewById(R.id.textViewDateNumber);
            textViewDay = (TextView) view.findViewById(R.id.textViewDay);
            textViewDate = (TextView) view.findViewById(R.id.textViewDate);

            // Data from DB
            textViewServiceNum = (TextView) view.findViewById(R.id.textViewServiceNum);
            textViewCustomer = (TextView) view.findViewById(R.id.textViewCustomer);
            textViewEngineer = (TextView) view.findViewById(R.id.textViewEngineer);
            textViewStatus = (TextView) view.findViewById(R.id.textViewStatus);

            // ImageButtons
            buttonTask = (ImageButton) view.findViewById(R.id.buttonTask);
            buttonTask.setOnClickListener(this);

            // ImageButton Links
            textViewTask = (TextView) view.findViewById(R.id.textViewTask);

            frameLayoutButtonSJ = (FrameLayout) view.findViewById(R.id.frameLayoutButtonSJ);
            frameLayoutButtonSJ.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == frameLayoutButtonSJ.getId()) {
                if (mCallback != null) {
                    mCallback.onHandleSelection(getAdapterPosition(), mDataSet.get(getAdapterPosition()), ACTION_VIEW_TASK);
                }
            } else if (v.getId() == buttonTask.getId() /*|| v.getId() == textViewTask.getId()*/) {
                if (mCallback != null) {
                    mSetHelper.setActionOnClick(mCallback, getAdapterPosition(), mDataSet.get(getAdapterPosition()), PROJECT_JOB_TASK_START_DRAWING);
                }
            }

        }
    }
}
