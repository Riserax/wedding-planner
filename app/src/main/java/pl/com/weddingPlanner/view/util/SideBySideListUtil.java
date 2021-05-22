package pl.com.weddingPlanner.view.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import java.util.List;
import java.util.Objects;

import pl.com.weddingPlanner.R;
import pl.com.weddingPlanner.enums.CategoryType;
import pl.com.weddingPlanner.enums.MenuMore;
import pl.com.weddingPlanner.enums.MenuMoreResource;
import pl.com.weddingPlanner.enums.Settings;
import pl.com.weddingPlanner.enums.SettingsResource;
import pl.com.weddingPlanner.exception.EnumValueNotFoundException;
import pl.com.weddingPlanner.persistence.entity.Category;
import pl.com.weddingPlanner.util.DebouncedOnClickListener;
import pl.com.weddingPlanner.util.Logger;
import pl.com.weddingPlanner.view.budget.BudgetCategoriesFragment;
import pl.com.weddingPlanner.view.budget.BudgetCategoryActivity;
import pl.com.weddingPlanner.view.subcontractors.SubcontractorsCategoriesFragment;
import pl.com.weddingPlanner.view.subcontractors.SubcontractorsCategoryActivity;
import pl.com.weddingPlanner.view.tasks.TasksCategoriesFragment;
import pl.com.weddingPlanner.view.tasks.TasksCategoryActivity;

import static pl.com.weddingPlanner.view.util.ComponentsUtil.getIcon;

public class SideBySideListUtil {

    public static String CATEGORY_NAME_EXTRA = "categoryNameExtra";
    public static String FRAGMENT_SOURCE_EXTRA = "fragmentSourceExtra";

    public static void renderButtons(Fragment fragment, List<?> positions, LinearLayout leftColumn, LinearLayout rightColumn) {
        Context context = fragment.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        for (int i = 0; i < positions.size(); i++) {
            RelativeLayout relativeLayout = null;

            if (positions.get(i) instanceof MenuMore) {
                try {
                    relativeLayout = prepareOptionButton(fragment, Objects.requireNonNull(getResource(positions.get(i))), inflater);
                } catch (EnumValueNotFoundException e) {
                    Logger.logToDevice(context.getClass().getName(), e);
                    //TODO wyswietlic komunikat o nieprawidlowym typie
                }
            } else {
                relativeLayout = prepareCategoryButton(fragment, positions.get(i), inflater);
            }

            displayButtonsBySides(i, leftColumn, rightColumn, relativeLayout);
        }

        setEmptyMessage(positions, leftColumn);
    }

    public static void renderButtons(Activity activity, List<?> positions, LinearLayout leftColumn, LinearLayout rightColumn) {
        LayoutInflater inflater = LayoutInflater.from(activity);

        for (int i = 0; i < positions.size(); i++) {
            RelativeLayout relativeLayout = null;

            try {
                relativeLayout = prepareOptionButton(activity, Objects.requireNonNull(getResource(positions.get(i))), inflater);
            } catch (EnumValueNotFoundException e) {
                Logger.logToDevice(activity.getClass().getName(), e);
                //TODO wyswietlic komunikat o nieprawidlowym typie
            }

            displayButtonsBySides(i, leftColumn, rightColumn, relativeLayout);
        }

        setEmptyMessage(positions, leftColumn);
    }

    private static RelativeLayout prepareOptionButton(Fragment fragment, Enum<?> resource, LayoutInflater inflater) {
        Context context = fragment.getContext();
        RelativeLayout relativeLayout = setRelativeLayout(context, resource, fragment);
        View buttonView = inflater.inflate(R.layout.category_item, relativeLayout);
        String text = context.getResources().getString(getResourceId(resource));

        setIcon(buttonView, context, getIconCode(resource));
        setTextView(buttonView, text);

        return relativeLayout;
    }

    private static RelativeLayout prepareOptionButton(Activity activity, Enum<?> resource, LayoutInflater inflater) {
        RelativeLayout relativeLayout = setRelativeLayout(activity, resource);
        View buttonView = inflater.inflate(R.layout.category_item, relativeLayout);
        String text = activity.getResources().getString(getResourceId(resource));

        setIcon(buttonView, activity, getIconCode(resource));
        setTextView(buttonView, text);

        return relativeLayout;
    }

    private static RelativeLayout prepareCategoryButton(Fragment fragment, Object entity, LayoutInflater inflater) {
        Context context = fragment.getContext();
        RelativeLayout relativeLayout = setRelativeLayout(context, entity, fragment);
        View buttonView = inflater.inflate(R.layout.category_item, relativeLayout);

        setIcon(buttonView, context, getIconCode(entity));
        setTextView(buttonView, getCategoryName(entity));

        return relativeLayout;
    }

    private static RelativeLayout setRelativeLayout(Context context, Enum<?> resource, Fragment fragment) {
        RelativeLayout relativeLayout = new RelativeLayout(context);
        relativeLayout.setOnClickListener(new DebouncedOnClickListener(context.getResources().getInteger(R.integer.debounce_long_block_time_ms)) {
            @Override
            public void onDebouncedClick(View v) {
                Intent intent = new Intent(context, getTargetActivity(resource));
                setExtras(fragment, intent, resource);
                context.startActivity(intent);
            }
        });
        return relativeLayout;
    }

