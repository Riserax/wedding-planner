package pl.com.weddingPlanner.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public enum PaymentState {

    PENDING("ic_clock"),
    PAID("ic_check_circle_outline"),
    ;

    private final String iconCode;
}
