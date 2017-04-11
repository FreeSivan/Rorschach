package sivan.yue.nlp.common.exception;

/**
 * Created by xiwen.yxw on 2017/3/27.
 */
public class StructErrException extends IllegalStateException{
    public StructErrException(String message) {
        super(message);
    }
    public StructErrException(String message, Throwable cause) {
        super(message, cause);
    }
}
