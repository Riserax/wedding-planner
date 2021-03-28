package pl.com.weddingPlanner.view.util;

import android.content.Context;

import org.apache.commons.lang3.StringUtils;
import org.threeten.bp.LocalDate;
import org.threeten.bp.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

import pl.com.weddingPlanner.databinding.ActivityNewTaskBinding;
import pl.com.weddingPlanner.enums.TaskStatus;
import pl.com.weddingPlanner.model.PickedDate;
import pl.com.weddingPlanner.model.PickedTime;
import pl.com.weddingPlanner.persistence.entity.Bookmark;
import pl.com.weddingPlanner.persistence.entity.Person;
import pl.com.weddingPlanner.persistence.entity.Task;
import pl.com.weddingPlanner.persistence.entity.Wedding;
import pl.com.weddingPlanner.util.DAOUtil;
import pl.com.weddingPlanner.util.DateUtil;

import static java.util.Calendar.DECEMBER;
import static java.util.Calendar.JANUARY;
import static java.util.Calendar.MONTH;
import static java.util.Calendar.YEAR;

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

    public static void setSelectedTaskStatus(TaskStatus taskStatus, ActivityNewTaskBinding binding, Context context) {
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

    public static List<Bookmark> getBookmarks(Task taskDetails, Context context) {
        List<Bookmark> bookmarks = new ArrayList<>();

        if (StringUtils.isNotBlank(taskDetails.getBookmarks())) {
            String[] bookmarksIds = taskDetails.getBookmarks().split(",", -1);

            for (String bookmarksIdString : bookmarksIds) {
                int bookmarkId = Integer.parseInt(bookmarksIdString);
                bookmarks.add(DAOUtil.getBookmarkById(context, bookmarkId));
            }
        }

        return bookmarks;
    }

    public static List<Person> getAssignees(Task taskDetails, Context context) {
        List<Person> assignees = new ArrayList<>();

        if (StringUtils.isNotBlank(taskDetails.getAssignees())) {
            String[] assigneesIds = taskDetails.getAssignees().split(",", -1);

            for (String assigneeIdString : assigneesIds) {
                int assigneeId = Integer.parseInt(assigneeIdString);
                assignees.add(DAOUtil.getPersonById(context, assigneeId));
            }
        }

        return assignees;
    }

    public static Map<Integer,String> getMonthsMap(Context context) {
        Map<Integer, String> months = new LinkedHashMap<>();
        int count = 0;

        Wedding wedding = DAOUtil.getWeddingById(context, 1); //TODO rozpoznawać w którym weselu jesteśmy

        Calendar weddingCreationDateCalendar = getCalendarFromStringDate(wedding.getCreationDate());
        Calendar weddingDateCalendar = getCalendarFromStringDate(wedding.getDate());

        while (!yearsAndMonthsEqual(weddingCreationDateCalendar, weddingDateCalendar)) {
            String monthYear = getMonthYear(weddingCreationDateCalendar, context);
            months.put(count++, monthYear);

            if (!yearsEqual(weddingCreationDateCalendar, weddingDateCalendar)) {
                proceedWhenYearsNotEqual(weddingCreationDateCalendar);
            } else if (!monthsEqual(weddingCreationDateCalendar, weddingDateCalendar)) {
                addMonth(weddingCreationDateCalendar);

                if (monthsEqual(weddingCreationDateCalendar, weddingDateCalendar)) {
                    monthYear = getMonthYear(weddingCreationDateCalendar, context);
                    months.put(count++, monthYear);
                }
            }
        }

        return months;
    }

    private static Calendar getCalendarFromStringDate(String stringDate) {
        Date date = DateUtil.convertStringToDate(stringDate);
        return getCalendarFromDate(date);
    }

    private static Calendar getCalendarFromDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }

    private static boolean yearsAndMonthsEqual(Calendar calendar1, Calendar calendar2) {
        return yearsEqual(calendar1, calendar2) && monthsEqual(calendar1, calendar2);
    }

    private static boolean yearsEqual(Calendar calendar1, Calendar calendar2) {
        return calendar1.get(YEAR) == calendar2.get(YEAR);
    }

    private static boolean monthsEqual(Calendar calendar1, Calendar calendar2) {
        return calendar1.get(MONTH) == calendar2.get(MONTH);
    }

    private static boolean monthEqualsDecember(Calendar calendar) {
        return calendar.get(MONTH) == DECEMBER;
    }

    public static String getMonthYear(String stringDate, Context context) {
        Calendar calendar = getCalendarFromStringDate(stringDate);
        return DateUtil.getMonth(calendar, context) + " " + calendar.get(YEAR);
    }

    private static String getMonthYear(Calendar calendar, Context context) {
        return DateUtil.getMonth(calendar, context) + " " + calendar.get(YEAR);
    }

    private static void proceedWhenYearsNotEqual(Calendar calendar) {
        if (monthEqualsDecember(calendar)) {
            calendar.add(YEAR, 1);
            calendar.set(MONTH, JANUARY);
        } else {
            addMonth(calendar);
        }
    }

    private static void addMonth(Calendar calendar) {
        calendar.add(MONTH, 1);
    }

    public static int getCurrentMonthYearTab(Context context, Map<Integer, String> months) {
        Calendar calendarNow = getCalendarFromDate(new Date());
        String monthYearNow = getMonthYear(calendarNow, context);

        int tabId = 0;
        for (Map.Entry<Integer, String> month : months.entrySet()) {
            if (monthYearNow.equals(month.getValue())) {
                tabId = month.getKey();
            }
        }

        return tabId;
    }
}
