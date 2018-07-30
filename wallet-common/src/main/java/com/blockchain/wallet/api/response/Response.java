package com.blockchain.wallet.api.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;

/**
 *
 * @author shujun
 * @date 2018/6/26
 */
@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Response<T> implements Serializable {

    private static final long serialVersionUID = -951966605600789450L;
    public Response() {}
    protected int code;
    protected String message;
    protected T data;

    public String toString() {
        return "Response(code=" + getCode() + ", message=" + getMessage() + ", data=" + getData() + ")";
    }

    public boolean succeed() {
        return getCode() == ResponseMessage.SUCCESS.getCode();
    }

    public boolean failed() {
        return getCode() != ResponseMessage.SUCCESS.getCode();
    }

    private Response(int code, String msg) {
        this.code = code;
        this.message = msg;
    }

    private Response(ResponseMessageInterface msg) {
        this.code = msg.getCode();
        this.message = msg.getMessage();
    }

    private Response(ResponseMessageInterface msg, T data) {
        this.code = msg.getCode();
        this.message = msg.getMessage();
        this.data = data;
    }

    public static <T> Response<T> success() {
        return new Response(ResponseMessage.SUCCESS);
    }

    public static <T> Response<T> success(T body) {
        return new Response(ResponseMessage.SUCCESS, body);
    }

    public static <T> Response<T> error() {
        return new Response(ResponseMessage.ERROR);
    }

    public static <T> Response<T> error(String msg) {
        return new Response(ResponseMessage.ERROR.getCode(), msg);
    }

    public static <T> Response<T> error(ResponseMessageInterface msg, T data) {
        return new Response(msg, data);
    }

    public static <T> Response<T> error(ResponseMessageInterface msg) {
        return new Response(msg);
    }

}
