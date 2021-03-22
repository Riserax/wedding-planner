package pl.com.weddingPlanner.util;

import android.content.Context;

import java.util.List;

import pl.com.weddingPlanner.persistence.dao.AgeRangeDAO;
import pl.com.weddingPlanner.persistence.dao.BookmarkDAO;
import pl.com.weddingPlanner.persistence.dao.CategoryDAO;
import pl.com.weddingPlanner.persistence.dao.ExpenseDAO;
import pl.com.weddingPlanner.persistence.dao.GuestDAO;
import pl.com.weddingPlanner.persistence.dao.PaymentDAO;
import pl.com.weddingPlanner.persistence.dao.PersonDAO;
import pl.com.weddingPlanner.persistence.dao.SubTaskDAO;
import pl.com.weddingPlanner.persistence.dao.SubcontractorDAO;
import pl.com.weddingPlanner.persistence.dao.TableDAO;
import pl.com.weddingPlanner.persistence.dao.TaskDAO;
import pl.com.weddingPlanner.persistence.dao.WeddingDAO;
import pl.com.weddingPlanner.persistence.database.AppDatabase;
import pl.com.weddingPlanner.persistence.entity.AgeRange;
import pl.com.weddingPlanner.persistence.entity.Bookmark;
import pl.com.weddingPlanner.persistence.entity.Category;
import pl.com.weddingPlanner.persistence.entity.Expense;
import pl.com.weddingPlanner.persistence.entity.Guest;
import pl.com.weddingPlanner.persistence.entity.Payment;
import pl.com.weddingPlanner.persistence.entity.Person;
import pl.com.weddingPlanner.persistence.entity.SubTask;
import pl.com.weddingPlanner.persistence.entity.Subcontractor;
import pl.com.weddingPlanner.persistence.entity.Table;
import pl.com.weddingPlanner.persistence.entity.Task;
import pl.com.weddingPlanner.persistence.entity.Wedding;

import static pl.com.weddingPlanner.persistence.database.AppDatabase.getInstance;

public class DAOUtil {

    public static void deleteTaskById(Context context, int id) {
        AppDatabase appDatabase = getInstance(context);
        TaskDAO taskDAO = appDatabase.taskDAO();
        taskDAO.deleteById(id);
    }

    public static void deleteExpenseById(Context context, int id) {
        AppDatabase appDatabase = getInstance(context);
        ExpenseDAO expenseDAO = appDatabase.expenseDAO();
        expenseDAO.deleteById(id);
    }

    public static void deletePaymentById(Context context, int id) {
        AppDatabase appDatabase = getInstance(context);
        PaymentDAO paymentDAO = appDatabase.paymentDAO();
        paymentDAO.deleteById(id);
    }

    public static void deleteAllPaymentsByExpenseId(Context context, int expenseId) {
        AppDatabase appDatabase = getInstance(context);
        PaymentDAO paymentDAO = appDatabase.paymentDAO();
        paymentDAO.deleteByExpenseId(expenseId);
    }

    public static void deleteGuest(Context context, Guest guest) {
        AppDatabase appDatabase = getInstance(context);
        GuestDAO guestDAO = appDatabase.guestDAO();
        guestDAO.delete(guest);
    }

    public static void deleteGuestById(Context context, Integer id) {
        AppDatabase appDatabase = getInstance(context);
        GuestDAO guestDAO = appDatabase.guestDAO();
        guestDAO.deleteById(id);
    }

    public static void deleteSubcontractor(Context context, Subcontractor subcontractor) {
        AppDatabase appDatabase = getInstance(context);
        SubcontractorDAO subcontractorDAO = appDatabase.subcontractorDAO();
        subcontractorDAO.delete(subcontractor);
    }

    public static List<Category> getAllCategoriesByType(Context context, String type) {
        AppDatabase appDatabase = getInstance(context);
        CategoryDAO categoryDAO = appDatabase.categoryDAO();
        return categoryDAO.getAllByType(type);
    }

