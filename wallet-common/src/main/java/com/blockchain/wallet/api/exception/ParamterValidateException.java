package com.blockchain.wallet.api.exception;

/**
 *
 * @author shujun
 * @date 2018/6/28
 */
public class ParamterValidateException extends RuntimeException {

    private static final long serialVersionUID = -2952132794097588379L;
    /**
     * 异常信息
     */
    protected String message;

    /**
     * 具体异常码
     */
    protected int code;

    public ParamterValidateException(int code, String msgFormat, Object... args) {
        super(String.format(msgFormat, args));
        this.code = code;
        this.message = String.format(msgFormat, args);
    }

    public ParamterValidateException() {
        super();
    }

    public ParamterValidateException(String message, Throwable cause) {
        super(message, cause);
    }

    public ParamterValidateException(Throwable cause) {
        super(cause);
    }

    public ParamterValidateException(String message) {
        super(message);
    }

    @Override
    public String getMessage() {
        return message;
    }

    public int getCode() {
        return code;
    }

    /**
     * 实例化异常
     *
     * @param msgFormat
     * @param args
     * @return
     */
    public ParamterValidateException newInstance(String msgFormat, Object... args) {
        return new ParamterValidateException(this.code, msgFormat, args);
    }

}
