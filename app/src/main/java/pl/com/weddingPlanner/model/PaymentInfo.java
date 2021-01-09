package pl.com.weddingPlanner.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.com.weddingPlanner.view.enums.StateEnum;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentInfo implements Serializable {

    private int itemId;
    private String title;
    private StateEnum state;
    private String amount;
    private String payer;
    private String date;
}
