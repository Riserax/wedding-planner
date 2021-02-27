package pl.com.weddingPlanner.view.guests;

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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import pl.com.weddingPlanner.R;
import pl.com.weddingPlanner.databinding.ActivityNewGuestBinding;
import pl.com.weddingPlanner.enums.CategoryTypeEnum;
import pl.com.weddingPlanner.enums.GuestTypeEnum;
import pl.com.weddingPlanner.enums.PresenceEnum;
import pl.com.weddingPlanner.persistence.entity.AgeRange;
import pl.com.weddingPlanner.persistence.entity.Category;
import pl.com.weddingPlanner.persistence.entity.Guest;
import pl.com.weddingPlanner.persistence.entity.Table;
import pl.com.weddingPlanner.util.DAOUtil;
import pl.com.weddingPlanner.util.DebouncedOnClickListener;
import pl.com.weddingPlanner.view.BaseActivity;
import pl.com.weddingPlanner.view.dialog.SingleSelectionListDialog;
import pl.com.weddingPlanner.view.util.ComponentsUtil;
import pl.com.weddingPlanner.view.util.GuestUtil;

import static pl.com.weddingPlanner.view.util.ComponentsUtil.setButtonEnability;
import static pl.com.weddingPlanner.view.util.LambdaUtil.getOnTextChangedTextWatcher;

public class NewGuestActivity extends BaseActivity {

    private ActivityNewGuestBinding binding;

    private GuestTypeEnum guestType = GuestTypeEnum.GUEST;
    private PresenceEnum presenceEnum = PresenceEnum.NONE;

