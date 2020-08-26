package com.melody.java.aio;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.*;

/**
 * 基于 Aio 实现的客户端
 * @author zqhuangc
 * @see AsynchronousSocketChannel
 */
public class AioClient {

    public final AsynchronousSocketChannel client;

    public AioClient() throws Exception{
        client = AsynchronousSocketChannel.open();
    }

    public void connect(String host, int port){

        client.connect(new InetSocketAddress(host, port), null, new CompletionHandler<Void, Object>(){

            @Override
            public void completed(Void result, Object attachment) {
                try {
                    // 写（发送）
                    client.write(ByteBuffer.wrap(("test message:"+ System.currentTimeMillis()).getBytes())).get();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failed(Throwable exc, Object attachment) {
                exc.printStackTrace();
            }
        } );

        final ByteBuffer buf = ByteBuffer.allocate(1024);


        // 读（接收）
        client.read(buf, null, new CompletionHandler<Integer, Object>() {

            @Override
            public void completed(Integer result, Object attachment) {
                //System.out.println("result:" + new String(buf.array()));
                buf.flip();
                while(buf.position() < buf.limit()){
                    System.out.print((char)buf.get());
                }
                System.out.println("melo");
                buf.clear();
            }

            // IO操作失败
            @Override
            public void failed(Throwable exc, Object attachment) {
                exc.printStackTrace();
            }
        });

        try {
            Thread.sleep(Integer.MAX_VALUE);
        } catch (InterruptedException e) {
            System.out.println(e.toString());
        }

    }

    public static void main(String[] args) {
        int count = 10;
        final CountDownLatch latch = new CountDownLatch(10);

        for (int i = 0; i < count; i++) {
            latch.countDown();

            new Thread(() -> {
                try {
                    latch.await();
                    new AioClient().connect("localhost", 8080);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }).start();
        }

        try {
            Thread.sleep(10 * 60 * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }

}
