package pl.com.weddingPlanner.view.util;

import android.util.SparseBooleanArray;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ListViewUtil {

    public static String getSelectedItemsAndBuildSeparatedString(ListView listView, Map<Integer, String> items) {
        SparseBooleanArray selectedPositions = listView.getCheckedItemPositions();
        List<Integer> selectedKeys = getSelectedKeys(selectedPositions);
        List<String> selectedItems = getSelectedItems(items, selectedKeys);

        return getSeparatedString(selectedItems);
    }

    public static List<Integer> getSelectedKeys(SparseBooleanArray selectedPositions) {
        List<Integer> selectedKeys = new ArrayList<>();

        for (int i = 0; i < selectedPositions.size(); i++) {
            int key = selectedPositions.keyAt(i);
            if (selectedPositions.get(key, false))
                selectedKeys.add(selectedPositions.keyAt(i));
        }

        return selectedKeys;
    }

    private static List<String> getSelectedItems(Map<Integer, String> items, List<Integer> selectedKeys) {
        List<String> selectedItems = new ArrayList<>();

        for (Map.Entry<Integer, String> entry : items.entrySet()) {
            int key = entry.getKey();
            if (selectedKeys.contains(key))
                selectedItems.add(items.get(key));
        }

        return selectedItems;
    }

    private static String getSeparatedString(List<String> selectedItems) {
        StringBuilder itemsBuilder = new StringBuilder();

        for (int i = 0; i < selectedItems.size(); i++) {
            if (i == selectedItems.size() - 1) {
                itemsBuilder.append(selectedItems.get(i));
            } else {
                itemsBuilder.append(selectedItems.get(i)).append(" | ");
            }
        }

        return itemsBuilder.toString();
    }
}
