package pl.com.weddingPlanner.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.com.weddingPlanner.view.enums.GuestTypeEnum;
import pl.com.weddingPlanner.view.enums.PresenceEnum;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GuestInfo implements Serializable {

    private int itemId;
    private GuestTypeEnum guestType;
    private String name;
    private String surname;
    private int tableNumber;
    private PresenceEnum presence;
}