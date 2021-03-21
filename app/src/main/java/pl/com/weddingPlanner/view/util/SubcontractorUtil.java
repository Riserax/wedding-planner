package pl.com.weddingPlanner.view.util;

import android.content.Context;

import org.apache.commons.lang3.StringUtils;

import pl.com.weddingPlanner.databinding.ActivityNewSubcontractorBinding;
import pl.com.weddingPlanner.enums.CollaborationStageEnum;
import pl.com.weddingPlanner.persistence.entity.Subcontractor;

public class SubcontractorUtil {

    public static void setSelectedCollaborationStage(CollaborationStageEnum collaborationStage, ActivityNewSubcontractorBinding binding, Context context) {
        switch (collaborationStage) {
            case CONSIDERING:
                setConsideringButtonsSelection(binding, context);
                break;
            case IN_CONTACT:
                setInContactButtonsSelection(binding, context);
                break;
            case CONFIRMED:
                setConfirmedButtonsSelection(binding, context);
                break;
            case PAID:
                setPaidButtonsSelection(binding, context);
        }
    }

    public static void setConsideringButtonsSelection(ActivityNewSubcontractorBinding binding, Context context) {
        ButtonsUtil.setButtonSelection(binding.consideringButton, context, true);
        ButtonsUtil.setButtonSelection(binding.inContactButton, context, false);
        ButtonsUtil.setButtonSelection(binding.confirmedButton, context, false);
        ButtonsUtil.setButtonSelection(binding.paidButton, context, false);
    }

    public static void setInContactButtonsSelection(ActivityNewSubcontractorBinding binding, Context context) {
        ButtonsUtil.setButtonSelection(binding.consideringButton, context, false);
        ButtonsUtil.setButtonSelection(binding.inContactButton, context, true);
        ButtonsUtil.setButtonSelection(binding.confirmedButton, context, false);
        ButtonsUtil.setButtonSelection(binding.paidButton, context, false);
    }

    public static void setConfirmedButtonsSelection(ActivityNewSubcontractorBinding binding, Context context) {
        ButtonsUtil.setButtonSelection(binding.consideringButton, context, false);
        ButtonsUtil.setButtonSelection(binding.inContactButton, context, false);
        ButtonsUtil.setButtonSelection(binding.confirmedButton, context, true);
        ButtonsUtil.setButtonSelection(binding.paidButton, context, false);
    }

    public static void setPaidButtonsSelection(ActivityNewSubcontractorBinding binding, Context context) {
        ButtonsUtil.setButtonSelection(binding.consideringButton, context, false);
        ButtonsUtil.setButtonSelection(binding.inContactButton, context, false);
        ButtonsUtil.setButtonSelection(binding.confirmedButton, context, false);
        ButtonsUtil.setButtonSelection(binding.paidButton, context, true);
    }

    public static void setWebsiteField(Subcontractor subcontractor, ActivityNewSubcontractorBinding binding) {
        String websiteAnchor = subcontractor.getWebsite();

        if (StringUtils.isNotBlank(websiteAnchor)) {
            int startIndex = websiteAnchor.indexOf(">");
            int endIndex = websiteAnchor.lastIndexOf("<");
            String website = websiteAnchor.substring(startIndex + 1, endIndex);

            binding.website.setText(website);
        }
    }
}
