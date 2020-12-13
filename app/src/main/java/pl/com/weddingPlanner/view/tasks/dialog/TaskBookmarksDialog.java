package pl.com.weddingPlanner.view.tasks.dialog;

import android.util.SparseBooleanArray;
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
import pl.com.weddingPlanner.view.CustomAlertDialog;
import pl.com.weddingPlanner.view.tasks.NewTaskActivity;

public class TaskBookmarksDialog extends CustomAlertDialog {

    private DialogTaskBookmarksBinding binding;
    private Map<Integer, String> bookmarks;

    public TaskBookmarksDialog(NewTaskActivity activity) {
        super(activity, R.layout.dialog_task_bookmarks);

        binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.dialog_task_bookmarks, null, false);

        setAllBookmarks();
        initListView();

        setPositiveButton(R.string.dialog_pick, (dialog, which) -> activity.setFieldText(getSelectedBookmarks(), activity.findViewById(R.id.task_bookmarks_name)));
        setNegativeButton(R.string.dialog_back, (dialog, which) -> {});

        setView(binding.getRoot()).setCancelable(true);
    }

    private void setAllBookmarks() {
        Map<Integer, String> bookmarks = new LinkedHashMap<>();
        bookmarks.put(0,"Ważne");
        bookmarks.put(1, "Ciągłe");
        bookmarks.put(2, "Wkrótce");
        bookmarks.put(3, "Załatwione");
        bookmarks.put(4, "Zapłacone");

        this.bookmarks = bookmarks;
    }

    private void initListView() {
        List<String> bookmarks = new ArrayList<>();
        for (int i = 0; i < this.bookmarks.size(); i++) {
            bookmarks.add(this.bookmarks.get(i));
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), R.layout.list_row_multiple_choice, bookmarks);
        binding.taskBookmarks.setAdapter(adapter);
        binding.taskBookmarks.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        binding.taskBookmarks.setItemsCanFocus(false);
    }

    private List<String> getSelectedBookmarks() {
        SparseBooleanArray selectedItems = binding.taskBookmarks.getCheckedItemPositions();

        List<Integer> selectedKeys = new ArrayList<>();
        for (int i = 0; i < selectedItems.size(); i++) {
            selectedKeys.add(selectedItems.keyAt(i));
        }

        List<String> selectedBookmarks = new ArrayList<>();
        for (Map.Entry<Integer, String> entry : bookmarks.entrySet()) {
            int key = entry.getKey();
            if (selectedKeys.contains(key))
                selectedBookmarks.add(bookmarks.get(key));
        }

        return selectedBookmarks;
    }


    public void showDialog() {
        super.showTwoButtonDialog();
    }
}
