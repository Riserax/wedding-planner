package pl.com.weddingPlanner.view.util;

import org.threeten.bp.LocalDate;
import org.threeten.bp.format.DateTimeFormatter;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import pl.com.weddingPlanner.persistence.entity.Task;

public class TasksUtil {

    public static Map<Integer, LocalDate> getSortedIdDateMap(List<Task> allTasks) {
        Map<Integer, String> idDateStringMap = getIdDateStringMap(allTasks);

        Map<Integer, LocalDate> unsortedIdDateMap = getUnsortedIdDateMap(idDateStringMap);
        Map<Integer, LocalDate> sortedIdDateMap = new LinkedHashMap<>();

        unsortedIdDateMap.entrySet().stream()
                .sorted(Map.Entry.comparingByValue())
                .forEachOrdered(x -> sortedIdDateMap.put(x.getKey(), x.getValue()));

        return sortedIdDateMap;
    }

    public static Map<Integer, Task> getTasksMap(List<Task> allTasks) {
        Map<Integer, Task> tasksMap = new HashMap<>();
        for (Task task : allTasks) {
            tasksMap.put(task.getId(), task);
        }
        return tasksMap;
    }

    private static Map<Integer, String> getIdDateStringMap(List<Task> allTasks) {
        Map<Integer, String> idDateStringMap = new HashMap<>();
        for (Task task : allTasks) {
            idDateStringMap.put(task.getId(), task.getDate());
        }
        return idDateStringMap;
    }

    private static Map<Integer, LocalDate> getUnsortedIdDateMap(Map<Integer, String> idDateStringMap) {
        Map<Integer, LocalDate> unsortedIdDateMap = new HashMap<>();
        for (Map.Entry<Integer, String> idDate : idDateStringMap.entrySet()) {
            unsortedIdDateMap.put(idDate.getKey(), LocalDate.parse(idDate.getValue(), DateTimeFormatter.ISO_DATE));
        }
        return unsortedIdDateMap;
    }
}
