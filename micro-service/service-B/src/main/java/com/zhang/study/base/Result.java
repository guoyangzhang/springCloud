package com.zhang.study.base;

import com.zhang.study.common.MsgConsts;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

public class Result implements Serializable {

    private static final long serialVersionUID = 2907211119535752349L;

    /**
     * 是否成功
     */
    @ApiModelProperty(value = "是否成功")
    private boolean success;
    /**
     * 请求结果码
     */
    @ApiModelProperty(value = "请求结果码")
    private String code;
    /**
     * 请求消息
     */
    @ApiModelProperty(value = "请求消息")
    private String message;
    /**
     * 返回数据
     */
    @ApiModelProperty(value = "返回数据")
    private Object data;
    /**
     * 数据数量
     */
    @ApiModelProperty(value = "数据数量")
    private int count;

    public Result() {
    }

    private Result(boolean success, String code, String message, Object data) {
        this.success = success;
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public Result(boolean success, String code, String message, Object data, int count) {
        this.success = success;
        this.code = code;
        this.message = message;
        this.data = data;
        this.count = count;
    }

    public static Result ok(String code, String message, Object data) {
        return new Result(true, code, message, data);
    }

    public static Result ok(Object data, int count) {
        return new Result(true, MsgConsts.SUCCESS_COMMON_CODE, MsgConsts.SUCCESS_COMMON_MSG, data, count);
    }

    public static Result ok(Object data) {
        return new Result(true, MsgConsts.SUCCESS_COMMON_CODE, MsgConsts.SUCCESS_COMMON_MSG, data);
    }

    public static Result error(String code, String message, Object data) {
        return new Result(false, code, message, data);
    }

    public static Result error(String message) {
        return new Result(false, null, message, null);
    }


    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
