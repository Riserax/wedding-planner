package pl.com.weddingPlanner.view.component;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.core.content.ContextCompat;

import java.util.List;

import lombok.Getter;
import pl.com.weddingPlanner.R;
import pl.com.weddingPlanner.enums.LocationEnum;
import pl.com.weddingPlanner.persistence.entity.Bookmark;


public class Bookmarks {

    private final int BOOKMARKS_DETAILS_PER_ROW = 4;

    private final Context context;
    private final List<Bookmark> bookmarkList;
    @Getter
    private final LinearLayout bookmarksContainer;
    private final LocationEnum location;

    public Bookmarks(Context context, List<Bookmark> bookmarkList, LocationEnum location) {
        this.context = context;
        this.bookmarkList = bookmarkList;
        this.bookmarksContainer = new LinearLayout(context);
        this.location = location;
        buildBookmarks();
    }

    private void buildBookmarks() {
        bookmarksContainer.setOrientation(LinearLayout.VERTICAL);

        for (int i = 0; i < getRowsNumber(); i++) {
            createAndAddOneRowBookmarksLayout(i);
        }
    }

    private int getRowsNumber() {
        int rowsNumber = 1;

        if (LocationEnum.DETAILS == location) {
            rowsNumber = (int) Math.ceil((double) bookmarkList.size() / BOOKMARKS_DETAILS_PER_ROW);
        }

        return rowsNumber;
    }

    private void createAndAddOneRowBookmarksLayout(int row) {
        LinearLayout bookmarksSingleRowLayout = null;

        if (LocationEnum.LIST_ITEM == location) {
            bookmarksSingleRowLayout = setListBookmarksRowLayout();

            for (Bookmark bookmark : bookmarkList) {
                prepareAndAddListBookmark(bookmark, bookmarksSingleRowLayout);
            }
        } else if (LocationEnum.DETAILS == location) {
            bookmarksSingleRowLayout = setDetailsBookmarksRowLayout();

            for (int i = 0; i < bookmarkList.size(); i++) {
                if (isOneRow(row, i)) {
                    prepareAndAddDetailsBookmark(bookmarkList.get(i), bookmarksSingleRowLayout);
                } else if (isMoreThanOneRow(row, i)) {
                    prepareAndAddDetailsBookmark(bookmarkList.get(i), bookmarksSingleRowLayout);
                }
            }
        }

        if (bookmarksSingleRowLayout != null) {
            bookmarksContainer.addView(bookmarksSingleRowLayout);
        }
    }

    private boolean isOneRow(int row, int i) {
        return row == 0 && i < BOOKMARKS_DETAILS_PER_ROW;
    }

    private boolean isMoreThanOneRow(int row, int i) {
        return row > 0 && (i >= (row * BOOKMARKS_DETAILS_PER_ROW) && i < (row * BOOKMARKS_DETAILS_PER_ROW + BOOKMARKS_DETAILS_PER_ROW));
    }

    private LinearLayout setListBookmarksRowLayout() {
        LinearLayout iconsLayout = new LinearLayout(context);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        iconsLayout.setLayoutParams(layoutParams);
        iconsLayout.setOrientation(LinearLayout.HORIZONTAL);

        return iconsLayout;
    }

    private LinearLayout setDetailsBookmarksRowLayout() {
        LinearLayout iconsLayout = new LinearLayout(context);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        int marginBottom = Math.round(context.getResources().getDimension(R.dimen.field_margin));
        layoutParams.setMargins(0, 0, 0, marginBottom);

        iconsLayout.setLayoutParams(layoutParams);
        iconsLayout.setHorizontalGravity(Gravity.CENTER);
        iconsLayout.setOrientation(LinearLayout.HORIZONTAL);

        return iconsLayout;
    }

    private void prepareAndAddListBookmark(Bookmark bookmark, LinearLayout bookmarksRowLayout) {
        ImageButton imageButton = createListImageButton(bookmark);
        bookmarksRowLayout.addView(imageButton);
    }

    private void prepareAndAddDetailsBookmark(Bookmark bookmark, LinearLayout bookmarksRowLayout) {
        ImageButton imageButton = createDetailsImageButton(bookmark);
        bookmarksRowLayout.addView(imageButton);
    }

    private ImageButton createListImageButton(Bookmark bookmark) {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(context.getResources().getDimensionPixelSize(R.dimen.image_button_size_20), context.getResources().getDimensionPixelSize(R.dimen.image_button_size_20));

        int marginEnd = Math.round(context.getResources().getDimension(R.dimen.image_button_margin_end_1));
        layoutParams.setMargins(-12, -10, marginEnd, 0);

        ImageButton imageButton = new ImageButton(context);
        imageButton.setLayoutParams(layoutParams);
        imageButton.setBackgroundColor(ContextCompat.getColor(context, R.color.transparent));
        imageButton.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_bookmark_small));
        imageButton.setColorFilter(Color.parseColor(bookmark.getColorId()));

        return imageButton;
    }

    private ImageButton createDetailsImageButton(Bookmark bookmark) {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(context.getResources().getDimensionPixelSize(R.dimen.image_button_size_24), context.getResources().getDimensionPixelSize(R.dimen.image_button_size_24));

        int marginStart = Math.round(context.getResources().getDimension(R.dimen.image_button_margin_start));
        int marginEnd = Math.round(context.getResources().getDimension(R.dimen.image_button_margin_end_5));
        layoutParams.setMargins(marginStart, 0, marginEnd, 0);

        ImageButton imageButton = new ImageButton(context);
        imageButton.setLayoutParams(layoutParams);
        imageButton.setClickable(true);
        imageButton.setFocusable(true);
        imageButton.setBackgroundColor(ContextCompat.getColor(context, R.color.transparent));
        imageButton.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_bookmark));
        imageButton.setColorFilter(Color.parseColor(bookmark.getColorId()));

        return imageButton;
    }
}
