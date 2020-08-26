package com.melody.java.nio.buffer;

import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * ByteBuffer demo
 * @author zqhuangc
 * @see ByteBuffer
 */
public class BufferDemo {

    public static ByteBuffer allocateHeapBuffer(int capacity){
        return ByteBuffer.allocate(capacity);
    }

    public static ByteBuffer allocateHeapBuffer(byte[] array, int offset, int length){
        return ByteBuffer.wrap(array, offset, length);
    }

    public static ByteBuffer allocateHeapBuffer(byte[] array){
        return ByteBuffer.wrap(array, 0, array.length);
    }

    public static ByteBuffer allocateDirectBuffer(int capacity){
        return ByteBuffer.allocateDirect(capacity);
    }



    public void startScanner() throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(258);
        while(true) {
            int read = System.in.read();

            if(read == -1){
                break;
            }


            buffer.put((byte) read);

            if(read == '\n') {
                buffer.flip();
                // 构建一个byte数组
                byte [] content = new byte[buffer.limit()];
                // 从ByteBuffer中读取数据到byte数组中
                buffer.get(content);
                // 把byte数组的内容写到标准输出
                System.out.print(new String(content));
                // 调用clear()使position变为0,limit变为capacity的值，
                // 为接下来写入数据到ByteBuffer中做准备
                buffer.clear();
            }
        }
    }
}
