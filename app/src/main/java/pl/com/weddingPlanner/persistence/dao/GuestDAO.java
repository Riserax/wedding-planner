package pl.com.weddingPlanner.persistence.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import pl.com.weddingPlanner.persistence.entity.Guest;

@Dao
public interface GuestDAO {

    @Query("SELECT * FROM guest ORDER BY nameSurname")
    List<Guest> getAll();

    @Query("SELECT * FROM guest WHERE type = 'guest' AND connectedWithId = null")
    List<Guest> getAllGuestsWithoutAccompany();

    @Insert
    Long insert(Guest guest);

    @Delete
    void delete(Guest guest);

    @Update
    void merge(Guest guest);

    @Query("SELECT COUNT(*) FROM guest")
    int count();
}
