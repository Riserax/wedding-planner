package pl.com.weddingPlanner.model;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.core.content.ContextCompat;

import java.util.List;

import lombok.Getter;
import pl.com.weddingPlanner.R;
import pl.com.weddingPlanner.persistence.entity.Bookmark;

@Getter
public class Bookmarks {

    private final int BOOKMARK_PER_ROW = 4;

    private final Context context;
    private final List<Bookmark> bookmarkList;
    private final LinearLayout bookmarksContainer;

    public Bookmarks(Activity activity, List<Bookmark> bookmarkList) {
        this.context = activity.getApplicationContext();
        this.bookmarkList = bookmarkList;
        this.bookmarksContainer = new LinearLayout(context);
        buildBookmarks();
    }

    private void buildBookmarks() {
        bookmarksContainer.setOrientation(LinearLayout.VERTICAL);

        int rowsNumber = (int) Math.ceil((double) bookmarkList.size() / BOOKMARK_PER_ROW);

        for (int i = 0; i < rowsNumber; i++) {
            createAndAddOneRowBookmarksLayout(i);
        }
    }

    private void createAndAddOneRowBookmarksLayout(int row) {
        LinearLayout bookmarksSingleRowLayout = setBookmarksRowLayout();

        for (int i = 0; i < bookmarkList.size(); i++) {
            if (isOneRow(row, i)) {
                prepareAndAddBookmark(bookmarkList.get(i), bookmarksSingleRowLayout);
            } else if (isMoreThanOneRow(row, i)) {
                prepareAndAddBookmark(bookmarkList.get(i), bookmarksSingleRowLayout);
            }
        }

        bookmarksContainer.addView(bookmarksSingleRowLayout);
    }

    private boolean isOneRow(int row, int i) {
        return row == 0 && i < BOOKMARK_PER_ROW;
    }

    private boolean isMoreThanOneRow(int row, int i) {
        return row > 0 && (i >= (row * BOOKMARK_PER_ROW) && i < (row * BOOKMARK_PER_ROW + BOOKMARK_PER_ROW));
    }

    private LinearLayout setBookmarksRowLayout() {
        LinearLayout iconsLayout = new LinearLayout(context);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        int marginBottom = Math.round(context.getResources().getDimension(R.dimen.field_margin_bottom));
        layoutParams.setMargins(0, 0, 0, marginBottom);

        iconsLayout.setLayoutParams(layoutParams);
        iconsLayout.setHorizontalGravity(Gravity.CENTER);
        iconsLayout.setOrientation(LinearLayout.HORIZONTAL);

        return iconsLayout;
    }

    private void prepareAndAddBookmark(Bookmark bookmark, LinearLayout bookmarksRowLayout) {
        ImageButton imageButton = createImageButton(bookmark);
        bookmarksRowLayout.addView(imageButton);
    }

    private ImageButton createImageButton(Bookmark bookmark) {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(context.getResources().getDimensionPixelSize(R.dimen.image_button_size_24), context.getResources().getDimensionPixelSize(R.dimen.image_button_size_24));

        int marginStart = Math.round(context.getResources().getDimension(R.dimen.image_button_margin_start));
        int marginEnd = Math.round(context.getResources().getDimension(R.dimen.image_button_margin_end));
        layoutParams.setMargins(marginStart, 0, marginEnd, 0);

        ImageButton imageButton = new ImageButton(context);
        imageButton.setLayoutParams(layoutParams);
        imageButton.setClickable(true);
        imageButton.setFocusable(true);
        imageButton.setBackgroundColor(ContextCompat.getColor(context, R.color.gray_F2F2F2));
        imageButton.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_bookmark));
        imageButton.setColorFilter(Color.parseColor(bookmark.getColorId()));

        return imageButton;
    }
}
