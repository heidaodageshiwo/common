package com.common.common.rocketmq;

import com.alibaba.fastjson.JSON;
import org.apache.rocketmq.acl.common.AclClientRPCHook;
import org.apache.rocketmq.acl.common.SessionCredentials;
import org.apache.rocketmq.client.consumer.PullResult;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.TransactionListener;
import org.apache.rocketmq.client.producer.TransactionMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.message.MessageQueue;
import org.apache.rocketmq.remoting.RPCHook;
import org.apache.rocketmq.remoting.common.RemotingHelper;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
public class TransactionProducer {
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

    public static void main(String[] args) throws MQClientException, InterruptedException {


        //博客参考地址 https://blog.csdn.net/Little_fxc/article/details/124045665
        //博客参考地址 https://blog.csdn.net/xiaozhongmiaoyun/article/details/106087304
        //博客参考地址 https://blog.csdn.net/Saintmm/article/details/119826091
        // 生产者事务监听器
        TransactionListener transactionListener = new OrderTransactionListener();
        TransactionMQProducer producer = new TransactionMQProducer("ProducerGroupName", getAclRPCHook());
        ExecutorService executorService = new ThreadPoolExecutor(
                2, 5, 100,
                TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(2000),
                r -> new Thread(r, "client-transaction-msg-check-thread"));

        producer.setExecutorService(executorService);
        producer.setTransactionListener(transactionListener);
        producer.setNamesrvAddr("192.168.56.211:9876");
        producer.setProducerGroup("producer_order_trans_group");
        producer.start();

        // 发送消息
        String topic = "transaction-topic";
        String tags = "trans-order";
        Order order = new Order();
        order.setId(1);
        order.setUserId(1);
        order.setGoodsName("小脆面");
        order.setTotal(2);
        String orderJson = JSON.toJSONString(order);
        try {
            byte[] orderBytes = orderJson.getBytes(RemotingHelper.DEFAULT_CHARSET);
            Message msg = new Message(topic, tags, "order", orderBytes);
            producer.sendMessageInTransaction(msg, null);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        producer.shutdown();
    }
}
