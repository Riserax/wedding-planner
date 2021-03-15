package pl.com.weddingPlanner.persistence.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import pl.com.weddingPlanner.persistence.entity.Wedding;

@Dao
public interface WeddingDAO {

    @Query("SELECT * FROM wedding ORDER BY name COLLATE NOCASE ASC")
    List<Wedding> getAll();

    @Query("SELECT * FROM wedding WHERE id = :id")
    Wedding getById(Integer id);

    @Insert
    Long insert(Wedding wedding);

    @Delete
    void delete(Wedding wedding);

    @Query("DELETE FROM wedding WHERE id = :id")
    void deleteById(Integer id);

    @Update
    void merge(Wedding wedding);
}
