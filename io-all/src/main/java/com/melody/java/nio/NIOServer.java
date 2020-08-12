package com.melody.java.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.Charset;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * @Description: TODO
 * @author: melody_wongzq
 * @since: 2020/6/18
 * @see
 */
public class NIOServer {

    private final ServerSocketChannel server;
    private final Selector selector;
    private final Charset charset = Charset.forName("UTF-8");

    //用来记录在线人数，以及昵称
    private static HashSet<String> online_users = new HashSet<>();

    private static String USER_EXIST = "系统提示：该昵称已经存在，请换一个昵称";
    //相当于自定义协议格式，与客户端协商好
    private static String USER_CONTENT_SEPARATOR = "#@#";


    public NIOServer(int port) throws IOException {
        server = ServerSocketChannel.open();
        server.configureBlocking(false);
        server.bind(new InetSocketAddress(port));


        selector = Selector.open();
        // 注册监听事件，服务端只能监听接收消息
        server.register(selector, SelectionKey.OP_ACCEPT);

        System.out.println("服务已启动，监听端口是：" + port);
    }

    public void listen() throws IOException {

        while(true){
            // 查看是否有等待的 channel
            int readyChannel = selector.select();
            if(readyChannel == 0){
                continue;
            }

            // 等待处理
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while(iterator.hasNext()) {
                SelectionKey key = (SelectionKey) iterator.next();
                iterator.remove();
                //处理逻辑
                process(key);
            }

        }
    }

    private void process(SelectionKey key) throws IOException {
        if(key.isAcceptable()){
            //接收到一个客户端后，重新创建一个socket连接进行通信
            ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
            // 连接
            SocketChannel client = server.accept();
            //非阻塞模式
            client.configureBlocking(false);
            // 注册选择器，并设置为读取模式，收到一个连接请求
            // 后续该客户端关注读事件
            client.register(selector, SelectionKey.OP_READ);

            // 服务端继续关注连接上来事件
            key.interestOps(SelectionKey.OP_ACCEPT);
            // System.out.println("有客户端连接，IP地址为 :" + sc.getRemoteAddress());
            client.write(charset.encode("请输入你的昵称"));
        } else if(key.isReadable()){
            // 处理来自客户端的数据读取请求
            SocketChannel client = (SocketChannel) key.channel();
            // 往缓冲区读数据
            ByteBuffer buf = ByteBuffer.allocate(512);

            StringBuilder content = new StringBuilder();

            try{
                while(client.read(buf) > 0) {
                    buf.flip();
                    content.append(charset.decode(buf));
                }
                // 处于连接时会一直输出
                // System.out.println("从 " + client.getRemoteAddress() + "接收到消息: " + content);
                //将此对应的channel设置为准备下一次接受数据
                key.interestOps(SelectionKey.OP_READ);
            }catch (IOException io){
                key.cancel();
                if(key.channel() != null)
                {
                    key.channel().close();
                }
            }
            if(content.length() == 0){
                return;
            }
            String[] messages = content.toString().split(USER_CONTENT_SEPARATOR);
            //注册用户
            if(messages != null && messages.length == 1) {
                String nickName = messages[0];
                if (online_users.contains(messages[0])) {
                    client.write(charset.encode(USER_EXIST));
                } else {
                    online_users.add(nickName);
                    int onlineCount = onlineCount();
                    String message = "欢迎 " + nickName + " 进入聊天室! 当前在线人数:" + onlineCount;
                    broadCast(null, message);
                }
            }

        }
    }

    //TODO 若能检测连接中断，就不用这么统计了
    private int onlineCount() {
        int total = 0;
        for(SelectionKey key : selector.keys()){
            Channel target = key.channel();

            if(target instanceof SocketChannel){
                total++;
            }
        }
        return total;
    }

    private void broadCast(SocketChannel client, String message) throws IOException {
        //广播数据到所有的SocketChannel中
        for(SelectionKey key : selector.keys()) {
            Channel receiver = key.channel();
            //如果client不为空，不回发给发送此内容的客户端
            if(receiver instanceof SocketChannel && receiver != client) {
                SocketChannel target = (SocketChannel) receiver;
                target.write(charset.encode(message));
            }
        }
    }

    public static void main(String[] args) throws IOException {
        new NIOServer(8080).listen();
    }
}
