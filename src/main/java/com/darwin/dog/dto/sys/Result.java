package com.darwin.dog.dto.sys;

import com.darwin.dog.exception.CommonException;
import com.darwin.dog.util.GlobalStaticBean;
import lombok.Data;

import java.io.Serializable;


@Data
public abstract class Result implements Serializable {
    protected boolean success;
    public Result(boolean success){
        setSuccess(success);
    }

    public String toJson(){
        return GlobalStaticBean.GSON.toJson(this);
    }

    private static class SuccessResult extends Result{
        Object content;

        public SuccessResult(Object content) {
            super(true);
            this.content = content;
        }

        public Object getContent() {
            return content;
        }

        public void setContent(Object content) {
            this.content = content;
        }
    }
    private static class FailureResult extends Result{
        private long errorCode;
        // 主消息
        private String message;
        // 附加信息对象
        private Object additionalInfo;

        public FailureResult(long errorCode, String message, Object additionalInfo) {
            super(false);
            this.errorCode = errorCode;
            this.message = message;
            this.additionalInfo = additionalInfo;
        }

        public long getErrorCode() {
            return errorCode;
        }

        public void setErrorCode(long errorCode) {
            this.errorCode = errorCode;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public Object getAdditionalInfo() {
            return additionalInfo;
        }

        public void setAdditionalInfo(Object additionalInfo) {
            this.additionalInfo = additionalInfo;
        }
    }

    /**
     * 表示过程的成功，对实际结果的是无关的，只要未抛出异常就是 success
     * @param data 返回数据
     * @return Result
     */
    public static Result success(Object data){
        return new SuccessResult(data);
    }

    /**
     * 表示过程异常
     * @param exception 运行异常
     * @return Result
     */
    public static Result fail(CommonException exception){
        return new FailureResult(
                exception.getFullErrorCode()
                ,exception.getMessage()
                ,exception.getAdditionalInfo());
    }

}
