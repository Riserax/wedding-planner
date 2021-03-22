package pl.com.weddingPlanner.view.tasks.dialog;

import android.view.LayoutInflater;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.databinding.DataBindingUtil;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import pl.com.weddingPlanner.R;
import pl.com.weddingPlanner.databinding.DialogTaskBookmarksBinding;
import pl.com.weddingPlanner.persistence.entity.Bookmark;
import pl.com.weddingPlanner.util.DAOUtil;
import pl.com.weddingPlanner.view.dialog.CustomAlertDialog;
import pl.com.weddingPlanner.view.tasks.NewTaskActivity;
import pl.com.weddingPlanner.view.util.ListViewUtil;

public class TaskBookmarksDialog extends CustomAlertDialog {

    private final DialogTaskBookmarksBinding binding;
    private Map<Integer, String> bookmarks;

    public TaskBookmarksDialog(NewTaskActivity activity, List<Integer> selectedBookmarksKeys) {
        super(activity, R.layout.dialog_task_bookmarks);

        binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.dialog_task_bookmarks, null, false);

        setPositiveButton(R.string.dialog_pick, (dialog, which) -> {
            activity.setSelectedBookmarksKeys(ListViewUtil.getSelectedKeys(binding.taskBookmarks.getCheckedItemPositions()));
            activity.setFieldText(
                    ListViewUtil.getSelectedItemsAndBuildSeparatedString(binding.taskBookmarks, bookmarks),
                    activity.findViewById(R.id.bookmarks_name)
            );
        });
        setNegativeButton(R.string.dialog_back, (dialog, which) -> {});

        setView(binding.getRoot()).setCancelable(true);

        setAllBookmarks();
        initListView(selectedBookmarksKeys);
    }

    private void setAllBookmarks() {
        Map<Integer, String> bookmarks = new LinkedHashMap<>();

        int id = 0;
        for (Bookmark bookmark : DAOUtil.getAllBookmarks(getContext())) {
            bookmarks.put(id++, bookmark.getName());
        }

        this.bookmarks = bookmarks;
    }

    private void initListView(List<Integer> selectedBookmarksKeys) {
        List<String> bookmarks = new ArrayList<>();
        for (int i = 0; i < this.bookmarks.size(); i++) {
            bookmarks.add(this.bookmarks.get(i));
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), R.layout.list_row_multiple_choice, bookmarks);
        binding.taskBookmarks.setAdapter(adapter);
        binding.taskBookmarks.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        binding.taskBookmarks.setItemsCanFocus(false);

        setSelectedPositions(selectedBookmarksKeys);
    }

    private void setSelectedPositions(List<Integer> selectedBookmarksKeys) {
        if (!selectedBookmarksKeys.isEmpty()) {
            for (Integer selectedPosition : selectedBookmarksKeys)
                binding.taskBookmarks.setItemChecked(selectedPosition, true);
        }
    }

    @Override
    public void showDialog() {
        super.showTwoButtonDialog();
    }
}
