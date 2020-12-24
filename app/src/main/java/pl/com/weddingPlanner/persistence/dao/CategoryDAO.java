package pl.com.weddingPlanner.persistence.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import pl.com.weddingPlanner.persistence.entity.Category;

@Dao
public interface CategoryDAO {

    @Query("SELECT * FROM category")
    List<Category> getAll();

    @Insert
    Long insert(Category category);

    @Delete
    void delete(Category category);

    @Query("DELETE FROM category WHERE name = :name")
    void delete(String name);

    @Update
    void merge(Category category);

    @Query("SELECT * FROM category WHERE name = :name")
    Category get(String name);

    @Query("SELECT COUNT(*) FROM category")
    int count();
}
