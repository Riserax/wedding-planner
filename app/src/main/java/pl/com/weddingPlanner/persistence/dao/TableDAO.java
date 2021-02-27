package pl.com.weddingPlanner.persistence.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import pl.com.weddingPlanner.persistence.entity.Table;

@Dao
public interface TableDAO {

    @Query("SELECT * FROM `table`")
    List<Table> getAll();

    @Insert
    Long insert(Table table);

    @Delete
    void delete(Table table);

    @Update
    void merge(Table table);

    @Query("SELECT COUNT(*) FROM `table`")
    int count();
}
