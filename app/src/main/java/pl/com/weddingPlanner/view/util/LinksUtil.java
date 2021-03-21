package pl.com.weddingPlanner.view.util;

import android.content.Context;
import android.graphics.Paint;
import android.widget.EditText;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import org.apache.commons.lang3.StringUtils;

import pl.com.weddingPlanner.R;

public class LinksUtil {

    public static String getWebsiteLink(EditText websiteEditText) {
        String website = websiteEditText.getText().toString();
        StringBuilder websiteBuilder = new StringBuilder();

        if (StringUtils.isNotBlank(website)) {
            websiteBuilder.append("<a href=\"");

            if (!website.startsWith("http://") && !website.startsWith("https://")) {
                websiteBuilder.append("http://");
            }

            websiteBuilder.append(website).append("\">").append(website).append("</a>");
        }

        return websiteBuilder.toString();
    }

    public static void makeLinkAlike(TextView textView, Context context, String text) {
        textView.setText(text);
        textView.setTextColor(ContextCompat.getColor(context, R.color.colorPrimaryDark));
        textView.setPaintFlags(textView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
    }
}
