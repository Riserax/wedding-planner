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
import pl.com.weddingPlanner.databinding.DialogTaskPeopleBinding;
import pl.com.weddingPlanner.view.CustomAlertDialog;
import pl.com.weddingPlanner.view.tasks.NewTaskActivity;

public class TaskPeopleDialog extends CustomAlertDialog {

    private DialogTaskPeopleBinding binding;
    private Map<Integer, String> people;

    public TaskPeopleDialog(NewTaskActivity activity) {
        super(activity, R.layout.dialog_task_people);

        binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.dialog_task_people, null, false);

        setAllPeople();
        initListView();

        setPositiveButton(R.string.dialog_pick, (dialog, which) -> activity.setFieldText(getSelectedPeople(), activity.findViewById(R.id.task_people_name)));
        setNegativeButton(R.string.dialog_back, (dialog, which) -> {});

        setView(binding.getRoot()).setCancelable(true);
    }

    private void setAllPeople() {
        Map<Integer, String> people = new LinkedHashMap<>();
        people.put(0,"Pani młoda");
        people.put(1, "Pan młody");

        this.people = people;
    }

    private void initListView() {
        List<String> bookmarks = new ArrayList<>();
        for (int i = 0; i < this.people.size(); i++) {
            bookmarks.add(this.people.get(i));
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), R.layout.list_row_multiple_choice, bookmarks);
        binding.taskPeople.setAdapter(adapter);
        binding.taskPeople.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        binding.taskPeople.setItemsCanFocus(false);
    }

    private List<String> getSelectedPeople() {
        SparseBooleanArray selectedItems = binding.taskPeople.getCheckedItemPositions();

        List<Integer> selectedKeys = new ArrayList<>();
        for (int i = 0; i < selectedItems.size(); i++) {
            selectedKeys.add(selectedItems.keyAt(i));
        }

        List<String> selectedPeople = new ArrayList<>();
        for (Map.Entry<Integer, String> entry : people.entrySet()) {
            int key = entry.getKey();
            if (selectedKeys.contains(key))
                selectedPeople.add(people.get(key));
        }

        return selectedPeople;
    }


    public void showDialog() {
        super.showTwoButtonDialog();
    }
}
