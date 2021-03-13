package pl.com.weddingPlanner.model.info;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.com.weddingPlanner.enums.GuestTypeEnum;
import pl.com.weddingPlanner.enums.PresenceEnum;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GuestInfo implements Serializable {

    private int itemId;
    private GuestTypeEnum type;
    private String nameSurname;
    private int tableNumber;
    private PresenceEnum presence;
}
