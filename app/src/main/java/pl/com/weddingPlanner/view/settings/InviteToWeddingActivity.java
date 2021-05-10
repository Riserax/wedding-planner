package pl.com.weddingPlanner.view.settings;

import android.os.Bundle;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;

import java.util.ArrayList;
import java.util.List;

import pl.com.weddingPlanner.R;
import pl.com.weddingPlanner.databinding.ActivityInviteToWeddingBinding;
import pl.com.weddingPlanner.model.User;
import pl.com.weddingPlanner.model.WeddingInvitation;
import pl.com.weddingPlanner.util.FirebaseUtil;
import pl.com.weddingPlanner.util.StringUtil;
import pl.com.weddingPlanner.view.BaseActivity;
import pl.com.weddingPlanner.view.settings.dialog.InviteToWeddingConfirmationDialog;

import static pl.com.weddingPlanner.util.FirebaseUtil.isSuccessfulAndNotNull;

public class InviteToWeddingActivity extends BaseActivity {

    private ActivityInviteToWeddingBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_invite_to_wedding);
        setActivityToolbarContentWithBackIcon(R.string.header_title_invite_to_wedding);

        setListeners();
    }

    private void setListeners() {
        binding.inviteButton.setOnClickListener(v -> processInvitation());
    }

    private void processInvitation() {
        FirebaseUtil.getUser(databaseReference, currentUser).addOnCompleteListener(task -> {
            if (isSuccessfulAndNotNull(task)) {
                User user = task.getResult().getValue(User.class);
                String emailAddress = binding.email.getText().toString();
                WeddingInvitation invitation = WeddingInvitation.builder()
                        .email(emailAddress)
                        .wedding(user.getCurrentWedding())
                        .build();

                if (canSendTo(user, emailAddress)) {
                    insertAndShowCode(invitation, user.getInvitedPeople());
                } else {
                    Toast.makeText(InviteToWeddingActivity.this, "Już wysłałaś/eś zaproszenie dla użytkownika o tym adresie e-mail",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private boolean canSendTo(User user, String emailAddress) {
        return user != null && (user.getInvitedPeople() == null || !user.getInvitedPeople().contains(emailAddress));
    }

    private void insertAndShowCode(WeddingInvitation invitation, List<String> invitedPeople) {
        String invitationCode = StringUtil.generateRandom();
        FirebaseUtil.getInvitationRoot(databaseReference).child(invitationCode).setValue(invitation).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                updateInvitedPeople(invitedPeople, invitation, invitationCode);
            }
        });
    }

    private void updateInvitedPeople(List<String> invitedPeople, WeddingInvitation invitation, String invitationCode) {
        List<String> updatedInvitedPeople = getUpdatedInvitedPeople(invitedPeople, invitation.getEmail());

        FirebaseUtil.getUserChild(databaseReference, currentUser).child("invitedPeople").setValue(updatedInvitedPeople).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                new InviteToWeddingConfirmationDialog(InviteToWeddingActivity.this, invitationCode, invitation.getEmail()).showDialog();
            }
        });
    }

    private List<String> getUpdatedInvitedPeople(List<String> invitedPeople, String emailAddress) {
        if (invitedPeople == null) {
            invitedPeople = new ArrayList<>();
        }

        invitedPeople.add(emailAddress);

        return invitedPeople;
    }
}
