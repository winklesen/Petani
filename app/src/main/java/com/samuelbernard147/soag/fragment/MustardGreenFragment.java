package com.samuelbernard147.soag.fragment;


import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.samuelbernard147.soag.Helper.PlantHelper;
import com.samuelbernard147.soag.R;
import com.samuelbernard147.soag.preference.TimerPreference;

/**
 * A simple {@link Fragment} subclass.
 */
public class MustardGreenFragment extends Fragment implements View.OnClickListener{
    private TimerPreference pref;
    private PlantHelper helper;
    private CountDownTimer timer;
    private Boolean isRunning = false;

    private ProgressBar pbCircle;
    private TextView tvTime;
    private TextView tvDateEnd;
    private SwipeRefreshLayout refresh;
    private Button btnStart, btnHistory;


    public MustardGreenFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sawi, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        pbCircle = view.findViewById(R.id.pb_circle_mustard_green);
        tvTime = view.findViewById(R.id.tv_time_mustard_green);
        tvDateEnd = view.findViewById(R.id.tv_date_end_mustard_green);

        btnStart = view.findViewById(R.id.btn_start_mustard_green);
        btnHistory = view.findViewById(R.id.btn_history_mustard_green);
        btnStart.setOnClickListener(this);
        btnHistory.setOnClickListener(this);

        if (getActivity() != null) {
            helper = new PlantHelper(getActivity());
            pref = new TimerPreference(getActivity());
        }

        refresh = view.findViewById(R.id.refresh_mustard_green);
        refresh.setColorSchemeResources(R.color.colorAccent);
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        checkPlant();
                        refresh.setRefreshing(false);
                    }
                }, 2000); //4000 millisecond = 4 detik
            }
        });
        checkPlant();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (pref.getStatus(TimerPreference.MUSTARD_GREEN)) {
            checkPlant();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (pref.getStatus(TimerPreference.MUSTARD_GREEN)) {
            checkPlant();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_start_mustard_green:
                helper.postStatus(TimerPreference.MUSTARD_GREEN, "on");
                pref.setStatus(TimerPreference.MUSTARD_GREEN, true);
                pref.setInterval(TimerPreference.MUSTARD_GREEN_INTERVAL, 600);
                tvTime.clearAnimation();
                tvTime.animate().cancel();
                startTimer();
                btnStart.setEnabled(false);
                break;
            case R.id.btn_history_mustard_green:
                showDialog();
                break;
        }
    }

    private void startTimer() {
        isRunning = true;
        btnStart.setEnabled(false);
        final int second = pref.getInterval(TimerPreference.MUSTARD_GREEN_INTERVAL);
        timer = new CountDownTimer(second * 1000, 500) {
            @Override
            public void onTick(long leftTimeInMilliseconds) {
                long seconds = leftTimeInMilliseconds / 1000;
                pbCircle.setProgress((int) seconds);
                tvDateEnd.setText(getResources().getString(R.string.perkiraan_panen));
                tvTime.setText(helper.timeConverter(leftTimeInMilliseconds));
            }

            @Override
            public void onFinish() {
                btnStart.setEnabled(true);
                pref.setStatus(TimerPreference.MUSTARD_GREEN, false);
                pref.setInterval(TimerPreference.MUSTARD_GREEN_INTERVAL, 0);
                tvDateEnd.setText(getResources().getString(R.string.selesai));
                tvTime.setText(getResources().getString(R.string.siap_panen));
                helper.showNotification(
                        getActivity(),
                        getResources().getString(R.string.panen_title),
                        String.format(getResources().getString(R.string.panen_desc), "Sawi"),
                        3
                );
                blink(500);
                helper.postStatus(TimerPreference.MUSTARD_GREEN_INTERVAL, "off");
                isRunning = false;
            }
        }.start();
    }

    private void blink(final int time) {
        Animation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(time); //You can manage the time of the blink with this parameter
        anim.setStartOffset(20);
        anim.setRepeatMode(Animation.REVERSE);
        anim.setRepeatCount(Animation.INFINITE);
        tvTime.startAnimation(anim);
    }

    private void checkPlant() {
        helper.loadPlant(TimerPreference.MUSTARD_GREEN, TimerPreference.MUSTARD_GREEN_INTERVAL);
        if (pref.getStatus(TimerPreference.MUSTARD_GREEN)) {
            if (isRunning) {
                timer.cancel();
            }
            startTimer();
            btnStart.setEnabled(false);
        } else {
            if (isRunning) {
                timer.cancel();
            }
            pbCircle.setProgress(0);
            tvDateEnd.setText(getResources().getString(R.string.belum_dipanen));
            tvTime.setText(getResources().getString(R.string.time));
            btnStart.setEnabled(true);
        }
    }

    private void showDialog() {
        BottomSheetFragment bottomSheetFragment = new BottomSheetFragment();
        Bundle bundle = new Bundle();
        bundle.putString("TAG",TimerPreference.MUSTARD_GREEN);
        bottomSheetFragment.setArguments(bundle);
        bottomSheetFragment.show(getChildFragmentManager(), bottomSheetFragment.getTag());
    }
}
