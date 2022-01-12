package com.qingfeng.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * 封装返回数据的实体类
 *
 * @Author 清风学Java
 * @param <T>
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ResultVO<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 响应的状态码
     */
    private Integer code;
    /**
     * 封装返回数据是否成功
     */
    private Boolean success;
    /**
     * 封装返回的信息
     */
    private String message;
    /**
     * 封装返回的数据
     */
    private T data;

    private ResultVO(int code, boolean success, T data, String message) {
        this.code = code;
        this.data = data;
        this.success = success;
        this.message = message;
    }

    public static <T> ResultVO<T> success(int code, boolean success, T data, String message) {
        return new ResultVO(code, success, data, message);
    }

    public static <T> ResultVO<T> success() {
        return new ResultVO(200, true, (Object) null, "操作成功");
    }

    public static <T> ResultVO<T> success(String message) {
        return success(200, true, null, message);
    }

    public static <T> ResultVO<T> fail() {
        return new ResultVO(500, false, (Object) null, "操作失败");
    }

    public static <T> ResultVO<T> fail(String message) {
        return new ResultVO(500, false, (Object) null, message);
    }

    public void setSuccess(final boolean success) {
        this.success = success;
    }
}
