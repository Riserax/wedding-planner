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
import pl.com.weddingPlanner.enums.CategoryTypeEnum;
import pl.com.weddingPlanner.enums.CollaborationStageEnum;
import pl.com.weddingPlanner.persistence.entity.Category;
import pl.com.weddingPlanner.persistence.entity.Subcontractor;
import pl.com.weddingPlanner.util.DAOUtil;
import pl.com.weddingPlanner.util.DebouncedOnClickListener;
import pl.com.weddingPlanner.validator.AmountValidator;
import pl.com.weddingPlanner.validator.ValidationUtil;
import pl.com.weddingPlanner.view.BaseActivity;
import pl.com.weddingPlanner.view.dialog.SingleSelectionListDialog;
import pl.com.weddingPlanner.view.util.ButtonsUtil;
import pl.com.weddingPlanner.view.util.ComponentsUtil;
import pl.com.weddingPlanner.view.util.FormatUtil;
import pl.com.weddingPlanner.view.util.SubcontractorUtil;

import static pl.com.weddingPlanner.view.util.ComponentsUtil.setButtonEnability;
import static pl.com.weddingPlanner.view.util.ExtraUtil.ACTIVITY_TITLE_EXTRA;
import static pl.com.weddingPlanner.view.util.ExtraUtil.SUBCONTRACTOR_ID_EXTRA;
import static pl.com.weddingPlanner.view.util.LambdaUtil.getOnTextChangedTextWatcher;
import static pl.com.weddingPlanner.view.util.ResourceUtil.CATEGORY_OTHER;

public class NewSubcontractorActivity extends BaseActivity {

    private ActivityNewSubcontractorBinding binding;

    private int subcontractorId;
    private int headerTitle;

    private Subcontractor subcontractor;

    private AmountValidator amountValidator;

    private CollaborationStageEnum collaborationStage = CollaborationStageEnum.NONE;

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
        setButtonEnability(binding.addSaveButton, areFieldsValid());
    }

    private void getExtrasAndSetVariables() {
        subcontractorId = getIntent().getIntExtra(SUBCONTRACTOR_ID_EXTRA, 0);
        headerTitle = getIntent().getIntExtra(ACTIVITY_TITLE_EXTRA, R.string.header_title_subcontractor_new);
    }

    private void getAndSetData() {
        if (subcontractorId > 0) {
            subcontractor = DAOUtil.getSubcontractorById(this, subcontractorId);

            if (StringUtils.isNotBlank(subcontractor.getCollaborationStage())) {
                collaborationStage = CollaborationStageEnum.valueOf(subcontractor.getCollaborationStage());
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
                setButtonEnability(binding.addSaveButton, areFieldsValid())
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
                List<Category> categories = DAOUtil.getAllCategoriesByType(NewSubcontractorActivity.this, CategoryTypeEnum.SUBCONTRACTORS.name());
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
            if (CollaborationStageEnum.CONSIDERING.equals(collaborationStage)) {
                ButtonsUtil.setButtonSelection(binding.consideringButton, this, false);
                collaborationStage = CollaborationStageEnum.NONE;
            } else {
                SubcontractorUtil.setConsideringButtonsSelection(binding, getApplicationContext());
                collaborationStage = CollaborationStageEnum.CONSIDERING;
            }
        });
    }

    private void setInContactButtonListener() {
        binding.inContactButton.setOnClickListener(v -> {
            clearFocusAndHideKeyboard();
            if (CollaborationStageEnum.IN_CONTACT.equals(collaborationStage)) {
                ButtonsUtil.setButtonSelection(binding.inContactButton, this, false);
                collaborationStage = CollaborationStageEnum.NONE;
            } else {
                SubcontractorUtil.setInContactButtonsSelection(binding, getApplicationContext());
                collaborationStage = CollaborationStageEnum.IN_CONTACT;
            }
        });
    }

    private void setConfirmedButtonListener() {
        binding.confirmedButton.setOnClickListener(v -> {
            clearFocusAndHideKeyboard();
            if (CollaborationStageEnum.CONFIRMED.equals(collaborationStage)) {
                ButtonsUtil.setButtonSelection(binding.confirmedButton, this, false);
                collaborationStage = CollaborationStageEnum.NONE;
            } else {
                SubcontractorUtil.setConfirmedButtonsSelection(binding, getApplicationContext());
                collaborationStage = CollaborationStageEnum.CONFIRMED;
            }
        });
    }

    private void setPaidButtonListener() {
        binding.paidButton.setOnClickListener(v -> {
            clearFocusAndHideKeyboard();
            if (CollaborationStageEnum.PAID.equals(collaborationStage)) {
                ButtonsUtil.setButtonSelection(binding.paidButton, this, false);
                collaborationStage = CollaborationStageEnum.NONE;
            } else {
                SubcontractorUtil.setPaidButtonsSelection(binding, getApplicationContext());
                collaborationStage = CollaborationStageEnum.PAID;
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

        return Subcontractor.builder()
                .name(binding.name.getText().toString())
                .category(isCategoryChosen ? category : CATEGORY_OTHER)
                .email(binding.email.getText().toString())
                .phone(binding.phone.getText().toString())
                .website(SubcontractorUtil.getWebsiteLink(binding.website))
                .address(binding.address.getText().toString())
                .collaborationStage(collaborationStage.name())
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
            insertSubcontractorAndStartActivity(newSubcontractor);
        }
    }

    private boolean isNameUnique(String name) {
        Subcontractor subcontractor = DAOUtil.getSubcontractorByName(this, name);
        return subcontractor == null;
    }

    private void insertSubcontractorAndStartActivity(Subcontractor newSubcontractor) {
        DAOUtil.insertSubcontractor(getApplicationContext(), newSubcontractor);

        Intent intent = new Intent(NewSubcontractorActivity.this, SubcontractorsActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @Override
    public void setDefaultFieldName(TextView view) {
        if (R.id.category_name == view.getId()) {
            view.setText(getResources().getString(R.string.field_category));
        }
    }
}
