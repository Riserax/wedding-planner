package pl.com.weddingPlanner.view.dialog;

import android.view.LayoutInflater;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.databinding.DataBindingUtil;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import pl.com.weddingPlanner.R;
import pl.com.weddingPlanner.databinding.DialogPeopleBinding;
import pl.com.weddingPlanner.persistence.entity.Person;
import pl.com.weddingPlanner.util.DAOUtil;
import pl.com.weddingPlanner.view.BaseActivity;
import pl.com.weddingPlanner.view.util.ListViewUtil;

public class PeopleDialog extends CustomAlertDialog {

    private DialogPeopleBinding binding;
    private Map<Integer, String> people;

    public PeopleDialog(BaseActivity activity, List<Integer> selectedPeopleKeys) {
        super(activity, R.layout.dialog_people);

        binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.dialog_people, null, false);

        setPositiveButton(R.string.dialog_pick, (dialog, which) -> {
            activity.setSelectedPeopleKeys(ListViewUtil.getSelectedKeys(binding.peopleList.getCheckedItemPositions()));
            activity.setFieldText(
                    ListViewUtil.getSelectedItemsAndBuildSeparatedString(binding.peopleList, people),
                    activity.findViewById(R.id.people_name)
            );
        });
        setNegativeButton(R.string.dialog_back, (dialog, which) -> {});

        setView(binding.getRoot()).setCancelable(true);

        setAllPeople();
        initListView(selectedPeopleKeys);
    }

    private void setAllPeople() {
        Map<Integer, String> people = new LinkedHashMap<>();

        int id = 0;
        for (Person person : DAOUtil.getAllPersons(getContext())) {
            people.put(id++, person.getName());
        }

        this.people = people;
    }

    private void initListView(List<Integer> selectedPeopleKeys) {
        List<String> people = new ArrayList<>();
        for (int i = 0; i < this.people.size(); i++) {
            people.add(this.people.get(i));
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), R.layout.list_row_multiple_choice, people);
        binding.peopleList.setAdapter(adapter);
        binding.peopleList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        binding.peopleList.setItemsCanFocus(false);

        setSelectedPositions(selectedPeopleKeys);
    }

    private void setSelectedPositions(List<Integer> selectedPeopleKeys) {
        if (!selectedPeopleKeys.isEmpty()) {
            for (Integer selectedPosition : selectedPeopleKeys)
                binding.peopleList.setItemChecked(selectedPosition, true);
        }
    }

    @Override
    public void showDialog() {
        super.showTwoButtonDialog();
    }
}
