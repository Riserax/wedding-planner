package pl.com.weddingPlanner.view.guests;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import org.apache.commons.lang3.StringUtils;

import pl.com.weddingPlanner.R;
import pl.com.weddingPlanner.databinding.ActivityGuestDetailsBinding;
import pl.com.weddingPlanner.enums.GuestType;
import pl.com.weddingPlanner.enums.Presence;
import pl.com.weddingPlanner.persistence.entity.Guest;
import pl.com.weddingPlanner.util.DAOUtil;
import pl.com.weddingPlanner.view.BaseActivity;
import pl.com.weddingPlanner.view.dialog.QuestionDialog;
import pl.com.weddingPlanner.view.util.LinksUtil;

import static pl.com.weddingPlanner.view.util.ExtraUtil.ACTIVITY_TITLE_EXTRA;
import static pl.com.weddingPlanner.view.util.ExtraUtil.GUEST_ID_EXTRA;

public class GuestDetailsActivity extends BaseActivity {

    private ActivityGuestDetailsBinding binding;

    private int headerTitleResId = R.string.header_title_guest_details;

    private Guest guestDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_guest_details);

        getAndSetGuestDetails();
        setHeaderTitleResId();

        setActivityToolbarContentWithBackIcon(headerTitleResId);

        setComponents();
        setListeners();
    }

    private void getAndSetGuestDetails() {
        int guestId = getIntent().getExtras().getInt(GUEST_ID_EXTRA, 0);
        guestDetails = DAOUtil.getGuestById(this, guestId);
    }

    private void setHeaderTitleResId() {
        headerTitleResId = GuestType.valueOf(guestDetails.getType()).getNameResId();
    }

    private void setComponents() {
        binding.nameSurname.setText(guestDetails.getNameSurname());

        setConnectedOrAccompanyLayout();
        setAgeRange();
        setCategory();
        setTable();
        setInvitationStatus();
        setContact();
        setNotes();
    }

    private void setConnectedOrAccompanyLayout() {
        if (guestDetails.getConnectedWithId() > 0) {
            if (isGuest()) {
                binding.accompanyLayout.setVisibility(View.VISIBLE);
                LinksUtil.makeLinkAlike(binding.accompany, this, getConnectedGuestNameSurname());
            } else if (!isGuest()) {
                binding.connectedWithLayout.setVisibility(View.VISIBLE);
                LinksUtil.makeLinkAlike(binding.connectedWith, this, getConnectedGuestNameSurname());
            }
        }
    }

    private String getConnectedGuestNameSurname() {
        return DAOUtil.getGuestById(this, guestDetails.getConnectedWithId()).getNameSurname();
    }

    private void setAgeRange() {
        if (StringUtils.isNotBlank(guestDetails.getAgeRange())) {
            binding.age.setText(guestDetails.getAgeRange());
        } else {
            binding.age.setText(getString(R.string.field_not_specified));
        }
    }

    private void setCategory() {
        if (StringUtils.isNotBlank(guestDetails.getCategory())) {
            binding.category.setText(guestDetails.getCategory());
        } else {
            binding.category.setText(getString(R.string.field_not_specified));
        }
    }

    private void setTable() {
        if (guestDetails.getTableNumber() != 0) {
            String table = "Stół nr " + guestDetails.getTableNumber();
            binding.table.setText(table);
        } else {
            binding.table.setText(getString(R.string.field_not_specified));
        }
    }

    private void setInvitationStatus() {
        if (StringUtils.isNotBlank(guestDetails.getPresence())) {
            int presenceResId = Presence.valueOf(guestDetails.getPresence()).getTextResourceId();
            binding.invitationStatus.setText(getString(presenceResId));
        } else {
            binding.invitationStatus.setText(getString(R.string.field_not_specified));
        }
    }

    private void setContact() {
        if (StringUtils.isNotBlank(guestDetails.getContact())) {
            binding.contact.setText(guestDetails.getContact());
        } else {
            binding.contact.setText(getString(R.string.field_lack));
        }
    }

    private void setNotes() {
        if (StringUtils.isNotBlank(guestDetails.getNotes())) {
            binding.notes.setText(guestDetails.getNotes());
        } else {
            binding.notes.setText(getString(R.string.field_lack));
        }
    }

    private void setListeners() {
        setConnectedAccompanyOnClickListener();
        setConnectedGuestOnClickListener();
        setGuestFloatingButtonListener();
        setDeleteGuestListener();
        setEditGuestListener();
    }

    private void setConnectedAccompanyOnClickListener() {
        binding.accompany.setOnClickListener(v -> {
            Intent intent = new Intent(this, GuestDetailsActivity.class);
            intent.putExtra(GUEST_ID_EXTRA, guestDetails.getConnectedWithId());
            startActivity(intent);
        });
    }

    private void setConnectedGuestOnClickListener() {
        binding.connectedWith.setOnClickListener(v -> {
            Intent intent = new Intent(this, GuestDetailsActivity.class);
            intent.putExtra(GUEST_ID_EXTRA, guestDetails.getConnectedWithId());
            startActivity(intent);
        });
    }

    private void setGuestFloatingButtonListener() {
        binding.guestFloatingButton.setOnClickListener(v -> {
            if (binding.deleteLayout.getVisibility() == View.GONE && binding.editLayout.getVisibility() == View.GONE) {
                showFloatingMenu();
            } else {
                hideFloatingMenu();
            }
        });
    }

    private void setDeleteGuestListener() {
        binding.deleteLayout.setOnClickListener(v -> {
            int questionResId = isGuestAndHasConnection() ? R.string.guest_details_delete_question_guest : R.string.guest_details_delete_question;
            new QuestionDialog(GuestDetailsActivity.this, getString(questionResId)).showDialog();
            hideFloatingMenu();
        });
    }

    private boolean isGuestAndHasConnection() {
        return isGuest() && guestDetails.getConnectedWithId() > 0;
    }

    private void setEditGuestListener() {
        binding.editLayout.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), NewGuestActivity.class);
            intent.putExtra(GUEST_ID_EXTRA, guestDetails.getId());
            intent.putExtra(ACTIVITY_TITLE_EXTRA, R.string.header_title_guest_edit);
            startActivity(intent);

            hideFloatingMenu();
        });
    }

    private void showFloatingMenu() {
        binding.deleteLayout.setVisibility(View.VISIBLE);
        binding.editLayout.setVisibility(View.VISIBLE);
        binding.backgroundFade.setVisibility(View.VISIBLE);
        binding.guestFloatingButton.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_clear));
    }

    private void hideFloatingMenu() {
        binding.deleteLayout.setVisibility(View.GONE);
        binding.editLayout.setVisibility(View.GONE);
        binding.backgroundFade.setVisibility(View.GONE);
        binding.guestFloatingButton.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_menu));
    }

    @Override
    public void executeQuestionDialog() {
        DAOUtil.deleteGuest(this, guestDetails);
        updateGuestConnectedWithId();

        Intent intent = new Intent(this, GuestsActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private void updateGuestConnectedWithId() {
        if (isGuest()) {
            DAOUtil.deleteGuestById(this, guestDetails.getConnectedWithId());
        } else {
            if (guestDetails.getConnectedWithId() != 0) {
                DAOUtil.updateGuestConnectedWithId(getApplicationContext(), guestDetails.getConnectedWithId(), 0);
            }
        }
    }

    private boolean isGuest() {
        return GuestType.GUEST.name().equals(guestDetails.getType());
    }
}

