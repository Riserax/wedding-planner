package pl.com.weddingPlanner.view.budget;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import org.apache.commons.lang3.StringUtils;

import java.util.List;

import pl.com.weddingPlanner.R;
import pl.com.weddingPlanner.databinding.ActivityNewPaymentBinding;
import pl.com.weddingPlanner.enums.PaymentState;
import pl.com.weddingPlanner.model.PickedDate;
import pl.com.weddingPlanner.persistence.entity.Payment;
import pl.com.weddingPlanner.persistence.entity.Person;
import pl.com.weddingPlanner.util.DAOUtil;
import pl.com.weddingPlanner.util.DebouncedOnClickListener;
import pl.com.weddingPlanner.validator.AmountValidator;
import pl.com.weddingPlanner.validator.ValidationUtil;
import pl.com.weddingPlanner.view.BaseActivity;
import pl.com.weddingPlanner.view.dialog.DateDialog;
import pl.com.weddingPlanner.view.dialog.QuestionDialog;
import pl.com.weddingPlanner.view.dialog.SingleSelectionListDialog;
import pl.com.weddingPlanner.view.util.ComponentsUtil;
import pl.com.weddingPlanner.view.util.FormatUtil;
import pl.com.weddingPlanner.view.util.PersonUtil;

import static pl.com.weddingPlanner.view.budget.ExpenseActivity.EXPENSE_ID_EXTRA;
import static pl.com.weddingPlanner.view.budget.ExpenseActivity.TAB_ID_EXTRA;
import static pl.com.weddingPlanner.view.budget.ExpenseActivity.TAB_PAYMENTS_ID;
import static pl.com.weddingPlanner.view.budget.ExpensePaymentsFragment.PAYMENT_ID_EXTRA;
import static pl.com.weddingPlanner.view.dialog.QuestionDialog.CLASS_EXTRA;
import static pl.com.weddingPlanner.view.util.ComponentsUtil.setButtonEnability;
import static pl.com.weddingPlanner.view.util.ExtraUtil.ACTIVITY_TITLE_EXTRA;
import static pl.com.weddingPlanner.view.util.LambdaUtil.getOnTextChangedTextWatcher;
import static pl.com.weddingPlanner.view.util.ResourceUtil.AMOUNT_ZERO;

public class NewPaymentActivity extends BaseActivity {

    private AmountValidator amountValidator;

    private ActivityNewPaymentBinding binding;

    private int expenseId;
    private int paymentId;
    private int headerTitle;

    private Payment paymentDetails;

    private PickedDate pickedDate;
    private PaymentState state = PaymentState.PENDING;

