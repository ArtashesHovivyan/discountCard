package am.smarket.discountcardappcommon.exeption;

public class UnknownHostException extends Exception {
    public UnknownHostException() {
    }

    public UnknownHostException(String message) {
        super(message);
    }

    public UnknownHostException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnknownHostException(Throwable cause) {
        super(cause);
    }

    public UnknownHostException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