    private static RelativeLayout setRelativeLayout(Activity activity, Enum<?> resource) {
        RelativeLayout relativeLayout = new RelativeLayout(activity);
        relativeLayout.setOnClickListener(new DebouncedOnClickListener(activity.getResources().getInteger(R.integer.debounce_long_block_time_ms)) {
            @Override
            public void onDebouncedClick(View v) {
                Intent intent = new Intent(activity, getTargetActivity(resource));
                activity.startActivity(intent);
            }
        });
        return relativeLayout;
    }

    private static RelativeLayout setRelativeLayout(Context context, Object entity, Fragment fragment) {
        RelativeLayout relativeLayout = new RelativeLayout(context);
        relativeLayout.setOnClickListener(new DebouncedOnClickListener(context.getResources().getInteger(R.integer.debounce_long_block_time_ms)) {
            @Override
            public void onDebouncedClick(View v) {
                Intent intent = new Intent(context, getTargetActivity(entity));
                setExtras(fragment, intent, entity);
                context.startActivity(intent);
            }
        });
        return relativeLayout;
    }

    private static void setIcon(View buttonView, Context context, String resName) {
        ImageView icon = buttonView.findViewById(R.id.row_icon);
        icon.setImageDrawable(getIcon(context, ResourceUtil.getResId(resName, R.drawable.class)));
    }

    private static void setTextView(View buttonView, String text) {
        TextView textView = buttonView.findViewById(R.id.main_caption);
        textView.setText(text);
    }

    private static Enum<?> getResource(Object object) throws EnumValueNotFoundException {
        if (object instanceof MenuMore) {
            return MenuMoreResource.of((MenuMore) object);
        } else if (object instanceof Settings) {
            return SettingsResource.of((Settings) object);
        } else {
            return null;
        }
    }

    private static String getIconCode(Enum<?> resource) {
        if (resource instanceof MenuMoreResource) {
            return ((MenuMoreResource) resource).getIconCode();
        } else if (resource instanceof SettingsResource) {
            return ((SettingsResource) resource).getIconCode();
        } else {
            return null;
        }
    }

    private static String getIconCode(Object object) {
        if (object instanceof Category) {
            return ((Category) object).getIconId();
        } else {
            return null;
        }
    }

    private static int getResourceId(Enum<?> resource) {
        if (resource instanceof MenuMoreResource) {
            return ((MenuMoreResource) resource).getResourceId();
        } else if (resource instanceof SettingsResource) {
            return ((SettingsResource) resource).getResourceId();
        } else {
            return -1;
        }
    }

    private static String getCategoryName(Object object) {
        if (object instanceof Category) {
            return ((Category) object).getName();
        } else {
            return null;
        }
    }

    private static Class<?> getTargetActivity(Enum<?> resource) {
        if (resource instanceof MenuMoreResource) {
            return ((MenuMoreResource) resource).getTargetActivity();
        } else if (resource instanceof SettingsResource) {
            return ((SettingsResource) resource).getTargetActivity();
        } else {
            return null;
        }
    }

    private static Class<?> getTargetActivity(Object object) {
        if (object instanceof Category) {
            Category category = (Category) object;
            if (CategoryType.TASKS == CategoryType.valueOf(category.getType())) {
                return TasksCategoryActivity.class;
            } else if (CategoryType.BUDGET == CategoryType.valueOf(category.getType())) {
                return BudgetCategoryActivity.class;
            } else if (CategoryType.SUBCONTRACTORS == CategoryType.valueOf(category.getType())) {
                return SubcontractorsCategoryActivity.class;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    private static void setExtras(Fragment fragment, Intent intent, Enum<?> resource) {
        if (fragment instanceof TasksCategoriesFragment || fragment instanceof BudgetCategoriesFragment) {
            intent.putExtra(CATEGORY_NAME_EXTRA, getResourceId(resource));
            intent.putExtra(FRAGMENT_SOURCE_EXTRA, fragment.getClass().toString());
        }
    }

    private static void setExtras(Fragment fragment, Intent intent, Object entity) {
        if (fragment instanceof TasksCategoriesFragment
                || fragment instanceof BudgetCategoriesFragment
                || fragment instanceof SubcontractorsCategoriesFragment) {
            intent.putExtra(CATEGORY_NAME_EXTRA, getCategoryName(entity));
            intent.putExtra(FRAGMENT_SOURCE_EXTRA, fragment.getClass().toString());
        }
    }

    private static void displayButtonsBySides(int i, LinearLayout leftColumn, LinearLayout rightColumn, RelativeLayout relativeLayout) {
        if (i % 2 == 0) {
            leftColumn.addView(relativeLayout);
        } else {
            rightColumn.addView(relativeLayout);
        }
    }

    private static void setEmptyMessage(List<?> positions, LinearLayout leftColumn) {
        if (positions.isEmpty()) {
            ConstraintLayout sideBySideMenu = (ConstraintLayout) leftColumn.getParent();
            TextView emptyMessage = (TextView) sideBySideMenu.getChildAt(0);
            emptyMessage.setVisibility(View.VISIBLE);
        }
    }
}
