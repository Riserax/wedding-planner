package pl.com.weddingPlanner.persistence.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import pl.com.weddingPlanner.persistence.entity.AgeRange;

@Dao
public interface AgeRangeDAO {

    @Query("SELECT * FROM ageRange")
    List<AgeRange> getAll();

    @Insert
    Long insert(AgeRange ageRange);

    @Delete
    void delete(AgeRange ageRange);

    @Update
    void merge(AgeRange ageRange);

    @Query("SELECT COUNT(*) FROM ageRange")
    int count();
}
