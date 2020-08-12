package com.melody.java.aio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Map;
import java.util.concurrent.*;

/**
 * @Description: TODO
 * @author: melody_wongzq
 * @since: 2020/6/17
 * @see
 */
public class AioServer {

    private final int port;

    public AioServer(int port) {
        this.port = port;
    }

    public void listen(){

        try {
            //线程缓冲池，为了体现异步
            ExecutorService executorService = Executors.newCachedThreadPool();

            //给线程池初始化一个线程
            final AsynchronousChannelGroup asynchronousChannelGroup = AsynchronousChannelGroup.withCachedThreadPool(executorService, 1);

            final AsynchronousServerSocketChannel server = AsynchronousServerSocketChannel.open();


            server.bind(new InetSocketAddress(port));

            System.out.println("server started! listen at port:" + port);

            final Map<String,Integer> count = new ConcurrentHashMap<String, Integer>();
            count.put("count", 0);

            //开始等待客户端连接
            server.accept(null, new CompletionHandler<AsynchronousSocketChannel, Object>() {

                final ByteBuffer byteBuffer = ByteBuffer.allocateDirect(1024);

                @Override
                public void completed(AsynchronousSocketChannel result, Object attachment) {
                    count.put("count", count.get("count") + 1);

                    System.out.println(count.get("count"));

                    try {
                        byteBuffer.clear();
                        result.read(byteBuffer).get();
                        byteBuffer.flip();
                        while(byteBuffer.position() < byteBuffer.limit()){
                            System.out.print((char)byteBuffer.get());
                        }
                        byteBuffer.flip();
                        result.write(byteBuffer);
                    }  catch (Exception e) {
                        System.out.println(e);
                    } finally {
                        try {
                            result.close();
                            server.accept(null, this);
                        } catch (IOException e) {
                            System.out.println(e);
                        }

                    }

                }

                @Override
                public void failed(Throwable exc, Object attachment) {
                    System.out.println("io operation failed:" + exc);
                }
            });

            try {
                Thread.sleep(Integer.MAX_VALUE);
            } catch (InterruptedException ex) {
                System.out.println(ex);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new AioServer(8080).listen();
    }
}
