package pl.com.weddingPlanner.view.budget;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

import pl.com.weddingPlanner.R;
import pl.com.weddingPlanner.databinding.ActivityNewExpenseBinding;
import pl.com.weddingPlanner.persistence.entity.Category;
import pl.com.weddingPlanner.persistence.entity.Expense;
import pl.com.weddingPlanner.persistence.entity.Person;
import pl.com.weddingPlanner.persistence.entity.Subcontractor;
import pl.com.weddingPlanner.util.DAOUtil;
import pl.com.weddingPlanner.util.DateUtil;
import pl.com.weddingPlanner.util.DebouncedOnClickListener;
import pl.com.weddingPlanner.validator.AmountValidator;
import pl.com.weddingPlanner.validator.ValidationUtil;
import pl.com.weddingPlanner.view.BaseActivity;
import pl.com.weddingPlanner.view.NavigationActivity;
import pl.com.weddingPlanner.view.dialog.PeopleDialog;
import pl.com.weddingPlanner.view.dialog.SingleSelectionListDialog;
import pl.com.weddingPlanner.enums.CategoryType;
import pl.com.weddingPlanner.view.util.ComponentsUtil;
import pl.com.weddingPlanner.view.util.FormatUtil;
import pl.com.weddingPlanner.view.util.PersonUtil;

import static pl.com.weddingPlanner.view.NavigationActivity.FRAGMENT_TO_LOAD_ID;
import static pl.com.weddingPlanner.view.budget.ExpenseActivity.EXPENSE_ID_EXTRA;
import static pl.com.weddingPlanner.view.util.ComponentsUtil.setButtonEnablity;
import static pl.com.weddingPlanner.view.util.ExtraUtil.ACTIVITY_TITLE_EXTRA;
import static pl.com.weddingPlanner.view.util.LambdaUtil.getOnTextChangedTextWatcher;
import static pl.com.weddingPlanner.view.util.ResourceUtil.AMOUNT_ZERO;
import static pl.com.weddingPlanner.view.util.ResourceUtil.CATEGORY_OTHER;

public class NewExpenseActivity extends BaseActivity {

    private ActivityNewExpenseBinding binding;

    private int expenseId;
    private int headerTitle;

    private Expense expenseDetails;
    private Category categoryDetails;

    private AmountValidator amountValidator;

