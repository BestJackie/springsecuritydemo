package com.example.springsecurity.thirtypart.weixin.adapter;

import com.example.springsecurity.thirtypart.weixin.model.WeixinUserInfo;
import com.example.springsecurity.thirtypart.weixin.service.Weixin;
import org.springframework.social.connect.ApiAdapter;
import org.springframework.social.connect.ConnectionValues;
import org.springframework.social.connect.UserProfile;

/**
 * FileName: WeixinAdapter
 * Author:   haichaoyang3
 * Date:     2019/9/20 11:19
 * Description:
 * History:
 * since: 1.0.0
 */
public class WeixinAdapter implements ApiAdapter<Weixin> {

    private String openId;

    public WeixinAdapter() {
    }

    public WeixinAdapter(String openId) {
        this.openId = openId;
    }

    @Override
    public boolean test(Weixin weixin) {
        return true;
    }

    @Override
    public void setConnectionValues(Weixin weixin, ConnectionValues connectionValues) {
        WeixinUserInfo userInfo = weixin.getUserInfo(openId);

        connectionValues.setProviderUserId(userInfo.getOpenId());
        connectionValues.setDisplayName(userInfo.getNickname());
        connectionValues.setImageUrl(userInfo.getHeadimgurl());
    }

    @Override
    public UserProfile fetchUserProfile(Weixin weixin) {
        return null;
    }

    @Override
    public void updateStatus(Weixin weixin, String s) {

    }
}
