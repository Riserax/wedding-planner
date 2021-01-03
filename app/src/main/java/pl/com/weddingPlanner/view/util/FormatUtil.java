package pl.com.weddingPlanner.view.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public class FormatUtil {

    public static final String AMOUNT_FORMAT = "0.00";

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

    public static String convertBigDecimalToString(BigDecimal d) {
        return d == null ? (new DecimalFormat(AMOUNT_FORMAT)).format(0L) : (new DecimalFormat(AMOUNT_FORMAT)).format(d);
    }

    public static String convertDoubleToString(double d) {
        return (new DecimalFormat(AMOUNT_FORMAT)).format(d);
    }

    public static boolean isAmount(String kwota) {
        return kwota != null && kwota.trim().replace(" ", "").matches("^[-]?[0-9]+([.,][0-9]{1,2})?$");
    }
}
