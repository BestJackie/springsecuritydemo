package com.example.springsecurity.thirtypart.bind.view;

import org.springframework.web.servlet.view.AbstractView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * FileName: MyConnectedView
 * Author:   haichaoyang3
 * Date:     2019/9/23 10:59
 * Description:
 * History:
 * since: 1.0.0
 */
public class MyConnectedView extends AbstractView {
    @Override
    protected void renderMergedOutputModel(Map<String, Object> map, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {
        httpServletResponse.setContentType("text/html;charset=UTF-8");
        if (map.get("connection") == null){
            httpServletResponse.getWriter().write("绑定成功");
        }else {
            httpServletResponse.getWriter().write("解绑成功");
        }
    }
}
