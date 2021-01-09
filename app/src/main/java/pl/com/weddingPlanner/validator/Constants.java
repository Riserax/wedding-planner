package pl.com.weddingPlanner.validator;

public class Constants {

    public static final String AMOUNT_FORMAT = "0.00";
    public static final String AMOUNT_FORMAT_REGEX_WHEN_TYPING = "([0-9]{0,10}|[0-9]{1,3}( [0-9]{3})+)([,.][0-9]{0,2})?";
    public static final String INTEGER_AMOUNT_FORMAT_REGEX_WHEN_TYPING = "([0-9]{0,10}|[0-9]{1,3}( [0-9]{3})+)";
    public static final String AMOUNT_FORMAT_REGEX_FINISHED_TYPING = "([0-9]{1,3}( [0-9]{3}){0,3})[,.][0-9]{2}";
    public static final String INTEGER_AMOUNT_FORMAT_REGEX_FINISHED_TYPING = "[0-9]{1,3}( [0-9]{3}){0,3}";
    public static final String AMOUNT_COMMA_FIRST_PATTERN = "[,.][0-9]{0,2}";
    public static final String AMOUNT_PATTERN_INT = "[0-9]{1,10}";
    public static final String AMOUNT_PATTERN_HANGING_COMMA = "[0-9]{1,10}[,.]";
    public static final String AMOUNT_PATTERN_FLOAT_ONE_DIGIT = "[0-9]{1,10}[,.][0-9]";
    public static final String AMOUNT_PATTERN_FLOAT_COMPLETE = "[0-9]{1,10}[,.][0-9]{2}";
}
