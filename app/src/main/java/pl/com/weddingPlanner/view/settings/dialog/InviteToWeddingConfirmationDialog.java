package pl.com.weddingPlanner.view.settings.dialog;

import android.content.Intent;
import android.view.LayoutInflater;

import androidx.databinding.DataBindingUtil;

import pl.com.weddingPlanner.R;
import pl.com.weddingPlanner.databinding.DialogInviteToWeddingConfirmationBinding;
import pl.com.weddingPlanner.util.EmailUtil;
import pl.com.weddingPlanner.view.dialog.CustomAlertDialog;
import pl.com.weddingPlanner.view.settings.InviteToWeddingActivity;
import pl.com.weddingPlanner.view.settings.SettingsActivity;

public class InviteToWeddingConfirmationDialog extends CustomAlertDialog {

    private final DialogInviteToWeddingConfirmationBinding binding;

    public InviteToWeddingConfirmationDialog(InviteToWeddingActivity activity, String invitationCode, String emailAddress) {
        super(activity, R.layout.dialog_invite_to_wedding_confirmation);

        binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.dialog_invite_to_wedding_confirmation, null, false);

        setPositiveButton(R.string.dialog_send_email, (dialog, which) -> {
            composeEmail(emailAddress, invitationCode, activity);
        });
        setNegativeButton(R.string.dialog_back, (dialog, which) -> {
            activity.startActivity(new Intent(activity, SettingsActivity.class));
        });

        setInvitationCode(invitationCode);

        setView(binding.getRoot()).setCancelable(true);
    }

    private void setInvitationCode(String code) {
        binding.invitationCode.setText(code);
    }

    private void composeEmail(String emailAddress, String invitationCode, InviteToWeddingActivity activity) {
        String[] addresses = new String[]{emailAddress};
        String subject = activity.getString(R.string.invite_to_wedding_email_subject);
        String textBody = String.format(activity.getString(R.string.invite_to_wedding_email_text), invitationCode);
        EmailUtil.composeEmail(addresses, subject, textBody, activity);
    }

    @Override
    public void showDialog() {
        super.showTwoButtonDialog();
    }
}
