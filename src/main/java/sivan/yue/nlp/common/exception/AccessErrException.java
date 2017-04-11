package sivan.yue.nlp.common.exception;

/**
 * Created by xiwen.yxw on 2017/3/22.
 */
public class AccessErrException extends IllegalStateException{
    public AccessErrException(String message) {
        super(message);
    }
    public AccessErrException(String message, Throwable cause) {
        super(message, cause);
    }
}