    public static int getCategoriesCount(Context context) {
        AppDatabase appDatabase = getInstance(context);
        CategoryDAO categoryDAO = appDatabase.categoryDAO();
        return categoryDAO.count();
    }

    public static Category getCategoryByNameAndType(Context context, String name, String type) {
        AppDatabase appDatabase = getInstance(context);
        CategoryDAO categoryDAO = appDatabase.categoryDAO();
        return categoryDAO.getByNameAndType(name, type);
    }

    public static List<Task> getAllTasks(Context context) {
        AppDatabase appDatabase = getInstance(context);
        TaskDAO taskDAO = appDatabase.taskDAO();
        return taskDAO.getAll();
    }

    public static List<Task> getAllTasksByCategory(Context context, String category) {
        AppDatabase appDatabase = getInstance(context);
        TaskDAO taskDAO = appDatabase.taskDAO();
        return taskDAO.getAllByCategory(category);
    }

    public static Task getTaskById(Context context, int taskId) {
        AppDatabase appDatabase = getInstance(context);
        TaskDAO taskDAO = appDatabase.taskDAO();
        return taskDAO.get(taskId);
    }

    public static int getAllTasksCount(Context context) {
        AppDatabase appDatabase = getInstance(context);
        TaskDAO taskDAO = appDatabase.taskDAO();
        return taskDAO.count();
    }

    public static int getTasksByStatusCount(Context context, String status) {
        AppDatabase appDatabase = getInstance(context);
        TaskDAO taskDAO = appDatabase.taskDAO();
        return taskDAO.countByStatus(status);
    }

    public static List<SubTask> getAllSubTasks(Context context) {
        AppDatabase appDatabase = getInstance(context);
        SubTaskDAO subTaskDAO = appDatabase.subTaskDAO();
        return subTaskDAO.getAll();
    }

    public static SubTask getSubTaskById(Context context, int subTaskId) {
        AppDatabase appDatabase = getInstance(context);
        SubTaskDAO subTaskDAO = appDatabase.subTaskDAO();
        return subTaskDAO.get(subTaskId);
    }

    public static List<Bookmark> getAllBookmarks(Context context) {
        AppDatabase appDatabase = getInstance(context);
        BookmarkDAO bookmarkDAO = appDatabase.bookmarkDAO();
        return bookmarkDAO.getAll();
    }

    public static Bookmark getBookmarkById(Context context, int id) {
        AppDatabase appDatabase = getInstance(context);
        BookmarkDAO bookmarkDAO = appDatabase.bookmarkDAO();
        return bookmarkDAO.getById(id);
    }

    public static Bookmark getBookmarkByName(Context context, String name) {
        AppDatabase appDatabase = getInstance(context);
        BookmarkDAO bookmarkDAO = appDatabase.bookmarkDAO();
        return bookmarkDAO.getByName(name);
    }

    public static List<Person> getAllPersons(Context context) {
        AppDatabase appDatabase = getInstance(context);
        PersonDAO personDAO = appDatabase.personDAO();
        return personDAO.getAll();
    }

    public static int getPersonsCount(Context context) {
        AppDatabase appDatabase = getInstance(context);
        PersonDAO personDAO = appDatabase.personDAO();
        return personDAO.count();
    }

    public static Person getPersonById(Context context, int id) {
        AppDatabase appDatabase = getInstance(context);
        PersonDAO personDAO = appDatabase.personDAO();
        return personDAO.getById(id);
    }

    public static Person getPersonByName(Context context, String name) {
        AppDatabase appDatabase = getInstance(context);
        PersonDAO personDAO = appDatabase.personDAO();
        return personDAO.getByName(name);
    }

    public static List<Expense> getAllExpenses(Context context) {
        AppDatabase appDatabase = getInstance(context);
        ExpenseDAO expenseDAO = appDatabase.expenseDAO();
        return expenseDAO.getAll();
    }

