package com.common.common.kafka0.producer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.Date;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFutureCallback;

/**
 * java类简单作用描述
 *
 * @ProjectName: emaxdemo
 * @Package: com.emqx.emaxdemo.kafka0.producer
 * @ClassName: Producer
 * @Description: java类作用描述
 * @Author: zhangq
 * @CreateDate: 2021-11-17 16:58
 * @UpdateUser: zhangq
 * @UpdateDate: 2021-11-17 16:58
 * @UpdateRemark: The modified content
 * @Version: 1.0 *
 */
@Component
public class Producer {
  @Autowired
  private KafkaTemplate kafkaTemplate;

  private static Gson gson = new GsonBuilder().create();

  //发送消息方法
  public void send() {
    Message message = new Message();
    message.setId("KFK_"+System.currentTimeMillis());
    message.setMsg(UUID.randomUUID().toString());
    message.setSendTime(new Date());
    kafkaTemplate.send("test", gson.toJson(message));
  }
  //发送消息方法

  /**
   * 带回调的生产者
   *
   * kafkaTemplate提供了一个回调方法addCallback，我们可以在回调方法中监控消息是否发送成功 或 失败时做补偿处理，有两种写法，
   */
  public void send1() {
    Message message = new Message();
    message.setId("KFK_张强——"+System.currentTimeMillis());
    message.setMsg(UUID.randomUUID().toString());
    message.setSendTime(new Date());
    kafkaTemplate.send("test1", gson.toJson(message)).addCallback(success ->{
      System.out.println("发送成功！："+success.toString());
    },failure->{
      System.out.println("发送失败！："+failure.getMessage().toString());
    });
  }

  public void send2() {
    Message message = new Message();
    message.setId("KFK_张强send2——"+System.currentTimeMillis());
    message.setMsg(UUID.randomUUID().toString());
    message.setSendTime(new Date());
    kafkaTemplate.send("test2", gson.toJson(message)).addCallback(new ListenableFutureCallback() {
      @Override
      public void onFailure(Throwable throwable) {
        System.out.println("发送消息失败："+throwable.getMessage());

      }

      @Override
      public void onSuccess(Object o) {
        System.out.println("发送消息成功："+ gson.toJson(o));
      }
    });
  }
}
