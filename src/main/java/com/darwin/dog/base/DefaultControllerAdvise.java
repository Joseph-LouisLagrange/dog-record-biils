package com.darwin.dog.base;


import com.darwin.dog.annotation.NoDto;
import com.darwin.dog.dto.sys.Result;
import com.darwin.dog.exception.CommonException;
import lombok.NonNull;
import org.apache.commons.lang3.ClassUtils;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.beans.PropertyEditorSupport;
import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@ControllerAdvice
public class DefaultControllerAdvise implements ResponseBodyAdvice<Object>, RequestBodyAdvice {


    static final String controllerPath = "com.darwin.dog.controller";


    /**
     * 对全局异常的处理
     *
     * @param e 异常对象
     * @return 数据传输的结果对象
     */
    @ResponseBody
    @ExceptionHandler(Exception.class)
    public Result exceptionHandler(Exception e) throws Exception {
        if (ClassUtils.isAssignable(CommonException.class, e.getClass()))
            return Result.fail((CommonException) e);
        throw e;
    }


    /**
     * 设定完全支持
     *
     * @param methodParameter
     * @param aClass
     * @return
     */
    @Override
    public boolean supports(@NonNull MethodParameter returnType, @NonNull Class<? extends HttpMessageConverter<?>> converterType) {
        Class<?> parameterType = returnType.getParameterType();
        return !parameterType.isAssignableFrom(Void.TYPE)
                && !parameterType.isAnnotationPresent(NoDto.class);
    }

    /**
     * 把数据转化为传输对象
     *
     * @param o
     * @param methodParameter
     * @param mediaType
     * @param aClass
     * @param serverHttpRequest
     * @param serverHttpResponse
     * @return
     */
    @Override
    public Object beforeBodyWrite(Object o, MethodParameter methodParameter, @NonNull MediaType mediaType, @NonNull Class aClass, @NonNull ServerHttpRequest serverHttpRequest, @NonNull ServerHttpResponse serverHttpResponse) {
        Package p = methodParameter.getDeclaringClass().getPackage();
        if (!methodParameter.hasMethodAnnotation(NoDto.class)
                && p.getName().startsWith(controllerPath)) {
            Result result = Result.success(o);
            if (o == null || ClassUtils.isAssignable(String.class, o.getClass())) {
                return result.toJson();
            }
            if (!ClassUtils.isAssignable(Result.class, o.getClass()))
                return result;
        }
        return o;
    }

    @Override
    public boolean supports(@NonNull MethodParameter methodParameter, @NonNull Type targetType, @NonNull Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public @NonNull HttpInputMessage beforeBodyRead(@NonNull HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) throws IOException {
        return inputMessage;
    }

    @Override
    public @NonNull Object afterBodyRead(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        return body;
    }

    @Override
    public Object handleEmptyBody(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        return body;
    }


    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(LocalDate.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) throws IllegalArgumentException {
                setValue(LocalDate.parse(text, DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            }
        });
        binder.registerCustomEditor(LocalDateTime.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) throws IllegalArgumentException {
                setValue(LocalDateTime.parse(text, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            }
        });
        binder.registerCustomEditor(LocalTime.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) throws IllegalArgumentException {
                setValue(LocalTime.parse(text, DateTimeFormatter.ofPattern("HH:mm:ss")));
            }
        });
    }
}