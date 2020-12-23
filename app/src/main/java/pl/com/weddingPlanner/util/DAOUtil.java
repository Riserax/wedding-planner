package pl.com.weddingPlanner.util;

import android.content.Context;

import java.util.List;

import pl.com.weddingPlanner.persistence.dao.CategoryDAO;
import pl.com.weddingPlanner.persistence.database.AppDatabase;
import pl.com.weddingPlanner.persistence.entity.Category;

import static pl.com.weddingPlanner.persistence.database.AppDatabase.getInstance;

public class DAOUtil {

    public static List<Category> getAllCategories(Context context) {
        AppDatabase appDatabase = getInstance(context);
        CategoryDAO categoryDAO = appDatabase.categoryDAO();
        return categoryDAO.getAll();
    }
}
