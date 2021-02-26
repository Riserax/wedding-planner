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
public class Guest implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private Integer id;
    @NonNull
    private String type;
    private Integer connectedWithId;
    @NonNull
    private String nameSurname;
    private String age;
    private String category;
    private Integer tableNumber;
    private String presence;
    private String contact;
    private String notes;
}
