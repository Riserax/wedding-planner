package pl.com.weddingPlanner.view.util;

import android.util.SparseBooleanArray;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

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

    public static String getSeparatedString(List<String> selectedItems) {
        StringJoiner itemsJoiner = new StringJoiner(" | ");

        for (String selectedItem : selectedItems) {
            itemsJoiner.add(selectedItem);
        }

        return itemsJoiner.toString();
    }
}
