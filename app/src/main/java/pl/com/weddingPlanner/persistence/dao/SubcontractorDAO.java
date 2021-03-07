package pl.com.weddingPlanner.persistence.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import pl.com.weddingPlanner.persistence.entity.Subcontractor;

@Dao
public interface SubcontractorDAO {

    @Query("SELECT * FROM subcontractor ORDER BY name COLLATE NOCASE ASC")
    List<Subcontractor> getAll();

    @Query("SELECT * FROM subcontractor WHERE id = :id")
    Subcontractor getById(Integer id);

    @Query("SELECT * FROM subcontractor WHERE name = :name")
    Subcontractor getByNameSurname(String name);

    @Insert
    Long insert(Subcontractor subcontractor);

    @Delete
    void delete(Subcontractor subcontractor);

    @Query("DELETE FROM subcontractor WHERE id = :id")
    void deleteById(Integer id);

    @Update
    void merge(Subcontractor subcontractor);

    @Query("SELECT COUNT(*) FROM subcontractor")
    int count();
}
