package com.common.common.ES8.SQL;

/**
 * @ProjectName: common1
 * @PackageName: com.common.common.ES8.SQL
 * @ClassName: Restful
 * @Description: java类作用描述
 * @Author: zhangqiang
 * @CreateTime: 2022-09-23  11:02
 * @UpdateDate: 2022-09-23  11:02
 * @UpdateUser: zhangqiang
 * @UpdateRemark: The modified content
 * @Version: 1.0
 */
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * @Title: Restful.java
 * @Package Restful
 * @Description: TODO(用一句话描述该文件做什么)
 * @author Ishadow
 * @date 2016年6月19日 上午1:15:43
 * @version V1.0
 */
public class Restful {
    public static void main(String[] args) throws IOException, KeyManagementException, NoSuchAlgorithmException {
        HttpsURLConnection.setDefaultHostnameVerifier(new Restful().new NullHostNameVerifier());
        SSLContext sc = SSLContext.getInstance("TLS");
        sc.init(null, trustAllCerts, new SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        URL url = new URL(
                "https://xxx.xxx.xxx.xxx:xxxx/ValidateToken/rest/username?token=60f9102ad04b3129feea3ffad7af3f88");
        // 打开restful链接
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");// POST GET PUT DELETE
        // 设置访问提交模式，表单提交
        conn.setRequestProperty("Content-Type", "application/json;charset=utf-8");
        conn.setConnectTimeout(130000);// 连接超时 单位毫秒
        conn.setReadTimeout(130000);// 读取超时 单位毫秒
        // 读取请求返回值
        byte bytes[] = new byte[1024];
        InputStream inStream = conn.getInputStream();
        inStream.read(bytes, 0, inStream.available());
        System.out.println(new String(bytes, "utf-8"));
    }

    static TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            // TODO Auto-generated method stub
        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            // TODO Auto-generated method stub
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            // TODO Auto-generated method stub
            return null;
        }
    } };

    public class NullHostNameVerifier implements HostnameVerifier {
        /*
         * (non-Javadoc)
         *
         * @see javax.net.ssl.HostnameVerifier#verify(java.lang.String,
         * javax.net.ssl.SSLSession)
         */
        @Override
        public boolean verify(String arg0, SSLSession arg1) {
            // TODO Auto-generated method stub
            return true;
        }
    }
}