    public static List<Expense> getAllExpensesByCategory(Context context, String category) {
        AppDatabase appDatabase = getInstance(context);
        ExpenseDAO expenseDAO = appDatabase.expenseDAO();
        return expenseDAO.getAllByCategory(category);
    }

    public static Expense getExpenseById(Context context, int id) {
        AppDatabase appDatabase = getInstance(context);
        ExpenseDAO expenseDAO = appDatabase.expenseDAO();
        return expenseDAO.getById(id);
    }

    public static List<Payment> getAllPayments(Context context) {
        AppDatabase appDatabase = getInstance(context);
        PaymentDAO paymentDAO = appDatabase.paymentDAO();
        return paymentDAO.getAll();
    }

    public static List<Payment> getAllPaidPayments(Context context) {
        AppDatabase appDatabase = getInstance(context);
        PaymentDAO paymentDAO = appDatabase.paymentDAO();
        return paymentDAO.getAllPaid();
    }

    public static List<Payment> getAllPaymentsByExpenseId(Context context, int expenseId) {
        AppDatabase appDatabase = getInstance(context);
        PaymentDAO paymentDAO = appDatabase.paymentDAO();
        return paymentDAO.getAllByExpenseId(expenseId);
    }

    public static List<Payment> getAllPaidPaymentsByExpenseId(Context context, int expenseId) {
        AppDatabase appDatabase = getInstance(context);
        PaymentDAO paymentDAO = appDatabase.paymentDAO();
        return paymentDAO.getAllPaidByExpenseId(expenseId);
    }

    public static Payment getPaymentById(Context context, int id) {
        AppDatabase appDatabase = getInstance(context);
        PaymentDAO paymentDAO = appDatabase.paymentDAO();
        return paymentDAO.getById(id);
    }

    public static int getPaymentsCountByExpenseId(Context context, int expenseId) {
        AppDatabase appDatabase = getInstance(context);
        PaymentDAO paymentDAO = appDatabase.paymentDAO();
        return paymentDAO.countByExpenseId(expenseId);
    }

    public static List<Guest> getAllGuests(Context context) {
        AppDatabase appDatabase = getInstance(context);
        GuestDAO guestDAO = appDatabase.guestDAO();
        return guestDAO.getAll();
    }

    public static List<Guest> getAllGuestsWithoutAccompany(Context context) {
        AppDatabase appDatabase = getInstance(context);
        GuestDAO guestDAO = appDatabase.guestDAO();
        return guestDAO.getAllGuestsWithoutAccompany();
    }

    public static Guest getGuestById(Context context, Integer id) {
        AppDatabase appDatabase = getInstance(context);
        GuestDAO guestDAO = appDatabase.guestDAO();
        return guestDAO.getById(id);
    }

    public static Guest getGuestByNameSurname(Context context, String nameSurname) {
        AppDatabase appDatabase = getInstance(context);
        GuestDAO guestDAO = appDatabase.guestDAO();
        return guestDAO.getByNameSurname(nameSurname);
    }

    public static int getAllGuestsCount(Context context) {
        AppDatabase appDatabase = getInstance(context);
        GuestDAO guestDAO = appDatabase.guestDAO();
        return guestDAO.count();
    }

    public static int getGuestsCountByPresence(Context context, String presence) {
        AppDatabase appDatabase = getInstance(context);
        GuestDAO guestDAO = appDatabase.guestDAO();
        return guestDAO.countByPresence(presence);
    }

    public static List<AgeRange> getAllAgeRanges(Context context) {
        AppDatabase appDatabase = getInstance(context);
        AgeRangeDAO ageRangeDAO = appDatabase.ageRangeDAO();
        return ageRangeDAO.getAll();
    }

    public static List<Table> getAllTables(Context context) {
        AppDatabase appDatabase = getInstance(context);
        TableDAO tableDAO = appDatabase.tableDAO();
        return tableDAO.getAll();
    }

    public static Table getTableById(Context context, Integer id) {
        AppDatabase appDatabase = getInstance(context);
        TableDAO tableDAO = appDatabase.tableDAO();
        return tableDAO.getById(id);
    }

