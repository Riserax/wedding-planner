package pl.com.weddingPlanner.model;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.com.weddingPlanner.view.enums.BookmarkEnum;
import pl.com.weddingPlanner.view.enums.CategoryResource;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskInfo implements Serializable {

    private int itemId;
    private CategoryResource category;
    private String title;
    private List<BookmarkEnum> bookmarks;
    private String assignee;
    private String date;
}
