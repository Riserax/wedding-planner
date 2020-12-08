package pl.com.weddingPlanner.util;

import android.os.SystemClock;
import android.view.View;

import java.util.Map;
import java.util.WeakHashMap;

public abstract class DebouncedOnClickListener implements View.OnClickListener {

    private final long minimumInterval;
    private Map<View, Long> lastClickMap;

    public abstract void onDebouncedClick(View v);

    public DebouncedOnClickListener(long minimumIntervalMsec) {
        this.minimumInterval = minimumIntervalMsec;
        this.lastClickMap = new WeakHashMap<>();
    }

    @Override
    public void onClick(View clickedView) {
        Long previousClickTimestamp = lastClickMap.get(clickedView);
        long currentTimestamp = SystemClock.uptimeMillis();
        lastClickMap.put(clickedView, currentTimestamp);
        if (previousClickTimestamp == null || Math.abs(currentTimestamp - previousClickTimestamp) > minimumInterval) {
            onDebouncedClick(clickedView);
        }
    }
}