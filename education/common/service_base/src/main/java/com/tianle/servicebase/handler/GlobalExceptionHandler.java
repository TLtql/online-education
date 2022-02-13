package com.tianle.servicebase.handler;

import com.tianle.commonutils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Auther: Tianle
 * @Description: 统一异常处理
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    //全局异常处理
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public R error(Exception e){
        log.error(e.getMessage());
        e.printStackTrace();
        return R.error().message("未知异常");
    }

    //特殊异常处理
    @ExceptionHandler(ArithmeticException.class)
    @ResponseBody
    public R error(ArithmeticException e){
        log.error(e.getMessage());
        e.printStackTrace();
        return R.error().data("error", e);
    }

    //自定义异常处理
    @ExceptionHandler(CustomException.class)
    @ResponseBody
    public R error(CustomException e){
        log.error(e.getMessage());
        e.printStackTrace();
        return R.error().code(e.getCode()).message(e.getMsg());
    }
}
