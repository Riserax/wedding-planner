package pl.com.weddingPlanner.view.subcontractors;

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

import java.util.List;

import pl.com.weddingPlanner.R;
import pl.com.weddingPlanner.databinding.ActivityNewSubcontractorBinding;
import pl.com.weddingPlanner.enums.CategoryType;
import pl.com.weddingPlanner.enums.CollaborationStage;
import pl.com.weddingPlanner.persistence.entity.Category;
import pl.com.weddingPlanner.persistence.entity.Expense;
import pl.com.weddingPlanner.persistence.entity.Subcontractor;
import pl.com.weddingPlanner.util.DAOUtil;
import pl.com.weddingPlanner.util.DateUtil;
import pl.com.weddingPlanner.util.DebouncedOnClickListener;
import pl.com.weddingPlanner.validator.AmountValidator;
import pl.com.weddingPlanner.validator.ValidationUtil;
import pl.com.weddingPlanner.view.BaseActivity;
import pl.com.weddingPlanner.view.dialog.SingleSelectionListDialog;
import pl.com.weddingPlanner.view.util.ButtonsUtil;
import pl.com.weddingPlanner.view.util.ComponentsUtil;
import pl.com.weddingPlanner.view.util.FormatUtil;
import pl.com.weddingPlanner.view.util.LinksUtil;
import pl.com.weddingPlanner.view.util.SubcontractorUtil;

import static pl.com.weddingPlanner.view.util.ComponentsUtil.setButtonEnablity;
import static pl.com.weddingPlanner.view.util.ExtraUtil.ACTIVITY_TITLE_EXTRA;
import static pl.com.weddingPlanner.view.util.ExtraUtil.SUBCONTRACTOR_ID_EXTRA;
import static pl.com.weddingPlanner.view.util.LambdaUtil.getOnTextChangedTextWatcher;
import static pl.com.weddingPlanner.view.util.ResourceUtil.AMOUNT_ZERO;
import static pl.com.weddingPlanner.view.util.ResourceUtil.CATEGORY_OTHER;

public class NewSubcontractorActivity extends BaseActivity {

    private ActivityNewSubcontractorBinding binding;

    private int subcontractorId;
    private int headerTitle;

    private Subcontractor subcontractor;

    private AmountValidator amountValidator;