    public static List<Subcontractor> getAllSubcontractors(Context context) {
        AppDatabase appDatabase = getInstance(context);
        SubcontractorDAO subcontractorDAO = appDatabase.subcontractorDAO();
        return subcontractorDAO.getAll();
    }

    public static List<Subcontractor> getAllSubcontractorsByCategory(Context context, String category) {
        AppDatabase appDatabase = getInstance(context);
        SubcontractorDAO subcontractorDAO = appDatabase.subcontractorDAO();
        return subcontractorDAO.getAllByCategory(category);
    }

    public static List<Subcontractor> getAllNotConnectedSubcontractors(Context context) {
        AppDatabase appDatabase = getInstance(context);
        SubcontractorDAO subcontractorDAO = appDatabase.subcontractorDAO();
        return subcontractorDAO.getAllNotConnected();
    }

    public static Subcontractor getSubcontractorById(Context context, Integer id) {
        AppDatabase appDatabase = getInstance(context);
        SubcontractorDAO subcontractorDAO = appDatabase.subcontractorDAO();
        return subcontractorDAO.getById(id);
    }

    public static Subcontractor getSubcontractorByName(Context context, String name) {
        AppDatabase appDatabase = getInstance(context);
        SubcontractorDAO subcontractorDAO = appDatabase.subcontractorDAO();
        return subcontractorDAO.getByName(name);
    }

    public static int getAllSubcontractorsCount(Context context) {
        AppDatabase appDatabase = getInstance(context);
        SubcontractorDAO subcontractorDAO = appDatabase.subcontractorDAO();
        return subcontractorDAO.count();
    }

    public static int getSubcontractorsByStageCount(Context context, String collaborationStage) {
        AppDatabase appDatabase = getInstance(context);
        SubcontractorDAO subcontractorDAO = appDatabase.subcontractorDAO();
        return subcontractorDAO.countByStage(collaborationStage);
    }

    public static Wedding getWeddingById(Context context, Integer id) {
        AppDatabase appDatabase = getInstance(context);
        WeddingDAO weddingDAO = appDatabase.weddingDAO();
        return weddingDAO.getById(id);
    }

    public static void insertCategory(Context context, Category category) {
        AppDatabase appDatabase = getInstance(context);
        CategoryDAO categoryDAO = appDatabase.categoryDAO();
        categoryDAO.insert(category);
    }

    public static void insertTask(Context context, Task task) {
        AppDatabase appDatabase = getInstance(context);
        TaskDAO taskDAO = appDatabase.taskDAO();
        taskDAO.insert(task);
    }

    public static void insertBookmark(Context context, Bookmark bookmark) {
        AppDatabase appDatabase = getInstance(context);
        BookmarkDAO bookmarkDAO = appDatabase.bookmarkDAO();
        bookmarkDAO.insert(bookmark);
    }

    public static void insertPerson(Context context, Person person) {
        AppDatabase appDatabase = getInstance(context);
        PersonDAO personDAO = appDatabase.personDAO();
        personDAO.insert(person);
    }

    public static void insertSubTask(Context context, SubTask subTask) {
        AppDatabase appDatabase = getInstance(context);
        SubTaskDAO subTaskDAO = appDatabase.subTaskDAO();
        subTaskDAO.insert(subTask);
    }

    public static void insertExpense(Context context, Expense expense) {
        AppDatabase appDatabase = getInstance(context);
        ExpenseDAO expenseDAO = appDatabase.expenseDAO();
        expenseDAO.insert(expense);
    }

    public static void insertPayment(Context context, Payment payment) {
        AppDatabase appDatabase = getInstance(context);
        PaymentDAO paymentDAO = appDatabase.paymentDAO();
        paymentDAO.insert(payment);
    }

    public static void insertGuest(Context context, Guest guest) {
        AppDatabase appDatabase = getInstance(context);
        GuestDAO guestDAO = appDatabase.guestDAO();
        guestDAO.insert(guest);
    }

