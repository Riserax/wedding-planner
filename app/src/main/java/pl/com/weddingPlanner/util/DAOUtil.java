package pl.com.weddingPlanner.util;

import android.content.Context;

import java.util.List;

import pl.com.weddingPlanner.persistence.dao.CategoryDAO;
import pl.com.weddingPlanner.persistence.dao.TaskDAO;
import pl.com.weddingPlanner.persistence.database.AppDatabase;
import pl.com.weddingPlanner.persistence.entity.Category;
import pl.com.weddingPlanner.persistence.entity.Task;

import static pl.com.weddingPlanner.persistence.database.AppDatabase.getInstance;

public class DAOUtil {

    public static List<Category> getAllCategories(Context context) {
        AppDatabase appDatabase = getInstance(context);
        CategoryDAO categoryDAO = appDatabase.categoryDAO();
        return categoryDAO.getAll();
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

    public static void insertTask(Context context, Task task) {
        AppDatabase appDatabase = getInstance(context);
        TaskDAO taskDAO = appDatabase.taskDAO();
        taskDAO.insert(task);
    }
}
