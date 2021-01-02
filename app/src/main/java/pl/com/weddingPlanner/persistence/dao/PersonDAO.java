package pl.com.weddingPlanner.persistence.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import pl.com.weddingPlanner.persistence.entity.Person;

@Dao
public interface PersonDAO {

    @Query("SELECT * FROM person")
    List<Person> getAll();

    @Query("SELECT * FROM person WHERE id = :id")
    Person getById(int id);

    @Query("SELECT * FROM person WHERE name = :name")
    Person getByName(String name);

    @Insert
    Long insert(Person person);

    @Delete
    void delete(Person person);

    @Query("DELETE FROM person WHERE name = :name")
    void delete(String name);

    @Update
    void merge(Person person);

    @Query("SELECT COUNT(*) FROM person")
    int count();
}
