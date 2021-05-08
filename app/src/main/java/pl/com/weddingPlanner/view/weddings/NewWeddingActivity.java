package pl.com.weddingPlanner.view.weddings;

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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import pl.com.weddingPlanner.R;
import pl.com.weddingPlanner.databinding.ActivityNewWeddingBinding;
import pl.com.weddingPlanner.model.PickedDate;
import pl.com.weddingPlanner.model.PickedTime;
import pl.com.weddingPlanner.model.User;
import pl.com.weddingPlanner.model.Wedding;
import pl.com.weddingPlanner.util.DebouncedOnClickListener;
import pl.com.weddingPlanner.util.FirebaseUtil;
import pl.com.weddingPlanner.validator.AmountValidator;
import pl.com.weddingPlanner.validator.ValidationUtil;
import pl.com.weddingPlanner.view.BaseActivity;
import pl.com.weddingPlanner.view.NavigationActivity;
import pl.com.weddingPlanner.view.dialog.DateDialog;
import pl.com.weddingPlanner.view.tasks.dialog.TaskTimeDialog;
import pl.com.weddingPlanner.view.util.ComponentsUtil;
import pl.com.weddingPlanner.view.util.FormatUtil;

import static pl.com.weddingPlanner.view.util.ComponentsUtil.setButtonEnablity;
import static pl.com.weddingPlanner.view.util.LambdaUtil.getOnTextChangedTextWatcher;
import static pl.com.weddingPlanner.view.util.ResourceUtil.AMOUNT_ZERO;

public class NewWeddingActivity extends BaseActivity {

    private ActivityNewWeddingBinding binding;

    private AmountValidator amountValidator;

