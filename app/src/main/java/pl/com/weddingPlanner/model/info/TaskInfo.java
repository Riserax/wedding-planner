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
public class TaskInfo implements Serializable {

    private int itemId;
    private String title;
    private String categoryIconId;
    private String bookmarks;
    private String assignees;
    private String date;
}
