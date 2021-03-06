package admin4.techelm.com.techelmtechnologies.activity.toolbox_meeting_main.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import admin4.techelm.com.techelmtechnologies.R;
import admin4.techelm.com.techelmtechnologies.model.toolboxmeeting.ToolboxMeetingUploadsWrapper;
import admin4.techelm.com.techelmtechnologies.model.toolboxmeeting.ToolboxMeetingWrapper;

import static admin4.techelm.com.techelmtechnologies.utility.Constants.PROJECT_JOB_FORM_B1;
import static admin4.techelm.com.techelmtechnologies.utility.Constants.PROJECT_JOB_FORM_B2;
import static admin4.techelm.com.techelmtechnologies.utility.Constants.PROJECT_JOB_FORM_B3;
import static admin4.techelm.com.techelmtechnologies.utility.Constants.PROJECT_JOB_FORM_TYPE_KEY;
import static admin4.techelm.com.techelmtechnologies.utility.Constants.PROJECT_JOB_KEY;
import static admin4.techelm.com.techelmtechnologies.utility.Constants.TOOLBOX_MEETING_KEY;

public class ToolboxMeetingFragmentTab extends Fragment {

    private final static String TAG = ToolboxMeetingFragmentTab.class.getSimpleName();
    private TabLayout tabLayout;
    private HorizontalScrollView hScrollViewTab;
    private ViewPager viewPager;
    public static final int TAB_COUNT = 2;

    private ToolboxMeetingWrapper toolboxMeetingWrapper;
    private ToolboxMeetingUploadsWrapper toolboxMeetingUploadsWrapper;
    private MyAdapter mPagerAdapter;

    public static ToolboxMeetingFragmentTab newInstance(ToolboxMeetingWrapper toolboxMeetingWrapper) {
        ToolboxMeetingFragmentTab fragment = new ToolboxMeetingFragmentTab();
        Bundle args = new Bundle();

        args.putParcelable(TOOLBOX_MEETING_KEY, toolboxMeetingWrapper);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fromBundle();
    }

    private void fromBundle() {
        this.toolboxMeetingWrapper = getArguments().getParcelable(TOOLBOX_MEETING_KEY);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        /**
         *Inflate tab_layout and setup Views.
         */
        View view = inflater.inflate(R.layout.tab_layout, null); // View x = inflater.inflate(R.layout.tab_layout, container, false);

        setHeaderVisibilityByFragmentPosition(view);

        tabLayout = (TabLayout) view.findViewById(R.id.tabs);
        hScrollViewTab = (HorizontalScrollView) view.findViewById(R.id.hScrollViewTab);
        viewPager = (ViewPager) view.findViewById(R.id.viewpager);

        // This will prevent from swiping the ViewPager
        /*viewPager.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                viewPager.setCurrentItem(viewPager.getCurrentItem());
                return false;
            }
        });*/
        /**
         *Set an Apater for the View Pager
         */
        viewPager.setAdapter(new MyAdapter(getChildFragmentManager()));
        viewPager.setOffscreenPageLimit(2); // Three tabs all
        // viewPager.setHorizontalScrollBarEnabled(false);
        /**
         * Now , this is a workaround ,
         * The setupWithViewPager dose't works without the runnable .
         * Maybe a Support Library Bug .
         */
        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                tabLayout.setupWithViewPager(viewPager);
            }
        });

        initHeader(view);
        return view;
    }

    private void initHeader(View view) {
        inti_B2HeaderDetails(view);
        Log.e(TAG, "initHeader");
    }

    // Just to Initialize the Button Next, Prev in ProjectJobViewPagerActivty
    public ViewPager getViewPager() {
        return this.viewPager;
    }

    public Fragment getActiveFragment(ViewPager container, int position) {
        return this.mPagerAdapter.getActiveFragment(container, position);
    }

    public void setCurrentTab(int nextOrPrevious) {
        this.viewPager.setCurrentItem(getItem(nextOrPrevious), true);
    }
    public void setCurrentToFirstTab() {
        this.viewPager.setCurrentItem(0);
    }

    private int getCurrentPosition() { return viewPager.getCurrentItem(); }
    private int getItem(int i) { return getCurrentPosition() + i; }

    // Just to show the Header Layout ONLY FOR THE ProjectJob - SECTION B
    private void setHeaderVisibilityByFragmentPosition(View view) {
        LinearLayout projectJobLayoutB2B3Header = (LinearLayout) view.findViewById(R.id.projectJobLayoutB2B3Header);
        projectJobLayoutB2B3Header.setVisibility(View.VISIBLE);
    }

    // Header for B2 and B3
    private void inti_B2HeaderDetails(View view) {
        TextView textViewLabelProjRef = (TextView) view.findViewById(R.id.textViewLabelProjRef);
        TextView textViewLabelDateOfSiteWalk2 = (TextView) view.findViewById(R.id.textViewLabelDateOfSiteWalk2); // This is not used...
        TextView textViewLabelProjectSite = (TextView) view.findViewById(R.id.textViewLabelProjectSite);
        TextView textViewLabelSubContractor = (TextView) view.findViewById(R.id.textViewLabelSubContractor);
        TextView textViewLabelInspectionDate = (TextView) view.findViewById(R.id.textViewLabelInspectionDate);
        TextView textViewLabelWorkCompletionDate = (TextView) view.findViewById(R.id.textViewLabelWorkCompletionDate);
        TextView textViewLabelSignature = (TextView) view.findViewById(R.id.textViewLabelSignature);

        textViewLabelProjRef.setText(this.toolboxMeetingWrapper.getProjectRef());
        textViewLabelProjectSite.setText(this.toolboxMeetingWrapper.getProjectSite());
        textViewLabelSubContractor.setText(this.toolboxMeetingWrapper.getSecondInspector());
        textViewLabelInspectionDate.setText(this.toolboxMeetingWrapper.getStartDate());
        textViewLabelWorkCompletionDate.setText(this.toolboxMeetingWrapper.getTargetCompletionDate());
        textViewLabelSignature.setText(this.toolboxMeetingWrapper.getStatus() + "");

        // Title of the TAB
        TextView textViewLabelTitleTab = (TextView) view.findViewById(R.id.textViewLabelTitleTab);
        textViewLabelTitleTab.setText("Toolbox Meeting");
    }

    class MyAdapter extends FragmentPagerAdapter {
        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        /**
         * Return fragmentType with respect to Position .
         */
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return AttendanceFragment.newInstance(toolboxMeetingWrapper);
                case 1:
                    return MeetingDetailsFragment.newInstance(toolboxMeetingWrapper);
            }
            return null;
        }

        @Override
        public int getCount() { return TAB_COUNT; }

        /**
         * This method returns the title of the tab according to the position.
         */

        @Override
        public CharSequence getPageTitle(int position) {
            Log.e(TAG, "getPageTitle(Postion):" + position);
            switch (position) {
                case 0:
                    return "Attendance";
                case 1:
                    return "Meeting details";
            }
            return null;
        }

        private Fragment getActiveFragment(ViewPager container, int position) {
            String name = makeFragmentName(container.getId(), position);
            return getChildFragmentManager().findFragmentByTag(name);
        }

        /**
         * @param containerViewId the ViewPager this adapter is being supplied to
         * @param id              pass in getItemId(position) as this is whats used internally in this class
         * @return the tag used for this pages fragmentType
         */
        public String makeFragmentName(int containerViewId, long id) {
            return "android:switcher:" + containerViewId + ":" + id;
        }
    }

}
