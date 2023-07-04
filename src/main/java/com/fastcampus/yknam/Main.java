package com.fastcampus.yknam;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

public class Main {
    static int useNum = 15;
    static int durationSeconds = 300;
    static Random rand=new Random();
    static Set<String> ipSet=new HashSet<>();
    public static void main(String[] args) {

        System.out.println("Hello world!");
        CountDownLatch latch=new CountDownLatch(useNum);
        ExecutorService executorService= Executors.newFixedThreadPool(useNum);
        IntStream.range(0,useNum).forEach(i->{
            executorService.execute(new LogGenerator(latch, getIpAddr(), UUID.randomUUID().toString(), durationSeconds,rand));
        });

        executorService.shutdown();

    }

    public static String getIpAddr(){
        while (true) {
            String ipAddr = "192.168.0." + rand.nextInt(256);
            if (!ipSet.contains(ipAddr)) {
                ipSet.add(ipAddr);
                return ipAddr;
            }
        }
    }
}