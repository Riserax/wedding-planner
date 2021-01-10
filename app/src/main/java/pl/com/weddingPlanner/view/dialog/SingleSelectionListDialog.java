package pl.com.weddingPlanner.view.dialog;

import android.view.LayoutInflater;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.databinding.DataBindingUtil;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import pl.com.weddingPlanner.R;
import pl.com.weddingPlanner.databinding.DialogSingleSelectionListBinding;
import pl.com.weddingPlanner.persistence.entity.Category;
import pl.com.weddingPlanner.persistence.entity.Person;
import pl.com.weddingPlanner.view.BaseActivity;

public class SingleSelectionListDialog extends CustomAlertDialog {

    private DialogSingleSelectionListBinding binding;

    private Object listObject;
    private Map<Integer, String> positions;

    public SingleSelectionListDialog(BaseActivity activity, List positionsList, int headerCaptionId) {
        super(activity, R.layout.dialog_single_selection_list);

        binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.dialog_single_selection_list, null, false);

        setHeaderCaption(headerCaptionId);
        setAllPositions(positionsList);
        initListView();
        setListeners(activity);

        setNeutralButton(R.string.dialog_back, (dialog, which) -> {});

        setView(binding.getRoot()).setCancelable(true);
    }

    private void setHeaderCaption(int headerCaptionId) {
        binding.header.setText(headerCaptionId);
    }

    private void setAllPositions(List positionsList) {
        Map<Integer, String> positions = new LinkedHashMap<>();

        int id = 0;
        for (Object object : positionsList) {
            listObject = object;

            if (object instanceof Category) {
                Category category = (Category) object;
                positions.put(id++, category.getName());
            } else if (object instanceof Person) {
                Person category = (Person) object;
                positions.put(id++, category.getName());
            }
        }

        this.positions = positions;
    }

    private void initListView() {
        List<String> positions = new ArrayList<>();
        for (int i = 0; i < this.positions.size(); i++) {
            positions.add(this.positions.get(i));
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), R.layout.list_row, positions);
        binding.listView.setAdapter(adapter);
    }

    private void setListeners(BaseActivity activity) {
        binding.listView.setOnItemClickListener((adapterView, view, position, id) -> {
            activity.setFieldText(getSelectedPosition(position), getTextView(activity));
            super.hideDialog();
        });
    }

    private TextView getTextView(BaseActivity activity) {
        TextView textView;

        if (listObject instanceof Category) {
            textView = activity.findViewById(R.id.category_name);
        } else {
            textView = activity.findViewById(R.id.payer_name);
        }

        return textView;
    }

    private String getSelectedPosition(int position) {
        for (Map.Entry<Integer, String> entry : positions.entrySet()) {
            int key = entry.getKey();
            if (position == key)
                return positions.get(key);
        }
        return "";
    }

    @Override
    public void showDialog() {
        super.showOneButtonDialog();
    }
}
