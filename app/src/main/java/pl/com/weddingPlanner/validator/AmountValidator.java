package pl.com.weddingPlanner.validator;

import java.math.BigDecimal;

import pl.com.weddingPlanner.R;
import pl.com.weddingPlanner.view.BaseActivity;
import pl.com.weddingPlanner.view.util.FormatUtil;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static pl.com.weddingPlanner.validator.Constants.AMOUNT_COMMA_FIRST_PATTERN;
import static pl.com.weddingPlanner.validator.Constants.AMOUNT_FORMAT_REGEX_FINISHED_TYPING;
import static pl.com.weddingPlanner.validator.Constants.AMOUNT_PATTERN_FLOAT_COMPLETE;
import static pl.com.weddingPlanner.validator.Constants.AMOUNT_PATTERN_FLOAT_ONE_DIGIT;
import static pl.com.weddingPlanner.validator.Constants.AMOUNT_PATTERN_HANGING_COMMA;
import static pl.com.weddingPlanner.validator.Constants.AMOUNT_PATTERN_INT;

public class AmountValidator extends AbstractValidator {

    private final boolean allowEmpty;

    public AmountValidator(BaseActivity activity, boolean allowEmpty) {
        errorMsg = activity.getResources().getString(R.string.amount_invalid_format);
        this.allowEmpty = allowEmpty;
    }

    @Override
    public boolean isValid(String input) {
        BigDecimal amount = FormatUtil.convertStringToBigDecimal(input);

        if (allowEmpty && (isBlank(input) || amount.compareTo(BigDecimal.ZERO) == 0)
                && !input.equals(",")) return true;

        return (input.matches(AMOUNT_FORMAT_REGEX_FINISHED_TYPING) || input.matches(AMOUNT_PATTERN_FLOAT_COMPLETE)
                || input.matches(AMOUNT_PATTERN_FLOAT_ONE_DIGIT) || input.matches(AMOUNT_PATTERN_HANGING_COMMA)
                || input.matches(AMOUNT_COMMA_FIRST_PATTERN) || input.matches(AMOUNT_PATTERN_INT))
                && amount.compareTo(BigDecimal.ZERO) > 0;
    }
}
