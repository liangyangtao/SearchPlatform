package com.unbank.common.entity;

/**
 * 返回参数 Created by LiuLei on 2015/10/29.
 */
public class ResponseParam {
    /**
     * 执行结果（成功：SUCCESS，失败：ERROR）
     */
    private String status;

    /**
     * 返回提示信息
     */
    private String msg;

    /**
     * 返回对象封装Json
     */
    private String jsonData;

    /**
     * 总条数
     */
    private long count;

    /**
     * 返回结果
     */
    private Object result;

    /**
     * 其它数据
     */
    private String others;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getJsonData() {
        return jsonData;
    }

    public void setJsonData(String jsonData) {
        this.jsonData = jsonData;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public String getOthers() {
        return others;
    }

    public void setOthers(String others) {
        this.others = others;
    }
}
