package pl.com.weddingPlanner.persistence.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import pl.com.weddingPlanner.persistence.entity.Bookmark;

@Dao
public interface BookmarkDAO {

    @Query("SELECT * FROM bookmark")
    List<Bookmark> getAll();

    @Query("SELECT * FROM bookmark WHERE id = :id")
    Bookmark getById(Integer id);

    @Query("SELECT * FROM bookmark WHERE name = :name")
    Bookmark getByName(String name);

    @Insert
    Long insert(Bookmark bookmark);

    @Delete
    void delete(Bookmark bookmark);

    @Query("DELETE FROM bookmark WHERE name = :name")
    void delete(String name);

    @Update
    void merge(Bookmark bookmark);

    @Query("SELECT COUNT(*) FROM bookmark")
    int count();
}
