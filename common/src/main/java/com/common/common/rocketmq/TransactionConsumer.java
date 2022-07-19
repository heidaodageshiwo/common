package com.common.common.rocketmq;
import com.alibaba.fastjson.JSON;
import org.apache.rocketmq.acl.common.AclClientRPCHook;
import org.apache.rocketmq.acl.common.SessionCredentials;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.PullResult;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.consumer.rebalance.AllocateMessageQueueAveragely;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.message.MessageQueue;
import org.apache.rocketmq.remoting.RPCHook;

import java.nio.charset.StandardCharsets;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TransactionConsumer {
    private static final Map<MessageQueue, Long> OFFSE_TABLE = new HashMap<MessageQueue, Long>();

    private static final String ACL_ACCESS_KEY = "rocketmq2";

    private static final String ACL_SECRET_KEY = "12345678";

    private static void printBody(PullResult pullResult) {
        printBody(pullResult.getMsgFoundList());
    }

    private static void printBody(List<MessageExt> msg) {
        if (msg == null || msg.size() == 0)
            return;
        for (MessageExt m : msg) {
            if (m != null) {
                System.out.printf("msgId : %s  body : %s  \n\r", m.getMsgId(), new String(m.getBody()));
            }
        }
    }

    private static long getMessageQueueOffset(MessageQueue mq) {
        Long offset = OFFSE_TABLE.get(mq);
        if (offset != null)
            return offset;

        return 0;
    }

    private static void putMessageQueueOffset(MessageQueue mq, long offset) {
        OFFSE_TABLE.put(mq, offset);
    }

    static RPCHook getAclRPCHook() {
        return new AclClientRPCHook(new SessionCredentials(ACL_ACCESS_KEY,ACL_SECRET_KEY));
    }
    public static void main(String[] args) throws MQClientException {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("consumer_order_trans_group", getAclRPCHook(), new AllocateMessageQueueAveragely());
        consumer.setNamesrvAddr("192.168.56.211:9876");
        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
        consumer.subscribe("transaction-topic", "trans-order");
        consumer.setMaxReconsumeTimes(3);
        TransactionUtil.startTransaction();
        consumer.registerMessageListener(new MessageListenerConcurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
                try {
                    for (MessageExt msg : msgs) {
                        // 多次消费消息处理仍然失败后，发送邮件，人工处理
                        if (msg.getReconsumeTimes() >= 3) {
                            // 发送邮件，人工处理
                            sendMail();
                        }

                        String orderStr = new String(msg.getBody(), StandardCharsets.UTF_8);
                        Order order = JSON.parseObject(orderStr, Order.class);
                        // 幂等性保持
                        String sql1 = "select * from order_credits where order_id = ?";
                        ResultSet rs = TransactionUtil.select(sql1, order.getId());
                        if (rs != null && rs.next()) {
                            System.out.println("积分已添加，订单已处理！");
                        } else {
                            // 增加积分
                            String sql2 = "insert into order_credits(user_id,order_id,total) values(?,?,?)";
                            TransactionUtil.execute(sql2, order.getUserId(), order.getId(), order.getTotal() * 2);
                            System.out.printf("订单（id=%s）添加积分%n", order.getId());
                            TransactionUtil.commit();
                        }
                    }
                    return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                } catch (Exception e) {
                    TransactionUtil.rollback();
                    e.printStackTrace();
                }
                return ConsumeConcurrentlyStatus.RECONSUME_LATER;
            }

            private void sendMail() { }
        });

        consumer.start();

        System.out.println("Consumer Started.");
    }
}
