package pl.com.weddingPlanner.model.info;

import android.widget.LinearLayout;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.com.weddingPlanner.enums.CollaborationStageEnum;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubcontractorInfo implements Serializable {

    private int itemId;
    private String name;
    private String categoryIconId;
    private CollaborationStageEnum collaborationStage;
    private int paymentsPercentage;
    private LinearLayout stagePaymentsLayout;
}
