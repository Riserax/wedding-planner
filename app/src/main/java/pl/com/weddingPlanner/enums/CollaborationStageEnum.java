package pl.com.weddingPlanner.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import pl.com.weddingPlanner.R;

@Getter
@AllArgsConstructor
public enum CollaborationStageEnum {

    NONE(0, 0),
    CONSIDERING(R.string.subcontractor_field_considering, R.drawable.ic_help_circle_small),
    IN_CONTACT(R.string.subcontractor_field_in_contact, R.drawable.ic_conversation_small),
    CONFIRMED(R.string.subcontractor_field_confirmed, R.drawable.ic_check_circle_small),
    PAID(R.string.subcontractor_field_paid, R.drawable.ic_dollar_small),
    ;

    private final int textResourceId;
    private final int drawableResourceId;
}