    private List<Integer> selectedPeopleKeys = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_new_expense);

        getAndSetExtras();
        setActivityToolbarContentWithBackIcon(headerTitle);

        getAndSetData();
        fillFields();
        setValidator();
        setListeners();
        setButtonEnablity(binding.addSaveButton, areFieldsValid());
    }

    private void getAndSetExtras() {
        expenseId = getIntent().getIntExtra(EXPENSE_ID_EXTRA, 0);
        headerTitle = getIntent().getIntExtra(ACTIVITY_TITLE_EXTRA, R.string.header_title_budget_new);
    }

    private void getAndSetData() {
        if (expenseId > 0) {
            expenseDetails = DAOUtil.getExpenseById(this, expenseId);
            categoryDetails = DAOUtil.getCategoryByNameAndType(this, expenseDetails.getCategory(), CategoryType.BUDGET.name());
        }
    }

    private void fillFields() {
        if (expenseDetails != null) {
            binding.expenseName.setText(expenseDetails.getTitle());

            binding.categoryTitle.setVisibility(View.VISIBLE);
            binding.categoryName.setText(categoryDetails.getName());

            binding.initialAmount.setText(FormatUtil.convertToAmount(expenseDetails.getInitialAmount()));

            if (StringUtils.isNotBlank(expenseDetails.getRecipient())) {
                binding.recipientName.setText(expenseDetails.getRecipient());
            }

            if (StringUtils.isNotBlank(expenseDetails.getForWhat())) {
                binding.forWhatName.setText(expenseDetails.getForWhat());
            }

            if (StringUtils.isNotBlank(expenseDetails.getPayers())) {
                String payers = PersonUtil.getPersonsStringFromIds(this, expenseDetails.getPayers());
                binding.peopleTitle.setVisibility(View.VISIBLE);
                binding.peopleName.setText(payers);
            }

            if (expenseDetails.getSubcontractorId() > 0) {
                Subcontractor subcontractor = DAOUtil.getSubcontractorById(this, expenseDetails.getSubcontractorId());
                binding.connectedSubcontractorTitle.setVisibility(View.VISIBLE);
                binding.connectedSubcontractorName.setText(subcontractor.getName());
            }

            binding.addSaveButton.setText(getResources().getString(R.string.button_save));
        }
    }

    private void setValidator() {
        amountValidator = new AmountValidator(this, true);
    }

    private void setListeners() {
        initAddButtonEnableStatusListener();
        setCategoryOnClickListener();
        setPeopleOnClickListener();
        setSubcontractorOnClickListener();
        initRootScrollViewListener();
        setOnFocusChangeListener();
        setAddSaveButtonClickListener();
    }

    private void initAddButtonEnableStatusListener() {
        TextWatcher listener = getOnTextChangedTextWatcher((s, start, before, count) ->
                setButtonEnablity(binding.addSaveButton, areFieldsValid())
        );

        binding.expenseName.addTextChangedListener(listener);
        binding.categoryName.addTextChangedListener(listener);
        binding.initialAmount.addTextChangedListener(listener);
        binding.recipientName.addTextChangedListener(listener);
        binding.forWhatName.addTextChangedListener(listener);
        binding.peopleName.addTextChangedListener(listener);
    }

    private boolean areFieldsValid() {
        return !binding.expenseName.getText().toString().isEmpty();
    }

    private void setCategoryOnClickListener() {
        binding.categoryLayout.setOnClickListener(new DebouncedOnClickListener(getResources().getInteger(R.integer.debounce_long_block_time_ms)) {
            @Override
            public void onDebouncedClick(View v) {
                clearFocusAndHideKeyboard();
                List<Category> categories = DAOUtil.getAllCategoriesByType(NewExpenseActivity.this, CategoryType.BUDGET.name());
                new SingleSelectionListDialog(NewExpenseActivity.this, categories, R.string.dialog_category_choice).showDialog();
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

    private void setSubcontractorOnClickListener() {
        binding.connectedSubcontractorLayout.setOnClickListener(new DebouncedOnClickListener(getResources().getInteger(R.integer.debounce_long_block_time_ms)) {
            @Override
            public void onDebouncedClick(View v) {
                clearFocusAndHideKeyboard();
                List<Subcontractor> subcontractors = DAOUtil.getAllNotConnectedSubcontractors(NewExpenseActivity.this);
                new SingleSelectionListDialog(NewExpenseActivity.this, subcontractors, R.string.dialog_expense_subcontractor_choice).showDialog();
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

                if (R.id.initial_amount == v.getId()) {
                    String amount = binding.initialAmount.getText().toString();
                    amount = FormatUtil.format(amount);

                    ValidationUtil.isValid(amount, true, NewExpenseActivity.this, amountValidator);

                    binding.initialAmount.setText(FormatUtil.convertToAmount(amount));
                }
            } else {
                if (R.id.initial_amount == v.getId()) {
                    String amount = binding.initialAmount.getText().toString();
                    binding.initialAmount.setText(amount.replaceAll("\\s", ""));
                }
            }
        };

        binding.expenseName.setOnFocusChangeListener(listener);
        binding.initialAmount.setOnFocusChangeListener(listener);
        binding.recipientName.setOnFocusChangeListener(listener);
        binding.forWhatName.setOnFocusChangeListener(listener);
    }

    private void setAddSaveButtonClickListener() {
        binding.addSaveButton.setOnClickListener(new DebouncedOnClickListener(getResources().getInteger(R.integer.debounce_long_block_time_ms)) {
            @Override
            public void onDebouncedClick(View v) {
                clearFocusAndHideKeyboard();

                Expense newExpense = getNewExpenseData();

                if (!newExpense.getTitle().isEmpty() && isAmountValid(newExpense.getInitialAmount())) {
                    prepareAmount(newExpense);

                    if (expenseDetails != null && expenseId > 0) {
                        proceedWhenEditingExpense(newExpense);
                    } else {
                        proceedWhenNewExpense(newExpense);
                    }

                    updateSubcontractorExpenseId(newExpense);
                } else {
                    if (newExpense.getTitle().isEmpty()) {
                        Toast toast = Toast.makeText(NewExpenseActivity.this, "Nazwa nie może być pusta", Toast.LENGTH_LONG);
                        toast.show();
                    } else if (!isAmountValid(newExpense.getInitialAmount())) {
                        Toast toast = Toast.makeText(NewExpenseActivity.this, "Niepoprawna kwota", Toast.LENGTH_LONG);
                        toast.show();
                    }
                }
            }
        });
    }

    private Expense getNewExpenseData() {
        String category = binding.categoryName.getText().toString();
        String initialAmount = binding.initialAmount.getText().toString();
        String recipient = binding.recipientName.getText().toString();
        String forWhat = binding.forWhatName.getText().toString();
        String payers = binding.peopleName.getText().toString();
        String subcontractorName = binding.connectedSubcontractorName.getText().toString();

        boolean isCategoryChosen = !category.equals(getResources().getString(R.string.field_category));
        boolean isAmountSet = !initialAmount.isEmpty();
        boolean isRecipientSet = !recipient.equals(getResources().getString(R.string.expense_field_for_whom));
        boolean isForWhatSet = !forWhat.equals(getResources().getString(R.string.expense_field_for_what));
        boolean arePayersSet = !payers.equals(getResources().getString(R.string.field_payer));
        boolean isSubcontractorChosen = !subcontractorName.equals(getResources().getString(R.string.header_title_subcontractor));

        String payersIdsString = arePayersSet ? getPayersIds(payers) : StringUtils.EMPTY;

        return Expense.builder()
                .title(binding.expenseName.getText().toString())
                .editDate(DateUtil.getNewDateWithHourString())
                .initialAmount(isAmountSet ? initialAmount : AMOUNT_ZERO)
                .category(isCategoryChosen ? category : CATEGORY_OTHER)
                .recipient(isRecipientSet ? recipient : StringUtils.EMPTY)
                .forWhat(isForWhatSet ? forWhat : StringUtils.EMPTY)
                .payers(payersIdsString)
                .subcontractorId(isSubcontractorChosen ? getChosenSubcontractorId(subcontractorName) : 0)
                .build();
    }

    private String getPayersIds(String payers) {
        String[] payersNames = payers.split("\\|", -1);
        StringJoiner payersIdsJoiner = new StringJoiner(",");

        List<Person> payerList = new ArrayList<>();
        for (String payerName : payersNames) {
            payerList.add(DAOUtil.getPersonByName(this, payerName.trim()));
        }

        for (Person payer : payerList) {
            payersIdsJoiner.add(payer.getId().toString());
        }

        return payersIdsJoiner.toString();
    }

    private int getChosenSubcontractorId(String subcontractorName) {
        return DAOUtil.getSubcontractorByName(this, subcontractorName).getId();
    }

    private void proceedWhenEditingExpense(Expense newExpense) {
        newExpense.setId(expenseId);

        DAOUtil.mergeExpense(getApplicationContext(), newExpense);

        Intent intent = new Intent(NewExpenseActivity.this, ExpenseActivity.class);
        intent.putExtra(EXPENSE_ID_EXTRA, expenseId);
        startActivity(intent);
    }

    private void proceedWhenNewExpense(Expense newExpense) {
        DAOUtil.insertExpense(getApplicationContext(), newExpense);

        Intent intent = new Intent(NewExpenseActivity.this, NavigationActivity.class);
        intent.putExtra(FRAGMENT_TO_LOAD_ID, R.id.navigation_budget);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private void updateSubcontractorExpenseId(Expense newExpense) {
        if (newExpense.getSubcontractorId() > 0) {
            if (expenseId > 0) {
                DAOUtil.updateSubcontractorExpenseId(this, expenseId, newExpense.getSubcontractorId());
            } else {
                DAOUtil.updateSubcontractorExpenseId(this, getLastExpenseId(), newExpense.getSubcontractorId());
            }
        }
    }

    private Integer getLastExpenseId() {
        List<Expense> allExpenses = DAOUtil.getAllExpenses(getApplicationContext());
        allExpenses.sort((expense1, expense2) -> expense1.getId().compareTo(expense2.getId()));

        return allExpenses.get(allExpenses.size() - 1).getId();
    }

    private boolean isAmountValid(String amount) {
        return ValidationUtil.isValid(amount, true, NewExpenseActivity.this, amountValidator);
    }

    private void prepareAmount(Expense newExpense) {
        newExpense.setInitialAmount(newExpense.getInitialAmount().replace(",", ".").replaceAll("\\s", ""));
    }

    @Override
    public void setDefaultFieldName(TextView view) {
        if (R.id.people_name == view.getId()) {
            view.setText(getResources().getString(R.string.field_payer));
        }
    }

    @Override
    public void setSelectedPeopleKeys(List<Integer> selectedPeopleKeys) {
        this.selectedPeopleKeys = selectedPeopleKeys;
    }
}
