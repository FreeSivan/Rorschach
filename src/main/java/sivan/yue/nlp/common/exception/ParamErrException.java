package sivan.yue.nlp.common.exception;

/**
 * Created by xiwen.yxw on 2017/3/22.
 */
public class ParamErrException extends IllegalStateException{
    public ParamErrException(String message) {
        super(message);
    }
    public ParamErrException(String message, Throwable cause) {
        super(message, cause);
    }
}