    private boolean isAmountSet;
    private boolean isDateSet;
    private boolean isPayerSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_new_payment);

        getAndSetExtras();
        setActivityToolbarContentWithBackIcon(headerTitle);

        getAndSetData();
        fillFields();
        setValidator();
        getAndSetExtra();
        setListeners();
        setButtonEnability(binding.addSaveButton, areFieldsValid());
    }

    private void getAndSetExtras() {
        expenseId = getIntent().getIntExtra(EXPENSE_ID_EXTRA, 0);
        paymentId = getIntent().getIntExtra(PAYMENT_ID_EXTRA, 0);
        headerTitle = getIntent().getIntExtra(ACTIVITY_TITLE_EXTRA, R.string.header_title_payment_new);
    }

    private void getAndSetData() {
        if (paymentId > 0) {
            paymentDetails = DAOUtil.getPaymentById(this, paymentId);
        }
    }

    private void fillFields() {
        if (paymentDetails != null) {
            binding.toolbarLayout.deleteButton.setVisibility(View.VISIBLE);

            binding.paymentTitle.setText(paymentDetails.getTitle());

            binding.amount.setText(FormatUtil.convertToAmount(paymentDetails.getAmount()));

            if (StringUtils.isNotBlank(paymentDetails.getPayer())) {
                String payer = PersonUtil.getPersonsStringFromIds(this, paymentDetails.getPayer());
                binding.payerTitle.setVisibility(View.VISIBLE);
                binding.payerName.setText(payer);
            }

            binding.paymentDate.setText(paymentDetails.getDate());

            if (PaymentState.PENDING == PaymentState.valueOf(paymentDetails.getState())) {
                setPendingSelectedPaidNotSelected();
            } else {
                setPaidSelectedPendingNotSelected();
            }

            binding.infoText.setText(paymentDetails.getInfo());

            binding.addSaveButton.setText(getResources().getString(R.string.button_save));
        }
    }

    private void setValidator() {
        amountValidator = new AmountValidator(this,true);
    }

    private void getAndSetExtra() {
        this.expenseId = getIntent().getExtras().getInt(EXPENSE_ID_EXTRA, 0);
    }

    private void setListeners() {
        initAddButtonEnableStatusListener();
        setDeleteButtonListener();
        setPayerOnClickListener();
        setDateOnCLickListener();
        initRootScrollViewListener();
        setOnFocusChangeListener();
        setPendingButtonListener();
        setPaidButtonListener();
        setAddButtonClickListener();
    }

    private void initAddButtonEnableStatusListener() {
        TextWatcher listener = getOnTextChangedTextWatcher((s, start, before, count) ->
                setButtonEnability(binding.addSaveButton, areFieldsValid())
        );

        binding.paymentTitle.addTextChangedListener(listener);
        binding.amount.addTextChangedListener(listener);
        binding.payerName.addTextChangedListener(listener);
        binding.paymentDate.addTextChangedListener(listener);
        binding.infoText.addTextChangedListener(listener);
    }

    private boolean areFieldsValid() {
        return !binding.paymentTitle.getText().toString().isEmpty();
    }

    private void setDeleteButtonListener() {
        binding.toolbarLayout.deleteButton.setOnClickListener(v -> {
            new QuestionDialog(this, getResources().getString(R.string.payment_delete_question), getDeleteIntent()).showDialog();
        });
    }

    private void setPayerOnClickListener() {
        binding.payerLayout.setOnClickListener(new DebouncedOnClickListener(getResources().getInteger(R.integer.debounce_long_block_time_ms)) {
            @Override
            public void onDebouncedClick(View v) {
                clearFocusAndHideKeyboard();
                List<Person> people = DAOUtil.getAllPersons(NewPaymentActivity.this);
                new SingleSelectionListDialog(NewPaymentActivity.this, people, R.string.dialog_person_choice).showDialog();
            }
        });
    }

    private void setDateOnCLickListener() {
        binding.paymentDateLayout.setOnClickListener(new DebouncedOnClickListener(getResources().getInteger(R.integer.debounce_long_block_time_ms)) {
            @Override
            public void onDebouncedClick(View v) {
                clearFocusAndHideKeyboard();
                new DateDialog(NewPaymentActivity.this, pickedDate).showDialog();
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

                if (R.id.amount == v.getId()) {
                    String amount = binding.amount.getText().toString();
                    amount = FormatUtil.format(amount);

                    ValidationUtil.isValid(amount, true, NewPaymentActivity.this, amountValidator);

                    binding.amount.setText(FormatUtil.convertToAmount(amount));
                }
            } else {
                if (R.id.amount == v.getId()) {
                    String amount = binding.amount.getText().toString();
                    binding.amount.setText(amount.replaceAll("\\s", ""));
                }
            }
        };

        binding.paymentTitle.setOnFocusChangeListener(listener);
        binding.amount.setOnFocusChangeListener(listener);
        binding.infoText.setOnFocusChangeListener(listener);
    }

    private void setPendingButtonListener() {
        binding.awaitingButton.setOnClickListener(v -> {
            setPendingSelectedPaidNotSelected();
            setButtonEnability(binding.addSaveButton, areFieldsValid());
        });
    }

    private void setPaidButtonListener() {
        binding.paidButton.setOnClickListener(v -> {
            setPaidSelectedPendingNotSelected();
            setButtonEnability(binding.addSaveButton, areFieldsValid());
        });
    }

    private void setAddButtonClickListener() {
        binding.addSaveButton.setOnClickListener(new DebouncedOnClickListener(getResources().getInteger(R.integer.debounce_long_block_time_ms)) {
            @Override
            public void onDebouncedClick(View v) {
                clearFocusAndHideKeyboard();

                Payment newPayment = getNewPaymentData();

                if (!newPayment.getTitle().isEmpty() && !newPayment.getDate().isEmpty() && isAmountValid(newPayment.getAmount())) {
                    prepareAmount(newPayment);

                    if (paymentDetails != null && paymentId > 0) {
                        newPayment.setId(paymentId);
                        DAOUtil.mergePayment(getApplicationContext(), newPayment);
                    } else {
                        DAOUtil.insertPayment(getApplicationContext(), newPayment);
                    }

                    Intent intent = new Intent(NewPaymentActivity.this, ExpenseActivity.class);
                    intent.putExtra(EXPENSE_ID_EXTRA, expenseId);
                    intent.putExtra(TAB_ID_EXTRA, TAB_PAYMENTS_ID);
                    startActivity(intent);
                } else {
                    Toast toast;
                    if (newPayment.getTitle().isEmpty()) {
                        toast = Toast.makeText(NewPaymentActivity.this, "Tytuł nie może być pusty", Toast.LENGTH_LONG);
                        toast.show();
                    } else if (newPayment.getDate().isEmpty()) {
                        toast = Toast.makeText(NewPaymentActivity.this, "Data nie może być pusta", Toast.LENGTH_LONG);
                        toast.show();
                    }
                }
            }
        });
    }

    private Payment getNewPaymentData() {
        String amount = binding.amount.getText().toString();
        String payer = binding.payerName.getText().toString();
        String date = binding.paymentDate.getText().toString();

        isAmountSet = !amount.isEmpty();
        isPayerSet = !payer.equals(getResources().getString(R.string.field_payer));
        isDateSet = !date.equals(getResources().getString(R.string.payment_field_date));

        String payersIdsString = isPayerSet ? getPayerId(payer) : StringUtils.EMPTY;

        return Payment.builder()
                .title(binding.paymentTitle.getText().toString())
                .expenseId(expenseId)
                .date(isDateSet ? date : StringUtils.EMPTY)
                .amount(isAmountSet ? amount : AMOUNT_ZERO)
                .payer(isPayerSet ? payersIdsString : StringUtils.EMPTY)
                .state(state.name())
                .info(binding.infoText.getText().toString())
                .build();
    }

    private String getPayerId(String payer) {
        Person person = DAOUtil.getPersonByName(this, payer.trim());
        return person.getId().toString();
    }

    private boolean isAmountValid(String amount) {
        return ValidationUtil.isValid(amount, true, NewPaymentActivity.this, amountValidator);
    }

    private void prepareAmount(Payment newPayment) {
        newPayment.setAmount(newPayment.getAmount().replace(",", ".").replaceAll("\\s", ""));
    }

    private void setPendingSelectedPaidNotSelected() {
        setPendingButtonSelected();
        setPaidButtonNotSelected();
        state = PaymentState.PENDING;
    }

    private void setPaidSelectedPendingNotSelected() {
        setPendingButtonNotSelected();
        setPaidButtonSelected();
        state = PaymentState.PAID;
    }

    private void setPendingButtonSelected() {
        binding.awaitingButton.setTextColor(ContextCompat.getColor(this, R.color.white_FFFFFF));
        binding.awaitingButton.setBackground(ContextCompat.getDrawable(this, R.drawable.button_shape_light));
        binding.awaitingButton.setClickable(false);
    }

    private void setPendingButtonNotSelected() {
        binding.awaitingButton.setTextColor(ContextCompat.getColor(this, R.color.colorPrimaryLight));
        binding.awaitingButton.setBackground(ContextCompat.getDrawable(this, R.drawable.button_shape_border_only));
        binding.awaitingButton.setClickable(true);
    }

    private void setPaidButtonSelected() {
        binding.paidButton.setTextColor(ContextCompat.getColor(this, R.color.white_FFFFFF));
        binding.paidButton.setBackground(ContextCompat.getDrawable(this, R.drawable.button_shape_light));
        binding.paidButton.setClickable(false);
    }

    private void setPaidButtonNotSelected() {
        binding.paidButton.setTextColor(ContextCompat.getColor(this, R.color.colorPrimaryLight));
        binding.paidButton.setBackground(ContextCompat.getDrawable(this, R.drawable.button_shape_border_only));
        binding.paidButton.setClickable(true);
    }

    public Intent getDeleteIntent() {
        Intent intent = new Intent(NewPaymentActivity.this, ExpenseActivity.class);
        intent.putExtra(PAYMENT_ID_EXTRA, paymentId);
        intent.putExtra(EXPENSE_ID_EXTRA, expenseId);
        intent.putExtra(CLASS_EXTRA, NewPaymentActivity.class.toString());
        intent.putExtra(TAB_ID_EXTRA, TAB_PAYMENTS_ID);
        return intent;
    }

    @Override
    public void setDefaultFieldName(TextView view) {
        switch (view.getId()) {
            case R.id.people_name:
                view.setText(getResources().getString(R.string.field_payer));
                break;
            case R.id.payment_date:
                view.setText(getResources().getString(R.string.payment_field_date));
                break;
        }
    }

    @Override
    public void setPickedDate(PickedDate pickedDate) {
        this.pickedDate = pickedDate;
    }
}
