package com.blockchain.wallet.api.exception;

/**
 *
 * @author Administrator
 * @date 2018/6/28
 */
public class BizException extends RuntimeException {

    private static final long serialVersionUID = 7430997148636073733L;

//    /**
//     * 数据库操作,insert返回0
//     */
//    public static final BizException DB_INSERT_RESULT_0 = new BizException(
//            10040001, "数据库操作,insert返回0");
//
//    /**
//     * 数据库操作,update返回0
//     */
//    public static final BizException DB_UPDATE_RESULT_0 = new BizException(
//            10040002, "数据库操作,update返回0");
//
//    /**
//     * 数据库操作,selectOne返回null
//     */
//    public static final BizException DB_SELECTONE_IS_NULL = new BizException(
//            10040003, "数据库操作,selectOne返回null");
//
//    /**
//     * 数据库操作,list返回null
//     */
//    public static final BizException DB_LIST_IS_NULL = new BizException(
//            10040004, "数据库操作,list返回null");

    /**
     * Token 验证不通过
     */
    public static final BizException TOKEN_IS_ILLICIT = new BizException(
            10040005, "Token 验证非法");
    /**
     * 会话超时　获取session时，如果是空，throws 下面这个异常 拦截器会拦截爆会话超时页面
     */
    public static final BizException SESSION_IS_OUT_TIME = new BizException(
            10040006, "会话超时");

    /**
     * 生成序列异常时
     */
    public static final BizException DB_GET_SEQ_NEXT_VALUE_ERROR = new BizException(
            10040007, "序列生成超时");


    public static final BizException DB_OPERATION_ERROR = new BizException(
            1000);


    public static final BizException ACCOUNT_VALIDATE_ERROR = new BizException(
            20010001);


    public static final BizException ACCOUNT_IS_NOT_EXSITS = new BizException(
            20010001, "账户不存在，数据完整性校验不通过");

    /**
     * 异常信息
     */
    protected String message;

    /**
     * 具体异常码
     */
    protected int code;

    public BizException(int code, String msgFormat, Object... args) {
        super(String.format(msgFormat, args));
        this.code = code;
        this.message = String.format(msgFormat, args);
    }

    public BizException(int code) {
        this.code = code;
    }

    public BizException() {
        super();
    }

    public BizException(String message, Throwable cause) {
        super(message, cause);
    }

    public BizException(Throwable cause) {
        super(cause);
    }

    public BizException(String message) {
        super(message);
    }

    @Override
    public String getMessage() {
        return message;
    }

    public int getCode() {
        return code;
    }


    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * 实例化异常
     *
     * @param msgFormat
     * @param args
     * @return
     */
    public BizException newInstance(String msgFormat, Object... args) {
        return new BizException(this.code, msgFormat, args);
    }

}
