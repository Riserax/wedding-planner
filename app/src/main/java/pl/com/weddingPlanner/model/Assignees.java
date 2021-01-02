package pl.com.weddingPlanner.model;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import org.apache.commons.lang3.StringUtils;

import java.util.List;

import lombok.Getter;
import pl.com.weddingPlanner.R;
import pl.com.weddingPlanner.persistence.entity.Person;

@Getter
public class Assignees {

    private final int ASSIGNEES_PER_ROW = 3;
    private final String SPACE = StringUtils.SPACE;

    private final Context context;
    private final List<Person> assigneeList;
    private final LinearLayout assigneesContainer;

    public Assignees(Activity activity, List<Person> assigneeList) {
        this.context = activity.getApplicationContext();
        this.assigneeList = assigneeList;
        this.assigneesContainer = new LinearLayout(context);
        buildAssignees();
    }

    private void buildAssignees() {
        assigneesContainer.setOrientation(LinearLayout.VERTICAL);

        int rowsNumber = (int) Math.ceil((double) assigneeList.size() / ASSIGNEES_PER_ROW);

        for (int i = 0; i < rowsNumber; i++) {
            createAndAddOneRowAssigneesLayout(i);
        }
    }

    private void createAndAddOneRowAssigneesLayout(int row) {
        LinearLayout assigneesSingleRowLayout = setAssigneesRowLayout();

        for (int i = 0; i < assigneeList.size(); i++) {
            if (isOneRow(row, i)) {
                prepareAndAddAssignee(assigneeList.get(i), assigneesSingleRowLayout);
            } else if (isMoreThanOneRow(row, i)) {
                prepareAndAddAssignee(assigneeList.get(i), assigneesSingleRowLayout);
            }
        }

        assigneesContainer.addView(assigneesSingleRowLayout);
    }

    private boolean isOneRow(int row, int i) {
        return row == 0 && i < ASSIGNEES_PER_ROW;
    }

    private boolean isMoreThanOneRow(int row, int i) {
        return row > 0 && (i >= (row * ASSIGNEES_PER_ROW) && i < (row * ASSIGNEES_PER_ROW + ASSIGNEES_PER_ROW));
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

    private LinearLayout createBackgroundLayout() {
        LinearLayout backgroundLayout = new LinearLayout(context);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(context.getResources().getDimensionPixelSize(R.dimen.assignee_circle_size), context.getResources().getDimensionPixelSize(R.dimen.assignee_circle_size));

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
        textView.setText(getInitials(assignee.getName()));
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
        textView.setTextColor(ContextCompat.getColor(context, R.color.gray_555555));
        textView.setTypeface(Typeface.DEFAULT_BOLD);

        return textView;
    }

    private String getInitials(String personName) {
        if (personName.contains(SPACE)) {
            String firstLInitial = StringUtils.substring(personName, 0, 1);
            String secondInitial = StringUtils.substring(personName, personName.indexOf(SPACE) + 1, personName.indexOf(SPACE) + 2);
            return firstLInitial.toUpperCase() + secondInitial.toUpperCase();
        } else {
            return StringUtils.substring(personName, 0, 2).toUpperCase();
        }
    }
}
