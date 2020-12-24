package pl.com.weddingPlanner.persistence.entity;

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
@Entity(indices = {@Index(value = "name", unique = true)})
@Builder(toBuilder = true)
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class Guest implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private Integer id;
    private String guestType;
    private String name;
    private String surname;
    private Integer age;
    private String accompany;
    private Integer tableNumber;
    private String presence;
    private String contact;
    private String comments;
}
