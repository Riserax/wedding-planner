package pl.com.weddingPlanner.model.info;

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
    private CollaborationStageEnum subcontractorStage;
    private String paymentPercentage;
}
