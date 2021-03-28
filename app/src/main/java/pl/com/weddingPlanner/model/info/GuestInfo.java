package pl.com.weddingPlanner.model.info;

import android.widget.LinearLayout;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.com.weddingPlanner.enums.GuestType;
import pl.com.weddingPlanner.enums.Presence;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GuestInfo implements Serializable {

    private int itemId;
    private GuestType type;
    private String nameSurname;
    private String ageRange;
    private String category;
    private int tableNumber;
    private Presence presence;
    private LinearLayout tablePresenceLayout;
}
