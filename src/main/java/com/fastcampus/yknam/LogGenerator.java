package com.fastcampus.yknam;

import java.util.Properties;
import java.util.Random;
import java.util.concurrent.CountDownLatch;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

public class LogGenerator implements Runnable{
    private CountDownLatch latch;
    private String ipAddr;
    private String sessionId;
    private int durationSeconds;
    private Random rand;
    private final static long MINIMUM_SLEEP_TIME=500;
    private final static long MAXIMUM_SLEEP_TIME=60*1000;
    private final static String TOPIC_NAME="weblog";
    public LogGenerator(CountDownLatch latch, String ipAddr, String sessionId,int durationSeconds, Random rand){
        this.latch=latch;
        this.ipAddr=ipAddr;
        this.sessionId=sessionId;
        this.durationSeconds=durationSeconds;
        this.rand=rand;
    }
    @Override
    public void run() {
        long sleepTime = MINIMUM_SLEEP_TIME + Double.valueOf(rand.nextDouble()
                * (MAXIMUM_SLEEP_TIME - MINIMUM_SLEEP_TIME)).longValue();
        Properties props=new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,"winubuntu:9092");
        props.put(ProducerConfig.CLIENT_ID_CONFIG, "LogGenenrator");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        KafkaProducer<String,String>producer=new KafkaProducer<>(props);
        while(isDuration(startTime)){

                try {
                    Thread.sleep(sleepTime);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                String msg=String.format("%s,%s,%s",ipAddr,sessionId,System.currentTimeMillis());
            System.out.println(msg);
            producer.send(new ProducerRecord<>(TOPIC_NAME,msg));

        }
        producer.close();
        this.latch.countDown();
    }
}
