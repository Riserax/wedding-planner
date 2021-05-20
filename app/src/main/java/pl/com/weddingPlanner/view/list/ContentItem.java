package pl.com.weddingPlanner.view.list;

import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import pl.com.weddingPlanner.R;
import pl.com.weddingPlanner.enums.ContentItemState;
import pl.com.weddingPlanner.enums.TaskStatus;
import pl.com.weddingPlanner.model.info.ExpenseInfo;
import pl.com.weddingPlanner.model.info.GuestInfo;
import pl.com.weddingPlanner.model.info.MyWeddingInfo;
import pl.com.weddingPlanner.model.info.PaymentInfo;
import pl.com.weddingPlanner.model.info.SubcontractorInfo;
import pl.com.weddingPlanner.model.info.TaskInfo;
import pl.com.weddingPlanner.enums.GuestType;
import pl.com.weddingPlanner.enums.PaymentState;
import pl.com.weddingPlanner.view.util.FormatUtil;
import pl.com.weddingPlanner.view.util.ResourceUtil;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
public class ContentItem extends ListItem implements Serializable {

    private String itemUUID;
    private String mainCaption;
    private String subCaption;

    private int itemId;
    private int mainCaptionColor;
    private int subCaptionColor;
    private int leftIconId;
    private int leftIconColor;

    private ContentItemState state;

    private LinearLayout topLayout;
    private LinearLayout rightLayout;

    public static ContentItem of(TaskInfo info) {
        return ContentItem.builder()
                .itemId(info.getItemId())
                .mainCaption(info.getTitle())
                .mainCaptionColor(R.color.black)
                .leftIconId(ResourceUtil.getResId(info.getCategoryIconId(), R.drawable.class))
                .leftIconColor(getLeftIconColor(info.getStatus()))
                .state(getItemState(info.getStatus()))
                .topLayout(info.getBookmarksLayout())
                .rightLayout(info.getAssigneesLayout())
                .build();
    }

    public static ContentItem of(GuestInfo info) {
        return ContentItem.builder()
                .itemId(info.getItemId())
                .mainCaption(info.getNameSurname())
                .mainCaptionColor(R.color.black)
                .leftIconId(getLeftIconId(info.getType()))
                .leftIconColor(R.color.colorPrimaryDark)
                .rightLayout(info.getTablePresenceLayout())
                .build();
    }

    public static ContentItem of(ExpenseInfo info) {
        return ContentItem.builder()
                .itemId(info.getItemId())
                .mainCaption(info.getTitle())
                .mainCaptionColor(R.color.black)
                .subCaption(FormatUtil.convertToAmount(info.getAmount()))
                .subCaptionColor(R.color.colorPrimaryDark)
                .leftIconId(ResourceUtil.getResId(info.getCategoryIconId(), R.drawable.class))
                .leftIconColor(R.color.colorPrimaryDark)
                .rightLayout(info.getPayersLayout())
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
                .rightLayout(info.getPayerLayout())
                .state(getItemState(info.getState()))
                .build();
    }

    public static ContentItem of(SubcontractorInfo info) {
        return ContentItem.builder()
                .itemId(info.getItemId())
                .mainCaption(info.getName())
                .mainCaptionColor(R.color.black)
                .leftIconId(ResourceUtil.getResId(info.getCategoryIconId(), R.drawable.class))
                .leftIconColor(R.color.colorPrimaryDark)
                .rightLayout(info.getStagePaymentsLayout())
                .build();
    }

    public static ContentItem of(MyWeddingInfo info) {
        return ContentItem.builder()
                .itemUUID(info.getId())
                .mainCaption(info.getName())
                .mainCaptionColor(R.color.black)
                .leftIconId(0)
                .build();
    }

    private static int getLeftIconId(GuestType guestType) {
        switch (guestType) {
            case ACCOMPANY:
                return R.drawable.ic_person_add;
            case GUEST:
            default:
                return R.drawable.ic_person;
        }
    }

    private static int getLeftIconColor(PaymentState paymentState) {
        switch (paymentState) {
            case PAID:
                return R.color.gray_949494;
            case PENDING:
            default:
                return R.color.colorPrimaryDark;
        }
    }

    private static int getLeftIconColor(TaskStatus taskStatus) {
        switch (taskStatus) {
            case DONE:
                return R.color.gray_949494;
            case NEW:
            case IN_PROGRESS:
            default:
                return R.color.colorPrimaryDark;
        }
    }

    private static int getSubCaptionColor(PaymentState paymentState) {
        switch (paymentState) {
            case PENDING:
                return R.color.colorPrimaryDark;
            case PAID:
            default:
                return R.color.gray_949494;
        }
    }

    private static ContentItemState getItemState(PaymentState paymentState) {
        switch (paymentState) {
            case PAID:
                return ContentItemState.DONE;
            case PENDING:
            default:
                return ContentItemState.NORMAL;
        }
    }

    private static ContentItemState getItemState(TaskStatus taskStatus) {
        switch (taskStatus) {
            case DONE:
                return ContentItemState.DONE;
            case NEW:
            case IN_PROGRESS:
            default:
                return ContentItemState.NORMAL;
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