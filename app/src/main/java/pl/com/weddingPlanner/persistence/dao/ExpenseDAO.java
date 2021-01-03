package pl.com.weddingPlanner.persistence.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import pl.com.weddingPlanner.persistence.entity.Expense;

@Dao
public interface ExpenseDAO {

    @Query("SELECT * FROM expense")
    List<Expense> getAll();

    @Query("SELECT * FROM expense WHERE category = :category")
    List<Expense> getAllByCategory(String category);

    @Query("SELECT * FROM expense WHERE id = :id")
    Expense getById(int id);

    @Insert
    Long insert(Expense expense);

    @Delete
    void delete(Expense expense);

    @Query("DELETE FROM expense WHERE id = :id")
    void deleteById(int id);

    @Update
    void merge(Expense expense);

    @Query("SELECT COUNT(*) FROM expense")
    int count();
}
