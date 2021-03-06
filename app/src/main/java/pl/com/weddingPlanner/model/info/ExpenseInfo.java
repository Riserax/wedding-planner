package pl.com.weddingPlanner.model.info;

import android.widget.LinearLayout;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExpenseInfo implements Serializable {

    private int itemId;
    private String title;
    private String categoryIconId;
    private String amount;
    private String date;
    private LinearLayout payersLayout;
}
