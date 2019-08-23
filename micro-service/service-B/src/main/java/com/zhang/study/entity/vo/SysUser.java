package com.zhang.study.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: Administrator
 * Date: 2018-01-26
 * Time: 15:45
 */
@Data
@ApiModel(value = "修改密码对象")
public class SysUser extends BaseEntity {
    /**
     *
     */
    @ApiModelProperty(value = "GUID", name = "GUID")
    private String userGuid;

    /**
     * 用户账户
     */
    @ApiModelProperty(value = "用户账户")
    private String account;

    /**
     * 用户密码
     */
    @ApiModelProperty(value = "用户密码")
    private String password;

    /**
     * 用户姓名
     */
    @ApiModelProperty(value = "用户姓名")
    private String name;

    /**
     * 手机号
     */
    @ApiModelProperty(value = "手机号")
    private String mobilePhone;

    /**
     * 性别
     */
    @ApiModelProperty(value = "性别")
    private String sex;

    /**
     * 角色ID
     */
    @ApiModelProperty(value = "角色ID")
    private String roleGuid;

    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人")
    private String createBy;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    /**
     * 修改人
     */
    @ApiModelProperty(value = "修改人")
    private String updateBy;

    /**
     * 修改时间
     */
    @ApiModelProperty(value = "修改时间")
    private Date updateTime;

    /**
     * 模糊查询参数
     */
    @ApiModelProperty(value = "模糊查询参数")
    private String searchParam;

    /**
     * 是否可见
     */
    @ApiModelProperty(value = "是否可见")
    private String vaild;

    /**
     * 旧密码
     */
    @ApiModelProperty(value = "旧密码")
    private String oldPassword;

    /**
     * 验证码
     */
    @ApiModelProperty(value = "验证码")
    private String validationCode;

    /**
     * 父节点ID
     */
    @ApiModelProperty(value = "父节点ID")
    private String parentId;

    /**
     * 组织名称
     */
    @ApiModelProperty(value = "组织名称")
    private String orgName;

    /**
     * 角色名称
     */
    @ApiModelProperty(value = "角色名称")
    private String roleName;

//    /**
//     * 角色菜单
//     */
//    @ApiModelProperty(value = "角色菜单")
//    private List<SysAuthority> sysAuthorityList;

    /**
     * 排序类型
     */
    @ApiModelProperty(value = "排序类型")
    private int orderBy;

    /**
     * 正序逆序 0asc 1 desc
     */
    @ApiModelProperty(value = "正序逆序")
    private int orderByType;
    /**
     * 排序sql
     */
    @ApiModelProperty(value = "排序sql")
    private String orderString;

    private String picUrl;
    private String orgGuid;

}