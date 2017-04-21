package admin4.techelm.com.techelmtechnologies.activity.projectjob_main.fragment.b1;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import admin4.techelm.com.techelmtechnologies.R;
import admin4.techelm.com.techelmtechnologies.activity.projectjob_main.fragment.ProjectJobViewPagerActivity;
import admin4.techelm.com.techelmtechnologies.activity.service_report.AddReplacementPart_3;
import admin4.techelm.com.techelmtechnologies.activity.service_report.PartReplacement_2;
import admin4.techelm.com.techelmtechnologies.activity.service_report.SigningOff_4;
import admin4.techelm.com.techelmtechnologies.activity.service_report_fragment.ServiceJobViewPagerActivity;

/**
 * Created by Ratan on 7/29/2015.
 */
public class DrawingFragmentTest extends Fragment {

    private Context mContext;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.drawing_layout_test, container, false);

        this.mContext = container.getContext();

        initButton(view);

        return view;
    }
    /**
     * These Two Lines should be included on every Fragment to maintain the state and donnot load again
     * @param outState
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        System.out.println("Drawing, currently under construction");
    }
    @Override
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        System.out.println("Drawing, currently under construction");
    }

    private void initButton(View view) {
        /** BUTTON BACK */
        Button button_back = (Button) view.findViewById(R.id.button_back);
        button_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*startActivity(new Intent(AddReplacementPart_FRGMT_3.this, PartReplacement_FRGMT_2.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        .putExtra(RECORD_JOB_SERVICE_KEY, mServiceJobFromBundle));
                overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);*/
                ((ProjectJobViewPagerActivity)getActivity()).fromFragmentNavigate(-1);

            }
        });

        /** BUTTON NEXT */
        Button button_next = (Button) view.findViewById(R.id.button_next);
        button_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*startActivity(new Intent(AddReplacementPart_FRGMT_3.this, SigningOff_FRGMT_4.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        .putExtra(RECORD_JOB_SERVICE_KEY, mServiceJobFromBundle));
                overridePendingTransition(R.anim.enter, R.anim.exit);*/
                ((ProjectJobViewPagerActivity)getActivity()).fromFragmentNavigate(1);
            }
        });
    }

}
