package pl.com.weddingPlanner.persistence.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import pl.com.weddingPlanner.persistence.entity.Payment;

@Dao
public interface PaymentDAO {

    @Query("SELECT * FROM payment")
    List<Payment> getAll();

    @Query("SELECT * FROM payment WHERE expenseId = :expenseId")
    List<Payment> getAllByExpenseId(int expenseId);

    @Query("SELECT * FROM payment WHERE id = :id")
    Payment getById(int id);

    @Insert
    Long insert(Payment payment);

    @Delete
    void delete(Payment payment);

    @Query("DELETE FROM payment WHERE id = :id")
    void deleteById(int id);

    @Query("DELETE FROM payment WHERE expenseId = :expenseId")
    void deleteByExpenseId(int expenseId);

    @Update
    void merge(Payment payment);

    @Query("SELECT COUNT(*) FROM payment")
    int countAll();

    @Query("SELECT COUNT(*) FROM payment WHERE expenseId = :expenseId")
    int countAllByExpenseId(int expenseId);
}
