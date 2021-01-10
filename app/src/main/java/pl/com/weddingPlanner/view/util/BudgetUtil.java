package pl.com.weddingPlanner.view.util;

import org.threeten.bp.LocalDate;
import org.threeten.bp.format.DateTimeFormatter;

import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import pl.com.weddingPlanner.persistence.entity.Expense;
import pl.com.weddingPlanner.persistence.entity.Payment;
import pl.com.weddingPlanner.util.DateUtil;

public class BudgetUtil {

    public static Map<Integer, LocalDate> getSortedIdDateMap(List objectList) {
        Map<Integer, String> idDateStringMap = getIdDateStringMap(objectList);

        Map<Integer, LocalDate> unsortedIdDateMap = getUnsortedIdDateMap(idDateStringMap);
        Map<Integer, LocalDate> sortedIdDateMap = new LinkedHashMap<>();

        unsortedIdDateMap.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .forEachOrdered(x -> sortedIdDateMap.put(x.getKey(), x.getValue()));

        return sortedIdDateMap;
    }

    public static Map<Integer, Object> getObjectsMap(List objectList) {
        Map<Integer, Object> objectsMap = new HashMap<>();
        for (Object object : objectList) {
            if (object instanceof Expense) {
                Expense expense = (Expense) object;
                objectsMap.put(expense.getId(), object);
            } else if (object instanceof Payment) {
                Payment payment = (Payment) object;
                objectsMap.put(payment.getId(), object);
            }
        }
        return objectsMap;
    }

    private static Map<Integer, String> getIdDateStringMap(List objectList) {
        Map<Integer, String> idDateStringMap = new HashMap<>();
        for (Object object : objectList) {
            if (object instanceof Expense) {
                Expense expense = (Expense) object;
                String date = DateUtil.getDateFromDateTime(expense.getEditDate());
                idDateStringMap.put(expense.getId(), date);
            } else if (object instanceof Payment) {
                Payment payment = (Payment) object;
                idDateStringMap.put(payment.getId(), payment.getDate());
            }
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
