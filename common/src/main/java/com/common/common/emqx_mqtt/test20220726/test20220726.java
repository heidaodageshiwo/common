package com.common.common.emqx_mqtt.test20220726;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;


/**
 * @ProjectName: common1
 * @PackageName: com.common.common.emqx_mqtt
 * @ClassName: test20220726
 * @Description: java类作用描述
 * @Author: zhangqiang
 * @CreateTime: 2022-07-26  09:46
 * @UpdateDate: 2022-07-26  09:46
 * @UpdateUser: zhangqiang
 * @UpdateRemark: The modified content
 * @Version: 1.0
 */
public class test20220726 {
    public static void main(String[] args) {
        String subTopic = "test1";
        String pubTopic = "test1";
        String content = "Hello World12222222";
        int qos = 2;
        String broker = "tcp://47.117.66.23:1883";
        String clientId = "e10adc3949ba59abbe56e057f20f883e";
        MemoryPersistence persistence = new MemoryPersistence();

        try {
            MqttClient client = new MqttClient(broker, clientId, persistence);

            // MQTT 连接选项
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setUserName("zhangqiang1");
            connOpts.setPassword("123456".toCharArray());
            // 保留会话
            connOpts.setCleanSession(false);

            // 设置回调
            client.setCallback(new OnMessageCallback());

            // 建立连接
            System.out.println("Connecting to broker: " + broker);
            client.connect(connOpts);

            System.out.println("Connected");
            System.out.println("Publishing message: " + content);

            // 订阅
            client.subscribe(subTopic);

            // 消息发布所需参数
            MqttMessage message = new MqttMessage(content.getBytes());
            message.setQos(qos);
            client.publish(pubTopic, message);
            System.out.println("Message published");

            client.disconnect();
            System.out.println("Disconnected");
            client.close();
            System.exit(0);
        } catch (MqttException me) {
            System.out.println("reason " + me.getReasonCode());
            System.out.println("msg " + me.getMessage());
            System.out.println("loc " + me.getLocalizedMessage());
            System.out.println("cause " + me.getCause());
            System.out.println("excep " + me);
            me.printStackTrace();
        }
    }
}
