package pl.com.weddingPlanner.model;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class PickedDate {

    private final int year;
    private final int month;
    private final int dayOfMonth;
}
