package pl.com.weddingPlanner.persistence.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Entity(indices = {@Index(value = "id", unique = true)})
@Builder(toBuilder = true)
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class Expense implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private Integer id;
    @NonNull
    private String title;
    @NonNull
    private String initialAmount;
    @NonNull
    private String category;
    private String recipient;
    private String forWhat;
    private String payers;
    private String subExpenses;
    @NonNull
    private String editDate;
}
