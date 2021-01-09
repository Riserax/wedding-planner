package pl.com.weddingPlanner.view.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;

import static pl.com.weddingPlanner.validator.Constants.AMOUNT_COMMA_FIRST_PATTERN;
import static pl.com.weddingPlanner.validator.Constants.AMOUNT_FORMAT;
import static pl.com.weddingPlanner.validator.Constants.AMOUNT_PATTERN_FLOAT_ONE_DIGIT;
import static pl.com.weddingPlanner.validator.Constants.AMOUNT_PATTERN_HANGING_COMMA;
import static pl.com.weddingPlanner.validator.Constants.AMOUNT_PATTERN_INT;

public class FormatUtil {

    public static String convertToAmount(Object in) {
        String amount = "0,00";
        if (in != null && in.toString().length() != 0) {
            if (in instanceof BigDecimal) {
                amount = convertBigDecimalToString((BigDecimal)in);
            } else if (in instanceof Double) {
                amount = convertDoubleToString((Double)in);
            } else {
                amount = in.toString();
            }

            if (!isAmount(amount)) {
                return amount;
            } else {
                amount = (new StringBuffer(amount)).reverse().toString().replace(" ", "").replace(".", ",").replaceAll("([0-9]*,[0-9]{3}|[0-9]{3})(?=[0-9])", "$1 ").replaceAll("^,", "00,").replaceAll("^([0-9],)", "0$1");
                return (new StringBuffer(amount)).reverse().toString();
            }
        } else {
            return amount;
        }
    }

    public static String format(String amount) {
        if (amount.matches(AMOUNT_COMMA_FIRST_PATTERN)) {
            amount = "0" + amount;
        }
        if (amount.matches(AMOUNT_PATTERN_INT)) {
            amount += ",00";
        }
        if (amount.matches(AMOUNT_PATTERN_HANGING_COMMA)) {
            amount += "00";
        }
        if (amount.matches(AMOUNT_PATTERN_FLOAT_ONE_DIGIT)) {
            amount += "0";
        }

        amount = amount.replaceFirst("^0+(?!$|,[0-9]{0,2})", "");
        return amount;
    }

    public static String convertBigDecimalToString(BigDecimal d) {
        return d == null ? (new DecimalFormat(AMOUNT_FORMAT)).format(0L) : (new DecimalFormat(AMOUNT_FORMAT)).format(d);
    }

    public static String convertDoubleToString(double d) {
        return (new DecimalFormat(AMOUNT_FORMAT)).format(d);
    }

    public static BigDecimal convertStringToBigDecimal(String s) {
        if (s == null) {
            return new BigDecimal(0);
        } else {
            s = (isNegative(s) ? "-0" : "0") + s.replace(",", ".").replaceAll("[^0-9\\.]", "").replaceAll("\\.([0-9]*(?=\\.))", "$1");
            return new BigDecimal(s);
        }
    }

    public static boolean isAmount(String kwota) {
        return kwota != null && kwota.trim().replace(" ", "").matches("^[-]?[0-9]+([.,][0-9]{1,2})?$");
    }

    public static boolean isNegative(Object s) {
        return s != null && s.toString().replaceAll("[^0-9\\.-]", "").matches("^-.*");
    }
}
