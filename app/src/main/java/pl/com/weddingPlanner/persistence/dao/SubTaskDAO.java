package pl.com.weddingPlanner.persistence.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import pl.com.weddingPlanner.persistence.entity.SubTask;

@Dao
public interface SubTaskDAO {

    @Query("SELECT * FROM subTask")
    List<SubTask> getAll();

    @Query("SELECT * FROM subTask WHERE id = :id")
    SubTask get(Integer id);

    @Query("SELECT * FROM subTask WHERE name = :name")
    SubTask get(String name);

    @Insert
    Long insert(SubTask subTask);

    @Delete
    void delete(SubTask subTask);

    @Query("DELETE FROM subTask WHERE id = :id")
    void delete(Integer id);

    @Update
    void merge(SubTask subTask);

    @Query("UPDATE subTask SET done = :done WHERE id = :id")
    void setDone(String done, Integer id);

    @Query("SELECT COUNT(*) FROM subTask")
    int count();
}