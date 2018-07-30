package com.blockchain.wallet.api.exception;

import com.alibaba.dubbo.rpc.RpcException;
import com.blockchain.wallet.api.WalletApiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

/**
 *
 * @author shujun
 * @date 2018/6/28
 */
@RestControllerAdvice
@Slf4j
public class GlobalControllerExceptionHandler {

    @ExceptionHandler(value = { BizException.class })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrorResponse biz(BizException ex) {
        return new ApiErrorResponse(500, 5001, ex.getMessage());
    }

    @ExceptionHandler(value = { ParamterValidateException.class })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrorResponse pve(ParamterValidateException ex) {
        return new ApiErrorResponse(400, 4001, ex.getMessage());
    }

    @ExceptionHandler(value = { WalletApiException.class })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrorResponse walletApiException(WalletApiException ex) {
        return new ApiErrorResponse(400, 4001, ex.getMessage());
    }

    @ExceptionHandler(value = { NoHandlerFoundException.class })
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiErrorResponse noHandlerFoundException(Exception ex) {
        return new ApiErrorResponse(404, 4041, ex.getMessage());
    }

    @ExceptionHandler(value = { RpcException.class })
    @ResponseStatus(HttpStatus.GATEWAY_TIMEOUT)
    public ApiErrorResponse rpcException(Exception ex) {
        log.error(ex.getMessage());
        return new ApiErrorResponse(504, 50401, "rpc invoke error ");
    }


    @ExceptionHandler(value = { Exception.class })
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiErrorResponse unknownException(Exception ex) {
        return new ApiErrorResponse(405, 4005, ex.getMessage());
    }
}
