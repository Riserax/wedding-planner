package pl.com.weddingPlanner.view.list;

import androidx.annotation.Nullable;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import pl.com.weddingPlanner.R;
import pl.com.weddingPlanner.model.GuestInfo;
import pl.com.weddingPlanner.model.TaskInfo;
import pl.com.weddingPlanner.view.enums.CategoryEnum;
import pl.com.weddingPlanner.view.enums.GuestTypeEnum;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
public class ContentItem extends ListItem implements Serializable {

    private String itemId;
    private String mainCaption;
//    private String subCaption;
    private String detailsId;

    private int mainCaptionColor;
//    private int subCaptionColor;
    private int leftIconId;
    private int leftIconColor;

    public static ContentItem of(TaskInfo info) {
        return ContentItem.builder()
                .itemId(String.valueOf(info.getItemId()))
                .mainCaption(info.getTitle())
                .mainCaptionColor(R.color.black)
                .leftIconId(R.drawable.ic_dashboard)
                .leftIconColor(R.color.gray_949494)
                .build();
    }

    public static ContentItem of(GuestInfo info) {
        return ContentItem.builder()
                .itemId(String.valueOf(info.getItemId()))
                .mainCaption(info.getName() + " " + info.getSurname())
                .mainCaptionColor(R.color.black)
                .leftIconId(getLeftIconId(info.getGuestType()))
                .leftIconColor(R.color.gray_949494)
                .build();
    }

    //TODO nie bazować na CategoryEnum
    private static int getLeftIconId(CategoryEnum category) {
        switch (category) {
            case MOST_IMPORTANT:
                return R.drawable.ic_star;
            case CEREMONY:
                return R.drawable.ic_dashboard;
            case WEDDING_HALL:
                return R.drawable.ic_home;
            case SUBCONTRACTORS:
                return R.drawable.ic_engineering;
            case STYLIZATION:
                return R.drawable.ic_style;
            case FORMAL_DOCUMENTS:
                return R.drawable.ic_assignment;
            default:
                return R.drawable.ic_dashboard;
        }
    }

    private static int getLeftIconId(GuestTypeEnum guestType) {
        switch (guestType) {
            case ACCOMPANYING:
                return R.drawable.ic_person_add;
            case GUEST:
            default:
                return R.drawable.ic_person;
        }
    }

    public String getName() {
        return mainCaption;
    }

    @Override
    public void setName(String name) {
        this.mainCaption = name;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj instanceof ContentItem) {
            ContentItem contentItem = (ContentItem) obj;
//            if (!contentItem.getDashboardItemType().equals(this.dashboardItemType)) return false;
//            if (!contentItem.getDetailsId().equals(this.detailsId)) return false;
//            if (contentItem.getItemId() == null && this.itemId == null) return true;
            return contentItem.getItemId().equals(this.itemId);
        }
        return false;
    }
}