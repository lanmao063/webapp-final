package com.neu.webapp.exception;


public class BusinessException extends RuntimeException {

    private final int code;

    public BusinessException(int code, String message) {
        super(message);
        this.code = code;
    }
    //自定义错误码
    public BusinessException(String message) {
        this(400, message);
    }
    //默认400错误码
    public int getCode() {
        return code;
    }
}
//这是一个自定义业务异常类，继承自RuntimeException，处理业务逻辑违规情况
