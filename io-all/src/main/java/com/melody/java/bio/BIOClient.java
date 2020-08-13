package com.melody.java.bio;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;

/**
 * 基于 BIO 实现的客户端
 * @author zqhuangc
 * @see Socket
 */
public class BIOClient {

    public static void main(String[] args) {

        BIOClient.syncSend();
        BIOClient.asyncSend();
    }

    private static void syncSend(){
        try {
            Socket client = new Socket("localhost", 8080);
            //输出流通道打开
            OutputStream os = client.getOutputStream();
            //产生一个随机的字符串,UUID
            String name = UUID.randomUUID().toString();

            //发送给服务端
            os.write((name+":sync").getBytes());
            os.close();
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void asyncSend(){
        int count = 50;

        final CountDownLatch latch = new CountDownLatch(count);

        for(int i = 0 ; i < count; i ++){

            new Thread(()->{
                try{
                    latch.await();

                    Socket client = new Socket("localhost", 8080);

                    OutputStream os = client.getOutputStream();

                    String name = UUID.randomUUID().toString();

                    os.write((name + ":async:" + Thread.currentThread().getId()).getBytes());
                    os.close();
                    client.close();

                }catch(Exception e){
                    e.printStackTrace();
                }
            }).start();

            latch.countDown();
        }
    }

}
