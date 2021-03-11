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
import pl.com.weddingPlanner.view.util.ButtonsUtil;
import pl.com.weddingPlanner.view.util.ComponentsUtil;
import pl.com.weddingPlanner.view.util.GuestUtil;

import static pl.com.weddingPlanner.view.util.ComponentsUtil.setButtonEnability;
import static pl.com.weddingPlanner.view.util.ExtraUtil.ACTIVITY_TITLE_EXTRA;
import static pl.com.weddingPlanner.view.util.ExtraUtil.GUEST_ID_EXTRA;
import static pl.com.weddingPlanner.view.util.LambdaUtil.getOnTextChangedTextWatcher;

public class NewGuestActivity extends BaseActivity {

    private ActivityNewGuestBinding binding;

    private int guestId;
    private int headerTitle;

    private Guest guestDetails;

    private GuestTypeEnum guestType = GuestTypeEnum.GUEST;
    private PresenceEnum selectedPresenceStatus = PresenceEnum.NONE;

    private boolean isConnectedWithChosen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_new_guest);

        getExtrasAndSetVariables();
        setActivityToolbarContentWithBackIcon(headerTitle);

        getAndSetData();
        fillFields();
        setListeners();
        setButtonEnability(binding.addSaveButton, areFieldsValid());
    }

    private void getExtrasAndSetVariables() {
        guestId = getIntent().getIntExtra(GUEST_ID_EXTRA, 0);
        headerTitle = getIntent().getIntExtra(ACTIVITY_TITLE_EXTRA, R.string.header_title_guest_new);
    }

    private void getAndSetData() {
        if (guestId > 0) {
            guestDetails = DAOUtil.getGuestById(this, guestId);

            if (StringUtils.isNotBlank(guestDetails.getPresence())) {
                selectedPresenceStatus = PresenceEnum.valueOf(guestDetails.getPresence());
            }
        }
    }

    private void fillFields() {
        if (guestDetails != null) {
            binding.guestNameSurname.setText(guestDetails.getNameSurname());

            if (isGuest()) {
                setGuestSelectedAccompanyNotSelected();
            } else {
                setAccompanySelectedGuestNotSelected();
            }

            if (!isGuest() && guestDetails.getConnectedWithId() > 0) {
                Guest connectedGuest = DAOUtil.getGuestById(this, guestDetails.getConnectedWithId());
                binding.connectedWithLayout.setVisibility(View.VISIBLE);
                binding.connectedWithInfo.setText(getString(R.string.guest_field_connected_with_info, connectedGuest.getNameSurname()));
            }

            if (StringUtils.isNotBlank(guestDetails.getAgeRange())) {
                binding.ageTitle.setVisibility(View.VISIBLE);
                binding.ageName.setText(guestDetails.getAgeRange());
            }

            if (StringUtils.isNotBlank(guestDetails.getCategory())) {
                binding.categoryTitle.setVisibility(View.VISIBLE);
                binding.categoryName.setText(guestDetails.getCategory());
            }

            if (guestDetails.getTableNumber() > 0) {
                Table table = DAOUtil.getTableById(this, guestDetails.getTableNumber());
                binding.tableTitle.setVisibility(View.VISIBLE);
                binding.tableName.setText(GuestUtil.getTableDescription(table));
            }

            GuestUtil.setSelectedInvitationStatus(selectedPresenceStatus, binding, this);

            if (StringUtils.isNotBlank(guestDetails.getContact())) {
                binding.guestContact.setText(guestDetails.getContact());
            }

            if (StringUtils.isNotBlank(guestDetails.getNotes())) {
                binding.guestNotes.setText(guestDetails.getNotes());
            }

            binding.addSaveButton.setText(getResources().getString(R.string.button_save));
        }
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
        setAddSaveButtonClickListener();
    }

    private void setAddButtonEnableStatusListener() {
        TextWatcher listener = getOnTextChangedTextWatcher((s, start, before, count) ->
                setButtonEnability(binding.addSaveButton, areFieldsValid())
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
            binding.connectedWithLayout.setVisibility(View.GONE);
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
            clearFocusAndHideKeyboard();
            if (PresenceEnum.INVITATION_SENT.equals(selectedPresenceStatus)) {
                ButtonsUtil.setButtonSelection(binding.sentButton, this, false);
                selectedPresenceStatus = PresenceEnum.NONE;
            } else {
                GuestUtil.setInvitationSentButtonsSelection(binding, getApplicationContext());
                selectedPresenceStatus = PresenceEnum.INVITATION_SENT;
            }
        });
    }

    private void setInvitationAcceptedButtonListener() {
        binding.acceptedButton.setOnClickListener(v -> {
            clearFocusAndHideKeyboard();
            if (PresenceEnum.CONFIRMED_PRESENCE.equals(selectedPresenceStatus)) {
                ButtonsUtil.setButtonSelection(binding.acceptedButton, this, false);
                selectedPresenceStatus = PresenceEnum.NONE;
            } else {
                GuestUtil.setInvitationAcceptedButtonsSelection(binding, getApplicationContext());
                selectedPresenceStatus = PresenceEnum.CONFIRMED_PRESENCE;
            }
        });
    }

    private void setInvitationRejectedButtonListener() {
        binding.rejectedButton.setOnClickListener(v -> {
            clearFocusAndHideKeyboard();
            if (PresenceEnum.CONFIRMED_ABSENCE.equals(selectedPresenceStatus)) {
                ButtonsUtil.setButtonSelection(binding.rejectedButton, this, false);
                selectedPresenceStatus = PresenceEnum.NONE;
            } else {
                GuestUtil.setInvitationRejectedButtonsSelection(binding, getApplicationContext());
                selectedPresenceStatus = PresenceEnum.CONFIRMED_ABSENCE;
            }
        });
    }

    private void setInvitationAwaitingButtonListener() {
        binding.awaitingButton.setOnClickListener(v -> {
            clearFocusAndHideKeyboard();
            if (PresenceEnum.AWAITING.equals(selectedPresenceStatus)) {
                ButtonsUtil.setButtonSelection(binding.awaitingButton, this, false);
                selectedPresenceStatus = PresenceEnum.NONE;
            } else {
                GuestUtil.setInvitationAwaitingButtonsSelection(binding, getApplicationContext());
                selectedPresenceStatus = PresenceEnum.AWAITING;
            }
        });
    }

    private void setAddSaveButtonClickListener() {
        binding.addSaveButton.setOnClickListener(new DebouncedOnClickListener(getResources().getInteger(R.integer.debounce_long_block_time_ms)) {
            @Override
            public void onDebouncedClick(View v) {
                clearFocusAndHideKeyboard();

                Guest newGuest = getNewGuestData();

                if (StringUtils.isNotBlank(newGuest.getNameSurname())) {
                    if (guestDetails != null && guestId > 0) {
                        proceedWhenEditingGuest(newGuest);
                    } else {
                        proceedWhenNewGuest(newGuest);
                    }
                } else {
                    Toast.makeText(NewGuestActivity.this, "Imię i nazwisko nie może być puste", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private Guest getNewGuestData() {
        String connectedWithInfo = binding.connectedWithInfo.getText().toString();
        String ageRange = binding.ageName.getText().toString();
        String category = binding.categoryName.getText().toString();
        String tableInfo = binding.tableName.getText().toString();

        isConnectedWithChosen = StringUtils.isNotBlank(connectedWithInfo);
        boolean isAgeChosen = !ageRange.equals(getResources().getString(R.string.guest_field_age));
        boolean isCategoryChosen = !category.equals(getResources().getString(R.string.field_category));
        boolean isTableChosen = !tableInfo.equals(getResources().getString(R.string.guest_field_table));
        boolean isPresenceChosen = !PresenceEnum.NONE.equals(selectedPresenceStatus);

        String connectedWithNameSurname = getConnectedWithNameSurname(connectedWithInfo);

        return Guest.builder()
                .nameSurname(binding.guestNameSurname.getText().toString())
                .type(guestType.name())
                .connectedWithId(GuestTypeEnum.ACCOMPANY.equals(guestType) ? getConnectedWithId(connectedWithNameSurname) : 0)
                .ageRange(isAgeChosen ? ageRange : StringUtils.EMPTY)
                .category(isCategoryChosen ? category : StringUtils.EMPTY)
                .tableNumber(isTableChosen ? getTableNumber(tableInfo) : 0)
                .presence(isPresenceChosen ? selectedPresenceStatus.name() : StringUtils.EMPTY)
                .contact(binding.guestContact.getText().toString())
                .notes(binding.guestNotes.getText().toString())
                .build();
    }

    private int getConnectedWithId(String connectedWithNameSurname) {
        if (StringUtils.isNotBlank(connectedWithNameSurname)) {
            return DAOUtil.getGuestByNameSurname(this, connectedWithNameSurname).getId();
        } else {
            return 0;
        }
    }

    private String getConnectedWithNameSurname(String connectedWithInfo) {
        if (isConnectedWithChosen) {
            if (guestId > 0) {
                return connectedWithInfo.substring(13);
            } else {
                return connectedWithInfo.substring(26);
            }
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

    private void proceedWhenEditingGuest(Guest newGuest) {
        newGuest.setId(guestId);

        DAOUtil.mergeGuest(getApplicationContext(), newGuest);

        Intent intent = new Intent(NewGuestActivity.this, GuestsActivity.class);
        startActivity(intent);
    }

    private void proceedWhenNewGuest(Guest newGuest) {
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

    private void insertGuestAndStartActivity(Guest newGuest) {
        DAOUtil.insertGuest(getApplicationContext(), newGuest);
        updateGuestConnectedWithId(newGuest.getConnectedWithId());

        Intent intent = new Intent(NewGuestActivity.this, GuestsActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private void updateGuestConnectedWithId(Integer connectedWIthId) {
        List<Guest> allGuests = DAOUtil.getAllGuests(getApplicationContext());
        allGuests.sort((guest1, guest2) -> guest1.getId().compareTo(guest2.getId()));

        Integer lastGuestId = allGuests.get(allGuests.size() - 1).getId();
        DAOUtil.updateGuestConnectedWithId(getApplicationContext(), lastGuestId, connectedWIthId);
    }

    private void setGuestSelectedAccompanyNotSelected() {
        ButtonsUtil.setButtonSelection(binding.guestButton, this, true);
        ButtonsUtil.setButtonSelection(binding.accompanyButton, this, false);
        guestType = GuestTypeEnum.GUEST;
    }

    private void setAccompanySelectedGuestNotSelected() {
        ButtonsUtil.setButtonSelection(binding.guestButton, this, false);
        ButtonsUtil.setButtonSelection(binding.accompanyButton, this, true);
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

    private boolean isGuest() {
        return GuestTypeEnum.GUEST.name().equals(guestDetails.getType());
    }
}
