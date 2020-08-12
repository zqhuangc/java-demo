package com.melody.java.bio;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;

/**
 * @Description: TODO
 * @author: melody_wongzq
 * @since: 2020/6/17
 * @see
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

            new Thread(){
                @Override
                public void run() {
                    try{
                        latch.await();

                        Socket client = new Socket("localhost", 8080);

                        OutputStream os = client.getOutputStream();

                        String name = UUID.randomUUID().toString();

                        os.write((name + ":async:" + Thread.currentThread().getId()).getBytes());
                        os.close();
                        client.close();

                    }catch(Exception e){

                    }
                }

            }.start();

            latch.countDown();
        }
    }

}
