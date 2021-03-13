package pl.com.weddingPlanner.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import pl.com.weddingPlanner.R;

@Getter
@AllArgsConstructor
public enum CollaborationStageEnum {

    NONE(0),
    CONSIDERING(R.string.subcontractor_field_considering),
    IN_CONTACT(R.string.subcontractor_field_in_contact),
    CONFIRMED(R.string.subcontractor_field_confirmed),
    PAID(R.string.subcontractor_field_paid),
    ;

    private final int resourceId;
}
