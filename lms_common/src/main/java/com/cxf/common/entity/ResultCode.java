package com.cxf.common.entity;

public enum ResultCode {

    SUCCESS(true, 10000, "操作成功！"),

    //---系统错误返回码-----
    FAIL(false, 10001, "操作失败"),
    UNAUTHENTICATED(false, 10002, "您还未登录"),
    UNAUTHORISE(false, 10003, "权限不足"),
    SERVER_ERROR(false, 99999, "抱歉，系统繁忙，请稍后重试！"),

    //---用户操作返回码----
    MOBILEORPASSWORDERROR(false,20000,"用户名或密码错误"),
    OLDPWDFAIL(false, 20001, "原密码错误"),
    UPLOADFILEERROR(false, 20002, "上传文件错误"),
    USERENABLESTATE(false, 20003, "该用户已被禁用"),
    USERNOTFIND(false, 20004, "该用户不存在"),
    CAPYCHAERROR(false, 20005, "验证码错误"),
    CAPYCHAEMPTY(false, 20006, "验证码已过期,请刷新"),
    LOGINSUCCESS(true, 20007, "登陆成功！"),
    LOGINNOSAFE(true, 20008, "登陆成功!为账户安全,请尽快修改原始密码"),

    //---组织操作返回码----
    COMMUNITYNAMEREPEAT(false,30000,"组织名称已存在"),
    COMMUNITYENABLESTATE(false,30001,"所在组织已被禁用"),
    COMMUNITYTYPEERROR(false,30002,"删除失败！该组织类型仍有组织在使用"),

    //---权限操作返回码----
    ROLEDELFAIL(false,40000,"尚有用户在使用该角色");

    //---其他操作返回码----

    //操作是否成功
    boolean success;
    //操作代码
    int code;
    //提示信息
    String message;

    ResultCode(boolean success, int code, String message) {
        this.success = success;
        this.code = code;
        this.message = message;
    }

    public boolean success() {
        return success;
    }

    public int code() {
        return code;
    }

    public String message() {
        return message;
    }

}
