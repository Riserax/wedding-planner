package pl.com.weddingPlanner.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class EmailUtil {

    public static void composeEmail(String[] addresses, String subject, String textBody, Context context) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"));
        intent.putExtra(Intent.EXTRA_EMAIL, addresses);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, textBody);
        context.startActivity(intent);
    }
}
