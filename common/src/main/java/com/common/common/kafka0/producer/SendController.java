package com.common.common.kafka0.producer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * java类简单作用描述
 *
 * @ProjectName: emaxdemo
 * @Package: com.emqx.emaxdemo.kafka0.producer
 * @ClassName: SendController
 * @Description: java类作用描述
 * @Author: zhangq
 * @CreateDate: 2021-11-17 17:00
 * @UpdateUser: zhangq
 * @UpdateDate: 2021-11-17 17:00
 * @UpdateRemark: The modified content
 * @Version: 1.0 *
 */
@RestController
@RequestMapping("/kafka")
public class SendController {
  @Autowired
  private Producer producer;

  @RequestMapping(value = "/send")
  public String send() {
    producer.send();
    return "{\"code\":0}";
  }
  @RequestMapping(value = "/send1")
  public String send1() {
    producer.send1();
    return "{\"code\":0}";
  }
  //https://blog.csdn.net/yuanlong122716/article/details/105160545/
  //发送地址
  @RequestMapping(value = "/send2")
  public String send2() {
    producer.send2();
    return "{\"code\":0}";
  }
}
