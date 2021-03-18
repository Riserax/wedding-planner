package pl.com.weddingPlanner.model;

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
import pl.com.weddingPlanner.persistence.entity.Person;
import pl.com.weddingPlanner.view.util.PersonUtil;


public class Assignees {

    private final Context context;
    private final List<Person> assigneeList;
    @Getter
    private final LinearLayout assigneesContainer;
    private int assigneesPerRow = 3;

    public Assignees(Context context, List<Person> assigneeList) {
        this.context = context;
        this.assigneeList = assigneeList;
        this.assigneesContainer = new LinearLayout(context);
        buildAssignees();
    }

    public Assignees(Context context, List<Person> assigneeList, int itemsPerRow) {
        this.context = context;
        this.assigneeList = assigneeList;
        this.assigneesContainer = new LinearLayout(context);
        this.assigneesPerRow = itemsPerRow;
        buildAssignees();
    }

    private void buildAssignees() {
        assigneesContainer.setOrientation(LinearLayout.VERTICAL);

        int rowsNumber = (int) Math.ceil((double) assigneeList.size() / assigneesPerRow);

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
        return row == 0 && i < assigneesPerRow;
    }

    private boolean isMoreThanOneRow(int row, int i) {
        return row > 0 && (i >= (row * assigneesPerRow) && i < (row * assigneesPerRow + assigneesPerRow));
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
        textView.setText(PersonUtil.getInitials(assignee.getName()));
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
        textView.setTextColor(ContextCompat.getColor(context, R.color.gray_555555));
        textView.setTypeface(Typeface.DEFAULT_BOLD);

        return textView;
    }


}
