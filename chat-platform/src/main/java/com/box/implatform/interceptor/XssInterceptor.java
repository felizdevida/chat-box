package com.box.implatform.interceptor;

import com.box.implatform.enums.ResultCode;
import com.box.implatform.exception.GlobalException;
import com.box.implatform.util.XssUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.util.Map;

@Slf4j
public class XssInterceptor implements HandlerInterceptor {


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 检查参数
        Map<String, String[]> paramMap =  request.getParameterMap();
        for(String[] values:paramMap.values()){
            for(String value:values){
                if(XssUtil.checkXss(value)){
                    throw new GlobalException(ResultCode.XSS_PARAM_ERROR);
                }
            }
        }
        //  检查body
        String body = getBody(request);
        if(XssUtil.checkXss(body)){
            throw new GlobalException(ResultCode.XSS_PARAM_ERROR);
        }
        return true;
    }

    @SneakyThrows
    private String getBody(HttpServletRequest request){
        BufferedReader reader = request.getReader();
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            stringBuilder.append(line);
        }
        return stringBuilder.toString();
    }
}
