package pl.com.weddingPlanner.view.budget;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

import pl.com.weddingPlanner.R;
import pl.com.weddingPlanner.databinding.ActivityNewExpenseBinding;
import pl.com.weddingPlanner.persistence.entity.Expense;
import pl.com.weddingPlanner.persistence.entity.Person;
import pl.com.weddingPlanner.util.DAOUtil;
import pl.com.weddingPlanner.util.DateUtil;
import pl.com.weddingPlanner.util.DebouncedOnClickListener;
import pl.com.weddingPlanner.view.BaseActivity;
import pl.com.weddingPlanner.view.NavigationActivity;
import pl.com.weddingPlanner.view.dialog.CategoriesDialog;
import pl.com.weddingPlanner.view.dialog.PeopleDialog;
import pl.com.weddingPlanner.view.enums.CategoryTypeEnum;
import pl.com.weddingPlanner.view.util.ComponentsUtil;

import static pl.com.weddingPlanner.view.NavigationActivity.FRAGMENT_TO_LOAD_ID;
import static pl.com.weddingPlanner.view.util.ResourceUtil.AMOUNT_ZERO;
import static pl.com.weddingPlanner.view.util.ResourceUtil.CATEGORY_OTHER;

public class NewExpenseActivity extends BaseActivity {

    private ActivityNewExpenseBinding binding;

    private List<Integer> selectedPeopleKeys = new ArrayList<>();

    private boolean isCategoryChosen;
    private boolean isAmountSet;
    private boolean isRecipientAndPurposeSet;
    private boolean arePayersSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_new_expense);
        setActivityToolbarContentWithBackIcon(R.string.header_title_budget_new);

        setListeners();
    }

    private void setListeners() {
        setCategoryOnClickListener();
        setPeopleOnClickListener();
        initRootScrollViewListener();
        setOnFocusChangeListener();
        setAddButtonClickListener();
    }

    private void setCategoryOnClickListener() {
        binding.categoryLayout.setOnClickListener(new DebouncedOnClickListener(getResources().getInteger(R.integer.debounce_long_block_time_ms)) {
            @Override
            public void onDebouncedClick(View v) {
                clearFocusAndHideKeyboard();
                new CategoriesDialog(NewExpenseActivity.this, CategoryTypeEnum.BUDGET.name()).showDialog();
            }
        });
    }

    private void setPeopleOnClickListener() {
        binding.peopleLayout.setOnClickListener(new DebouncedOnClickListener(getResources().getInteger(R.integer.debounce_long_block_time_ms)) {
            @Override
            public void onDebouncedClick(View v) {
                clearFocusAndHideKeyboard();
                new PeopleDialog(NewExpenseActivity.this, selectedPeopleKeys).showDialog();
            }
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initRootScrollViewListener() {
        binding.expenseRootScrollView.setOnTouchListener((v, event) -> {
            clearFocusAndHideKeyboard();
            return false;
        });
    }

    private void clearFocusAndHideKeyboard() {
        View focused = getCurrentFocus();
        if (focused instanceof EditText) {
            focused.clearFocus();
            ComponentsUtil.hideKeyboard(this, focused);
        }
    }

    private void setOnFocusChangeListener() {
        final View.OnFocusChangeListener listener = (v, hasFocus) -> {
            if (!hasFocus) {
                ComponentsUtil.hideKeyboard(this, v);
            }
        };

        binding.expenseName.setOnFocusChangeListener(listener);
        binding.forWhoName.setOnFocusChangeListener(listener);
    }

    private void setAddButtonClickListener() {
        binding.addButton.setOnClickListener(new DebouncedOnClickListener(getResources().getInteger(R.integer.debounce_long_block_time_ms)) {
            @Override
            public void onDebouncedClick(View v) {
                clearFocusAndHideKeyboard();

                Expense newExpense = getNewExpenseData();

                if (!newExpense.getTitle().isEmpty()) {
                    DAOUtil.insertExpense(getApplicationContext(), newExpense);

                    Intent intent = new Intent(NewExpenseActivity.this, NavigationActivity.class);
                    intent.putExtra(FRAGMENT_TO_LOAD_ID, R.id.navigation_budget);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                } else {
                    Toast toast;
                    if (newExpense.getTitle().isEmpty()) {
                        toast = Toast.makeText(NewExpenseActivity.this, "Nazwa nie może być pusta!", Toast.LENGTH_LONG);
                        toast.show();
                    }
                }
            }
        });
    }

    private Expense getNewExpenseData() {
        String category = binding.categoryName.getText().toString();
        String initialAmount = binding.initialAmount.getText().toString();
        String recipientAndPurpose = binding.forWhoName.getText().toString();
        String payers = binding.peopleName.getText().toString();

        isCategoryChosen = !category.equals(getResources().getString(R.string.field_category));
        isAmountSet = !initialAmount.equals(getResources().getString(R.string.expense_field_amount));
        isRecipientAndPurposeSet = !recipientAndPurpose.equals(getResources().getString(R.string.expense_field_for_who));
        arePayersSet = !payers.equals(getResources().getString(R.string.expense_field_payer));

        String payersIdsString = arePayersSet ? getPayersIds(payers) : StringUtils.EMPTY;

        return Expense.builder()
                .title(binding.expenseName.getText().toString())
                .editDate(DateUtil.getNewDateWithHourString())
                .initialAmount(isAmountSet ? initialAmount : AMOUNT_ZERO)
                .category(isCategoryChosen ? category : CATEGORY_OTHER)
                .forWhomAndWhat(isRecipientAndPurposeSet ? recipientAndPurpose : StringUtils.EMPTY)
                .payers(payersIdsString)
                .build();
    }

    private String getPayersIds(String payers) {
        StringBuilder payersIdsSB = new StringBuilder();
        String[] payersNames = payers.split("\\|", -1);

        List<Person> payerList = new ArrayList<>();
        for (String payerName : payersNames) {
            payerList.add(DAOUtil.getPersonByName(this, payerName.trim()));
        }

        for (int i = 0; i < payerList.size(); i++) {
            if (i != payerList.size() - 1) {
                payersIdsSB.append(payerList.get(i).getId()).append(",");
            } else {
                payersIdsSB.append(payerList.get(i).getId());
            }
        }

        return payersIdsSB.toString();
    }

    @Override
    public void setDefaultFieldName(TextView view) {
        if (R.id.people_name == view.getId()) {
            view.setText(getResources().getString(R.string.expense_field_payer));
        }
    }

    @Override
    public void setSelectedPeopleKeys(List<Integer> selectedPeopleKeys) {
        this.selectedPeopleKeys = selectedPeopleKeys;
    }
}
