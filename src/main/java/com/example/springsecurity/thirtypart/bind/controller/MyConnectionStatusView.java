package com.example.springsecurity.thirtypart.bind.controller;

import com.alibaba.fastjson.JSONObject;
import org.springframework.social.connect.Connection;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.servlet.view.AbstractView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * FileName: MyConnectionStatusView
 * Author:   haichaoyang3
 * Date:     2019/9/23 10:30
 * Description:
 * History:
 * since: 1.0.0
 */
@Component("connect/status")
public class MyConnectionStatusView extends AbstractView {

    @Override
    protected void renderMergedOutputModel(Map<String, Object> map, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {
        Map<String, List<Connection<?>>> connections = (Map<String, List<Connection<?>>>) map.get("connectionMap");
        Map<String,Boolean> result = new HashMap<>();
        for (String key : connections.keySet()) {
            result.put(key,!CollectionUtils.isEmpty(connections.get(key)));
        }

        httpServletResponse.setContentType("application/json;charset=UTF-8");
        httpServletResponse.getWriter().write(JSONObject.toJSONString(result));
    }
}
