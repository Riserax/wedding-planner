package pl.com.weddingPlanner.view.list;

import androidx.annotation.Nullable;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import pl.com.weddingPlanner.R;
import pl.com.weddingPlanner.model.ExpenseInfo;
import pl.com.weddingPlanner.model.GuestInfo;
import pl.com.weddingPlanner.model.PaymentInfo;
import pl.com.weddingPlanner.model.TaskInfo;
import pl.com.weddingPlanner.view.enums.GuestTypeEnum;
import pl.com.weddingPlanner.view.enums.StateEnum;
import pl.com.weddingPlanner.view.util.ResourceUtil;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
public class ContentItem extends ListItem implements Serializable {

    private int itemId;
    private String mainCaption;
//    private String subCaption;

    private int mainCaptionColor;
//    private int subCaptionColor;
    private int leftIconId;
    private int leftIconColor;

    public static ContentItem of(TaskInfo info) {
        return ContentItem.builder()
                .itemId(info.getItemId())
                .mainCaption(info.getTitle())
                .mainCaptionColor(R.color.black)
                .leftIconId(ResourceUtil.getResId(info.getCategoryIconId(), R.drawable.class))
                .leftIconColor(R.color.colorPrimaryDark)
                .build();
    }

    public static ContentItem of(GuestInfo info) {
        return ContentItem.builder()
                .itemId(info.getItemId())
                .mainCaption(info.getName() + " " + info.getSurname())
                .mainCaptionColor(R.color.black)
                .leftIconId(getLeftIconId(info.getGuestType()))
                .leftIconColor(R.color.colorPrimaryDark)
                .build();
    }

    public static ContentItem of(ExpenseInfo info) {
        return ContentItem.builder()
                .itemId(info.getItemId())
                .mainCaption(info.getTitle())
                .mainCaptionColor(R.color.black)
                .leftIconId(ResourceUtil.getResId(info.getCategoryIconId(), R.drawable.class))
                .leftIconColor(R.color.colorPrimaryDark)
                .build();
    }

    public static ContentItem of(PaymentInfo info) {
        return ContentItem.builder()
                .itemId(info.getItemId())
                .mainCaption(info.getTitle())
                .mainCaptionColor(R.color.black)
                .leftIconId(ResourceUtil.getResId(info.getState().getIconCode(), R.drawable.class))
                .leftIconColor(getLeftIconColor(info.getState()))
                .build();
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

    private static int getLeftIconColor(StateEnum stateEnum) {
        switch (stateEnum) {
            case AWAITING:
                return R.color.colorPrimaryDark;
            case PAID:
            default:
                return R.color.gray_949494;
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
            return contentItem.getItemId() == this.itemId;
        }
        return false;
    }
}