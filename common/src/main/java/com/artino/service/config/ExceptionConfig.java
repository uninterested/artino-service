package com.artino.service.config;

import com.artino.service.base.BusinessException;
import com.artino.service.base.R;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.servlet.NoHandlerFoundException;

@ControllerAdvice
public class ExceptionConfig {
    @ResponseBody
    @ExceptionHandler(value = {NoHandlerFoundException.class, HttpRequestMethodNotSupportedException.class})
    public R<?> notFound() {
        return R.error(404, "无效的请求");
    }

    @ResponseBody
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public R<?> invalidParams() {
        return R.error(0, "请检查参数");
    }

    @ResponseBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public R<?> initializedException(MethodArgumentNotValidException ex) {
        BindingResult bindingResult = ex.getBindingResult();
        if (bindingResult.hasErrors()) {
            ObjectError first = bindingResult.getAllErrors().get(0);
            return R.error(0, first.getDefaultMessage());
        }
        return R.error(0, "参数错误");
    }

    @ResponseBody
    @ExceptionHandler(BusinessException.class)
    public R<?> businessException(BusinessException ex) {
        return R.error(ex.getCode(), ex.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(Exception.class)
    public R<?> serverException(Exception ex) {
        ex.printStackTrace();
        return R.error(500, "服务端异常，请重试");
    }
}