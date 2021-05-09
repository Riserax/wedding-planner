package pl.com.weddingPlanner.view.dialog;

import android.content.Intent;
import android.view.LayoutInflater;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

import pl.com.weddingPlanner.R;
import pl.com.weddingPlanner.databinding.DialogJoinWeddingBinding;
import pl.com.weddingPlanner.model.User;
import pl.com.weddingPlanner.model.Wedding;
import pl.com.weddingPlanner.model.WeddingInvitation;
import pl.com.weddingPlanner.util.FirebaseUtil;
import pl.com.weddingPlanner.view.BaseActivity;
import pl.com.weddingPlanner.view.NavigationActivity;

import static pl.com.weddingPlanner.util.FirebaseUtil.isSuccessfulAndNotNull;
import static pl.com.weddingPlanner.view.NavigationActivity.FRAGMENT_TO_LOAD_ID;

public class JoinWeddingDialog extends CustomAlertDialog {

    private final DialogJoinWeddingBinding binding;

    private final BaseActivity activity;

    private FirebaseUser currentUser;
    private DatabaseReference databaseReference;

    private String invitationCode;

    public JoinWeddingDialog(BaseActivity activity) {
        super(activity, R.layout.dialog_join_wedding);

        binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.dialog_join_wedding, null, false);
        this.activity = activity;

        setPositiveButton(R.string.dialog_confirm, (dialog, which) -> {
            processJoinRequest();
        });
        setNegativeButton(R.string.dialog_cancel, (dialog, which) -> {});

        initFirebase();

        setView(binding.getRoot()).setCancelable(true);
    }

    private void initFirebase() {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance(activity.getString(R.string.firebase_database_url));
        databaseReference = firebaseDatabase.getReference();
    }

    private void processJoinRequest() {
        invitationCode = binding.code.getText().toString();
        if (StringUtils.isNotBlank(invitationCode)) {
            FirebaseUtil.getInvitation(databaseReference, invitationCode).addOnCompleteListener(task -> {
                if (isSuccessfulAndNotNull(task)) {
                    proceedWhenInvitationFound(task.getResult());
                } else {
                    Toast.makeText(activity, "Nie znaleziono zaproszenia o podanym kodzie", Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    private void proceedWhenInvitationFound(DataSnapshot taskResult) {
        WeddingInvitation invitation = taskResult.getValue(WeddingInvitation.class);
        if (isNotValid(invitation)) {
            Toast.makeText(activity, "Nieprawidłowe zaproszenie", Toast.LENGTH_LONG).show();
        } else {
            proceedWhenValid(invitation.getWedding());
        }
    }

    private boolean isNotValid(WeddingInvitation invitation) {
        return !currentUser.getEmail().equals(invitation.getEmail());
    }

    private void proceedWhenValid(String weddingId) {
        FirebaseUtil.getUser(databaseReference, currentUser).addOnCompleteListener(task -> {
            if (isSuccessfulAndNotNull(task)) {
                User user = getAndUpdateUser(task.getResult(), weddingId);
                FirebaseUtil.getUserChild(databaseReference, currentUser).setValue(user);
                updateWedding(weddingId);
            }
        });
    }

    private User getAndUpdateUser(DataSnapshot taskResult, String weddingId) {
        User user = taskResult.getValue(User.class);

        List<String> weddings = new ArrayList<>();
        if (user.getWeddings() != null) {
            weddings = user.getWeddings();
            weddings.add(weddingId);
        }
        user.setWeddings(weddings);
        user.setCurrentWedding(weddingId);

        return user;
    }

    private void updateWedding(String weddingId) {
        FirebaseUtil.getWedding(databaseReference, weddingId).addOnCompleteListener(task -> {
            if (isSuccessfulAndNotNull(task)) {
                List<String> updatedPeople = getAndUpdateWeddingPeople(task.getResult());
                FirebaseUtil.getWeddingChild(databaseReference, weddingId).child("people").setValue(updatedPeople);
                FirebaseUtil.getInvitationChild(databaseReference, invitationCode).removeValue();
                goToJointWeddingDashboard();
            }
        });
    }

    private List<String> getAndUpdateWeddingPeople(DataSnapshot taskResult) {
        List<String> weddingPeople = taskResult.getValue(Wedding.class).getPeople();
        List<String> toReturn = new ArrayList<>();

        if (weddingPeople != null) {
            toReturn = weddingPeople;
            toReturn.add(currentUser.getUid());
        }

        return toReturn;
    }

    private void goToJointWeddingDashboard() {
        Intent intent = new Intent(activity, NavigationActivity.class);
        intent.putExtra(FRAGMENT_TO_LOAD_ID, R.id.navigation_dashboard);
        activity.startActivity(intent);
        super.hideDialog();
    }

    @Override
    public void showDialog() {
        super.showTwoButtonDialog();
    }
}
