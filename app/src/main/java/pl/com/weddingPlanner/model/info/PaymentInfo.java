package pl.com.weddingPlanner.model.info;

import android.widget.LinearLayout;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.com.weddingPlanner.enums.PaymentStateEnum;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentInfo implements Serializable {

    private int itemId;
    private String title;
    private PaymentStateEnum state;
    private String amount;
    private String date;
    private LinearLayout payerLayout;
}
