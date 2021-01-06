package pl.com.weddingPlanner.view.util;

import org.threeten.bp.LocalDate;
import org.threeten.bp.format.DateTimeFormatter;

import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import pl.com.weddingPlanner.persistence.entity.Expense;
import pl.com.weddingPlanner.util.DateUtil;

public class BudgetUtil {

    public static Map<Integer, LocalDate> getSortedIdDateMap(List<Expense> allExpenses) {
        Map<Integer, String> idDateStringMap = getIdDateStringMap(allExpenses);

        Map<Integer, LocalDate> unsortedIdDateMap = getUnsortedIdDateMap(idDateStringMap);
        Map<Integer, LocalDate> sortedIdDateMap = new LinkedHashMap<>();

        unsortedIdDateMap.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .forEachOrdered(x -> sortedIdDateMap.put(x.getKey(), x.getValue()));

        return sortedIdDateMap;
    }

    public static Map<Integer, Expense> getExpensesMap(List<Expense> allExpenses) {
        Map<Integer, Expense> expensesMap = new HashMap<>();
        for (Expense expense : allExpenses) {
            expensesMap.put(expense.getId(), expense);
        }
        return expensesMap;
    }

    private static Map<Integer, String> getIdDateStringMap(List<Expense> allExpenses) {
        Map<Integer, String> idDateStringMap = new HashMap<>();
        for (Expense expense : allExpenses) {
            String date = DateUtil.getDateFromDateTime(expense.getEditDate());
            idDateStringMap.put(expense.getId(), date);
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
