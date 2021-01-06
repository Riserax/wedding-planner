package pl.com.weddingPlanner.persistence.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import pl.com.weddingPlanner.persistence.entity.SubExpense;

@Dao
public interface SubExpenseDAO {

    @Query("SELECT * FROM subExpense")
    List<SubExpense> getAll();

    @Query("SELECT * FROM subExpense WHERE id = :id")
    SubExpense getById(int id);

    @Insert
    Long insert(SubExpense subExpense);

    @Delete
    void delete(SubExpense subExpense);

    @Query("DELETE FROM subExpense WHERE id = :id")
    void deleteById(int id);

    @Update
    void merge(SubExpense subExpense);

    @Query("SELECT COUNT(*) FROM subExpense")
    int count();
}