    private PickedDate pickedDate;
    private PickedTime pickedTime;

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_new_wedding);

        setActivityToolbarContentWithBackIcon(R.string.header_title_wedding_new);

        setValidator();
        setListeners();
        setButtonEnablity(binding.addSaveButton, false);
    }

    private void setValidator() {
        amountValidator = new AmountValidator(this, true);
    }

    private void setListeners() {
        initAddButtonEnableStatusListener();
        setDateOnCLickListener();
        setTimeOnClickListener();
        initRootScrollViewListener();
        setOnFocusChangeListener();
        setAddButtonClickListener();
    }

    private void initAddButtonEnableStatusListener() {
        TextWatcher listener = getOnTextChangedTextWatcher((s, start, before, count) ->
                setButtonEnablity(binding.addSaveButton, areFieldsValid())
        );

        binding.weddingName.addTextChangedListener(listener);
        binding.weddingDate.addTextChangedListener(listener);
        binding.weddingTime.addTextChangedListener(listener);
        binding.initialAmount.addTextChangedListener(listener);
        binding.partnerName.addTextChangedListener(listener);
        binding.partnerEmail.addTextChangedListener(listener);
    }

    private boolean areFieldsValid() {
        return !binding.weddingName.getText().toString().isEmpty()
                && !binding.initialAmount.getText().toString().isEmpty()
                && !binding.partnerName.getText().toString().isEmpty();
    }

    private void setDateOnCLickListener() {
        binding.weddingDateLayout.setOnClickListener(new DebouncedOnClickListener(getResources().getInteger(R.integer.debounce_long_block_time_ms)) {
            @Override
            public void onDebouncedClick(View v) {
                clearFocusAndHideKeyboard();
                new DateDialog(NewWeddingActivity.this, pickedDate).showDialog();
            }
        });
    }

    private void setTimeOnClickListener() {
        binding.weddingTimeLayout.setOnClickListener(new DebouncedOnClickListener(getResources().getInteger(R.integer.debounce_long_block_time_ms)) {
            @Override
            public void onDebouncedClick(View v) {
                clearFocusAndHideKeyboard();
                new TaskTimeDialog(NewWeddingActivity.this, pickedTime).showDialog();
            }
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initRootScrollViewListener() {
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

                if (R.id.initial_amount == v.getId()) {
                    String amount = binding.initialAmount.getText().toString();
                    amount = FormatUtil.format(amount);

                    ValidationUtil.isValid(amount, true, NewWeddingActivity.this, amountValidator);

                    binding.initialAmount.setText(FormatUtil.convertToAmount(amount));
                }
            } else {
                if (R.id.initial_amount == v.getId()) {
                    String amount = binding.initialAmount.getText().toString();
                    binding.initialAmount.setText(amount.replaceAll("\\s", ""));
                }
            }
        };

        binding.weddingName.setOnFocusChangeListener(listener);
        binding.initialAmount.setOnFocusChangeListener(listener);
        binding.partnerName.setOnFocusChangeListener(listener);
        binding.partnerEmail.setOnFocusChangeListener(listener);
    }

    private void setAddButtonClickListener() {
        binding.addSaveButton.setOnClickListener(new DebouncedOnClickListener(getResources().getInteger(R.integer.debounce_long_block_time_ms)) {
            @Override
            public void onDebouncedClick(View v) {
                clearFocusAndHideKeyboard();

                Wedding newWedding = getNewWeddingData();

                if (!newWedding.getName().isEmpty() && !newWedding.getDate().isEmpty()) {
                    proceed(newWedding);
                } else {
                    Toast toast;
                    if (newWedding.getName().isEmpty()) {
                        toast = Toast.makeText(NewWeddingActivity.this, "Nazwa nie może być pusta", Toast.LENGTH_LONG);
                        toast.show();
                    } else if (newWedding.getDate().isEmpty()) {
                        toast = Toast.makeText(NewWeddingActivity.this, "Data nie może być pusta", Toast.LENGTH_LONG);
                        toast.show();
                    }
                }
            }
        });
    }

    private Wedding getNewWeddingData() {
        String date = binding.weddingDate.getText().toString();
        String time = binding.weddingTime.getText().toString();
        String initialAmount = binding.initialAmount.getText().toString();

        boolean isDateSet = !date.equals(getResources().getString(R.string.field_date));
        boolean isTimeSet = !time.equals(getResources().getString(R.string.field_time));
        boolean isAmountSet = !initialAmount.isEmpty();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", new Locale("PL"));
        String nowDateString = simpleDateFormat.format(new Date());

        return Wedding.builder()
                .id(UUID.randomUUID().toString())
                .name(binding.weddingName.getText().toString())
                .date(isDateSet ? date : StringUtils.EMPTY)
                .time(isTimeSet ? time : StringUtils.EMPTY)
                .ceremonyVenue(binding.ceremonyVenue.getText().toString())
                .banquetVenue(binding.banquetVenue.getText().toString())
                .initialBudget(isAmountSet ? initialAmount : AMOUNT_ZERO)
                .ownerUid(currentUser.getUid())
                .partnerUid(StringUtils.EMPTY)
                .partnerName(binding.partnerName.getText().toString())
                .partnerEmail(binding.partnerEmail.getText().toString())
                .people(StringUtils.EMPTY)
                .creationDate(nowDateString)
                .build();
    }

    private void proceed(Wedding newWedding) {
//        DAOUtil.insertWedding(getApplicationContext(), newWedding);

        FirebaseUtil.getWeddingChild(databaseReference, newWedding).setValue(newWedding).addOnCompleteListener(addWeddingTask -> {
            if (addWeddingTask.isSuccessful()) {
                getAndSetUserInfo(newWedding);
            }
        });
    }

    private void getAndSetUserInfo(Wedding newWedding) {
        FirebaseUtil.getUser(databaseReference, currentUser).addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                User userInfo = task.getResult().getValue(User.class);
                if (userInfo != null) {
                    User userUpdated = getUpdatedUserInfo(userInfo, newWedding);
                    updateUserWeddingsAndStartActivity(userUpdated);
                }
            } else {
                Toast.makeText(NewWeddingActivity.this, "Niepowodzenie podczas tworzenia wesela",
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    private User getUpdatedUserInfo(User userInfo, Wedding newWedding) {
        List<String> userWeddings = userInfo.getWeddings();

        if (userWeddings == null) {
            userWeddings = new ArrayList<>();
        }
        userWeddings.add(newWedding.getId());

        userInfo.setWeddings(userWeddings);
        userInfo.setCurrentWedding(newWedding.getId());

        return userInfo;
    }

    private void updateUserWeddingsAndStartActivity(User userUpdated) {
        FirebaseUtil.getUserChild(databaseReference, currentUser).setValue(userUpdated).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                startActivity(new Intent(NewWeddingActivity.this, NavigationActivity.class));
            }
        });
    }

    @Override
    public void setDefaultFieldName(TextView view) {
        if (view.getId() == R.id.wedding_date) {
            view.setText(getResources().getString(R.string.field_date));
        }
    }

    @Override
    public void setPickedDate(PickedDate pickedDate) {
        this.pickedDate = pickedDate;
    }

    @Override
    public void setPickedTime(PickedTime pickedTime) {
        this.pickedTime = pickedTime;
    }
}
