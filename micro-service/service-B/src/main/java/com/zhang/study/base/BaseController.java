package com.zhang.study.base;

import com.zhang.study.common.Commonconstants;
import com.zhang.study.entity.vo.SysUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;

public abstract class BaseController {

    public Logger logger = LoggerFactory.getLogger(getClass());


    public static String getUserId(HttpServletRequest request) {
        String userId = "";
        SysUser userInfo = (SysUser) request.getSession().getAttribute(Commonconstants.USER_SESSION_KEY);
        if (userInfo != null) {
            userId = userInfo.getAccount();
        }

        return userId;
    }

    public static String getId(HttpServletRequest request) {
        String userId = "";
        SysUser userInfo = (SysUser) request.getSession().getAttribute(Commonconstants.USER_SESSION_KEY);
        if (userInfo != null) {
            userId = userInfo.getUserGuid();
        }

        return userId;
    }

}

