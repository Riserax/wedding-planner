package pl.com.weddingPlanner.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import pl.com.weddingPlanner.R;

@Getter
@AllArgsConstructor
public enum TaskStatusEnum {

    NEW(R.string.task_field_status_new),
    IN_PROGRESS(R.string.task_field_status_in_progress),
    DONE(R.string.task_field_status_done),
    ;

    private final int nameResId;
}
