package com.darwin.dog.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 通用的异常
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class CommonException extends RuntimeException{
    // 基本错误类型
    transient private BaseExceptionType baseExceptionType;
    // 异常码 (完整的)
    private long fullErrorCode;
    // 附加信息对象
    private Object additionalInfo;

    public CommonException(BaseExceptionType baseExceptionType,
                           int objectCode,
                           String message,
                           Object additionalInfo){
        super(message);
        this.additionalInfo = additionalInfo;
        this.fullErrorCode = baseExceptionType.getBitCode() | objectCode;
        this.baseExceptionType= baseExceptionType;
    }

    public static CommonException of(BaseExceptionType baseExceptionType,
                                     int objectCode,
                                     String message,
                                     Object additionalInfo){
        return new CommonException(baseExceptionType,objectCode,message,additionalInfo);
    }

    public static CommonException of(BaseExceptionType baseExceptionType,
                                     int objectCode,
                                     String message){
        return of(baseExceptionType, objectCode, message,null);
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
