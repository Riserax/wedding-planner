package pl.com.weddingPlanner.view.util;

import android.content.Context;

import org.threeten.bp.LocalDate;
import org.threeten.bp.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

import pl.com.weddingPlanner.databinding.ActivityNewTaskBinding;
import pl.com.weddingPlanner.enums.TaskStatusEnum;
import pl.com.weddingPlanner.model.PickedDate;
import pl.com.weddingPlanner.model.PickedTime;
import pl.com.weddingPlanner.persistence.entity.Bookmark;
import pl.com.weddingPlanner.persistence.entity.Person;
import pl.com.weddingPlanner.persistence.entity.Task;
import pl.com.weddingPlanner.util.DAOUtil;

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

    public static String getBookmarksIds(String bookmarks, Context context) {
        String[] bookmarksNames = bookmarks.split("\\|", -1);
        List<Bookmark> bookmarkList = new ArrayList<>();
        StringJoiner bookmarksIdsJoiner = new StringJoiner(",");

        for (String bookmarkName : bookmarksNames) {
            bookmarkList.add(DAOUtil.getBookmarkByName(context, bookmarkName.trim()));
        }

        for (Bookmark bookmark : bookmarkList) {
            bookmarksIdsJoiner.add(bookmark.getId().toString());
        }

        return bookmarksIdsJoiner.toString();
    }

    public static String getAssigneesIds(String assignees, Context context) {
        String[] assigneesNames = assignees.split("\\|", -1);
        List<Person> assigneeList = new ArrayList<>();
        StringJoiner assigneesIdsJoiner = new StringJoiner(",");

        for (String assigneeName : assigneesNames) {
            assigneeList.add(DAOUtil.getPersonByName(context, assigneeName.trim()));
        }

        for (Person assignee : assigneeList) {
            assigneesIdsJoiner.add(assignee.getId().toString());
        }

        return assigneesIdsJoiner.toString();
    }

    public static String getBookmarksNamesSeparated(String idsSeparatedWithCommas, Context context) {
        String[] bookmarksIds = idsSeparatedWithCommas.split(",", -1);

        StringJoiner stringJoiner = new StringJoiner(" | ");

        for (String bookmarkId : bookmarksIds) {
            Bookmark bookmark = DAOUtil.getBookmarkById(context, Integer.parseInt(bookmarkId));
            stringJoiner.add(bookmark.getName());
        }

        return stringJoiner.toString();
    }

    public static String getAssigneeNamesSeparated(String idsSeparatedWithCommas, Context context) {
        String[] assigneeIds = idsSeparatedWithCommas.split(",", -1);

        StringJoiner stringJoiner = new StringJoiner(" | ");

        for (String assigneeId : assigneeIds) {
            Person person = DAOUtil.getPersonById(context, Integer.parseInt(assigneeId));
            stringJoiner.add(person.getName());
        }

        return stringJoiner.toString();
    }

    public static List<Integer> getListOfBookmarksIds(String idsSeparatedWithCommas, Context context) {
        String bookmarkNamesSeparated = getBookmarksNamesSeparated(idsSeparatedWithCommas, context);
        List<Integer> selectedBookmarksIds = new ArrayList<>();

        int count = 0;
        for (Bookmark bookmark : DAOUtil.getAllBookmarks(context)) {
            if (bookmarkNamesSeparated.contains(bookmark.getName())) {
                selectedBookmarksIds.add(count);
            }
            count++;
        }

        return selectedBookmarksIds;
    }

    public static List<Integer> getListOfAssigneesIds(String idsSeparatedWithCommas, Context context) {
        String assigneesNamesSeparated = getAssigneeNamesSeparated(idsSeparatedWithCommas, context);
        List<Integer> selectedAssigneesIds = new ArrayList<>();

        int count = 0;
        for (Person person : DAOUtil.getAllPersons(context)) {
            if (assigneesNamesSeparated.contains(person.getName())) {
                selectedAssigneesIds.add(count);
            }
            count++;
        }

        return selectedAssigneesIds;
    }

    public static PickedDate getPickedDateFromString(String dateString) {
        String[] dateComponents = dateString.split("-", -1);

        return PickedDate.builder()
                .year(Integer.parseInt(dateComponents[0]))
                .month(Integer.parseInt(dateComponents[1]))
                .dayOfMonth(Integer.parseInt(dateComponents[2]))
                .build();
    }

    public static PickedTime getPickedTimeFromString(String timeString) {
        String[] timeComponents = timeString.split(":", -1);

        return PickedTime.builder()
                .hour(Integer.parseInt(timeComponents[0]))
                .minute(Integer.parseInt(timeComponents[1]))
                .build();
    }

    public static void setSelectedTaskStatus(TaskStatusEnum taskStatus, ActivityNewTaskBinding binding, Context context) {
        switch (taskStatus) {
            case NEW:
                setTaskNewButtonsSelection(binding, context);
                break;
            case IN_PROGRESS:
                setTaskInProgressButtonsSelection(binding, context);
                break;
            case DONE:
                setTaskDoneButtonsSelection(binding, context);
        }
    }

    public static void setTaskNewButtonsSelection(ActivityNewTaskBinding binding, Context context) {
        ButtonsUtil.setButtonSelection(binding.newButton, context, true);
        ButtonsUtil.setButtonSelection(binding.inProgressButton, context, false);
        ButtonsUtil.setButtonSelection(binding.doneButton, context, false);
    }

    public static void setTaskInProgressButtonsSelection(ActivityNewTaskBinding binding, Context context) {
        ButtonsUtil.setButtonSelection(binding.newButton, context, false);
        ButtonsUtil.setButtonSelection(binding.inProgressButton, context, true);
        ButtonsUtil.setButtonSelection(binding.doneButton, context, false);
    }

    public static void setTaskDoneButtonsSelection(ActivityNewTaskBinding binding, Context context) {
        ButtonsUtil.setButtonSelection(binding.newButton, context, false);
        ButtonsUtil.setButtonSelection(binding.inProgressButton, context, false);
        ButtonsUtil.setButtonSelection(binding.doneButton, context, true);
    }
}
