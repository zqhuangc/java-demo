package com.melody.java.bio;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 基于 BIO 实现的服务端
 * @author zqhuangc
 * @see ServerSocket
 */
public class BIOServer {

    ServerSocket server;

    public BIOServer(int port){
        try {
            //把Socket服务端启动
            server = new ServerSocket(port);
            System.out.println("BIO服务已启动，监听端口是：" + port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void listen(){

        while(true) {
            try {
                //等待客户端连接，阻塞方法
                Socket accept = server.accept();
                InputStream inputStream = accept.getInputStream();
                byte[] buf = new byte[1024];
                int len = inputStream.read(buf);
                inputStream.close();
                //只要一直有数据写入，len就会一直大于0
                if(len > 0){
                    String msg = new String(buf,0,len);
                    System.out.println("收到" + msg);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }

    public static void main(String[] args) {
        new BIOServer(8080).listen();
    }
}
