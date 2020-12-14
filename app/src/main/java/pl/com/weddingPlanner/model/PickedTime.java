package pl.com.weddingPlanner.model;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class PickedTime {

    private final int hour;
    private final int minute;
}