    private boolean isGuestChosen;
    private boolean isAgeChosen;
    private boolean isCategoryChosen;
    private boolean isTableChosen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_new_guest);

        setActivityToolbarContentWithBackIcon(R.string.header_title_guest_new);

        setListeners();
        setButtonEnability(binding.addButton, false);
    }

    private void setListeners() {
        setAddButtonEnableStatusListener();
        setAgeRangeOnClickListener();
        setCategoryOnClickListener();
        setTableOnClickListener();
        setRootScrollViewListener();
        setOnFocusChangeListener();
        setGuestButtonListener();
        setAccompanyButtonListener();
        setInvitationSentButtonListener();
        setInvitationAcceptedButtonListener();
        setInvitationRejectedButtonListener();
        setInvitationAwaitingButtonListener();
        setAddButtonClickListener();
    }

    private void setAddButtonEnableStatusListener() {
        TextWatcher listener = getOnTextChangedTextWatcher((s, start, before, count) ->
                setButtonEnability(binding.addButton, areFieldsValid())
        );

        binding.guestNameSurname.addTextChangedListener(listener);
    }

    private boolean areFieldsValid() {
        return !binding.guestNameSurname.getText().toString().isEmpty();
    }

    private void setGuestButtonListener() {
        binding.guestButton.setOnClickListener(v -> {
            clearFocusAndHideKeyboard();
            setGuestSelectedAccompanyNotSelected();
            binding.chosenGuestLayout.setVisibility(View.GONE);
        });
    }

    private void setAccompanyButtonListener() {
        binding.accompanyButton.setOnClickListener(v -> {
            clearFocusAndHideKeyboard();
            setAccompanySelectedGuestNotSelected();
            List<Guest> guestsWithoutAccompany = DAOUtil.getAllGuestsWithoutAccompany(NewGuestActivity.this);
            new SingleSelectionListDialog(NewGuestActivity.this, guestsWithoutAccompany, R.string.dialog_guest_choice).showDialog();
        });
    }

    private void setAgeRangeOnClickListener() {
        binding.ageLayout.setOnClickListener(new DebouncedOnClickListener(getResources().getInteger(R.integer.debounce_long_block_time_ms)) {
            @Override
            public void onDebouncedClick(View v) {
                clearFocusAndHideKeyboard();
                List<AgeRange> ageRanges = DAOUtil.getAllAgeRanges(NewGuestActivity.this);
                new SingleSelectionListDialog(NewGuestActivity.this, ageRanges, R.string.dialog_age_choice).showDialog();
            }
        });
    }

    private void setCategoryOnClickListener() {
        binding.categoryLayout.setOnClickListener(new DebouncedOnClickListener(getResources().getInteger(R.integer.debounce_long_block_time_ms)) {
            @Override
            public void onDebouncedClick(View v) {
                clearFocusAndHideKeyboard();
                List<Category> categories = DAOUtil.getAllCategoriesByType(NewGuestActivity.this, CategoryTypeEnum.GUESTS.name());
                new SingleSelectionListDialog(NewGuestActivity.this, categories, R.string.dialog_category_choice).showDialog();
            }
        });
    }

    private void setTableOnClickListener() {
        binding.tableLayout.setOnClickListener(new DebouncedOnClickListener(getResources().getInteger(R.integer.debounce_long_block_time_ms)) {
            @Override
            public void onDebouncedClick(View v) {
                clearFocusAndHideKeyboard();
                List<Table> tables = DAOUtil.getAllTables(NewGuestActivity.this);
                new SingleSelectionListDialog(NewGuestActivity.this, tables, R.string.dialog_table_choice).showDialog();
            }
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setRootScrollViewListener() {
        binding.guestRootScrollView.setOnTouchListener((v, event) -> {
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

        binding.guestNameSurname.setOnFocusChangeListener(listener);
        binding.guestButton.setOnFocusChangeListener(listener);
        binding.accompanyButton.setOnFocusChangeListener(listener);
        binding.guestContact.setOnFocusChangeListener(listener);
        binding.guestNotes.setOnFocusChangeListener(listener);
    }

    private void setInvitationSentButtonListener() {
        binding.sentButton.setOnClickListener(v -> {
            if (PresenceEnum.INVITATION_SENT.equals(presenceEnum)) {
                clearFocusAndHideKeyboard();
                GuestUtil.setButtonSelection(binding.sentButton, this, false);
                presenceEnum = PresenceEnum.NONE;
            } else {
                clearFocusAndHideKeyboard();
                GuestUtil.setInvitationSentButtonsSelection(binding, getApplicationContext());
                presenceEnum = PresenceEnum.INVITATION_SENT;
            }
        });
    }

    private void setInvitationAcceptedButtonListener() {
        binding.acceptedButton.setOnClickListener(v -> {
            if (PresenceEnum.CONFIRMED_PRESENCE.equals(presenceEnum)) {
                clearFocusAndHideKeyboard();
                GuestUtil.setButtonSelection(binding.acceptedButton, this, false);
                presenceEnum = PresenceEnum.NONE;
            } else {
                clearFocusAndHideKeyboard();
                GuestUtil.setInvitationAcceptedButtonsSelection(binding, getApplicationContext());
                presenceEnum = PresenceEnum.CONFIRMED_PRESENCE;
            }
        });
    }

    private void setInvitationRejectedButtonListener() {
        binding.rejectedButton.setOnClickListener(v -> {
            if (PresenceEnum.CONFIRMED_ABSENCE.equals(presenceEnum)) {
                clearFocusAndHideKeyboard();
                GuestUtil.setButtonSelection(binding.rejectedButton, this, false);
                presenceEnum = PresenceEnum.NONE;
            } else {
                clearFocusAndHideKeyboard();
                GuestUtil.setInvitationRejectedButtonsSelection(binding, getApplicationContext());
                presenceEnum = PresenceEnum.CONFIRMED_ABSENCE;
            }
        });
    }

    private void setInvitationAwaitingButtonListener() {
        binding.awaitingButton.setOnClickListener(v -> {
            if (PresenceEnum.AWAITING.equals(presenceEnum)) {
                clearFocusAndHideKeyboard();
                GuestUtil.setButtonSelection(binding.awaitingButton, this, false);
                presenceEnum = PresenceEnum.NONE;
            } else {
                clearFocusAndHideKeyboard();
                GuestUtil.setInvitationAwaitingButtonsSelection(binding, getApplicationContext());
                presenceEnum = PresenceEnum.AWAITING;
            }
        });
    }

    private void setAddButtonClickListener() {
        binding.addButton.setOnClickListener(new DebouncedOnClickListener(getResources().getInteger(R.integer.debounce_long_block_time_ms)) {
            @Override
            public void onDebouncedClick(View v) {
                clearFocusAndHideKeyboard();

                Guest newGuest = getNewTaskData();

                if (StringUtils.isNotBlank(newGuest.getNameSurname())) {
                    proceedWhenNameSurnameNotBlank(newGuest);
                } else {
                    Toast.makeText(NewGuestActivity.this, "Imię i nazwisko nie może być puste", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private Guest getNewTaskData() {
        String chosenGuestInfo = binding.chosenGuestInfo.getText().toString();
        String ageRange = binding.ageName.getText().toString();
        String category = binding.categoryName.getText().toString();
        String tableInfo = binding.tableName.getText().toString();

        isGuestChosen = StringUtils.isNotBlank(chosenGuestInfo);
        isAgeChosen = !ageRange.equals(getResources().getString(R.string.guest_field_age));
        isCategoryChosen = !category.equals(getResources().getString(R.string.field_category));
        isTableChosen = !tableInfo.equals(getResources().getString(R.string.guest_field_table));

        String chosenGuestNameSurname = getChosenGuestNameSurname(chosenGuestInfo);

        return Guest.builder()
                .nameSurname(binding.guestNameSurname.getText().toString())
                .type(guestType.name())
                .connectedWithId(GuestTypeEnum.ACCOMPANY.equals(guestType) ? getChosenGuestId(chosenGuestNameSurname) : 0)
                .ageRange(isAgeChosen ? ageRange : StringUtils.EMPTY)
                .category(isCategoryChosen ? category : StringUtils.EMPTY)
                .tableNumber(isTableChosen ? getTableNumber(tableInfo) : 0)
                .presence(presenceEnum.name())
                .contact(binding.guestContact.getText().toString())
                .notes(binding.guestNotes.getText().toString())
                .build();
    }

    private int getChosenGuestId(String chosenGuestNameSurname) {
        if (StringUtils.isNotBlank(chosenGuestNameSurname)) {
            return DAOUtil.getGuestByNameSurname(this, chosenGuestNameSurname).getId();
        } else {
            return 0;
        }
    }

    private String getChosenGuestNameSurname(String chosenGuestInfo) {
        if (isGuestChosen) {
            return chosenGuestInfo.substring(26);
        } else {
            return StringUtils.EMPTY;
        }
    }

    private int getTableNumber(String tableInfo) {
        Pattern regex = Pattern.compile("(\\d+)");
        Matcher matcher = regex.matcher(tableInfo);

        if (matcher.find()) {
            return Integer.parseInt(matcher.group(1));
        }

        return 0;
    }

    private void proceedWhenNameSurnameNotBlank(Guest newGuest) {
        if (!isNameSurnameUnique(newGuest.getNameSurname())) {
            Toast.makeText(this, "Istnieje już gość o podanym imieniu i nazwisku", Toast.LENGTH_LONG).show();
        } else if (GuestTypeEnum.ACCOMPANY.name().equals(newGuest.getType()) && newGuest.getConnectedWithId() == 0) {
            Toast.makeText(this, "Należy wybrać gościa, dla którego będzie to osoba towarzysząca", Toast.LENGTH_LONG).show();
        } else {
            insertGuestAndStartActivity(newGuest);
        }
    }

    private boolean isNameSurnameUnique(String nameSurname) {
        Guest guest = DAOUtil.getGuestByNameSurname(this, nameSurname);
        return guest == null;
    }

    private void updateGuestConnectedWithId(Integer connectedWIthId) {
        List<Guest> allGuests = DAOUtil.getAllGuests(getApplicationContext());
        allGuests.sort((guest1, guest2) -> guest1.getId().compareTo(guest2.getId()));

        Integer lastGuestId = allGuests.get(allGuests.size() - 1).getId();
        DAOUtil.updateGuestConnectedWithId(getApplicationContext(), lastGuestId, connectedWIthId);
    }

    private void insertGuestAndStartActivity(Guest newGuest) {
        DAOUtil.insertGuest(getApplicationContext(), newGuest);
        updateGuestConnectedWithId(newGuest.getConnectedWithId());

        Intent intent = new Intent(NewGuestActivity.this, GuestsActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private void setGuestSelectedAccompanyNotSelected() {
        GuestUtil.setButtonSelection(binding.guestButton, this, true);
        GuestUtil.setButtonSelection(binding.accompanyButton, this, false);
        guestType = GuestTypeEnum.GUEST;
    }

    private void setAccompanySelectedGuestNotSelected() {
        GuestUtil.setButtonSelection(binding.guestButton, this, false);
        GuestUtil.setButtonSelection(binding.accompanyButton, this, true);
        guestType = GuestTypeEnum.ACCOMPANY;
    }

    @Override
    public void setDefaultFieldName(TextView view) {
        switch (view.getId()) {
            case R.id.age_name:
                view.setText(getResources().getString(R.string.guest_field_age));
                break;
            case R.id.category_name:
                view.setText(getResources().getString(R.string.field_category));
                break;
            case R.id.table_name:
                view.setText(getResources().getString(R.string.guest_field_table));
                break;
        }
    }
}
