package pl.com.weddingPlanner.view.util;

import android.content.Context;

import pl.com.weddingPlanner.databinding.ActivityNewSubcontractorBinding;

public class SubcontractorUtil {

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
}
