package pl.com.weddingPlanner.view.util;

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
import pl.com.weddingPlanner.exception.EnumValueNotFoundException;
import pl.com.weddingPlanner.persistence.entity.Category;
import pl.com.weddingPlanner.util.DebouncedOnClickListener;
import pl.com.weddingPlanner.util.Logger;
import pl.com.weddingPlanner.view.budget.BudgetCategoriesFragment;
import pl.com.weddingPlanner.view.budget.BudgetCategoryActivity;
import pl.com.weddingPlanner.view.enums.CategoryTypeEnum;
import pl.com.weddingPlanner.view.enums.MoreEnum;
import pl.com.weddingPlanner.view.enums.MoreResource;
import pl.com.weddingPlanner.view.tasks.TasksCategoriesFragment;
import pl.com.weddingPlanner.view.tasks.TasksCategoryActivity;

import static pl.com.weddingPlanner.view.util.ComponentsUtil.getIcon;

public class SideBySideListUtil {

    public static String CATEGORY_NAME_EXTRA = "categoryNameExtra";
    public static String FRAGMENT_SOURCE_EXTRA = "fragmentSourceExtra";

    public static void renderCategoriesButtons(Fragment fragment, List positions, LinearLayout leftColumn, LinearLayout rightColumn) {
        Context context = fragment.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        for (int i = 0; i < positions.size(); i++) {
            RelativeLayout relativeLayout = null;

            if (positions.get(i) instanceof MoreEnum) {
                try {
                    relativeLayout = prepareCategoryButton(fragment, Objects.requireNonNull(getResource(positions.get(i))), inflater);
                } catch (EnumValueNotFoundException e) {
                    Logger.logToDevice(context.getClass().getName(), e);
                    //TODO wyswietlic komunikat o nieprawidlowym typie
                }
            } else {
                relativeLayout = prepareCategoryButton(fragment, positions.get(i), inflater);
            }

            if (i % 2 == 0) {
                leftColumn.addView(relativeLayout);
            } else {
                rightColumn.addView(relativeLayout);
            }
        }

        if (positions.isEmpty()) {
            setEmptyMessage(leftColumn);
        }
    }

    private static RelativeLayout prepareCategoryButton(Fragment fragment, Enum resource, LayoutInflater inflater) {
        Context context = fragment.getContext();
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
                setExtras(fragment, intent, resource);
                context.startActivity(intent);
            }
        });

        return relativeLayout;
    }

    private static RelativeLayout prepareCategoryButton(Fragment fragment, Object entity, LayoutInflater inflater) {
        Context context = fragment.getContext();
        RelativeLayout relativeLayout = new RelativeLayout(context);
        View buttonView = inflater.inflate(R.layout.category_item, relativeLayout);

        ImageView icon = buttonView.findViewById(R.id.row_icon);
        icon.setImageDrawable(getIcon(context, ResourceUtil.getResId(getIconCode(entity), R.drawable.class)));

        TextView textView = buttonView.findViewById(R.id.row_caption);
        textView.setText(getCategoryName(entity));

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

    private static Enum getResource(Object object) throws EnumValueNotFoundException {
        if (object instanceof MoreEnum) {
            return MoreResource.of((MoreEnum) object);
        } else {
            return null;
        }
    }

    private static String getIconCode(Enum resource) {
        if (resource instanceof MoreResource) {
            return ((MoreResource) resource).getIconCode();
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

    private static int getResourceId(Enum resource) {
        if (resource instanceof MoreResource) {
            return ((MoreResource) resource).getResourceId();
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

    private static Class getTargetActivity(Enum resource) {
        if (resource instanceof MoreResource) {
            return ((MoreResource) resource).getTargetActivity();
        } else {
            return null;
        }
    }

    private static Class getTargetActivity(Object object) {
        if (object instanceof Category) {
            Category category = (Category) object;
            if (CategoryTypeEnum.TASKS == CategoryTypeEnum.valueOf(category.getType())) {
                return TasksCategoryActivity.class;
            } else if (CategoryTypeEnum.BUDGET == CategoryTypeEnum.valueOf(category.getType())) {
                return BudgetCategoryActivity.class;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    private static void setExtras(Fragment fragment, Intent intent, Enum resource) {
        if (fragment instanceof TasksCategoriesFragment || fragment instanceof BudgetCategoriesFragment) {
            intent.putExtra(CATEGORY_NAME_EXTRA, getResourceId(resource));
            intent.putExtra(FRAGMENT_SOURCE_EXTRA, fragment.getClass().toString());
        }
    }

    private static void setExtras(Fragment fragment, Intent intent, Object entity) {
        if (fragment instanceof TasksCategoriesFragment || fragment instanceof BudgetCategoriesFragment) {
            intent.putExtra(CATEGORY_NAME_EXTRA, getCategoryName(entity));
            intent.putExtra(FRAGMENT_SOURCE_EXTRA, fragment.getClass().toString());
        }
    }

    private static void setEmptyMessage(LinearLayout leftColumn) {
        ConstraintLayout sideBySideMenu = (ConstraintLayout) leftColumn.getParent();
        TextView emptyMessage = (TextView) sideBySideMenu.getChildAt(0);
        emptyMessage.setVisibility(View.VISIBLE);
    }
}
