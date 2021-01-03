package pl.com.weddingPlanner.util;

import android.content.Context;

import java.util.List;

import pl.com.weddingPlanner.persistence.dao.BookmarkDAO;
import pl.com.weddingPlanner.persistence.dao.CategoryDAO;
import pl.com.weddingPlanner.persistence.dao.PersonDAO;
import pl.com.weddingPlanner.persistence.dao.SubTaskDAO;
import pl.com.weddingPlanner.persistence.dao.TaskDAO;
import pl.com.weddingPlanner.persistence.database.AppDatabase;
import pl.com.weddingPlanner.persistence.entity.Bookmark;
import pl.com.weddingPlanner.persistence.entity.Category;
import pl.com.weddingPlanner.persistence.entity.Person;
import pl.com.weddingPlanner.persistence.entity.SubTask;
import pl.com.weddingPlanner.persistence.entity.Task;

import static pl.com.weddingPlanner.persistence.database.AppDatabase.getInstance;

public class DAOUtil {

    public static void deleteTaskById(Context context, int id) {
        AppDatabase appDatabase = getInstance(context);
        TaskDAO taskDAO = appDatabase.taskDAO();
        taskDAO.delete(id);
    }

    public static List<Category> getAllCategories(Context context) {
        AppDatabase appDatabase = getInstance(context);
        CategoryDAO categoryDAO = appDatabase.categoryDAO();
        return categoryDAO.getAll();
    }

    public static List<Category> getAllCategoriesByType(Context context, String type) {
        AppDatabase appDatabase = getInstance(context);
        CategoryDAO categoryDAO = appDatabase.categoryDAO();
        return categoryDAO.getAllByType(type);
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

    public static void setSubTaskDone(Context context, String done, int subTaskId) {
        AppDatabase appDatabase = getInstance(context);
        SubTaskDAO subTaskDAO = appDatabase.subTaskDAO();
        subTaskDAO.setDone(done, subTaskId);
    }
}
