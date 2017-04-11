package sivan.yue.nlp.common.exception;

/**
 * Created by xiwen.yxw on 2017/3/22.
 */
public class MatrixErrException extends IllegalStateException{
    public MatrixErrException(String message) {
        super(message);
    }
    public MatrixErrException(String message, Throwable cause) {
        super(message, cause);
    }
}
