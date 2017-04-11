package sivan.yue.nlp.common.exception;

/**
 * Created by xiwen.yxw on 2017/4/1.
 */
public class ViterbiErrException extends IllegalStateException {
    public ViterbiErrException(String message) {
        super(message);
    }
    public ViterbiErrException(String message, Throwable cause) {
        super(message, cause);
    }
}
