package pl.com.weddingPlanner.view.list;

import androidx.annotation.Nullable;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import pl.com.weddingPlanner.R;
import pl.com.weddingPlanner.model.info.ExpenseInfo;
import pl.com.weddingPlanner.model.info.GuestInfo;
import pl.com.weddingPlanner.model.info.PaymentInfo;
import pl.com.weddingPlanner.model.info.SubcontractorInfo;
import pl.com.weddingPlanner.model.info.TaskInfo;
import pl.com.weddingPlanner.enums.GuestTypeEnum;
import pl.com.weddingPlanner.enums.StateEnum;
import pl.com.weddingPlanner.view.util.FormatUtil;
import pl.com.weddingPlanner.view.util.ResourceUtil;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
public class ContentItem extends ListItem implements Serializable {

    private int itemId;

    private String mainCaption;
    private String subCaption;
    private String rightIconCaption;

    private int mainCaptionColor;
    private int subCaptionColor;
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
                .mainCaption(info.getNameSurname())
                .mainCaptionColor(R.color.black)
                .leftIconId(getLeftIconId(info.getType()))
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
                .subCaption(FormatUtil.convertToAmount(info.getAmount()))
                .subCaptionColor(getSubCaptionColor(info.getState()))
                .leftIconId(ResourceUtil.getResId(info.getState().getIconCode(), R.drawable.class))
                .leftIconColor(getLeftIconColor(info.getState()))
                .rightIconCaption(info.getPayer())
                .build();
    }

    public static ContentItem of(SubcontractorInfo info) {
        return ContentItem.builder()
                .itemId(info.getItemId())
                .mainCaption(info.getName())
                .mainCaptionColor(R.color.black)
                .leftIconId(ResourceUtil.getResId(info.getCategoryIconId(), R.drawable.class))
                .leftIconColor(R.color.colorPrimaryDark)
                .build();
    }

    private static int getLeftIconId(GuestTypeEnum guestType) {
        switch (guestType) {
            case ACCOMPANY:
                return R.drawable.ic_person_add;
            case GUEST:
            default:
                return R.drawable.ic_person;
        }
    }

    private static int getLeftIconColor(StateEnum stateEnum) {
        switch (stateEnum) {
            case PENDING:
                return R.color.colorPrimaryDark;
            case PAID:
            default:
                return R.color.gray_949494;
        }
    }

    private static int getSubCaptionColor(StateEnum stateEnum) {
        switch (stateEnum) {
            case PENDING:
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