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
import pl.com.weddingPlanner.util.DebouncedOnClickListener;
import pl.com.weddingPlanner.view.category.CategoryActivity;
import pl.com.weddingPlanner.view.enums.CategoryEnum;
import pl.com.weddingPlanner.view.enums.CategoryResource;

public class SideBySideListUtil {

    public static String CATEGORY_NAME_EXTRA = "categoryNameExtra";

    public static void renderCategoriesButtons(Context context, List<CategoryEnum> categories, LinearLayout leftColumn, LinearLayout rightColumn) {
        LayoutInflater inflater = LayoutInflater.from(context);
        for (int i = 0; i < categories.size(); i++) {
            RelativeLayout relativeLayout = prepareCategoryButton(context, Objects.requireNonNull(CategoryResource.of(categories.get(i))), inflater);
            if (i % 2 == 0) {
                leftColumn.addView(relativeLayout);
            } else {
                rightColumn.addView(relativeLayout);
            }
        }
    }

    private static RelativeLayout prepareCategoryButton(Context context, CategoryResource categoryResource, LayoutInflater inflater) {
        RelativeLayout relativeLayout = new RelativeLayout(context);
        View buttonView = inflater.inflate(R.layout.category_item, relativeLayout);

        ImageView icon = buttonView.findViewById(R.id.row_icon);
        icon.setImageDrawable(getIcon(context, ResourceUtil.getResId(categoryResource.getIconCode(), R.drawable.class)));

        TextView textView = buttonView.findViewById(R.id.row_caption);
        textView.setText(context.getResources().getString(categoryResource.getResourceId()));

        relativeLayout.setOnClickListener(new DebouncedOnClickListener(context.getResources().getInteger(R.integer.debounce_long_block_time_ms)) {
            @Override
            public void onDebouncedClick(View v) {
                Intent intent = new Intent(context, CategoryActivity.class);
                intent.putExtra(CATEGORY_NAME_EXTRA, categoryResource.getResourceId());
                context.startActivity(intent);
            }
        });

        return relativeLayout;
    }

    private static Drawable getIcon(Context context, int iconId) {
        return ContextCompat.getDrawable(context, iconId);
    }
}
