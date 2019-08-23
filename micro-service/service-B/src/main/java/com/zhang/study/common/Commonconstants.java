package com.zhang.study.common;

import org.elasticsearch.client.transport.TransportClient;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class Commonconstants {

    public static Map<String, Long> VALIDATE_CODE_MAP = new HashMap<String, Long>();

    public static final String VERIFICATION_CODE = "VERIFICATION_CODE";

    public static final String USER_SESSION_KEY = "USER_SESSION_KEY";

    public static final String USER_ROLE_KEY = "USER_ROLE_KEY";

    public static final String CODE = "code";

    public static final String MESSAGE = "message";

    public static final String GET_TYPE_APPLICATION = "app";

    public static final String GET_TYPE_DICTIONARY = "dic";

    public static final String GET_TYPE_ROLE = "role";

    public static final String FILE_TEMP_PATH = "/upload/temp";
    /**
     * 数据字典查询产品状态key
     */
    public static final String PRODUCT_STATUS_KEY = "PRODUCT_STATUS";
    /**
     * 操作类型
     */
    public static final String OPERATE_TYPE_ADD = "add";

    public static final String OPERATE_TYPE_EDIT = "edit";

    public static final String LOG_OPTION_ADD = "0";

    public static final String LOG_OPTION_UPDATE = "2";

    public static final String LOG_OPTION_DELETE = "1";

    public static final String TOKEN_TIME_OUT = "TOKEN_TIME_OUT";

    public static String WEB_REAL_PATH = "";

    public static String REQUEST_REAL_PATH = "";

    public static String CUSTOM_SERVICE_TYPE = "CUSTOM_SERVICE_TYPE";

    public static String MESSAGE_TYPE = "MESSAGE_TYPE";

    public static final String BLIND_EMAIL_BUTTON_NAME = "BLIND_EMAIL_BUTTON_NAME";

//    public static final String BLIND_EMAIL_BACK_URL = "BLIND_EMAIL_BACK_URL";

    public static final String EMAIL_BACK_URL = "/api/login/toBlindEmailAndResetPassword";

    public static final String BLIND_EMAIL_CONTENT = "BLIND_EMAIL_CONTENT";

    public static final String RESET_PASSWORD = "RESET_PASSWORD";

//    public static final String RESET_PASSWORD_BACK_URL = "RESET_PASSWORD_BACK_URL";

}

