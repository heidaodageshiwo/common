package com.common.common.flink.stream.kafka;


import org.apache.flink.api.common.ExecutionConfig;

import org.apache.flink.api.common.typeutils.base.VoidSerializer;
import org.apache.flink.api.java.typeutils.runtime.kryo.KryoSerializer;
import org.apache.flink.streaming.api.functions.sink.TwoPhaseCommitSinkFunction;

import java.io.*;
import java.util.*;

public class MyTransactionSink extends TwoPhaseCommitSinkFunction<String, ContentTransaction, Void> {
    //所有事务在预提交阶段时，模拟将事务所对应的数据存到Map当，模拟放到临时文件。
    ContentBuffer contentBuffer = new ContentBuffer();

    public MyTransactionSink() {
        super(new KryoSerializer<>(ContentTransaction.class,new ExecutionConfig()), VoidSerializer.INSTANCE);
    }

    /**
     * 当有数据的时候，会调用此方法，将数据写入到存储系统
     *
     * @param transaction
     * @param value
     * @param context
     * @throws Exception
     */
    @Override
    protected void invoke(ContentTransaction transaction, String value, Context context) throws Exception {
        System.err.println("====invoke====" + value + " transaction: " + transaction.toString());
        //将数据写入到本次事务所对应的缓存中
        transaction.tempContentWriter.write(value);

    }

    /**
     * 开启一个事务,在临时目录下，创建一个临时文件，之后将数据写入进去
     *
     * @return
     * @throws Exception
     */
    @Override
    protected ContentTransaction beginTransaction() throws Exception {
        //创建本次事务对应的一个数据缓存操作对象，即此tmp对象中保存了一次事务中的数据
        TempContentWriter tempContentWriter = contentBuffer.createWriter(UUID.randomUUID().toString());

        ContentTransaction contentTransaction = new ContentTransaction(tempContentWriter);
        System.out.println("=====begin transaction  " + contentTransaction.toString() );
        return contentTransaction;
    }

    /**
     * 在pre-commit阶段，flush缓存数据到磁盘，然后关闭该文件，确保不再写入新数据到该文件中
     * 同时开启一个新事务，执行属于下一个checkpoint的写入操作
     * @param transaction
     * @throws Exception
     */
    @Override
    protected void preCommit(ContentTransaction transaction) throws Exception {

        System.out.println("preCommit==== " + transaction.toString());
        //预提交阶段，将本次事务的数据刷到临时文件保存起来
        transaction.tempContentWriter.flush();
        //清空本次事务的数据
        transaction.tempContentWriter.close();
    }

    /**
     * 在commit阶段，我们以原子性的方式将预提交阶段的临时文件数据 写入真正的文件目录里，这里会有一定的延迟
     * @param transaction
     */
    @Override
    protected void commit(ContentTransaction transaction) {

        System.out.println("====commit====" + transaction.toString());
        //获取名称
        String name = transaction.tempContentWriter.getName();
        //获取数据，模拟将数据从临时文件读进来。
        Collection<String> content = contentBuffer.read(name);

        FileWriter fileWriter = null;

        try {
            fileWriter = new FileWriter(new File("data/flinkTransaction/" + name));
            //将数据写入到外部系统
            for (String line:content){

                fileWriter.write(line +"\n");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {

            try {
                System.out.println("====finish commit====" + transaction.toString());
                //清空本次事务的对应的临时文件数据
                contentBuffer.delete(transaction.tempContentWriter.getName());
                fileWriter.flush();
                fileWriter.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }



    }

    /**
     * 一旦有异常则终止事务，删除临时文件
     * @param transaction
     */
    @Override
    protected void abort(ContentTransaction transaction) {
        System.out.println("=====abort====" + transaction.tempContentWriter.getName());
        transaction.tempContentWriter.close();
        contentBuffer.delete(transaction.tempContentWriter.getName());
    }
}

/**
 * Contentbuffer 类中 放事务的临时数据，模拟数据放在临时文件种，
 *
 *
 * 注意：一个事务期间会有多条数据，一个事务的所有数据对应一个list;
 */
class ContentBuffer implements Serializable {
    //key为事务名称，value为事务的所有数据

    Map<String, List<String>> fileContent = new HashMap<>();

    public TempContentWriter createWriter(String name) {

        return new TempContentWriter(name,this);
    }

    public void putContent(String name, List<String> value) {

        fileContent.put(name, value);
    }

    public Collection<String> read(String name) {

        List<String> content = fileContent.get(name);

        return content;
    }

    public void delete(String name) {
        fileContent.remove(name);
    }



}

class TempContentWriter {
    private final ContentBuffer contentBuffer;

    private final String name;
    //一次事务里的所有数据缓存到一个list对象中
    private final List<String> buffer = new ArrayList<>();

    private boolean closed = false;


    TempContentWriter(String name ,ContentBuffer contentBuffer) {
        this.name = name;
        this.contentBuffer = contentBuffer;
    }

    public  TempContentWriter write(String value){
        if(!closed){
            //数据写入到缓存list中
            buffer.add(value);
        }
        return this;

    }

    public void flush(){
        //1.预提交阶段，将数据刷到临时文件保存起来，
        //2.为了方面，这里是简单的模拟将数据提交到一个Map中保存。
        contentBuffer.putContent(name,buffer);
    }

    public void close(){

        closed = true;
    }


    public String getName() {
        return name;
    }
}

class ContentTransaction {

    TempContentWriter tempContentWriter ;


    public ContentTransaction(TempContentWriter tempContentWriter) {
        this.tempContentWriter = tempContentWriter;
    }

    @Override
    public String toString() {
        return "contentTransaction: " + tempContentWriter.getName();
    }
}
