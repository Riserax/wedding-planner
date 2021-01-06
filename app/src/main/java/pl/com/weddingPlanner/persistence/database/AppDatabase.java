package pl.com.weddingPlanner.persistence.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import pl.com.weddingPlanner.persistence.Converters;
import pl.com.weddingPlanner.persistence.dao.BookmarkDAO;
import pl.com.weddingPlanner.persistence.dao.CategoryDAO;
import pl.com.weddingPlanner.persistence.dao.ExpenseDAO;
import pl.com.weddingPlanner.persistence.dao.PersonDAO;
import pl.com.weddingPlanner.persistence.dao.SubExpenseDAO;
import pl.com.weddingPlanner.persistence.dao.SubTaskDAO;
import pl.com.weddingPlanner.persistence.dao.TaskDAO;
import pl.com.weddingPlanner.persistence.entity.Bookmark;
import pl.com.weddingPlanner.persistence.entity.Category;
import pl.com.weddingPlanner.persistence.entity.Expense;
import pl.com.weddingPlanner.persistence.entity.Person;
import pl.com.weddingPlanner.persistence.entity.SubExpense;
import pl.com.weddingPlanner.persistence.entity.SubTask;
import pl.com.weddingPlanner.persistence.entity.Task;

@Database(entities = {Task.class, Category.class, SubTask.class, Bookmark.class, Person.class, Expense.class,
        SubExpense.class}, version = 1)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {

    private static final String DATABASE_NAME = "weddingPlannerDB";
    private static AppDatabase INSTANCE;

    public abstract TaskDAO taskDAO();
    public abstract SubTaskDAO subTaskDAO();
    public abstract CategoryDAO categoryDAO();
    public abstract BookmarkDAO bookmarkDAO();
    public abstract PersonDAO personDAO();
    public abstract ExpenseDAO expenseDAO();
    public abstract SubExpenseDAO subExpenseDAO();

    public static AppDatabase getInstance(final Context context) {

        if (INSTANCE == null) {
            INSTANCE = buildDatabase(context);
        }
        return INSTANCE;
    }

    private static AppDatabase buildDatabase(final Context context) {
        return Room.databaseBuilder(context, AppDatabase.class, DATABASE_NAME)
                .allowMainThreadQueries()
                .build();
    }
}
