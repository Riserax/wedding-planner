package pl.com.weddingPlanner.util;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;

public class FirebaseUtil {

    public static Task<DataSnapshot> getUser(DatabaseReference databaseReference, FirebaseUser currentUser) {
        return getUserChild(databaseReference, currentUser).get();
    }

    public static DatabaseReference getUserChild(DatabaseReference databaseReference, FirebaseUser currentUser) {
        return databaseReference.child("users").child(currentUser.getUid());
    }

    public static Task<DataSnapshot> getWedding(DatabaseReference databaseReference, String weddingId) {
        return getWeddingChild(databaseReference, weddingId).get();
    }

    public static DatabaseReference getWeddingChild(DatabaseReference databaseReference, String weddingId) {
        return databaseReference.child("weddings").child(weddingId);
    }

    public static Task<DataSnapshot> getInvitation(DatabaseReference databaseReference, String code) {
        return getInvitationChild(databaseReference, code).get();
    }

    public static DatabaseReference getInvitationChild(DatabaseReference databaseReference, String code) {
        return getInvitationRoot(databaseReference).child(code);
    }

    public static DatabaseReference getInvitationRoot(DatabaseReference databaseReference) {
        return databaseReference.child("invitations");
    }

    public static boolean isSuccessfulAndNotNull(Task<DataSnapshot> task) {
        return task.isSuccessful() && task.getResult() != null;
    }
}
