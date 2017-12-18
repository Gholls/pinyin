package com.gholl.pinyin;

/**
 * 异常类
 *
 * @author stuxuhai (dczxxuhai@gmail.com)
 */
public class PinyinException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private String message;

    public PinyinException() {
        super();
    }

    public PinyinException(final String message) {
        super(message);
    }

    public PinyinException(final Exception e) {
        super(e);
    }

    public PinyinException(Throwable cause) {
        super(cause);
    }

    public PinyinException(final String message, final Throwable cause) {
        super(message, cause);
    }

    @Override
    public String getMessage() {
        return this.message == null ? super.getMessage() : this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return this.message;
    }

}
