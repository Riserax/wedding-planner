package pl.com.weddingPlanner.exception;

public class EnumValueNotFoundException extends Exception {
    private static final String MESSAGE = "Nie odnaleziono wartosci parametru";

    public EnumValueNotFoundException() {
        super(MESSAGE);
    }

    public EnumValueNotFoundException(String param) {
        super(MESSAGE + " " + param);
    }

    public static String getMESSAGE() {
        return MESSAGE;
    }
}
