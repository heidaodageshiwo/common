package com.common.common.tree;


import org.springframework.util.StringUtils;

import java.util.HashMap;

/**
 * 操作消息提醒
 * 
 * @author ruoyi
 */
public class AjaxResultzq extends HashMap<String, Object>
{
    private static final long serialVersionUID = 1L;

    /** 状态码 */
    public static final String CODE_TAG = "code";

    /** 返回内容 */
    public static final String MSG_TAG = "msg";

    /** 数据对象 */
    public static final String DATA_TAG = "data";

    /**
     * 初始化一个新创建的 AjaxResult 对象，使其表示一个空消息。
     */
    public AjaxResultzq()
    {
    }

    /**
     * 初始化一个新创建的 AjaxResult 对象
     *
     * @param code 状态码
     * @param msg 返回内容
     */
    public AjaxResultzq(int code, String msg)
    {
        super.put(CODE_TAG, code);
        super.put(MSG_TAG, msg);
    }

    /**
     * 初始化一个新创建的 AjaxResult 对象
     *
     * @param code 状态码
     * @param msg 返回内容
     * @param data 数据对象
     */
    public AjaxResultzq(int code, String msg, Object data)
    {
        super.put(CODE_TAG, code);
        super.put(MSG_TAG, msg);
        if (!StringUtils.isEmpty(data))
        {
            super.put(DATA_TAG, data);
        }
    }
    
    /**
     * 方便链式调用
     *
     * @param key
     * @param value
     * @return
     */
    @Override
    public AjaxResultzq put(String key, Object value)
    {
        super.put(key, value);
        return this;
    }

    /**
     * 返回成功消息
     * 
     * @return 成功消息
     */
    public static AjaxResultzq success()
    {
        return AjaxResultzq.success("操作成功");
    }

    /**
     * 返回成功数据
     * 
     * @return 成功消息
     */
    public static AjaxResultzq success(Object data)
    {
        return AjaxResultzq.success("操作成功", data);
    }

    /**
     * 返回成功消息
     * 
     * @param msg 返回内容
     * @return 成功消息
     */
    public static AjaxResultzq success(String msg)
    {
        return AjaxResultzq.success(msg, null);
    }

    /**
     * 返回成功消息
     * 
     * @param msg 返回内容
     * @param data 数据对象
     * @return 成功消息
     */
    public static AjaxResultzq success(String msg, Object data)
    {
        return new AjaxResultzq(200, msg, data);
    }

    /**
     * 返回错误消息
     * 
     * @return
     */
    public static AjaxResultzq error()
    {
        return AjaxResultzq.error("操作失败");
    }

    /**
     * 返回错误消息
     * 
     * @param msg 返回内容
     * @return 警告消息
     */
    public static AjaxResultzq error(String msg)
    {
        return AjaxResultzq.error(msg, null);
    }

    /**
     * 返回错误消息
     * 
     * @param msg 返回内容
     * @param data 数据对象
     * @return 警告消息
     */
    public static AjaxResultzq error(String msg, Object data)
    {
        return new AjaxResultzq(500, msg, data);
    }

    /**
     * 返回错误消息
     * 
     * @param code 状态码
     * @param msg 返回内容
     * @return 警告消息
     */
    public static AjaxResultzq error(int code, String msg)
    {
        return new AjaxResultzq(code, msg, null);
    }
}
