package pl.com.weddingPlanner.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import pl.com.weddingPlanner.R;

@Getter
@AllArgsConstructor
public enum TaskStatusEnum {

    NEW(R.string.task_field_status_new, R.drawable.ic_new),
    IN_PROGRESS(R.string.task_field_status_in_progress, R.drawable.ic_progress),
    DONE(R.string.task_field_status_done, R.drawable.ic_check_circle_outline),
    ;

    private final int textResId;
    private final int drawableResId;
}