    private CollaborationStage collaborationStage = CollaborationStage.NONE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_new_subcontractor);

        getExtrasAndSetVariables();
        setActivityToolbarContentWithBackIcon(headerTitle);

        getAndSetData();
        fillFields();
        setValidator();
        setListeners();
        setButtonEnablity(binding.addSaveButton, areFieldsValid());
    }

    private void getExtrasAndSetVariables() {
        subcontractorId = getIntent().getIntExtra(SUBCONTRACTOR_ID_EXTRA, 0);
        headerTitle = getIntent().getIntExtra(ACTIVITY_TITLE_EXTRA, R.string.header_title_subcontractor_new);
    }

    private void getAndSetData() {
        if (subcontractorId > 0) {
            subcontractor = DAOUtil.getSubcontractorById(this, subcontractorId);

            if (StringUtils.isNotBlank(subcontractor.getCollaborationStage())) {
                collaborationStage = CollaborationStage.valueOf(subcontractor.getCollaborationStage());
            }
        }
    }

    private void fillFields() {
        if (subcontractor != null) {
            binding.name.setText(subcontractor.getName());

            if (StringUtils.isNotBlank(subcontractor.getCategory())) {
                binding.categoryTitle.setVisibility(View.VISIBLE);
                binding.categoryName.setText(subcontractor.getCategory());
            }

            if (StringUtils.isNotBlank(subcontractor.getEmail())) {
                binding.email.setText(subcontractor.getEmail());
            }

            if (StringUtils.isNotBlank(subcontractor.getPhone())) {
                binding.phone.setText(subcontractor.getPhone());
            }

            SubcontractorUtil.setWebsiteField(subcontractor, binding);

            if (StringUtils.isNotBlank(subcontractor.getAddress())) {
                binding.address.setText(subcontractor.getAddress());
            }

            SubcontractorUtil.setSelectedCollaborationStage(collaborationStage, binding, this);

            if (StringUtils.isNotBlank(subcontractor.getCost())) {
                binding.cost.setText(FormatUtil.convertToAmount(subcontractor.getCost()));
            }

            if (StringUtils.isNotBlank(subcontractor.getNotes())) {
                binding.notes.setText(subcontractor.getNotes());
            }

            if (subcontractorId > 0) {
                binding.infoLayout.setVisibility(View.GONE);
            }

            binding.addSaveButton.setText(getResources().getString(R.string.button_save));
        }
    }

    private void setValidator() {
        amountValidator = new AmountValidator(this, true);
    }

    private void setListeners() {
        setAddButtonEnableStatusListener();
        setCategoryOnClickListener();
        setRootScrollViewListener();
        setOnFocusChangeListener();
        setConsideringButtonListener();
        setInContactButtonListener();
        setConfirmedButtonListener();
        setPaidButtonListener();
        setAddSaveButtonClickListener();
    }

    private void setAddButtonEnableStatusListener() {
        TextWatcher listener = getOnTextChangedTextWatcher((s, start, before, count) ->
                setButtonEnablity(binding.addSaveButton, areFieldsValid())
        );

        binding.name.addTextChangedListener(listener);
        binding.categoryName.addTextChangedListener(listener);
    }

    private boolean areFieldsValid() {
        return !binding.name.getText().toString().isEmpty();
    }

    private void setCategoryOnClickListener() {
        binding.categoryLayout.setOnClickListener(new DebouncedOnClickListener(getResources().getInteger(R.integer.debounce_long_block_time_ms)) {
            @Override
            public void onDebouncedClick(View v) {
                clearFocusAndHideKeyboard();
                List<Category> categories = DAOUtil.getAllCategoriesByType(NewSubcontractorActivity.this, CategoryType.SUBCONTRACTORS.name());
                new SingleSelectionListDialog(NewSubcontractorActivity.this, categories, R.string.dialog_category_choice).showDialog();
            }
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setRootScrollViewListener() {
        binding.rootScrollView.setOnTouchListener((v, event) -> {
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

                if (R.id.cost == v.getId()) {
                    String cost = binding.cost.getText().toString();
                    cost = FormatUtil.format(cost);

                    ValidationUtil.isValid(cost, true, NewSubcontractorActivity.this, amountValidator);

                    binding.cost.setText(FormatUtil.convertToAmount(cost));
                }
            } else {
                if (R.id.cost == v.getId()) {
                    String amount = binding.cost.getText().toString();
                    binding.cost.setText(amount.replaceAll("\\s", ""));
                }
            }
        };

        binding.name.setOnFocusChangeListener(listener);
        binding.email.setOnFocusChangeListener(listener);
        binding.phone.setOnFocusChangeListener(listener);
        binding.website.setOnFocusChangeListener(listener);
        binding.address.setOnFocusChangeListener(listener);
        binding.cost.setOnFocusChangeListener(listener);
        binding.notes.setOnFocusChangeListener(listener);
    }

    private void setConsideringButtonListener() {
        binding.consideringButton.setOnClickListener(v -> {
            clearFocusAndHideKeyboard();
            if (CollaborationStage.CONSIDERING.equals(collaborationStage)) {
                ButtonsUtil.setButtonSelection(binding.consideringButton, this, false);
                collaborationStage = CollaborationStage.NONE;
            } else {
                SubcontractorUtil.setConsideringButtonsSelection(binding, getApplicationContext());
                collaborationStage = CollaborationStage.CONSIDERING;
            }
        });
    }

    private void setInContactButtonListener() {
        binding.inContactButton.setOnClickListener(v -> {
            clearFocusAndHideKeyboard();
            if (CollaborationStage.IN_CONTACT.equals(collaborationStage)) {
                ButtonsUtil.setButtonSelection(binding.inContactButton, this, false);
                collaborationStage = CollaborationStage.NONE;
            } else {
                SubcontractorUtil.setInContactButtonsSelection(binding, getApplicationContext());
                collaborationStage = CollaborationStage.IN_CONTACT;
            }
        });
    }

    private void setConfirmedButtonListener() {
        binding.confirmedButton.setOnClickListener(v -> {
            clearFocusAndHideKeyboard();
            if (CollaborationStage.CONFIRMED.equals(collaborationStage)) {
                ButtonsUtil.setButtonSelection(binding.confirmedButton, this, false);
                collaborationStage = CollaborationStage.NONE;
            } else {
                SubcontractorUtil.setConfirmedButtonsSelection(binding, getApplicationContext());
                collaborationStage = CollaborationStage.CONFIRMED;
            }
        });
    }

    private void setPaidButtonListener() {
        binding.paidButton.setOnClickListener(v -> {
            clearFocusAndHideKeyboard();
            if (CollaborationStage.PAID.equals(collaborationStage)) {
                ButtonsUtil.setButtonSelection(binding.paidButton, this, false);
                collaborationStage = CollaborationStage.NONE;
            } else {
                SubcontractorUtil.setPaidButtonsSelection(binding, getApplicationContext());
                collaborationStage = CollaborationStage.PAID;
            }
        });
    }

    private void setAddSaveButtonClickListener() {
        binding.addSaveButton.setOnClickListener(new DebouncedOnClickListener(getResources().getInteger(R.integer.debounce_long_block_time_ms)) {
            @Override
            public void onDebouncedClick(View v) {
                clearFocusAndHideKeyboard();

                Subcontractor newSubcontractor = getNewSubcontractorData();

                if (StringUtils.isNotBlank(newSubcontractor.getName()) && isCostValid(newSubcontractor.getCost())) {
                    prepareAmount(newSubcontractor);

                    if (subcontractor != null && subcontractorId > 0) {
                        proceedWhenEditingSubcontractor(newSubcontractor);
                    } else {
                        proceedWhenNewSubcontractor(newSubcontractor);
                    }
                } else {
                    if (newSubcontractor.getName().isEmpty()) {
                        Toast toast = Toast.makeText(NewSubcontractorActivity.this, "Nazwa nie może być pusta", Toast.LENGTH_LONG);
                        toast.show();
                    } else if (!isCostValid(newSubcontractor.getCost())) {
                        Toast toast = Toast.makeText(NewSubcontractorActivity.this, "Niepoprawna kwota", Toast.LENGTH_LONG);
                        toast.show();
                    }
                }
            }
        });
    }

    private Subcontractor getNewSubcontractorData() {
        String category = binding.categoryName.getText().toString();

        boolean isCategoryChosen = !category.equals(getResources().getString(R.string.field_category));
        boolean areSubcontractorAndExpenseIdNotNull = subcontractor != null && subcontractor.getExpenseId() != null;

        return Subcontractor.builder()
                .name(binding.name.getText().toString())
                .category(isCategoryChosen ? category : CATEGORY_OTHER)
                .email(binding.email.getText().toString())
                .phone(binding.phone.getText().toString())
                .website(LinksUtil.getWebsiteLink(binding.website))
                .address(binding.address.getText().toString())
                .collaborationStage(collaborationStage.name())
                .expenseId(areSubcontractorAndExpenseIdNotNull ? subcontractor.getExpenseId() : 0)
                .cost(binding.cost.getText().toString())
                .notes(binding.notes.getText().toString())
                .build();
    }

    private boolean isCostValid(String cost) {
        return ValidationUtil.isValid(cost, true, NewSubcontractorActivity.this, amountValidator);
    }

    private void prepareAmount(Subcontractor newSubcontractor) {
        newSubcontractor.setCost(newSubcontractor.getCost().replace(",", ".").replaceAll("\\s", ""));
    }

    private void proceedWhenEditingSubcontractor(Subcontractor newSubcontractor) {
        newSubcontractor.setId(subcontractorId);

        DAOUtil.mergeSubcontractor(getApplicationContext(), newSubcontractor);

        Intent intent = new Intent(NewSubcontractorActivity.this, SubcontractorsActivity.class);
        startActivity(intent);
    }

    private void proceedWhenNewSubcontractor(Subcontractor newSubcontractor) {
        if (!isNameUnique(newSubcontractor.getName())) {
            Toast.makeText(this, "Istnieje już podwykonawca o podanej nazwie", Toast.LENGTH_LONG).show();
        } else {
            insertExpense(newSubcontractor);
            insertSubcontractorAndStartActivity(newSubcontractor);
        }
    }

    private boolean isNameUnique(String name) {
        Subcontractor subcontractor = DAOUtil.getSubcontractorByName(this, name);
        return subcontractor == null;
    }

    private void insertExpense(Subcontractor newSubcontractor) {
        Expense newExpense = Expense.builder()
                .title(newSubcontractor.getName())
                .initialAmount(StringUtils.isNotBlank(newSubcontractor.getCost()) ? newSubcontractor.getCost() : AMOUNT_ZERO)
                .category(newSubcontractor.getCategory())
                .recipient(newSubcontractor.getName())
                .editDate(DateUtil.getNewDateWithHourString())
                .build();

        DAOUtil.insertExpense(this, newExpense);
    }

    private void insertSubcontractorAndStartActivity(Subcontractor newSubcontractor) {
        newSubcontractor.setExpenseId(getLastExpenseId());

        DAOUtil.insertSubcontractor(getApplicationContext(), newSubcontractor);
        DAOUtil.updateExpenseSubcontractorId(this, getLastSubcontractorId(), getLastExpenseId());

        Intent intent = new Intent(NewSubcontractorActivity.this, SubcontractorsActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private Integer getLastSubcontractorId() {
        List<Subcontractor> allSubcontractors = DAOUtil.getAllSubcontractors(getApplicationContext());
        allSubcontractors.sort((subcontractor1, subcontractor2) -> subcontractor1.getId().compareTo(subcontractor2.getId()));

        return allSubcontractors.get(allSubcontractors.size() - 1).getId();
    }

    private Integer getLastExpenseId() {
        List<Expense> allExpenses = DAOUtil.getAllExpenses(getApplicationContext());
        allExpenses.sort((expense1, expense2) -> expense1.getId().compareTo(expense2.getId()));

        return allExpenses.get(allExpenses.size() - 1).getId();
    }

    @Override
    public void setDefaultFieldName(TextView view) {
        if (R.id.category_name == view.getId()) {
            view.setText(getResources().getString(R.string.field_category));
        }
    }
}
