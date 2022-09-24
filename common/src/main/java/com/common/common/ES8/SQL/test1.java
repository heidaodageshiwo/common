package com.common.common.ES8.SQL;

import org.elasticsearch.xpack.sql.jdbc.EsDataSource;

import javax.net.ssl.*;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.sql.*;
import java.util.Properties;

/**
 * @ProjectName: common1
 * @PackageName: com.common.common.ES8.SQL
 * @ClassName: test1
 * @Description: java类作用描述
 * @Author: zhangqiang
 * @CreateTime: 2022-09-23  10:42
 * @UpdateDate: 2022-09-23  10:42
 * @UpdateUser: zhangqiang
 * @UpdateRemark: The modified content
 * @Version: 1.0
 */
public class test1 {/*
    static {
        disableSslVerification();
    }

    private static void disableSslVerification() {
        try
        {
            // Create a trust manager that does not validate certificate chains
            TrustManager[] trustAllCerts = new TrustManager[] {new X509TrustManager() {
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                }
                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                }
            }
            };

            // Install the all-trusting trust manager
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

            // Create all-trusting host name verifier
            HostnameVerifier allHostsValid = new HostnameVerifier() {
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            };

            // Install the all-trusting host verifier
            HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
    }
*/
  public static void main(String[] args) throws SQLException {
        EsDataSource dataSource = new EsDataSource();
        String address = "jdbc:es://http://192.168.56.213:9200/?timezone=UTC&page.size=250";
//        String address = "jdbc:es://" + "https://192.168.56.213:9200/";
        dataSource.setUrl(address);
        Properties connectionProperties =  new Properties();
        connectionProperties.put("user", "elastic");
        connectionProperties.put("password", "123456");
        dataSource.setProperties(connectionProperties);
        Connection connection = dataSource.getConnection();
        System.out.println(connection);
        try (Statement statement = connection.createStatement();
             ResultSet results = statement.executeQuery(
                     " select * from as")) {
            while (results.next()) {
                System.out.println("======================================");
            }
        }
    }
   /*public static void main(String[] args) {
       //1创建连接
       try {
           Connection connection = DriverManager.getConnection("jdbc:es://http://192.168.56.213:9200","elastic","123456");
//           http://192.168.56.213:9200/?auth_user=elastic&auth_password=123456

           //2创建statement
           Statement statement = connection.createStatement();
           //3执行sql语句
           ResultSet resultSet = statement.executeQuery("select * from as");
           //4获取结果
           while (resultSet.next()) {
              *//* String str = resultSet.getString(1)+","
                       +resultSet.getString(2)+","
                       +resultSet.getString(3)+","
                       +resultSet.getString(4);
               System.out.println(str);*//*
               System.out.println("======================================");
           }
       } catch (SQLException e) {
           e.printStackTrace();
       }
   }*/
}
