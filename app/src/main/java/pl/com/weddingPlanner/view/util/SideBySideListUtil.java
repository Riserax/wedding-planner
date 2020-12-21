package pl.com.weddingPlanner.view.util;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import java.util.List;
import java.util.Objects;

import pl.com.weddingPlanner.R;
import pl.com.weddingPlanner.exception.EnumValueNotFoundException;
import pl.com.weddingPlanner.util.DebouncedOnClickListener;
import pl.com.weddingPlanner.util.Logger;
import pl.com.weddingPlanner.view.category.CategoryActivity;
import pl.com.weddingPlanner.view.enums.CategoryEnum;
import pl.com.weddingPlanner.view.enums.CategoryResource;
import pl.com.weddingPlanner.view.enums.MoreEnum;
import pl.com.weddingPlanner.view.enums.MoreResource;

public class SideBySideListUtil {

    public static String CATEGORY_NAME_EXTRA = "categoryNameExtra";

    public static void renderCategoriesButtons(Context context, List categories, LinearLayout leftColumn, LinearLayout rightColumn) {
        LayoutInflater inflater = LayoutInflater.from(context);
        for (int i = 0; i < categories.size(); i++) {
            RelativeLayout relativeLayout = null;

            try {
                relativeLayout = prepareCategoryButton(context, Objects.requireNonNull(getResource(categories.get(i))), inflater);
            } catch (EnumValueNotFoundException e) {
                Logger.logToDevice(context.getClass().getName(), e);
                //TODO wyswietlic komunikat o nieprawidlowym typie
            }

            if (i % 2 == 0) {
                leftColumn.addView(relativeLayout);
            } else {
                rightColumn.addView(relativeLayout);
            }
        }
    }

    private static RelativeLayout prepareCategoryButton(Context context, Enum resource, LayoutInflater inflater) {
        RelativeLayout relativeLayout = new RelativeLayout(context);
        View buttonView = inflater.inflate(R.layout.category_item, relativeLayout);

        ImageView icon = buttonView.findViewById(R.id.row_icon);
        icon.setImageDrawable(getIcon(context, ResourceUtil.getResId(getIconCode(resource), R.drawable.class)));

        TextView textView = buttonView.findViewById(R.id.row_caption);
        textView.setText(context.getResources().getString(getResourceId(resource)));

        relativeLayout.setOnClickListener(new DebouncedOnClickListener(context.getResources().getInteger(R.integer.debounce_long_block_time_ms)) {
            @Override
            public void onDebouncedClick(View v) {
                Intent intent = new Intent(context, getTargetActivity(resource));
                intent.putExtra(CATEGORY_NAME_EXTRA, getResourceId(resource));
                context.startActivity(intent);
            }
        });

        return relativeLayout;
    }

    private static Enum getResource(Object object) throws EnumValueNotFoundException {
        if (object instanceof CategoryEnum) {
            return CategoryResource.of((CategoryEnum) object);
        } else if (object instanceof MoreEnum) {
            return MoreResource.of((MoreEnum) object);
        } else {
            return null;
        }
    }

    private static Drawable getIcon(Context context, int iconId) {
        return ContextCompat.getDrawable(context, iconId);
    }

    private static String getIconCode(Enum resource) {
        if (resource instanceof CategoryResource) {
            return ((CategoryResource) resource).getIconCode();
        } else if (resource instanceof MoreResource) {
            return ((MoreResource) resource).getIconCode();
        } else {
            return null;
        }
    }

    private static int getResourceId(Enum resource) {
        if (resource instanceof CategoryResource) {
            return ((CategoryResource) resource).getResourceId();
        } else if (resource instanceof MoreResource) {
            return ((MoreResource) resource).getResourceId();
        } else {
            return -1;
        }
    }

    private static Class getTargetActivity(Enum resource) {
        if (resource instanceof CategoryResource) {
            return CategoryActivity.class;
        } else if (resource instanceof MoreResource) {
            return ((MoreResource) resource).getTargetActivity();
        } else {
            return null;
        }
    }
}
