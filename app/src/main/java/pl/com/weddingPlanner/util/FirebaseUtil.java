package pl.com.weddingPlanner.util;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;

import pl.com.weddingPlanner.model.Wedding;

public class FirebaseUtil {

    public static Task<DataSnapshot> getUser(DatabaseReference databaseReference, FirebaseUser currentUser) {
        return getUserChild(databaseReference, currentUser).get();
    }

    public static DatabaseReference getUserChild(DatabaseReference databaseReference, FirebaseUser currentUser) {
        return databaseReference.child("users").child(currentUser.getUid());
    }

    public static DatabaseReference getWeddingChild(DatabaseReference databaseReference, Wedding newWedding) {
        return databaseReference.child("weddings").child(newWedding.getId());
    }

}
