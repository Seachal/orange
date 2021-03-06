package com.zhuzichu.mvvm.http.exception;

import android.net.ParseException;
import android.util.MalformedJsonException;
import com.google.gson.JsonParseException;
import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONException;
import retrofit2.HttpException;

import java.net.ConnectException;

/**
 * Created by Android Studio.
 * Blog: zhuzichu.com
 * User: zhuzichu
 * Date: 2019-05-27
 * Time: 16:31
 */
public class ExceptionHandle {

    private static final int UNAUTHORIZED = 401;
    private static final int FORBIDDEN = 403;
    private static final int NOT_FOUND = 404;
    private static final int REQUEST_TIMEOUT = 408;
    private static final int INTERNAL_SERVER_ERROR = 500;
    private static final int SERVICE_UNAVAILABLE = 503;

    public static ResponseThrowable handleException(Throwable e) {
        ResponseThrowable ex = new ResponseThrowable();
        if (e instanceof HttpException) {
            HttpException httpException = (HttpException) e;
            switch (httpException.code()) {
                case UNAUTHORIZED:
                    ex.msg = "操作未授权";
                    break;
                case FORBIDDEN:
                    ex.msg = "请求被拒绝";
                    break;
                case NOT_FOUND:
                    ex.msg = "资源不存在";
                    break;
                case REQUEST_TIMEOUT:
                    ex.msg = "服务器执行超时";
                    break;
                case INTERNAL_SERVER_ERROR:
                    ex.msg = "服务器内部错误";
                    break;
                case SERVICE_UNAVAILABLE:
                    ex.msg = "服务器不可用";
                    break;
                default:
                    ex.msg = "网络错误";
                    break;
            }
            return ex;
        } else if (e instanceof JsonParseException
                || e instanceof JSONException
                || e instanceof ParseException || e instanceof MalformedJsonException) {
            ex.msg = "解析错误";
            return ex;
        } else if (e instanceof ConnectException) {
            ex.msg = "连接失败";
            return ex;
        } else if (e instanceof javax.net.ssl.SSLException) {
            ex.msg = "证书验证失败";
            return ex;
        } else if (e instanceof ConnectTimeoutException) {
            ex.msg = "连接超时";
            return ex;
        } else if (e instanceof java.net.SocketTimeoutException) {
            ex.msg = "连接超时";
            return ex;
        } else if (e instanceof java.net.UnknownHostException) {
            ex.msg = "主机地址未知";
            return ex;
        } else if (e instanceof ResponseThrowable) {
            ex = (ResponseThrowable) e;
            return ex;
        } else {
            ex.msg = "未知错误";
            return ex;
        }


    }


    /**
     * 约定异常 这个具体规则需要与服务端或者领导商讨定义
     */
    public class ERROR {
        public static final int NO_DATA = 0;
        /**
         * 密码错误
         */
        public static final int PASSWORD_ERROR = 100;
        /**
         * 未知错误
         */
        public static final int UNKNOWN = 1000;
        /**
         * 解析错误
         */
        public static final int PARSE_ERROR = 1001;
        /**
         * 网络错误
         */
        public static final int NETWORD_ERROR = 1002;
        /**
         * 协议出错
         */
        public static final int HTTP_ERROR = 1003;

        /**
         * 证书出错
         */
        public static final int SSL_ERROR = 1005;

        /**
         * 连接超时
         */
        public static final int TIMEOUT_ERROR = 1006;
    }

}