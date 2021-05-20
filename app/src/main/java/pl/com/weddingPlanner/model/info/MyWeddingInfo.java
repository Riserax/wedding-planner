package pl.com.weddingPlanner.model.info;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MyWeddingInfo implements Serializable {
    private String id;
    private String name;
}
