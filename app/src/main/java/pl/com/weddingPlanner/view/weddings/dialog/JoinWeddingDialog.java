package pl.com.weddingPlanner.view.weddings.dialog;

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
import pl.com.weddingPlanner.model.WeddingItem;
import pl.com.weddingPlanner.util.FirebaseUtil;
import pl.com.weddingPlanner.view.BaseActivity;
import pl.com.weddingPlanner.view.NavigationActivity;
import pl.com.weddingPlanner.view.dialog.CustomAlertDialog;

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
                User user = task.getResult().getValue(User.class);
                getWeddingAndUpdate(weddingId, user);
            }
        });
    }

    private void getWeddingAndUpdate(String weddingId, User user) {
        FirebaseUtil.getWedding(databaseReference, weddingId).addOnCompleteListener(task -> {
            if (isSuccessfulAndNotNull(task)) {
                Wedding wedding = task.getResult().getValue(Wedding.class);
                User updatedUser = updateUser(user, wedding);

                FirebaseUtil.getUserChild(databaseReference, currentUser).setValue(updatedUser);
                updateWedding(wedding);
            }
        });
    }

    private User updateUser(User user, Wedding wedding) {
        List<WeddingItem> weddings = new ArrayList<>();
        if (user.getWeddings() != null) {
            weddings = user.getWeddings();
            weddings.add(buildWeddingItem(wedding));
        }

        user.setWeddings(weddings);
        user.setCurrentWedding(wedding.getId());

        return user;
    }

    private WeddingItem buildWeddingItem(Wedding newWedding) {
        return WeddingItem.builder()
                .id(newWedding.getId())
                .name(newWedding.getName())
                .build();
    }

    private void updateWedding(Wedding wedding) {
        List<String> updatedPeople = getAndUpdateWeddingPeople(wedding);
        FirebaseUtil.getWeddingChild(databaseReference, wedding.getId()).child("people").setValue(updatedPeople);
        FirebaseUtil.getInvitationChild(databaseReference, invitationCode).removeValue();
        goToJointWeddingDashboard();
    }

    private List<String> getAndUpdateWeddingPeople(Wedding wedding) {
        List<String> weddingPeople = wedding.getPeople();
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
