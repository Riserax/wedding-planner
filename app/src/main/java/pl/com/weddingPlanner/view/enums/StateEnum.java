package pl.com.weddingPlanner.view.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public enum StateEnum {

    PENDING("ic_clock"),
    PAID("ic_check_circle_outline"),
    ;

    private final String iconCode;
}
