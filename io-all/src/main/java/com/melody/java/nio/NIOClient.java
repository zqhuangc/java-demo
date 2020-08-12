package com.melody.java.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;

/**
 * @Description: TODO
 * @author: melody_wongzq
 * @since: 2020/6/18
 * @see
 */
public class NIOClient {

    private Selector selector;
    private SocketChannel client;

    private String nickName = "";
    private Charset charset = Charset.forName("UTF-8");
    private static String USER_EXIST = "系统提示：该昵称已经存在，请换一个昵称";
    private static String USER_CONTENT_SEPARATOR = "#@#";


    public NIOClient(String host, int port) throws IOException {
        client = SocketChannel.open(new InetSocketAddress(host, port));

        client.configureBlocking(false);

        selector = Selector.open();

        client.register(selector, SelectionKey.OP_READ);
    }

    private void openSession() {
        new Reader().start();
        new Writer().start();
    }

    private class Writer extends Thread{
        @Override
        public void run() {
            try{
                //在主线程中 从键盘读取数据输入到服务器端
                Scanner scan = new Scanner(System.in);
                while(scan.hasNextLine()){
                    String line = scan.nextLine();
                    if("".equals(line)) continue; //不允许发空消息
                    if("".equals(nickName)) {
                        nickName = line;
                        line = nickName + USER_CONTENT_SEPARATOR;
                    } else {
                        line = nickName + USER_CONTENT_SEPARATOR + line;
                    }
//		            client.register(selector, SelectionKey.OP_WRITE);
                    client.write(charset.encode(line));//client既能写也能读，这边是写
                }
                scan.close();
            }catch(Exception e){

            }
        }
    }

    private class Reader extends Thread{
        @Override
        public void run() {
            try {
                while(true){
                    int readyChannel = selector.select();
                    if (readyChannel <= 0) continue;;
                    Set<SelectionKey> selectionKeys = selector.selectedKeys();
                    Iterator<SelectionKey> keys = selectionKeys.iterator();
                    while(keys.hasNext()){
                        SelectionKey key = keys.next();
                        keys.remove();
                        process(key);
                    }
                }
            } catch (IOException e) {
                System.out.println(e);
            }
        }

        private void process(SelectionKey key) {
            try {
                if(key.isReadable()){
                    SocketChannel client = (SocketChannel) key.channel();
                    ByteBuffer buf = ByteBuffer.allocate(256);
                    StringBuffer message = new StringBuffer();
                    while (client.read(buf) > 0){
                        buf.flip();
                        message.append(charset.decode(buf));
                    }

                    //若系统发送通知名字已经存在，则需要换个昵称
                    if(USER_EXIST.equals(message.toString())) {
                        nickName = "";
                    }
                    System.out.println(message.toString());
                    key.interestOps(SelectionKey.OP_READ);
                }
            } catch (IOException e) {
                System.out.println(e);
            }
        }
    }



    public static void main(String[] args) throws IOException {
        new NIOClient("localhost", 8080).openSession();
    }

}
