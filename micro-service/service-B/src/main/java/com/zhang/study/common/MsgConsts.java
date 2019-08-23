package com.zhang.study.common;

/**
 * 消息公共类
 */
public interface MsgConsts {

    String SUCCESS_COMMON_CODE = "0";
    String SUCCESS_COMMON_MSG = "调用成功";

    String PARAMS_NULL_CODE = "1";
    String PARAMS_NULL_MSG = "参数为空";

    int EMPTY_EXCEL_IMPORT_KEY_CODE = 203;
    String EMPTY_EXCEL_IMPORT_KEY_MSG = "模板导入KEY为空";

    int ERROR_FILE_CODE = 206;
    String ERROR_FILE_MSG = "文件格式错误";

    int EMPTY_FILE_CODE = 204;
    String EMPTY_FILE_MSG = "导入文件为空";

    int ERROR_FILE_NAME_CODE = 207;
    String ERROR_FILE_NAME__MSG = "文件名错误";


    int TITLE_ERROR_FILE_CODE = 205;
    String TITLE_ERROR_FILE_MSG = "导入数据不是此模块的数据";

    int NO_DATA_DOWNLOAD_FAILED_CODE=223;
    String NO_DATA_DOWNLOAD_FAILED_MSG = "当前无数据可下载";

    String USER_PWD_INVALID_CODE = "901";
    String USER_PWD_INVALID_MSG = "用户名或密码错误";

    String USER_NOT_EXIST_CODE = "902";
    String USER_NOT_EXIST_MSG = "用户不存在";

    String USER_PWD_NOT_CORRECT_CODE = "903";
    String USER_PWD_NOT_CORRECT_MSG = "密码不正确";

    String USER_NO_LOGIN_CODE = "999";
    String USER_NO_LOGIN_MSG = "用户未登录或者已过期，请重新登录";

    String SYSTEM_ERROR_CODE = "9999";
    String SYSTEM_ERROR_MSG = "系统繁忙，请稍后再试";

}
