package pl.com.weddingPlanner.view.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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
import pl.com.weddingPlanner.enums.CollaborationStageEnum;
import pl.com.weddingPlanner.enums.PeriodTypeEnum;
import pl.com.weddingPlanner.enums.PresenceEnum;
import pl.com.weddingPlanner.enums.TaskStatusEnum;
import pl.com.weddingPlanner.persistence.entity.Wedding;
import pl.com.weddingPlanner.util.DAOUtil;
import pl.com.weddingPlanner.view.NavigationActivity;
import pl.com.weddingPlanner.view.util.BudgetUtil;
import pl.com.weddingPlanner.view.util.DashboardUtil;

public class DashboardFragment extends Fragment {

    private static final int TIME_1_SECOND = 1000;
    private static final int TIME_1_MINUTE = 60000;
    private static final int TIME_5_MINUTES = 300000;

    private FragmentDashboardBinding binding;

    private Wedding weddingDetails;

    private int guestsCount;
    private int guestsInvitedCount;
    private int guestsAcceptedCount;
    private int guestsRejectedCount;
    private int guestsAwaitingCount;

    private int tasksCount;
    private int tasksDoneCount;

    private int subcontractorsCount;
    private int subcontractorsConsideringCount;
    private int subcontractorsInContactCount;
    private int subcontractorsConfirmedCount;
    private int subcontractorsPaidCount;

    private double initialAmountsSum = 0.00;
    private double initialTotalAmount = 0.00;

    private Timer timer;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((NavigationActivity) requireActivity()).setFragmentWithoutToolbar();

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_dashboard, container, false);

        getAndSetDetails();
        setComponents();
        setSwipeRefresh();

        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        updateCountdown();
    }

    @Override
    public void onStop() {
        super.onStop();
        timer.cancel();
    }

    private void setSwipeRefresh() {
        SwipeRefreshLayout swipeRefresh = binding.swipeRefresh;
        swipeRefresh.setOnRefreshListener(() -> {
            swipeRefresh.setRefreshing(false);
            getAndSetDetails();
            setComponents();
        });
    }

    private void getAndSetDetails() {
        weddingDetails = DAOUtil.getWeddingById(getContext(), 1); //TODO rozróżniać w którym weselu się znajdujemy

        guestsCount = DAOUtil.getAllGuestsCount(getContext());
        guestsInvitedCount = DAOUtil.getGuestsCountByPresence(getContext(), PresenceEnum.INVITATION_SENT.name());
        guestsAcceptedCount = DAOUtil.getGuestsCountByPresence(getContext(), PresenceEnum.CONFIRMED_PRESENCE.name());
        guestsRejectedCount = DAOUtil.getGuestsCountByPresence(getContext(), PresenceEnum.CONFIRMED_ABSENCE.name());
        guestsAwaitingCount = DAOUtil.getGuestsCountByPresence(getContext(), PresenceEnum.AWAITING.name());

        tasksCount = DAOUtil.getAllTasksCount(getContext());
        tasksDoneCount = DAOUtil.getTasksByStatusCount(getContext(), TaskStatusEnum.DONE.name());

        initialAmountsSum = BudgetUtil.getInitialAmountsSum(getContext());
        initialTotalAmount = Double.parseDouble(weddingDetails.getInitialTotalAmount());

        subcontractorsCount = DAOUtil.getAllSubcontractorsCount(getContext());
        subcontractorsConsideringCount = DAOUtil.getSubcontractorsByStageCount(getContext(), CollaborationStageEnum.CONSIDERING.name());
        subcontractorsInContactCount = DAOUtil.getSubcontractorsByStageCount(getContext(), CollaborationStageEnum.IN_CONTACT.name());
        subcontractorsConfirmedCount = DAOUtil.getSubcontractorsByStageCount(getContext(), CollaborationStageEnum.CONFIRMED.name());
        subcontractorsPaidCount = DAOUtil.getSubcontractorsByStageCount(getContext(), CollaborationStageEnum.PAID.name());
    }

    private void setComponents() {
        binding.weddingName.setText(weddingDetails.getName());

        String weddingDateTime = weddingDetails.getDate() + " | " + weddingDetails.getTime().substring(0, weddingDetails.getTime().length() - 3);
        binding.weddingDate.setText(weddingDateTime);

        binding.guestsTitle.setText(getString(R.string.dashboard_guests_title, guestsCount));
        binding.invitationsSent.setText(String.valueOf(guestsInvitedCount));
        binding.invitationsAccepted.setText(String.valueOf(guestsAcceptedCount));
        binding.invitationsRejected.setText(String.valueOf(guestsRejectedCount));
        binding.invitationsAwaiting.setText(String.valueOf(guestsAwaitingCount));

        binding.ceremonyVenue.setText(weddingDetails.getCeremonyVenue());
        binding.banquetVenue.setText(weddingDetails.getBanquetVenue());

        binding.tasksDone.setText(String.valueOf(tasksDoneCount));
        binding.tasksCount.setText(String.valueOf(tasksCount));

        setBudgetProgressAndText();

        binding.subcontractorsTitle.setText(getString(R.string.dashboard_subcontractors_title, subcontractorsCount));
        binding.collaborationConsidering.setText(String.valueOf(subcontractorsConsideringCount));
        binding.collaborationInTouch.setText(String.valueOf(subcontractorsInContactCount));
        binding.collaborationConfirmed.setText(String.valueOf(subcontractorsConfirmedCount));
        binding.collaborationPaid.setText(String.valueOf(subcontractorsPaidCount));
    }

    private void setBudgetProgressAndText() {
        int percentage = (int) (initialAmountsSum / initialTotalAmount * 100);
        String progress = percentage + "%";

        binding.progressBar.setProgress(percentage, true);
        binding.txtProgress.setText(progress);
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
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", locale);

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