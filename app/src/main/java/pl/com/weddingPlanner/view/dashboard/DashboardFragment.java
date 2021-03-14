package pl.com.weddingPlanner.view.dashboard;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import org.joda.time.Interval;
import org.joda.time.Period;
import org.joda.time.PeriodType;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import pl.com.weddingPlanner.R;
import pl.com.weddingPlanner.databinding.FragmentDashboardBinding;
import pl.com.weddingPlanner.enums.PeriodTypeEnum;
import pl.com.weddingPlanner.persistence.entity.Wedding;
import pl.com.weddingPlanner.util.DAOUtil;
import pl.com.weddingPlanner.view.NavigationActivity;
import pl.com.weddingPlanner.view.util.DashboardUtil;

public class DashboardFragment extends Fragment {

    private static final int TIME_1_SECOND = 1000;
    private static final int TIME_1_MINUTE = 60000;
    private static final int TIME_5_MINUTES = 300000;

    private FragmentDashboardBinding binding;

    private Wedding weddingDetails;

    private Timer timer;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((NavigationActivity) requireActivity()).setFragmentWithoutToolbar();

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_dashboard, container, false);

        getAndSetWeddingDetails();
        setComponents();

        return binding.getRoot();
    }

    @Override
    public void onAttach(@Nullable Context context) {
        super.onAttach(context);
        updateCountdown();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        timer.cancel();
    }

    private void getAndSetWeddingDetails() {
        weddingDetails = DAOUtil.getWeddingById(getContext(), 1); //TODO rozróżniać w którym weselu się znajdujemy
    }

    private void setComponents() {
        binding.weddingName.setText(weddingDetails.getName());
        binding.weddingDate.setText(weddingDetails.getDate());
    }

    private void updateCountdown() {
        int refreshingMillis = getCountdownRefreshingMillis();

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                requireActivity().runOnUiThread(() -> setCountdownComponents());
            }
        }, 0, refreshingMillis);
    }

    private int getCountdownRefreshingMillis() {
        Period period = getRemainingInterval().toPeriod(PeriodType.yearMonthDayTime());

        if (period.getYears() > 0) {
            return TIME_5_MINUTES;
        } else if (period.getMonths() > 0) {
            return TIME_5_MINUTES;
        } else if (period.getDays() > 0) {
            return TIME_1_MINUTE;
        } else {
            return TIME_1_SECOND;
        }
    }

    private void setCountdownComponents() {
        Interval interval = getRemainingInterval();
        Period period = interval.toPeriod(PeriodType.yearMonthDayTime());

        if (period.getYears() > 0) {
            DashboardUtil.setValueAndTitleForPeriod(binding.value1, binding.value1Title, period.getYears(), PeriodTypeEnum.YEAR);
            DashboardUtil.setValueAndTitleForPeriod(binding.value2, binding.value2Title, period.getMonths(), PeriodTypeEnum.MONTH);
            DashboardUtil.setValueAndTitleForPeriod(binding.value3, binding.value3Title, period.getDays(), PeriodTypeEnum.DAY);
        } else if (period.getMonths() > 0) {
            DashboardUtil.setValueAndTitleForPeriod(binding.value1, binding.value1Title, period.getMonths(), PeriodTypeEnum.MONTH);
            DashboardUtil.setValueAndTitleForPeriod(binding.value2, binding.value2Title, period.getDays(), PeriodTypeEnum.DAY);
            binding.value3Layout.setVisibility(View.GONE);
        } else if (period.getDays() > 0) {
            DashboardUtil.setValueAndTitleForPeriod(binding.value1, binding.value1Title, period.getDays(), PeriodTypeEnum.DAY);
            DashboardUtil.setValueAndTitleForPeriod(binding.value2, binding.value2Title, period.getHours(), PeriodTypeEnum.HOUR);
            DashboardUtil.setValueAndTitleForPeriod(binding.value3, binding.value3Title, period.getMinutes(), PeriodTypeEnum.MINUTE);
        } else if (period.getHours() > 0) {
            DashboardUtil.setValueAndTitleForPeriod(binding.value1, binding.value1Title, period.getHours(), PeriodTypeEnum.HOUR);
            DashboardUtil.setValueAndTitleForPeriod(binding.value2, binding.value2Title, period.getMinutes(), PeriodTypeEnum.MINUTE);
            DashboardUtil.setValueAndTitleForPeriod(binding.value3, binding.value3Title, period.getSeconds(), PeriodTypeEnum.SECOND);
        } else {
            DashboardUtil.setValueAndTitleForPeriod(binding.value1, binding.value1Title, period.getMinutes(), PeriodTypeEnum.MINUTE);
            DashboardUtil.setValueAndTitleForPeriod(binding.value2, binding.value2Title, period.getSeconds(), PeriodTypeEnum.SECOND);
            binding.value3Layout.setVisibility(View.GONE);
        }
    }

    private Interval getRemainingInterval() {
        Locale locale = new Locale("PL");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/M/yyyy hh:mm:ss", locale);

        Wedding wedding = DAOUtil.getWeddingById(getContext(), 1);

        Date date1 = new Date();
        Date date2 = new Date();

        try {
            date1 = simpleDateFormat.parse(simpleDateFormat.format(Calendar.getInstance().getTime()));
            date2 = simpleDateFormat.parse(wedding.getDate() + " " + wedding.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return new Interval(date1.getTime(), date2.getTime());
    }
}