    public static void insertAgeRange(Context context, AgeRange ageRange) {
        AppDatabase appDatabase = getInstance(context);
        AgeRangeDAO ageRangeDAO = appDatabase.ageRangeDAO();
        ageRangeDAO.insert(ageRange);
    }

    public static void insertTable(Context context, Table table) {
        AppDatabase appDatabase = getInstance(context);
        TableDAO tableDAO = appDatabase.tableDAO();
        tableDAO.insert(table);
    }

    public static void insertSubcontractor(Context context, Subcontractor subcontractor) {
        AppDatabase appDatabase = getInstance(context);
        SubcontractorDAO subcontractorDAO = appDatabase.subcontractorDAO();
        subcontractorDAO.insert(subcontractor);
    }

    public static void insertWedding(Context context, Wedding wedding) {
        AppDatabase appDatabase = getInstance(context);
        WeddingDAO weddingDAO = appDatabase.weddingDAO();
        weddingDAO.insert(wedding);
    }

    public static void mergeExpense(Context context, Expense expense) {
        AppDatabase appDatabase = getInstance(context);
        ExpenseDAO expenseDAO = appDatabase.expenseDAO();
        expenseDAO.merge(expense);
    }

    public static void mergePayment(Context context, Payment payment) {
        AppDatabase appDatabase = getInstance(context);
        PaymentDAO paymentDAO = appDatabase.paymentDAO();
        paymentDAO.merge(payment);
    }

    public static void mergeGuest(Context context, Guest guest) {
        AppDatabase appDatabase = getInstance(context);
        GuestDAO guestDAO = appDatabase.guestDAO();
        guestDAO.merge(guest);
    }

    public static void mergeSubcontractor(Context context, Subcontractor subcontractor) {
        AppDatabase appDatabase = getInstance(context);
        SubcontractorDAO subcontractorDAO = appDatabase.subcontractorDAO();
        subcontractorDAO.merge(subcontractor);
    }

    public static void mergeTask(Context context, Task task) {
        AppDatabase appDatabase = getInstance(context);
        TaskDAO taskDAO = appDatabase.taskDAO();
        taskDAO.merge(task);
    }

    public static void setSubTaskDone(Context context, String done, int subTaskId) {
        AppDatabase appDatabase = getInstance(context);
        SubTaskDAO subTaskDAO = appDatabase.subTaskDAO();
        subTaskDAO.setDone(done, subTaskId);
    }

    public static void updateGuestConnectedWithId(Context context, Integer guestId, Integer connectedWithId) {
        AppDatabase appDatabase = getInstance(context);
        GuestDAO guestDAO = appDatabase.guestDAO();
        guestDAO.updateGuest(guestId, connectedWithId);
    }

    public static void updateExpenseSubcontractorId(Context context, Integer subcontractorId, Integer expenseId) {
        AppDatabase appDatabase = getInstance(context);
        ExpenseDAO expenseDAO = appDatabase.expenseDAO();
        expenseDAO.updateSubcontractorId(subcontractorId, expenseId);
    }

    public static void updateSubcontractorExpenseId(Context context, Integer expenseId, Integer subcontractorId) {
        AppDatabase appDatabase = getInstance(context);
        SubcontractorDAO subcontractorDAO = appDatabase.subcontractorDAO();
        subcontractorDAO.updateExpenseId(expenseId, subcontractorId);
    }

    public static void updateTaskSubTasks(Context context, String subTasksIds, Integer taskId) {
        AppDatabase appDatabase = getInstance(context);
        TaskDAO taskDAO = appDatabase.taskDAO();
        taskDAO.updateSubTasks(subTasksIds, taskId);
    }

    public static void clearSubcontractorExpenseId(Context context, Integer subcontractorId) {
        AppDatabase appDatabase = getInstance(context);
        SubcontractorDAO subcontractorDAO = appDatabase.subcontractorDAO();
        subcontractorDAO.clearExpenseId(subcontractorId);
    }
}
