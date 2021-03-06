package pl.com.weddingPlanner.view.component;

import android.content.Context;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import java.util.List;

import lombok.Getter;
import pl.com.weddingPlanner.R;
import pl.com.weddingPlanner.enums.Location;
import pl.com.weddingPlanner.persistence.entity.Person;
import pl.com.weddingPlanner.view.util.PersonUtil;

public class Assignees {

    private final int ASSIGNEES_PER_ROW_LIST = 2;
    private final int ASSIGNEES_PER_ROW_DETAILS = 3;

    private final Context context;
    private final List<Person> assigneeList;
    @Getter
    private final LinearLayout assigneesContainer;
    private final Location location;

    public Assignees(Context context, List<Person> assigneeList, Location location) {
        this.context = context;
        this.assigneeList = assigneeList;
        this.assigneesContainer = new LinearLayout(context);
        this.location = location;
        buildAssignees();
    }

    private void buildAssignees() {
        assigneesContainer.setOrientation(LinearLayout.VERTICAL);

        for (int i = 0; i < getRowsNumber(); i++) {
            createAndAddOneRowAssigneesLayout(i);
        }
    }

    private int getRowsNumber() {
        int rowsNumber = 1;

        if (Location.DETAILS == location) {
            rowsNumber = (int) Math.ceil((double) assigneeList.size() / ASSIGNEES_PER_ROW_DETAILS);
        }

        return rowsNumber;
    }

    private void createAndAddOneRowAssigneesLayout(int row) {
        LinearLayout assigneesSingleRowLayout = null;

        if (Location.LIST_ITEM == location) {
            assigneesSingleRowLayout = setAssigneesRowLayout();

            if (assigneeList.size() <= ASSIGNEES_PER_ROW_LIST) {
                for (Person assignee : assigneeList) {
                    prepareAndAddAssignee(assignee, assigneesSingleRowLayout);
                }
            } else {
                prepareAndAddAssignee(assigneeList.get(0), assigneesSingleRowLayout);
                prepareAndAddMoreInfo(assigneesSingleRowLayout);
            }
        } else if (Location.DETAILS == location) {
            assigneesSingleRowLayout = setAssigneesRowLayout();

            for (int i = 0; i < assigneeList.size(); i++) {
                if (isOneRow(row, i)) {
                    prepareAndAddAssignee(assigneeList.get(i), assigneesSingleRowLayout);
                } else if (isMoreThanOneRow(row, i)) {
                    prepareAndAddAssignee(assigneeList.get(i), assigneesSingleRowLayout);
                }
            }
        }

        if (assigneesSingleRowLayout != null) {
            assigneesContainer.addView(assigneesSingleRowLayout);
        }
    }

    private boolean isOneRow(int row, int i) {
        return row == 0 && i < ASSIGNEES_PER_ROW_DETAILS;
    }

    private boolean isMoreThanOneRow(int row, int i) {
        return row > 0 && (i >= (row * ASSIGNEES_PER_ROW_DETAILS) && i < (row * ASSIGNEES_PER_ROW_DETAILS + ASSIGNEES_PER_ROW_DETAILS));
    }

    private LinearLayout setAssigneesRowLayout() {
        LinearLayout assigneeLayout = new LinearLayout(context);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        assigneeLayout.setLayoutParams(layoutParams);
        assigneeLayout.setHorizontalGravity(Gravity.CENTER);
        assigneeLayout.setOrientation(LinearLayout.HORIZONTAL);

        return assigneeLayout;
    }

    private void prepareAndAddAssignee(Person assignee, LinearLayout assigneesRowLayout) {
        LinearLayout backgroundLayout = createBackgroundLayout();
        TextView textView = createTextView(assignee);

        backgroundLayout.addView(textView);
        assigneesRowLayout.addView(backgroundLayout);
    }

    private void prepareAndAddMoreInfo(LinearLayout assigneesRowLayout) {
        LinearLayout backgroundLayout = createBackgroundLayout();
        TextView textView = createInfoTextView();

        backgroundLayout.addView(textView);
        assigneesRowLayout.addView(backgroundLayout);
    }

    private LinearLayout createBackgroundLayout() {
        LinearLayout backgroundLayout = new LinearLayout(context);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(context.getResources().getDimensionPixelSize(R.dimen.circle_background_size), context.getResources().getDimensionPixelSize(R.dimen.circle_background_size));

        backgroundLayout.setLayoutParams(layoutParams);
        backgroundLayout.setGravity(Gravity.CENTER);
        backgroundLayout.setOrientation(LinearLayout.VERTICAL);
        backgroundLayout.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_circle_white));

        return backgroundLayout;
    }

    private TextView createTextView(Person assignee) {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        TextView textView = new TextView(context);
        textView.setLayoutParams(layoutParams);
        textView.setClickable(true);
        textView.setFocusable(true);
        textView.setText(PersonUtil.getInitials(assignee.getName()));
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
        textView.setTextColor(ContextCompat.getColor(context, R.color.gray_555555));
        textView.setTypeface(Typeface.DEFAULT_BOLD);

        return textView;
    }

    private TextView createInfoTextView() {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        TextView textView = new TextView(context);
        textView.setLayoutParams(layoutParams);
        textView.setClickable(true);
        textView.setFocusable(true);
        textView.setText(context.getString(R.string.assignees_2_plus));
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
        textView.setTextColor(ContextCompat.getColor(context, R.color.gray_555555));
        textView.setTypeface(Typeface.DEFAULT_BOLD);

        return textView;
    }

}
