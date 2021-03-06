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

    @Query("SELECT * FROM guest ORDER BY nameSurname COLLATE NOCASE ASC")
    List<Guest> getAll();

    @Query("SELECT * FROM guest " +
            "WHERE type = 'GUEST' " +
            "   AND connectedWithId = 0")
    List<Guest> getAllGuestsWithoutAccompany();

    @Query("SELECT * FROM guest WHERE id = :id")
    Guest getById(Integer id);

    @Query("SELECT * FROM guest WHERE nameSurname = :nameSurname")
    Guest getByNameSurname(String nameSurname);

    @Insert
    Long insert(Guest guest);

    @Delete
    void delete(Guest guest);

    @Query("DELETE FROM guest WHERE id = :id")
    void deleteById(Integer id);

    @Update
    void merge(Guest guest);

    @Query("UPDATE guest SET connectedWithId = :guestId WHERE id = :connectedWithId")
    void updateGuest(Integer guestId, Integer connectedWithId);

    @Query("SELECT COUNT(*) FROM guest")
    int count();

    @Query("SELECT COUNT(*) FROM guest WHERE presence = :presence")
    int countByPresence(String presence);
}
