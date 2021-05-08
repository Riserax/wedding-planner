package pl.com.weddingPlanner;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

public class FirebaseConfig extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance(getString(R.string.firebase_database_url)).setPersistenceEnabled(true);
    }
}
