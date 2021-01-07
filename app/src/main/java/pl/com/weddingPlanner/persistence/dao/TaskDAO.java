package pl.com.weddingPlanner.persistence.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import pl.com.weddingPlanner.persistence.entity.Task;

@Dao
public interface TaskDAO {

    @Query("SELECT * FROM task")
    List<Task> getAll();

    @Query("SELECT * FROM task WHERE category = :category")
    List<Task> getAllByCategory(String category);

    @Query("SELECT * FROM task WHERE id = :id")
    Task get(Integer id);

    @Insert
    Long insert(Task task);

    @Delete
    void delete(Task task);

    @Query("DELETE FROM task WHERE id = :id")
    void deleteById(Integer id);

    @Update
    void merge(Task task);

    @Query("SELECT COUNT(*) FROM task")
    int count();
}