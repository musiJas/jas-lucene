package cn.begonia.lucene.jaslucene.famatter.exception;

/**
 * @author begonia_chen
 * @data 2020/8/18 17:39
 * @description 参数为空异常
 **/
public class ArgumentIsNullException extends RuntimeException {

    public ArgumentIsNullException() {
    }

    public ArgumentIsNullException(String message) {
        super(message);
    }

    public ArgumentIsNullException(String message, Throwable cause) {
        super(message, cause);
    }

    public ArgumentIsNullException(Throwable cause) {
        super(cause);
    }

    public ArgumentIsNullException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
