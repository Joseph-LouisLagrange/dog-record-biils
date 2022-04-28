package com.darwin.dog.exception;

/**
 * 基本的错误类型
 */
public enum BaseExceptionType {
    MISS(1L <<31)   // 未命中 一般用于查数据为空的情况
    , ABNORMAL_STATUS(1L<<32)   // 状态异常
    ,FUCK(1L<<33);
    private long bit;
    BaseExceptionType(long bit){
        this.bit = bit;
    }

    public long getBitCode() {
        return bit;
    }
}
